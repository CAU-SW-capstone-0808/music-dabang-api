package com.anarchyadventure.music_dabang_api.controller;

import com.anarchyadventure.music_dabang_api.dto.music.ArtistDTO;
import com.anarchyadventure.music_dabang_api.dto.music.MusicContentDTO;
import com.anarchyadventure.music_dabang_api.dto.music.NewArtistDTO;
import com.anarchyadventure.music_dabang_api.dto.music.NewMusicContentDTO;
import com.anarchyadventure.music_dabang_api.dto.music.playlist.NewPlaylistDTO;
import com.anarchyadventure.music_dabang_api.dto.music.playlist.NewPlaylistItemDTO;
import com.anarchyadventure.music_dabang_api.dto.music.playlist.PlaylistDTO;
import com.anarchyadventure.music_dabang_api.dto.music.playlist.PlaylistItemDTO;
import com.anarchyadventure.music_dabang_api.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/artist")
    public ResponseEntity<ArtistDTO> createArtist(@Validated @RequestBody NewArtistDTO newArtistDTO) {
        return ResponseEntity.ok(adminService.createArtist(newArtistDTO));
    }

    @PostMapping("/music")
    public ResponseEntity<MusicContentDTO> uploadMusic(@Validated @RequestBody NewMusicContentDTO newMusicContentDTO) {
        return ResponseEntity.ok(adminService.uploadMusic(newMusicContentDTO));
    }

    @PatchMapping("/music/{musicId}")
    public ResponseEntity<MusicContentDTO> editMusic(
        @PathVariable Long musicId, @Validated @RequestBody NewMusicContentDTO newMusicContentDTO) {
        return ResponseEntity.ok(adminService.editMusic(musicId, newMusicContentDTO));
    }

    @DeleteMapping("/music/{musicId}")
    public ResponseEntity<?> deleteMusic(@PathVariable Long musicId) {
        adminService.deleteMusic(musicId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/playlist")
    public ResponseEntity<PlaylistDTO> createPlaylist(@Validated @RequestBody NewPlaylistDTO newPlaylistDTO) {
        return ResponseEntity.ok(adminService.createPlaylist(newPlaylistDTO));
    }

    @PostMapping("/playlist/{playlistId}/items")
    public ResponseEntity<?> addPlaylistItems(
        @PathVariable Long playlistId, @Validated @RequestBody NewPlaylistItemDTO newPlaylistItemDTO) {
        adminService.addPlaylistItem(playlistId, newPlaylistItemDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/playlist/{playlistId}/items")
    public ResponseEntity<List<PlaylistItemDTO>> getAllPlaylistItems(@PathVariable Long playlistId) {
        return ResponseEntity.ok(adminService.findAllPlaylistItems(playlistId));
    }
}
