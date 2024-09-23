package com.anarchyadventure.music_dabang_api.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisLogoutRepository implements LogoutRepository {
    private final RedisTemplate<String, String> redisTemplate;

    private String getKey(String key) {
        return "logout:" + key;
    }

    @Override
    public void logout(String token, long expireTime) {
        redisTemplate.opsForValue().set(getKey(token), "true", expireTime);
    }

    @Override
    public boolean isLogout(String token) {
        return redisTemplate.opsForValue().get(getKey(token)) != null;
    }
}
