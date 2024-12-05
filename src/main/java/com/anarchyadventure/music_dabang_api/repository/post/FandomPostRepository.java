package com.anarchyadventure.music_dabang_api.repository.post;

import com.anarchyadventure.music_dabang_api.entity.post.FandomPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FandomPostRepository extends JpaRepository<FandomPost, Long> {
    List<FandomPost> findByArtistIdOrderByCreatedAtDesc(Long artistId); // 최신순
    List<FandomPost> findByArtistIdOrderByLikesDesc(Long artistId); // 좋아요순
}
