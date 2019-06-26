package org.visola.photos.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.visola.spring.security.tokenfilter.TokenAuthenticationFilter;
import org.visola.spring.security.tokenfilter.TokenService;

@Component
public class AuthenticationFilter extends TokenAuthenticationFilter {

  private final TokenService tokenService;

  public AuthenticationFilter(TokenService tokenService) {
    super(tokenService);
    this.tokenService = tokenService;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    Optional<String> maybeAuthToken = Optional.ofNullable(request.getParameter("auth"));
    if (!maybeAuthToken.isPresent()) {
      super.doFilter(request, response, chain);
      return;
    }

    Optional<Authentication> authentication = tokenService.verifyToken(maybeAuthToken);
    if (authentication.isPresent()) {
      SecurityContextHolder.getContext().setAuthentication(authentication.get());
    }

    chain.doFilter(request, response);
  }

}
