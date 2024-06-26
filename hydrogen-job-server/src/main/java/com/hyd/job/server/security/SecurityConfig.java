package com.hyd.job.server.security;

import com.hyd.job.server.domain.User;
import com.hyd.job.server.mapper.UserMapper;
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
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import java.io.IOException;

import static org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  private UserMapper userMapper;

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

    ).headers(headers -> headers
      .addHeaderWriter(new XFrameOptionsHeaderWriter(SAMEORIGIN))

    ).build();
  }

  private void onLoginFail(
    HttpServletRequest request, HttpServletResponse response, AuthenticationException exception
  ) {
    log.info("User login failed: {}", exception.toString());
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
  }

  private void onLoginSuccess(
    HttpServletRequest request, HttpServletResponse response, Authentication authentication
  ) throws IOException {
    log.info("User logged in: {}", authentication.getPrincipal());
    UserForSpringSecurity sUser = (UserForSpringSecurity) authentication.getPrincipal();
    request.getSession().setAttribute("user", sUser.getUser());
    response.setContentType("text/plain");
    response.getWriter().write("");
    response.sendRedirect("index");
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
        user.setProductId(User.ALL_PRODUCT);
        user.setLineId(User.ALL_LINE);
      } else {
        user = userMapper.findByUsername(username);
      }

      return new UserForSpringSecurity(user);
    };
  }
}
