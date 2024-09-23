package com.anarchyadventure.music_dabang_api.dto.user;

import com.anarchyadventure.music_dabang_api.entity.user.User;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String nickname;
    private String profileImageUrl;

    public static UserDTO from(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setNickname(user.getNickname());
        userDTO.setProfileImageUrl(user.getProfileImageUrl());
        return userDTO;
    }
}
