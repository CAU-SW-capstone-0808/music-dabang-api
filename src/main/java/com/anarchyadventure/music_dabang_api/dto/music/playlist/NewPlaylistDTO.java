package com.anarchyadventure.music_dabang_api.dto.music.playlist;

import com.anarchyadventure.music_dabang_api.entity.music.Playlist;
import com.anarchyadventure.music_dabang_api.entity.user.User;
import lombok.Data;

// RequestDto, ResponseDto 구분해주기 (패키지명 or 클래스명)
@Data
public class NewPlaylistDTO {
    private String name;

    public Playlist toEntity(User user) {
        return new Playlist(name, user);
    }
}
