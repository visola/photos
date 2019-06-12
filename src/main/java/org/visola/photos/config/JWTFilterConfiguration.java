package org.visola.photos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.visola.photos.security.UserAuthenticationJwtClaimsSetTransformer;

@Configuration
public class JWTFilterConfiguration {

  @Bean
  public UserAuthenticationJwtClaimsSetTransformer claimsSetTransformer() {
    return new UserAuthenticationJwtClaimsSetTransformer();
  }

}
