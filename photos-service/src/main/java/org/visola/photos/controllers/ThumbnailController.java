package org.visola.photos.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.visola.photos.dao.ThumbnailDao;
import org.visola.photos.model.Thumbnail;
import org.visola.photos.model.User;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;

@RequestMapping("${api.base.path}/thumbnails")
@RestController
public class ThumbnailController {

  private final Storage storage;
  private final ThumbnailDao thumbnailDao;
  private final String thumbnailsBucketName;

  public ThumbnailController(
      Storage storage,
      ThumbnailDao thumbnailDao,
      @Value("${uploads.bucket.thumbnails}") String thumbnailsBucketName) {
    this.storage = storage;
    this.thumbnailDao = thumbnailDao;
    this.thumbnailsBucketName = thumbnailsBucketName;
  }

  @GetMapping(value = "/{uploadId}", produces = MimeTypeUtils.IMAGE_JPEG_VALUE)
  public void downloadThumbnail(
      @PathVariable long uploadId,
      OutputStream output,
      @AuthenticationPrincipal User user)
      throws IOException {
    Optional<Thumbnail> maybeThumbnail = thumbnailDao.findById(uploadId, user.getId());
    if (!maybeThumbnail.isPresent()) {
      throw new NotFoundException("Thumbnail not found.");
    }

    output.write(storage.get(BlobId.of(thumbnailsBucketName, maybeThumbnail.get().getPath())).getContent());
  }

  @GetMapping(value = "/{uploadId}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
  public Optional<Thumbnail> findThumbnail(
      @PathVariable long uploadId,
      OutputStream output,
      @AuthenticationPrincipal User user)
      throws IOException {
    return thumbnailDao.findById(uploadId, user.getId());
  }
}
