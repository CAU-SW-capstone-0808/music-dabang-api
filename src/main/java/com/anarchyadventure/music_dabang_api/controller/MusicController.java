package com.anarchyadventure.music_dabang_api.controller;

import com.anarchyadventure.music_dabang_api.dto.common.PageRequest;
import com.anarchyadventure.music_dabang_api.dto.common.PageResponse;
import com.anarchyadventure.music_dabang_api.dto.music.MusicContentDTO;
import com.anarchyadventure.music_dabang_api.dto.music.playlist.PlaylistDTO;
import com.anarchyadventure.music_dabang_api.dto.music.playlist.PlaylistItemDTO;
import com.anarchyadventure.music_dabang_api.entity.music.MusicContentType;
import com.anarchyadventure.music_dabang_api.service.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/music")
@RequiredArgsConstructor
public class MusicController {
    private final MusicService musicService;

    // 메인 화면에 있는 모든 플레이리스트를 가져옴
    @GetMapping("/playlists/main")
    public ResponseEntity<PageResponse<PlaylistDTO>> getMainPlaylists(@ModelAttribute PageRequest pageRequest) {
        return ResponseEntity.ok(musicService.findAllPlaylists(pageRequest));
    }

    @GetMapping("/playlists/my/items")
    public ResponseEntity<PageResponse<PlaylistItemDTO>> getMyPlaylistItems(@ModelAttribute PageRequest pageRequest) {
        return ResponseEntity.ok(musicService.findAllMyPlaylistItems(pageRequest));
    }

    @PostMapping("/playlists/my/items")
    public ResponseEntity<PlaylistItemDTO> addMyPlaylistItem(@RequestParam(name = "music_id") Long musicId) {
        return ResponseEntity.ok(musicService.addMyPlaylistItem(musicId));
    }

    @DeleteMapping("/playlists/items")
    public ResponseEntity<?> deletePlaylistItem(
        @RequestParam(name = "item_id") List<Long> itemIds) {
        musicService.deletePlaylistItem(itemIds);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/playlists/{playlistId}/items")
    public ResponseEntity<PageResponse<PlaylistItemDTO>> getPlaylistItems(
        @PathVariable Long playlistId, @ModelAttribute PageRequest pageRequest) {
        return ResponseEntity.ok(musicService.findPlaylistItems(playlistId, pageRequest));
    }

    @PostMapping("/playlists/{playlistId}/items")
    public ResponseEntity<PlaylistItemDTO> addPlaylistItem(
        @PathVariable Long playlistId, @RequestParam(name = "music_id") Long musicId) {
        return ResponseEntity.ok(musicService.addPlaylistItem(playlistId, musicId));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<MusicContentDTO>> searchMusic(
        @RequestParam(name = "q", required = false) String query,
        @RequestParam(name = "type", required = false) String musicContentType,
        @ModelAttribute PageRequest pageRequest) {
        MusicContentType mcType = MusicContentType.from(musicContentType);
        return ResponseEntity.ok(musicService.searchMusic(query, mcType, pageRequest));
    }

    @GetMapping("/search/autocomplete")
    public ResponseEntity<List<String>> autoCompleteSearchKeyword(
        @RequestParam(name = "q") String query,
        @RequestParam(name = "limit", defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(musicService.autoCompleteSearchKeyword(query, limit));
    }

//    public ResponseEntity<List<Music>>
}
