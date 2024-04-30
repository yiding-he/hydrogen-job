package com.hyd.job.server.security;

import com.hyd.job.server.domain.User;
import lombok.Getter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;

public record UserForSpringSecurity(
  @Nullable @Getter User user
) implements UserDetails {

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (user == null) {
      return emptyList();
    }
    return Stream.of(
      new AuthForSpringSecurity(AuthorityType.Product, user.getProductId()),
      new AuthForSpringSecurity(AuthorityType.Line, user.getLineId())
    ).toList();
  }

  @Override
  public String getPassword() {
    return user == null ? "" : user.getPassword();
  }

  @Override
  public String getUsername() {
    return user == null ? User.ANONYMOUS_USER_NAME : user.getUserName();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public String toString() {
    return "UserForSpringSecurity{" +
           (user != null ? (
             "userName=" + user.getUserName() +
             ", userId=" + user.getUserId()
           ) : "") +
           '}';
  }

}
