package org.visola.lifebooster.security;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.security.core.Authentication;
import org.visola.lifebooster.model.User;
import org.visola.spring.security.tokenfilter.jwt.AuthenticationJwtClaimsSetTransformer;

import com.nimbusds.jwt.JWTClaimsSet;

public class UserAuthenticationJwtClaimsSetTransformer implements
    AuthenticationJwtClaimsSetTransformer {

  private static final long TOKEN_DURATION = TimeUnit.HOURS.toMillis(1);
  public static final String CLAIM_ID = "id";

  @Override
  public JWTClaimsSet getClaimsSet(Authentication auth) {
    User user = (User) auth.getPrincipal();
    long now = System.currentTimeMillis();

    return new JWTClaimsSet.Builder()
        .subject(user.getEmail())
        .issueTime(new Date(now))
        .expirationTime(new Date(now + TOKEN_DURATION))
        .claim("id", user.getId().toString())
        .build();
  }

  @Override
  public Authentication getAuthentication(JWTClaimsSet claimSet) {
    try {
      User user = new User();
      user.setEmail(claimSet.getSubject());
      user.setId(UUID.fromString(claimSet.getStringClaim(CLAIM_ID)));

      return new UserAuthentication(user);
    } catch (ParseException e) {
      throw new RuntimeException("Error while extracting user from claimset", e);
    }
  }
}
