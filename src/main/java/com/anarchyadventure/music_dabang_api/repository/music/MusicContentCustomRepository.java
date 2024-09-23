package com.anarchyadventure.music_dabang_api.repository.music;

import com.anarchyadventure.music_dabang_api.dto.common.PageRequest;
import com.anarchyadventure.music_dabang_api.entity.music.Playlist;
import com.anarchyadventure.music_dabang_api.entity.music.PlaylistItem;

import java.util.List;

public interface MusicContentCustomRepository {
    List<Playlist> paginateMainPlaylist(PageRequest pageRequest);

    List<PlaylistItem> paginatePlaylistItems(Long playlistId, PageRequest pageRequest);
}
