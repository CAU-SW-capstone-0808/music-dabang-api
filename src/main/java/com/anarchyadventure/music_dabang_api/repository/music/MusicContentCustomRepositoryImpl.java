package com.anarchyadventure.music_dabang_api.repository.music;

import com.anarchyadventure.music_dabang_api.dto.common.PageRequest;
import com.anarchyadventure.music_dabang_api.entity.music.Playlist;
import com.anarchyadventure.music_dabang_api.entity.music.PlaylistItem;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
public class MusicContentCustomRepositoryImpl implements MusicContentCustomRepository {
    private final EntityManager entityManager;

    @Override
    public List<Playlist> paginateMainPlaylist(PageRequest pageRequest) {
        entityManager.createQuery("SELECT pl FROM Playlist pl" +
                " WHERE pl.usedForSystem = true", Playlist.class)
            .setMaxResults(pageRequest.getSize())
            .getResultList();
        return null;
    }

    @Override
    public List<PlaylistItem> paginatePlaylistItems(Long playlistId, PageRequest pageRequest) {
        return null;
    }
}
