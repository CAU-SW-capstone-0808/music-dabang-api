package com.anarchyadventure.music_dabang_api.dto.music;

import com.anarchyadventure.music_dabang_api.entity.music.Artist;
import com.anarchyadventure.music_dabang_api.entity.music.MusicContent;
import lombok.Data;

@Data
public class NewMusicContentDTO {
    private Long artistId;
    private String thumbnailUrl;
    private String musicContentUrl;
    private String videoContentUrl;
    private String title;

    public MusicContent toEntity(Artist artist) {
        return new MusicContent(
            thumbnailUrl,
            musicContentUrl,
            videoContentUrl,
            title,
            artist
        );
    }
}
