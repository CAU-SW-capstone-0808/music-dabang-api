package com.anarchyadventure.music_dabang_api.repository.post;

import com.anarchyadventure.music_dabang_api.entity.post.FandomPostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FandomPostCommentRepository extends JpaRepository<FandomPostComment, Long> {
    List<FandomPostComment> findByFandomPostId(Long fandomPostId);
}
