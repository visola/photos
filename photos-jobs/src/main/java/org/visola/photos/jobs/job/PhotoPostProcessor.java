package org.visola.photos.jobs.job;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;
import org.visola.photos.dao.ThumbnailDao;
import org.visola.photos.jobs.Job;
import org.visola.photos.model.PageRequest;
import org.visola.photos.model.Thumbnail;
import org.visola.photos.model.Upload;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.common.hash.Hashing;

@Component
public class PhotoPostProcessor implements Job {

  private static final Logger LOGGER = LoggerFactory.getLogger(PhotoPostProcessor.class);
  private static final int THUMBNAIL_HEIGHT = 200;

  private final String bucketName;
  private final Storage storage;
  private final String thumbnailsBucketName;
  private final ThumbnailDao thumbnailDao;

  public PhotoPostProcessor(
      Storage storage,
      @Value("${uploads.bucket.name}") String bucketName,
      @Value("${uploads.bucket.thumbnails}") String thumbnailsBucketName,
      ThumbnailDao thumbnailDao) {
    this.bucketName = bucketName;
    this.storage = storage;
    this.thumbnailsBucketName = thumbnailsBucketName;
    this.thumbnailDao = thumbnailDao;
  }

  @Override
  public String getName() {
    return "photos-post-processor";
  }

  @Override
  @Transactional
  public void run() {
    List<Upload> uploads = thumbnailDao.uploadsWithoutThumbnail(MimeTypeUtils.IMAGE_JPEG_VALUE, new PageRequest());
    if (uploads.isEmpty()) {
      return;
    }

    LOGGER.info("Processing {} jpegs...", uploads.size());
    for (Upload upload : uploads) {
      LOGGER.debug("Downloading {}...", upload.getPath());
      long start = System.currentTimeMillis();
      Blob blob = storage.get(BlobId.of(bucketName, upload.getPath()));
      byte [] bytes = blob.getContent();
      LOGGER.debug("Downloaded {} in {}ms.", upload.getPath(), System.currentTimeMillis() - start);

      try {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
        int width = image.getWidth();
        int height = image.getHeight();

        int scalledWidth = THUMBNAIL_HEIGHT * width / height;
        int scalledHeight = THUMBNAIL_HEIGHT;

        BufferedImage scalledImage = new BufferedImage(scalledWidth, scalledHeight, BufferedImage.TYPE_INT_RGB);

        AffineTransform at = AffineTransform.getScaleInstance(
            (double) scalledWidth/ width,
            (double) scalledHeight / height
        );

        Graphics2D g = scalledImage.createGraphics();
        g.drawRenderedImage(image, at);
        g.dispose();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(scalledImage, "jpg", output);

        String thumbnailHash = Hashing.sha256()
            .hashString(
                String.format("%s-%d", upload.getName(), upload.getUploadedAt()),
                StandardCharsets.UTF_8)
            .toString();

        Thumbnail thumbnail = new Thumbnail();
        thumbnail.setId(upload.getId());
        thumbnail.setPath(thumbnailHash);
        thumbnail.setHash(thumbnailHash);
        thumbnail.setCreatedAt(System.currentTimeMillis());
        thumbnail.setMime(MimeTypeUtils.IMAGE_JPEG_VALUE);
        thumbnail.setSize(output.size());
        thumbnail.setUserId(upload.getUserId());

        thumbnailDao.create(thumbnail);

        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(thumbnailsBucketName, thumbnail.getPath()))
            .setContentType(thumbnail.getMime())
            .build();
        storage.create(blobInfo, output.toByteArray());
      } catch (IOException e) {
        LOGGER.error("Error while loading image bytes to BufferedImage.", e);
      }
    }
  }

}
