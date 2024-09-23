package com.anarchyadventure.music_dabang_api.dto.music.playlist;

import com.anarchyadventure.music_dabang_api.entity.music.Playlist;
import lombok.Data;

@Data
public class PlaylistDTO {
    private Long id;
    private String name;
    private Boolean usedForSystem;
    private Boolean userVisible;

    public static PlaylistDTO from(Playlist playlist) {
        PlaylistDTO playlistDTO = new PlaylistDTO();
        playlistDTO.setId(playlist.getId());
        playlistDTO.setName(playlist.getName());
        playlistDTO.setUsedForSystem(playlist.getUsedForSystem());
        playlistDTO.setUserVisible(playlist.getUserVisible());
        return playlistDTO;
    }
}
