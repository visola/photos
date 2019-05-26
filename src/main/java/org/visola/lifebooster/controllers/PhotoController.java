package org.visola.lifebooster.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.visola.lifebooster.dao.PhotoDao;
import org.visola.lifebooster.model.Page;
import org.visola.lifebooster.model.Photo;
import org.visola.lifebooster.model.User;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteStreams;

@RequestMapping("${api.base.path}/photos")
@Controller
public class PhotoController {

  private final String bucketName;
  private final PhotoDao photoDao;
  private final Storage storage;
  private final String thumbnailsBucketName;

  private final byte[] emptyImage;

  public PhotoController(
      @Value("${photos.bucket.name}") String bucketName,
      @Value("${photos.bucket.thumbnails}") String thumbnailsBucketName,
      PhotoDao photoDao,
      Storage storage) throws IOException {
    this.bucketName = bucketName;
    this.photoDao = photoDao;
    this.storage = storage;
    this.thumbnailsBucketName = thumbnailsBucketName;

    emptyImage = ByteStreams.toByteArray(
        this.getClass().getClassLoader().getResourceAsStream("empty-image.png")
    );
  }

  @GetMapping("/{photoId}/thumbnail")
  public void downloadThumbnail(
      @PathVariable long photoId,
      OutputStream output,
      @AuthenticationPrincipal User user)
      throws IOException {
    Optional<Photo> maybePhoto = photoDao.findById(photoId, user.getId());
    if (!maybePhoto.isPresent()) {
      throw new NotFoundException("Image not found with ID: " + photoId);
    }

    Blob blob = storage.get(BlobId.of(thumbnailsBucketName, maybePhoto.get().getPath()));
    if (blob == null) {
      output.write(emptyImage);
      return;
    }

    output.write(blob.getContent());
  }

  @GetMapping
  @ResponseBody
  public ResponseEntity<?> getPhotos(
      @RequestParam(required = false, defaultValue = "1") int pageNumber,
      @RequestParam(required = false, defaultValue = "100") int pageSize,
      @AuthenticationPrincipal User user) {

    List<Photo> data = photoDao.fetchPage(user.getId(), (pageNumber - 1) * pageSize, pageSize);
    Page<Photo> page = new Page<>();
    page.setNumber(pageNumber);
    page.setSize(pageSize);
    page.setData(data);
    page.setTotalElements(photoDao.countPhotos(user.getId()));

    return ResponseEntity.ok(page);
  }

  @PostMapping
  @Transactional
  public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
      @AuthenticationPrincipal User user) throws IOException {
    byte[] bytes = file.getBytes();
    String hash = Hashing.sha256().hashBytes(bytes).toString();

    Optional<Photo> maybePhoto = photoDao.findByHash(hash, user.getId());
    if (maybePhoto.isPresent()) {
      return ResponseEntity.ok(maybePhoto.get());
    }

    Photo photo = new Photo();
    photo.setName(file.getOriginalFilename());
    photo.setSize(file.getSize());
    photo.setUploadedAt(System.currentTimeMillis());
    photo.setUserId(user.getId());

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
