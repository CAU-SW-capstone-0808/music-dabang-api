package com.anarchyadventure.music_dabang_api.dto.music;

import com.anarchyadventure.music_dabang_api.entity.music.MusicContent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MusicContentDTO {
    private final Long id;
    private final String thumbnailUrl;
    private final String musicContentUrl;
    private final String videoContentUrl;
    private final String title;
    private final ArtistDTO artist;

    public static MusicContentDTO from(MusicContent music) {
        return new MusicContentDTO(
            music.getId(),
            music.getThumbnailUrl(),
            music.getMusicContentUrl(),
            music.getVideoContentUrl(),
            music.getTitle(),
            ArtistDTO.from(music.getArtist())
        );
    }
}
