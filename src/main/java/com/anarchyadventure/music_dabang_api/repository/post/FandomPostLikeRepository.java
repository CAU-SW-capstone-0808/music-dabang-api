package com.anarchyadventure.music_dabang_api.repository.post;

import com.anarchyadventure.music_dabang_api.entity.post.FandomPost;
import com.anarchyadventure.music_dabang_api.entity.post.FandomPostLike;
import com.anarchyadventure.music_dabang_api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FandomPostLikeRepository extends JpaRepository<FandomPostLike, Long> {
    boolean existsByUserAndFandomPost(User user, FandomPost fandomPost);
}
