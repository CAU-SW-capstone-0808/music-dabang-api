package com.anarchyadventure.music_dabang_api.dto.music;

import com.anarchyadventure.music_dabang_api.entity.music.Artist;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ArtistDTO {
    private final Long id;
    private final String name;

    public static ArtistDTO from(Artist artist) {
        return new ArtistDTO(artist.getId(), artist.getName());
    }
}
