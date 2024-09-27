package com.anarchyadventure.music_dabang_api.controller;

import com.anarchyadventure.music_dabang_api.dto.user.TokenDTO;
import com.anarchyadventure.music_dabang_api.dto.user.UserDTO;
import com.anarchyadventure.music_dabang_api.dto.user.UserJoinDTO;
import com.anarchyadventure.music_dabang_api.dto.user.UserLoginDTO;
import com.anarchyadventure.music_dabang_api.exceptions.BaseException;
import com.anarchyadventure.music_dabang_api.exceptions.ErrorCode;
import com.anarchyadventure.music_dabang_api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/oauth/{provider}")
    public ResponseEntity<TokenDTO> oauthLogin(@PathVariable String provider,
                                               @RequestHeader(name = "X-OAUTH-TOKEN") String oauthAccessToken) {
        if (!provider.equals("kakao")) {
            throw new BaseException(ErrorCode.INVALID_PARAMETER);
        }
        if (oauthAccessToken == null) {
            throw new BaseException("OAuth2.0 access token is required", ErrorCode.INVALID_PARAMETER);
        }
        return ResponseEntity.ok(userService.loginWithKakao(oauthAccessToken));
    }

    @PostMapping("/join")
    public ResponseEntity<TokenDTO> joinWithPhone(@Validated @RequestBody UserJoinDTO userJoinDTO) {
        return ResponseEntity.ok(userService.joinWithPhone(userJoinDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> loginWithPhone(@Validated @RequestBody UserLoginDTO userLoginDTO) {
        return ResponseEntity.ok(userService.loginWithPhone(userLoginDTO));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenDTO> refreshToken(@Validated @RequestBody TokenDTO token) {
        return ResponseEntity.ok(userService.refreshToken(token));
    }

    @PostMapping("/token/logout")
    public ResponseEntity<?> logout(@Validated @RequestBody TokenDTO token) {
        userService.logout(token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMe() {
        return ResponseEntity.ok(userService.getMe());
    }
}
