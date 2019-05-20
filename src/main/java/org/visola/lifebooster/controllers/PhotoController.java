package org.visola.lifebooster.controllers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.visola.lifebooster.dao.PhotoDao;
import org.visola.lifebooster.model.Photo;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.common.hash.Hashing;

@RequestMapping("${api.base.path}/photos")
@Controller
public class PhotoController {

  private final String bucketName;
  private final PhotoDao photoDao;
  private final Storage storage;

  public PhotoController(
      @Value("${photos.bucket.name}") String bucketName,
      PhotoDao photoDao,
      Storage storage) {
    this.bucketName = bucketName;
    this.photoDao = photoDao;
    this.storage = storage;
  }

  @PostMapping
  @Transactional
  public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
    byte[] bytes = file.getBytes();
    String hash = Hashing.sha256().hashBytes(bytes).toString();

    Optional<Photo> maybePhoto = photoDao.findByHash(hash);
    if (maybePhoto.isPresent()) {
      return ResponseEntity.ok(maybePhoto.get());
    }

    Photo photo = new Photo();
    photo.setName(file.getOriginalFilename());
    photo.setSize(file.getSize());
    photo.setUploadedAt(System.currentTimeMillis());

    String path = Hashing.sha256()
        .hashString(
            String.format("%s-%d", photo.getName(), photo.getUploadedAt()),
            StandardCharsets.UTF_8)
        .toString();

    photo.setPath(path);
    photo.setHash(hash);
    photo.setId(photoDao.create(photo));

    BlobId blobId = BlobId.of(bucketName, photo.getPath());
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
        .setContentType(file.getContentType())
        .build();
    storage.create(blobInfo, bytes);

    return ResponseEntity.ok(photo);
  }

}
