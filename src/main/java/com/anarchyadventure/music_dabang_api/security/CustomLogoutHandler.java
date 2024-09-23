package com.anarchyadventure.music_dabang_api.security;

import com.anarchyadventure.music_dabang_api.exceptions.InvalidParameterException;
import com.anarchyadventure.music_dabang_api.repository.LogoutRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {
    private final LogoutRepository logoutRepository;
    private final JwtDecoder jwtDecoder;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (!request.getContentType().equals("application/json")) {
            throw new InvalidParameterException("logout 형식은 json이어야 합니다.");
        }

        Map<String, String> json;
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = request.getReader()) {
            br.lines().forEach(sb::append);
            json = objectMapper.readValue(sb.toString(), new TypeReference<>() {});
        } catch (IOException e) {
            log.error("json 데이터를 파싱하는 중 오류 발생");
            throw new RuntimeException(e);
        }

        // token logout
        tokenLogout(json.get("accessToken"));
        tokenLogout(json.get("refreshToken"));

        log.info("logout success");
    }

    public void tokenLogout(String token) {
        if (token == null) {
            return;
        }
        try {
            DecodedJWT jwt = JWT.decode(token);
            Date expiresAt = jwt.getExpiresAt();
            if (expiresAt == null) {
                throw new InvalidParameterException("jwt 만료시간 누락");
            }
            Instant now = Instant.now();
            Duration duration = Duration.between(now, expiresAt.toInstant());
            long timeout = duration.getSeconds();
            if (timeout > 0) {
                log.info("logout: subject={} iss_at={} exp_at={}", jwt.getSubject(), jwt.getIssuedAt(), jwt.getExpiresAt());
                logoutRepository.logout(token, timeout);
            }
        } catch (JWTDecodeException e) {
            log.error("logout: jwt로 파싱하는 중 오류 발생. e={} token={}", e, token, e);
            throw new InvalidParameterException(e);
        }
    }
}
