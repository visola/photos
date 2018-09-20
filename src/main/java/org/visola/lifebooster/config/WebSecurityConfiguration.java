package org.visola.lifebooster.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.visola.lifebooster.security.UserAuthenticationJwtClaimsSetTransformer;
import org.visola.spring.security.tokenfilter.TokenAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Value("${api.base.path}")
  private String baseApiPath;

  @Autowired
  private TokenAuthenticationFilter tokenAuthenticationFilter;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.addFilterBefore(tokenAuthenticationFilter, BasicAuthenticationFilter.class);

    http
        .authorizeRequests()

        // Anything except API endpoints need not to be authenticated
        .antMatchers("**/*").anonymous()

        // All Others API calls will be secure
        .antMatchers(baseApiPath + "/**").authenticated();
  }

}
