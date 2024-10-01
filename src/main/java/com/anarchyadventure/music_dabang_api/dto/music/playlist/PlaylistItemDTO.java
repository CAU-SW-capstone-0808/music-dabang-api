package com.anarchyadventure.music_dabang_api.dto.music.playlist;

import com.anarchyadventure.music_dabang_api.dto.music.MusicContentDTO;
import com.anarchyadventure.music_dabang_api.entity.music.PlaylistItem;
import lombok.Data;

@Data
public class PlaylistItemDTO {
    private Long id;
    private MusicContentDTO musicContent;
    private Long order;

    public static PlaylistItemDTO from(PlaylistItem playlistItem) {
        PlaylistItemDTO playlistItemDTO = new PlaylistItemDTO();
        playlistItemDTO.setId(playlistItem.getId());
        playlistItemDTO.setMusicContent(MusicContentDTO.from(playlistItem.getMusicContent()));
        playlistItemDTO.setOrder(playlistItem.getOrderingNum());
        return playlistItemDTO;
    }
}
