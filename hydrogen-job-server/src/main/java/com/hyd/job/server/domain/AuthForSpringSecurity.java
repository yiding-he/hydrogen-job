package com.hyd.job.server.domain;

import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;

public record AuthForSpringSecurity(
  @NonNull @Getter AuthorityType type,
  @NonNull @Getter Long id
) implements GrantedAuthority {

  @Override
  public String getAuthority() {
    return type.name() + ":" + id;
  }
}
