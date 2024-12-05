package com.anarchyadventure.music_dabang_api.entity.post;

import com.anarchyadventure.music_dabang_api.entity.BaseEntity;
import com.anarchyadventure.music_dabang_api.entity.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FandomPostComment extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fandom_post_id", nullable = false)
    @JsonBackReference
    private FandomPost fandomPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private FandomPostComment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FandomPostComment> replies = new ArrayList<>();

    private String content;

    public FandomPostComment(User user, FandomPost fandomPost, String content) {//댓글생성
        this.user = user;
        this.fandomPost = fandomPost;
        this.content = content;
    }

    public FandomPostComment(User user, FandomPostComment parent, String content) {//대댓글 생성
        this.user = user;
        this.parent = parent;
        this.fandomPost = parent.getFandomPost();
        this.content = content;
    }
}
