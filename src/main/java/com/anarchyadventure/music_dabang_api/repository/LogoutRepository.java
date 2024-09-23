package com.anarchyadventure.music_dabang_api.repository;

public interface LogoutRepository {
    void logout(String token, long expireTime);

    boolean isLogout(String token);
}
