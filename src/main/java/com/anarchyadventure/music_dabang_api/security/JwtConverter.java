package com.anarchyadventure.music_dabang_api.security;

import com.anarchyadventure.music_dabang_api.entity.user.User;
import com.anarchyadventure.music_dabang_api.repository.LogoutRepository;
import com.anarchyadventure.music_dabang_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

import static com.anarchyadventure.music_dabang_api.common.JwtConst.ACCESS_TOKEN_NAME;
import static com.anarchyadventure.music_dabang_api.common.JwtConst.REFRESH_TOKEN_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtConverter {
    private final UserRepository userRepository;
    private final LogoutRepository logoutRepository;

    private UsernamePasswordAuthenticationToken defaultAuthToken(PrincipalDetails principalDetails) {
        if (principalDetails == null)
            return new UsernamePasswordAuthenticationToken(null, "", null);
        return new UsernamePasswordAuthenticationToken(principalDetails, "", principalDetails.getAuthorities());
    }

    private UsernamePasswordAuthenticationToken refreshAuthToken(PrincipalDetails principalDetails) {
        // refresh token에는 REFRESH 권한만 부여
        return new UsernamePasswordAuthenticationToken(principalDetails, "",
            AuthorityUtils.createAuthorityList("REFRESH"));
    }

    @Transactional
    public AbstractAuthenticationToken convert(Jwt jwt) {
        if (jwt == null || jwt.getExpiresAt() == null) {
            return defaultAuthToken(null);
        }

        boolean isExpired = Instant.now().isAfter(jwt.getExpiresAt());
        String tokenType = jwt.getClaim("type");
        log.info("jwt token type {}", tokenType);
        if (isExpired) {
            log.info("jwt token expired token={}, time={}", jwt.getTokenValue(), jwt.getExpiresAt());
//            throw new UnauthorizedException("JWT token expired");
            return defaultAuthToken(null);
        } else if (tokenType == null) {
            return defaultAuthToken(null);
        }
//        else if (tokenType.equals(ANONYMOUS_TOKEN_NAME)) {
//            return anonymousAuthToken(jwt.getSubject());
//        }

        try {
            if (logoutRepository.isLogout(jwt.getTokenValue())) {
                log.info("jwt token is logout token={}", jwt.getTokenValue());
                return defaultAuthToken(null);
            }
        } catch (RuntimeException e) {
            log.info("logout repository throws - {}", e.getClass().getName(), e);
        }

        PrincipalDetails principalDetails;
        try {
            Long userId = Long.parseLong(jwt.getSubject());
            Optional<User> userOpt = userRepository.findById(userId);

            // early return 고려
            if (userOpt.isPresent()) {
                principalDetails = new PrincipalDetails(userOpt.get());
                if (tokenType.equals(ACCESS_TOKEN_NAME)) {
                    userOpt.get().onLogin();
                    return defaultAuthToken(principalDetails);
                } else if (tokenType.equals(REFRESH_TOKEN_NAME)) {
                    return refreshAuthToken(principalDetails);
                }
            }
            log.warn("principal details => null");
            return defaultAuthToken(null);
        } catch (Exception e) {
            log.error("error caused while generating auth token", e);
            return defaultAuthToken(null);
        }
    }
}
