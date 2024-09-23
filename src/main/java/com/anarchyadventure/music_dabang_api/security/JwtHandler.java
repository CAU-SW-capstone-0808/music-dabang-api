package com.anarchyadventure.music_dabang_api.security;

import com.anarchyadventure.music_dabang_api.dto.user.TokenDTO;
import com.anarchyadventure.music_dabang_api.entity.user.User;
import com.anarchyadventure.music_dabang_api.exceptions.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.stream.Collectors;

import static com.anarchyadventure.music_dabang_api.common.JwtConst.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtHandler {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final CustomLogoutHandler logoutHandler;

    private String genToken(long expiration, String type, String sub, String scope) {
        if (type == null || sub == null || scope == null) {
            log.error("type={}, sub={}, scope={} must not be null", type, sub, scope);
            throw new BaseException("invalid format: genToken(?, ?, ?, ?)", ErrorCode.SYSTEM_ERROR);
        }

        // generate JWT
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiration))
                .subject(sub)
                .claim("scope", scope)
                .claim("type", type)
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private String genToken(Authentication authentication, long expiration, String type, String sub, String scope) {
        if (authentication == null) {
            log.info("jwt authentication is null");
            throw new UnauthenticatedException();
        }
        if (type == null) {
            type = ACCESS_TOKEN_NAME;
        }
        if (sub == null && authentication.getPrincipal() instanceof PrincipalDetails p) {
            sub = p.getUser().getId().toString();
        }
        if (sub == null) {
            log.info("jwt subject is null");
            throw new UnauthenticatedException();
        }

        if (scope == null) {
            scope = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));
        }
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiration))
                .subject(sub)
                .claim("scope", scope)
                .claim("type", type)
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private String genAccessToken(Authentication authentication, String sub, User user) {
        String scope = new PrincipalDetails(user).getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
        return genToken(authentication, ACCESS_TOKEN_EXPIRE, ACCESS_TOKEN_NAME, sub, scope);
    }

    private String genAccessToken(User user) {
        String scope = new PrincipalDetails(user).getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
        return genToken(ACCESS_TOKEN_EXPIRE, ACCESS_TOKEN_NAME, user.getId().toString(), scope);
    }

//    private String genAnonymousToken(Authentication authentication, String sub) {
//        return genToken(authentication, ANONYMOUS_TOKEN_EXPIRE, ANONYMOUS_TOKEN_NAME, sub, "ANONYMOUS");
//    }

    private String genRefreshToken(Authentication authentication, String sub) {
        return genToken(authentication, REFRESH_TOKEN_EXPIRE, REFRESH_TOKEN_NAME, sub, "REFRESH");
    }

    private String genRefreshToken(User user) {
        return genToken(REFRESH_TOKEN_EXPIRE, REFRESH_TOKEN_NAME, user.getId().toString(), "REFRESH");
    }

    public TokenDTO createTokenInfo(Authentication authentication) {
        String sub;
        User user;
        if (authentication.getPrincipal() instanceof PrincipalDetails p) {
            sub = p.getUser().getId().toString();
            user = p.getUser();
        } else {
            throw new UnauthenticatedException();
        }
        return new TokenDTO(genAccessToken(authentication, sub, user), genRefreshToken(authentication, sub));
    }

    /**
     * user를 통해 TokenInfo를 생성.
     */
    public TokenDTO createTokenInfo(User user) {
        log.info("user(id={}) generate JWT", user.getId());
        return new TokenDTO(genAccessToken(user), genRefreshToken(user));
    }

    public TokenDTO refreshToken(String accessToken, String refreshToken) {
        // anonymous 여부
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAnonymous = authentication instanceof AnonymousAuthenticationToken;
        // SecurityContext에 있는 current user 가져오기
        User currentUser = isAnonymous ? null : SecurityHandler.getUserAuth();
        
        // jwt decoding 및 예외 처리
        // jwt accessToken 예외 : 만료된 토큰일 수 있음
        Jwt accessJwt = null;
        try {
            accessJwt = jwtDecoder.decode(accessToken);
        } catch (JwtValidationException e) {
            log.info("invalid access token request while refresh token {}", accessToken);
        }
        // parse refreshToken
        Jwt refreshJwt = jwtDecoder.decode(refreshToken);
        // 토큰 subject null 확인
        if ((accessJwt != null && accessJwt.getSubject() == null) || refreshJwt.getSubject() == null) {
            throw new InvalidParameterException("토큰 정보를 전달받지 못했습니다.");
        }
        // 유저 아이디 일치 여부 확인 (refresh만 검증)
        else if (!isAnonymous && !refreshJwt.getSubject().equals(currentUser.getId().toString())) {
            log.info("토큰 정보가 일치하지 않습니다. current_user={} <-> refresh_user={}, access_token_user={}",
                    currentUser.getId(), refreshJwt.getSubject(), accessJwt != null ? accessJwt.getSubject() : "null");
            throw new InvalidParameterException("토큰 정보가 일치하지 않습니다.");
        }
        else if (!refreshJwt.getClaim("type").equals(REFRESH_TOKEN_NAME)) {
            log.info("invalid refresh token type - {}", (String) refreshJwt.getClaim("type"));
            throw new InvalidParameterException("유효한 refresh token이 아닙니다");
        }

        // logout
        logoutHandler.tokenLogout(accessToken);
        logoutHandler.tokenLogout(refreshToken);

        // accessToken + refreshToken 반환
        String sub = refreshJwt.getSubject();
        if (isAnonymous) {
            throw new InvalidAccessException("익명 사용자는 토큰 갱신을 할 수 없습니다.");
//            return new TokenDTO(genAnonymousToken(authentication, sub), genRefreshToken(authentication, sub));
        }
        return new TokenDTO(genAccessToken(authentication, sub, currentUser), genRefreshToken(authentication, sub));
    }
}
