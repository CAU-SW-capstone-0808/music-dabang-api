package com.anarchyadventure.music_dabang_api.common;

/**
 * JWT 관련 상수 목록
 */
public class JwtConst {
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final long ACCESS_TOKEN_EXPIRE = 24 * 60 * 60L; // 1일
//    public static final long ACCESS_TOKEN_EXPIRE = 10L; // 10초: 테스트용
    public static final long ANONYMOUS_TOKEN_EXPIRE = 30L * 60L; // 30분
    public static final long REFRESH_TOKEN_EXPIRE = 14 * 24 * 60 * 60L; // 14일
    public static final String ACCESS_TOKEN_NAME = "accessToken";
    public static final String REFRESH_TOKEN_NAME = "refreshToken";
    public static final String ISSUER = "music-dabang";
}
