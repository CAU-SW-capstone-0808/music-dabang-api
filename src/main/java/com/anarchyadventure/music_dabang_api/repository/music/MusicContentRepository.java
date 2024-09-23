package com.anarchyadventure.music_dabang_api.repository.music;

import com.anarchyadventure.music_dabang_api.entity.music.MusicContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MusicContentRepository extends JpaRepository<MusicContent, Long>, MusicContentCustomRepository {
    @Query("SELECT mc" +
        " FROM MusicContent mc" +
        " JOIN FETCH mc.artist WHERE mc.id = :id")
    Optional<MusicContent> findByIdOpt(Long id);

    @Query("SELECT mc" +
        " FROM MusicContent mc" +
        " JOIN FETCH mc.artist" +
        " WHERE mc.title LIKE %:keyword% OR mc.artist.name LIKE %:keyword%")
    List<MusicContent> findAllByKeyword(String keyword);
}
