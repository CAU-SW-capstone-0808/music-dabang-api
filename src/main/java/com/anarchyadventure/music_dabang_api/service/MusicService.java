package com.anarchyadventure.music_dabang_api.service;

import com.anarchyadventure.music_dabang_api.dto.common.PageRequest;
import com.anarchyadventure.music_dabang_api.dto.music.MusicContentDTO;
import com.anarchyadventure.music_dabang_api.dto.music.playlist.PlaylistDTO;
import com.anarchyadventure.music_dabang_api.dto.music.playlist.PlaylistItemDTO;
import com.anarchyadventure.music_dabang_api.entity.music.MusicContent;
import com.anarchyadventure.music_dabang_api.entity.music.Playlist;
import com.anarchyadventure.music_dabang_api.entity.music.PlaylistItem;
import com.anarchyadventure.music_dabang_api.repository.music.MusicContentRepository;
import com.anarchyadventure.music_dabang_api.repository.music.PlaylistItemRepository;
import com.anarchyadventure.music_dabang_api.repository.music.PlaylistRepository;
import com.anarchyadventure.music_dabang_api.security.SecurityHandler;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MusicService {
    private final MusicContentRepository musicContentRepository;
    private final PlaylistRepository playlistRepository;
    private final PlaylistItemRepository playlistItemRepository;

    @Transactional(readOnly = true)
    public List<PlaylistDTO> findAllPlaylists(PageRequest pageRequest) {
        List<Playlist> playlists = playlistRepository.findAllByUsedForSystemTrue();
        return playlists.stream()
            .map(PlaylistDTO::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<PlaylistItemDTO> findAllMyPlaylistItems() {
        List<PlaylistItem> items = playlistItemRepository.findAllMyPlaylistItems(SecurityHandler.getUserAuth().getId());
        return items.stream()
            .map(PlaylistItemDTO::from)
            .toList();
    }

    public PlaylistItemDTO addMyPlaylistItem(Long musicId) {
        MusicContent music = musicContentRepository.findByIdOpt(musicId)
            .orElseThrow(() -> new EntityNotFoundException("Music not found"));
        PlaylistItem item = new PlaylistItem(SecurityHandler.getUserAuth(), music, null);
        playlistItemRepository.save(item);
        return PlaylistItemDTO.from(item);
    }

    public void deletePlaylistItem(List<Long> itemIds) {
        playlistItemRepository.deleteAllByItemIdsAndUserId(itemIds, SecurityHandler.getUserAuth().getId());
    }

    @Transactional(readOnly = true)
    public List<PlaylistItemDTO> findPlaylistItems(Long playlistId) {
        List<PlaylistItem> items = playlistItemRepository.findAllByUserIdAndPlaylistId(
            SecurityHandler.getUserAuth().getId(), playlistId);
        return items.stream()
            .map(PlaylistItemDTO::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<MusicContentDTO> searchMusic(String query) {
        List<MusicContent> musicContents = musicContentRepository
            .findAllByKeyword(query);
        return musicContents.stream()
            .map(MusicContentDTO::from)
            .toList();
    }
}
