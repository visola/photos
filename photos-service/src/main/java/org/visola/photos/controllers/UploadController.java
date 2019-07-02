package org.visola.photos.controllers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.visola.photos.dao.UploadDao;
import org.visola.photos.model.Page;
import org.visola.photos.model.Upload;
import org.visola.photos.model.User;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.common.hash.Hashing;

@RequestMapping("${api.base.path}/uploads")
@Controller
public class UploadController {

  private final String bucketName;
  private final UploadDao uploadDao;
  private final Storage storage;

  public UploadController(
      @Value("${uploads.bucket.name}") String bucketName,
      UploadDao uploadDao,
      Storage storage) throws IOException {
    this.bucketName = bucketName;
    this.uploadDao = uploadDao;
    this.storage = storage;
  }

  @GetMapping
  @ResponseBody
  public ResponseEntity<?> getUploads(
      @RequestParam(required = false, defaultValue = "1") int pageNumber,
      @RequestParam(required = false, defaultValue = "100") int pageSize,
      @AuthenticationPrincipal User user) {

    List<Upload> data = uploadDao.fetchPageByUserId(user.getId(), (pageNumber - 1) * pageSize, pageSize);
    Page<Upload> page = new Page<>();
    page.setNumber(pageNumber);
    page.setSize(pageSize);
    page.setData(data);
    page.setTotalElements(uploadDao.countUploads(user.getId()));

    return ResponseEntity.ok(page);
  }

  @PostMapping
  @Transactional
  public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
      @AuthenticationPrincipal User user) throws IOException {
    byte[] bytes = file.getBytes();
    String hash = Hashing.sha256().hashBytes(bytes).toString();

    Optional<Upload> maybePhoto = uploadDao.findByHash(hash, user.getId());
    if (maybePhoto.isPresent()) {
      return ResponseEntity.ok(maybePhoto.get());
    }

    Upload upload = new Upload();
    upload.setName(file.getOriginalFilename());
    upload.setMime(file.getContentType());
    upload.setSize(file.getSize());
    upload.setUploadedAt(System.currentTimeMillis());
    upload.setUserId(user.getId());

    String path = Hashing.sha256()
        .hashString(
            String.format("%s-%d", upload.getName(), upload.getUploadedAt()),
            StandardCharsets.UTF_8)
        .toString();

    upload.setPath(path);
    upload.setHash(hash);
    upload.setId(uploadDao.create(upload));

    BlobId blobId = BlobId.of(bucketName, upload.getPath());
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
        .setContentType(file.getContentType())
        .build();
    storage.create(blobInfo, bytes);

    return ResponseEntity.ok(upload);
  }

}
