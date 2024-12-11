package com.anarchyadventure.music_dabang_api.repository.post;

import com.anarchyadventure.music_dabang_api.entity.post.FandomPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FandomPostRepository extends JpaRepository<FandomPost, Long> {
    List<FandomPost> findByArtistIdOrderByCreatedAtDesc(Long artistId); // 최신순
    List<FandomPost> findByArtistIdOrderByLikesDesc(Long artistId); // 좋아요순

    @Query("SELECT p FROM FandomPost p" +
            " LEFT JOIN FETCH p.comments c" +
            " LEFT JOIN FETCH c.replies" +
            " WHERE p.id=:postId")
    Optional<FandomPost> findByIdWithComments(Long postId);
}
