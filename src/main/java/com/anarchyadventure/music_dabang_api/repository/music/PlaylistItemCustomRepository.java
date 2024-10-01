package com.anarchyadventure.music_dabang_api.repository.music;

import com.anarchyadventure.music_dabang_api.dto.common.PageRequest;
import com.anarchyadventure.music_dabang_api.entity.music.PlaylistItem;

import java.util.List;

public interface PlaylistItemCustomRepository {
    List<PlaylistItem> paginateMyPlaylistItems(Long userId, PageRequest pageRequest);

    List<PlaylistItem> paginatePlaylistItems(Long userId, Long playlistId, PageRequest pageRequest);

    Long countAllMyPlaylistItems(Long userId);

    Long lastOrderingNumInPlaylist(Long playlistId);

    Long lastOrderingNumInMyMusicList(Long userId);

    boolean isMusicItemInMyMusicList(Long userId, Long musicId);
}
