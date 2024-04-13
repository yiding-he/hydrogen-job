package com.hyd.job.server.security;

import com.hyd.job.server.domain.User;
import com.hyd.job.server.domain.UserForSpringSecurity;
import com.hyd.job.server.repositories.UserRepository;
import com.hyd.job.server.utilities.Jackson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  private UserRepository userRepository;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(config -> config
      .requestMatchers("/static/**").permitAll()
      .anyRequest().authenticated()

    ).csrf(
      AbstractHttpConfigurer::disable

    ).formLogin(login -> login
      .loginPage("/admin/login")
      .loginProcessingUrl("/admin/login-process")
      .successHandler(this::onLoginSuccess)
      .failureHandler(this::onLoginFail)
      .permitAll()

    ).logout(logout -> logout
      .logoutUrl("/admin/logout")
      .logoutSuccessUrl("/admin/login")
      .permitAll()

    ).build();
  }

  private void onLoginFail(
    HttpServletRequest request, HttpServletResponse response, AuthenticationException exception
  ) {
    log.info("User login failed", exception);
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
  }

  private void onLoginSuccess(
    HttpServletRequest request, HttpServletResponse response, Authentication authentication
  ) throws IOException {
    log.info("User logged in: {}", authentication.getPrincipal());
    request.getSession().setAttribute("user", ((UserForSpringSecurity)authentication.getPrincipal()).getUser());
    response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
    response.getWriter().write(Jackson.serializeStandardJson(Map.of(
      "code", "200",
      "message", "Login success"
    )));
    response.setStatus(HttpStatus.OK.value());
  }

  ///////////////////////

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    return username -> {
      User user;

      // FIXME: Dummy implementation for development
      if (username.equals("admin")) {
        user = new User();
        user.setUserName(username);
        user.setPassword(passwordEncoder.encode(username));
        user.setProduct("*");
        user.setLine("*");
      } else {
        user = userRepository.findByUserName(username);
      }

      return new UserForSpringSecurity(user);
    };
  }
}
