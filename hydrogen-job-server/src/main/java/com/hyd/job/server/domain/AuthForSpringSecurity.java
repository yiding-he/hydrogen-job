package com.hyd.job.server.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public class AuthForSpringSecurity implements GrantedAuthority {

  private final AuthorityType type;

  private final String name;

  @Override
  public String getAuthority() {
    return "";
  }
}
