package com.anarchyadventure.music_dabang_api.repository;

import com.anarchyadventure.music_dabang_api.entity.user.OAuthProvider;
import com.anarchyadventure.music_dabang_api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByOauthProviderAndOauthId(OAuthProvider oauthProvider, String oauthId);

    boolean existsByPhone(String phone);

    Optional<User> findByPhone(String phone);
}
