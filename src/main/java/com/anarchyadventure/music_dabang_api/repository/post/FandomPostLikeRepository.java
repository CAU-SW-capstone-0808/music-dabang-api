package com.anarchyadventure.music_dabang_api.repository.post;

import com.anarchyadventure.music_dabang_api.entity.post.FandomPost;
import com.anarchyadventure.music_dabang_api.entity.post.FandomPostLike;
import com.anarchyadventure.music_dabang_api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FandomPostLikeRepository extends JpaRepository<FandomPostLike, Long> {
    boolean existsByUserAndFandomPost(User user, FandomPost fandomPost);

    @Query("SELECT pl FROM FandomPostLike pl" +
            " JOIN FETCH pl.fandomPost" +
            " WHERE pl.fandomPost.id = :postId AND pl.fandomPost.user.id = :userId")
    Optional<FandomPostLike> findByPostIdAndUserId(Long postId, Long userId);
}
