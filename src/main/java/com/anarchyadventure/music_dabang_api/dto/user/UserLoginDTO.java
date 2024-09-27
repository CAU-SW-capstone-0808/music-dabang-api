package com.anarchyadventure.music_dabang_api.dto.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserLoginDTO {
    @NotEmpty
    private String phone;
    @NotEmpty
    private String password;
}
