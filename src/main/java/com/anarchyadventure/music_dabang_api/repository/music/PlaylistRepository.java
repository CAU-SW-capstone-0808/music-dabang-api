package com.anarchyadventure.music_dabang_api.repository.music;

import com.anarchyadventure.music_dabang_api.entity.music.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long>, PlaylistCustomRepository {
    List<Playlist> findAllByUsedForSystemTrue();

    @Query("SELECT p FROM Playlist p" +
        " WHERE p.user.id = :userId AND p.id = :playlistId")
    Optional<Playlist> findByUserIdAndPlaylistId(Long userId, Long playlistId);
}
