package com.anarchyadventure.music_dabang_api.entity.post;

import com.anarchyadventure.music_dabang_api.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FandomPostLike {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fandom_post_id", nullable = false)
    private FandomPost fandomPost;

    public FandomPostLike(User user, FandomPost fandomPost) {
        this.user = user;
        this.fandomPost = fandomPost;
    }
}
