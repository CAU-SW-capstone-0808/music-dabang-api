package com.anarchyadventure.music_dabang_api.service;

import com.anarchyadventure.music_dabang_api.dto.music.ArtistDTO;
import com.anarchyadventure.music_dabang_api.dto.music.MusicContentDTO;
import com.anarchyadventure.music_dabang_api.dto.music.NewArtistDTO;
import com.anarchyadventure.music_dabang_api.dto.music.NewMusicContentDTO;
import com.anarchyadventure.music_dabang_api.dto.music.playlist.NewPlaylistDTO;
import com.anarchyadventure.music_dabang_api.dto.music.playlist.NewPlaylistItemDTO;
import com.anarchyadventure.music_dabang_api.dto.music.playlist.PlaylistDTO;
import com.anarchyadventure.music_dabang_api.dto.music.playlist.PlaylistItemDTO;
import com.anarchyadventure.music_dabang_api.entity.music.Artist;
import com.anarchyadventure.music_dabang_api.entity.music.MusicContent;
import com.anarchyadventure.music_dabang_api.entity.music.Playlist;
import com.anarchyadventure.music_dabang_api.entity.music.PlaylistItem;
import com.anarchyadventure.music_dabang_api.entity.user.User;
import com.anarchyadventure.music_dabang_api.entity.user.UserRole;
import com.anarchyadventure.music_dabang_api.repository.music.ArtistRepository;
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
@RequiredArgsConstructor
public class AdminService {
    private final ArtistRepository artistRepository;
    private final MusicContentRepository musicContentRepository;
    private final PlaylistRepository playlistRepository;
    private final PlaylistItemRepository playlistItemRepository;

    public ArtistDTO createArtist(NewArtistDTO newArtistDTO) {
        log.info("createArtist: {}", newArtistDTO);
        Artist artist = new Artist(newArtistDTO.getName());
        artistRepository.save(artist);
        return ArtistDTO.from(artist);
    }

    public MusicContentDTO uploadMusic(NewMusicContentDTO newMusicContentDTO) {
        log.info("uploadMusicContent: {}", newMusicContentDTO);
        Artist artist = artistRepository.findById(newMusicContentDTO.getArtistId())
            .orElseThrow(() -> new EntityNotFoundException("Artist not found"));
        MusicContent musicContent = newMusicContentDTO.toEntity(artist);
        musicContentRepository.save(musicContent);
        return MusicContentDTO.from(musicContent);
    }

    public List<MusicContentDTO> findAllMusic() {
        return musicContentRepository.findAll().stream()
            .map(MusicContentDTO::from)
            .toList();
    }

    public MusicContentDTO editMusic(Long musicId, NewMusicContentDTO newMusicContentDTO) {
        log.info("editMusic: {}", newMusicContentDTO);
        MusicContent musicContent = musicContentRepository.findByIdOpt(musicId)
            .orElseThrow(() -> new EntityNotFoundException("Music not found"));
        musicContent.edit(
            newMusicContentDTO.getThumbnailUrl(),
            newMusicContentDTO.getMusicContentUrl(),
            newMusicContentDTO.getVideoContentUrl(),
            newMusicContentDTO.getTitle()
        );
        // jpa 더티 체킹을 이용하여 update
        musicContentRepository.save(musicContent);
        return MusicContentDTO.from(musicContent);
    }

    public void deleteMusic(Long musicId) {
        log.info("deleteMusic: {}", musicId);
        musicContentRepository.deleteById(musicId);
    }

    public PlaylistDTO createPlaylist(NewPlaylistDTO newPlaylistDTO) {
        log.info("createPlaylist: {}", newPlaylistDTO);
        Playlist playlist = newPlaylistDTO.toEntity(SecurityHandler.getUserAuth(UserRole.ADMIN));
        playlist.setUsedForSystem(true);
        playlistRepository.save(playlist);
        return PlaylistDTO.from(playlist);
    }

    public void addPlaylistItem(Long playlistId, NewPlaylistItemDTO newPlaylistItemDTO) {
        log.info("addPlaylistItem: {}", newPlaylistItemDTO);
        User user = SecurityHandler.getUserAuth(UserRole.ADMIN);
        Playlist playlist = playlistRepository.findById(playlistId)
            .orElseThrow(() -> new EntityNotFoundException("Playlist not found"));
        List<MusicContent> musics =
            musicContentRepository.findAllById(newPlaylistItemDTO.getMusicIds());
        List<PlaylistItem> items = musics.stream()
            .map((mc) -> new PlaylistItem(user, mc, playlist))
            .toList();
        playlistItemRepository.saveAll(items);
    }

    @Transactional(readOnly = true)
    public List<PlaylistItemDTO> findAllPlaylistItems(Long playlistId) {
        List<PlaylistItem> items = playlistItemRepository.findAllByPlaylistId(playlistId);
        return items.stream()
            .map(PlaylistItemDTO::from)
            .toList();
    }
}
