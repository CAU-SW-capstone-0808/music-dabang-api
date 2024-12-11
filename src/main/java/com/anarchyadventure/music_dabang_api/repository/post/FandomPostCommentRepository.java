package com.anarchyadventure.music_dabang_api.repository.post;

import com.anarchyadventure.music_dabang_api.entity.post.FandomPostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FandomPostCommentRepository extends JpaRepository<FandomPostComment, Long> {
    List<FandomPostComment> findByFandomPostId(Long fandomPostId);

    @Query("SELECT c FROM FandomPostComment c" +
            " JOIN FETCH c.fandomPost" +
            " WHERE c.parent IS NULL AND c.fandomPost.id=:postId AND c.id=:commentId")
    Optional<FandomPostComment> findByPostIdAndCommentId(Long postId, Long commentId);
}
