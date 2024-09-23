package com.anarchyadventure.music_dabang_api.dto.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenDTO {
    @NotEmpty
    private final String accessToken;
    @NotEmpty
    private final String refreshToken;
}
