package org.visola.lifebooster.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.visola.lifebooster.security.UserAuthenticationJwtClaimsSetTransformer;

@Configuration
public class JWTFilterConfiguration {

  @Bean
  public UserAuthenticationJwtClaimsSetTransformer claimsSetTransformer() {
    return new UserAuthenticationJwtClaimsSetTransformer();
  }

}
