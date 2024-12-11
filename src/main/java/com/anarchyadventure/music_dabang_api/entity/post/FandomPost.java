package com.anarchyadventure.music_dabang_api.entity.post;

import com.anarchyadventure.music_dabang_api.entity.BaseEntity;
import com.anarchyadventure.music_dabang_api.entity.music.Artist;
import com.anarchyadventure.music_dabang_api.entity.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FandomPost extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @Column(nullable = false, length = 300)
    private String title;

    @Column(nullable = false, length = 10000)
    private String content;

    private int likes = 0;

    @OneToMany(mappedBy = "fandomPost", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private final List<FandomPostComment> comments = new ArrayList<>();

    public void incrementLikes() {
        this.likes++;
    }

    public void decrementLikes() {
        this.likes--;
    }

    public FandomPost(User user, Artist artist, String title, String content) {
        this.user = user;
        this.artist = artist;
        this.title = title;
        this.content = content;
    }
}
