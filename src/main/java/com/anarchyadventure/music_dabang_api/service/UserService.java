package com.anarchyadventure.music_dabang_api.service;

import com.anarchyadventure.music_dabang_api.dto.user.*;
import com.anarchyadventure.music_dabang_api.entity.user.OAuthProvider;
import com.anarchyadventure.music_dabang_api.entity.user.User;
import com.anarchyadventure.music_dabang_api.entity.user.UserAge;
import com.anarchyadventure.music_dabang_api.exceptions.EntityAlreadyExistException;
import com.anarchyadventure.music_dabang_api.exceptions.UnauthenticatedException;
import com.anarchyadventure.music_dabang_api.repository.UserRepository;
import com.anarchyadventure.music_dabang_api.security.CustomLogoutHandler;
import com.anarchyadventure.music_dabang_api.security.JwtHandler;
import com.anarchyadventure.music_dabang_api.security.SecurityHandler;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final KakaoApiHelper kakaoApiHelper;
    private final UserRepository userRepository;
    private final JwtHandler jwtHandler;
    private final CustomLogoutHandler logoutHandler;
    private final PasswordEncoder passwordEncoder;

    public TokenDTO loginWithKakao(String accessToken) {
        KakaoUserDTO kakaoUser = kakaoApiHelper.getUserInfo(accessToken);
        Optional<User> userOpt
            = userRepository.findByOauthProviderAndOauthId(OAuthProvider.KAKAO, kakaoUser.getId().toString());
        User user;
        if (userOpt.isEmpty()) {
            user = new User(
                OAuthProvider.KAKAO,
                kakaoUser.getId().toString(),
                // 디미터 법칙 고려
                kakaoUser.getKakao_account().getProfile().getNickname(),
                kakaoUser.getKakao_account().getProfile().getProfile_image_url(),
                kakaoUser.getKakao_account().getPhone_number(),
                kakaoUser.getKakao_account().getBirth(),
                kakaoUser.getKakao_account().getGender()
            );
            userRepository.save(user);
        } else {
            user = userOpt.get();
        }
        return jwtHandler.createTokenInfo(user);
    }

    public TokenDTO joinWithPhone(UserJoinDTO userJoinDTO) {
        if (userRepository.existsByPhone(userJoinDTO.getPhone())) {
            throw new EntityAlreadyExistException("phone number already exists");
        }
        User user = new User(
            userJoinDTO.getNickname(),
            null,
            userJoinDTO.getPhone(),
            passwordEncoder.encode(userJoinDTO.getPassword()),
            userJoinDTO.getBirthday(),
            userJoinDTO.getGender()
        );
        userRepository.save(user);
        return jwtHandler.createTokenInfo(user);
    }

    public TokenDTO loginWithPhone(UserLoginDTO userLoginDTO) {
        User user = userRepository.findByPhone(userLoginDTO.getPhone())
            .orElseThrow(() -> new EntityAlreadyExistException("phone number not exists"));
        if (passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            return jwtHandler.createTokenInfo(user);
        } else {
            throw new UnauthenticatedException("password not match");
        }
    }

    public TokenDTO refreshToken(TokenDTO token) {
        return jwtHandler.refreshToken(token.getAccessToken(), token.getRefreshToken());
    }

    public void logout(TokenDTO token) {
        try {
            logoutHandler.tokenLogout(token.getAccessToken());
        } catch (Exception e) {
            log.error("access token logout error", e);
        }
        logoutHandler.tokenLogout(token.getRefreshToken());
    }

    @Transactional(readOnly = true)
    public UserDTO getMe() {
        return UserDTO.from(SecurityHandler.getUserAuth());
    }

    public UserDTO fillUserAge(UserAge userAge) {
        User user = userRepository.findById(SecurityHandler.getUserAuth().getId())
            .orElseThrow(() -> new EntityNotFoundException("user not exists"));
        user.setUserAge(userAge);
        return UserDTO.from(user);
    }
}
