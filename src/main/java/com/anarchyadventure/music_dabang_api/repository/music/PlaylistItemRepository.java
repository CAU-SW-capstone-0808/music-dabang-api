package com.anarchyadventure.music_dabang_api.repository.music;

import com.anarchyadventure.music_dabang_api.entity.music.PlaylistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlaylistItemRepository extends JpaRepository<PlaylistItem, Long>, PlaylistItemCustomRepository {
    @Query("SELECT pi FROM PlaylistItem pi" +
        " JOIN FETCH pi.user" +
        " JOIN FETCH pi.musicContent mc" +
        " JOIN FETCH mc.artist" +
        " WHERE pi.id = :id")
    Optional<PlaylistItem> findByIdOpt(Long id);

    @Modifying
    @Query("DELETE FROM PlaylistItem pi" +
        " WHERE pi.id IN :itemIds AND pi.user.id = :userId")
    void deleteAllByItemIdsAndUserId(List<Long> itemIds, Long userId);

    @Query("SELECT pi FROM PlaylistItem pi" +
        " JOIN FETCH pi.musicContent mc" +
        " JOIN FETCH mc.artist" +
        " WHERE pi.playlist.id = :playlistId")
    List<PlaylistItem> findAllByPlaylistId(Long playlistId);

    @Query("SELECT pi FROM PlaylistItem pi" +
        " JOIN pi.playlist" +
        " JOIN FETCH pi.musicContent mc" +
        " JOIN FETCH mc.artist" +
        " WHERE pi.playlist.id = :playlistId AND (pi.user.id = :userId OR pi.playlist.usedForSystem = true)")
    List<PlaylistItem> findAllByUserIdAndPlaylistId(Long userId, Long playlistId);

//    @Query("SELECT pi FROM PlaylistItem pi" +
//        " JOIN FETCH pi.musicContent mc" +
//        " JOIN FETCH mc.artist" +
//        " WHERE pi.user.id = :userId AND pi.playlist IS NULL")
//    List<PlaylistItem> findAllMyPlaylistItems(Long userId);

    Long countAllByPlaylistId(Long playlistId);
}
