package org.visola.photos.config;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Configuration
public class GoogleCloudConfiguration {

  @Bean
  public Storage cloudStorage(@Value("${google.cloud.credentials}") String credentialsJson)
      throws IOException {

    Credentials credentials = GoogleCredentials.fromStream(
        new ByteArrayInputStream(credentialsJson.getBytes(UTF_8))
    );

    return StorageOptions.newBuilder()
        .setCredentials(credentials)
        .build()
        .getService();
  }

}
