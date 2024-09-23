package com.anarchyadventure.music_dabang_api.repository.music;

import com.anarchyadventure.music_dabang_api.dto.common.PageRequest;
import com.anarchyadventure.music_dabang_api.entity.music.Playlist;

import java.util.List;

public interface PlaylistCustomRepository {
    List<Playlist> paginateMainPlaylist(PageRequest pageRequest);
}
