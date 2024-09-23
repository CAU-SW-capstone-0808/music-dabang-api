package com.anarchyadventure.music_dabang_api.service;

import com.anarchyadventure.music_dabang_api.dto.user.KakaoUserDTO;
import com.anarchyadventure.music_dabang_api.dto.user.TokenDTO;
import com.anarchyadventure.music_dabang_api.dto.user.UserDTO;
import com.anarchyadventure.music_dabang_api.dto.user.UserJoinDTO;
import com.anarchyadventure.music_dabang_api.entity.user.OAuthProvider;
import com.anarchyadventure.music_dabang_api.entity.user.User;
import com.anarchyadventure.music_dabang_api.exceptions.EntityAlreadyExistException;
import com.anarchyadventure.music_dabang_api.repository.UserRepository;
import com.anarchyadventure.music_dabang_api.security.CustomLogoutHandler;
import com.anarchyadventure.music_dabang_api.security.JwtHandler;
import com.anarchyadventure.music_dabang_api.security.SecurityHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public TokenDTO loginWithKakao(String accessToken) {
        KakaoUserDTO kakaoUser = kakaoApiHelper.getUserInfo(accessToken);
        Optional<User> userOpt
            = userRepository.findByOauthProviderAndOauthId(OAuthProvider.KAKAO, kakaoUser.getId().toString());
        User user;
        if (userOpt.isEmpty()) {
            user = new User(
                OAuthProvider.KAKAO,
                kakaoUser.getId().toString(),
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
            OAuthProvider.NONE,
            userJoinDTO.getPhone(),
            userJoinDTO.getNickname(),
            null,
            userJoinDTO.getPhone(),
            userJoinDTO.getBirthday(),
            userJoinDTO.getGender()
        );
        userRepository.save(user);
        return jwtHandler.createTokenInfo(user);
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

    public UserDTO getMe() {
        return UserDTO.from(SecurityHandler.getUserAuth());
    }
}
