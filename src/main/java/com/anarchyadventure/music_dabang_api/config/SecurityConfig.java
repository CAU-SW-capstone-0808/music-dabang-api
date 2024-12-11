package com.anarchyadventure.music_dabang_api.config;

import com.anarchyadventure.music_dabang_api.repository.LogoutRepository;
import com.anarchyadventure.music_dabang_api.security.JwtConverter;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static org.springframework.http.HttpMethod.POST;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtConverter jwtConverter;
    private final LogoutRepository logoutRepository;

    @Value("${jwt.public-key}")
    RSAPublicKey publicKey;

    @Value("${jwt.private-key}")
    RSAPrivateKey privateKey;

    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .formLogin(AbstractHttpConfigurer::disable)
            // jwt 설정
            .oauth2ResourceServer((oauth2ResourceServer) ->
                oauth2ResourceServer.jwt((jwt) ->
                    jwt.decoder(jwtDecoder())
                        .jwtAuthenticationConverter(jwtConverter::convert)
                )
            )
            // anonymous 설정 : disable
            // disable -> filter에서 직접 관리.
            // JwtHandler에서 anonymous authentication 주입
            // if anonymous authentication == null -> anonymous token(jwt) 주입
            .anonymous(AbstractHttpConfigurer::disable)
            // disable basic authentication
            .httpBasic(AbstractHttpConfigurer::disable)
            // cors 설정
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // csrf 비활성화 경로
            .csrf(CsrfConfigurer::disable)
            // anonymous handling
//            .addFilterAfter(new AnonymousJwtFilter(jwtConverter, jwtEncoder()), AnonymousAuthenticationFilter.class)
            // 경로 권한 설정
            .authorizeHttpRequests(auth -> auth
                // 권한 없는 경로
                .requestMatchers("/user/oauth/kakao", "/health", "/user/join", "/user/login", "/privacy-agreement.html")
                .permitAll()
                // 토큰 새로고침 경로 : REFRESH authority 필요 (RefreshToken만 받음)
                .requestMatchers(POST, "/user/token/refresh")
                .hasAuthority("REFRESH")
                // 유저 경로
                .requestMatchers("/user/**")
                .hasAnyAuthority("USER", "ADMIN")
                // 어드민 경로 (모두 /admin/** 으로 통일)
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                // api 문서 경로 (추후 권한 수정)
                .requestMatchers("/swagger-ui**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                // 기본값: 권한 필요
                .anyRequest().hasAnyAuthority("USER", "ADMIN")
            )
            .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("http://localhost:7000");
        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.addAllowedOrigin("http://localhost:8081");
        configuration.addAllowedOrigin("https://musicdb.cast-ing.kr");
        configuration.addAllowedOrigin("https://mdabang.jdn.kr");
        configuration.addAllowedMethod("*"); // allow all HTTP methods
        configuration.addAllowedHeader("*"); // allow all HTTP headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.publicKey).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(this.privateKey).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }
}
