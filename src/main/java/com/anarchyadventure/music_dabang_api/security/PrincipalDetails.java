package com.anarchyadventure.music_dabang_api.security;

import com.anarchyadventure.music_dabang_api.entity.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return user.getPhone();
    }

    // TODO : implement
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // TODO : implement
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // TODO : implement
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // TODO : implement
    @Override
    public boolean isEnabled() {
        return true;
    }
}
