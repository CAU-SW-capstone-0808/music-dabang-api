package com.anarchyadventure.music_dabang_api.entity.music;

import com.anarchyadventure.music_dabang_api.entity.BaseEntity;
import com.anarchyadventure.music_dabang_api.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Playlist extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "playlist_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean usedForSystem = false;

    @Setter
    @Column(nullable = false)
    @ColumnDefault("true")
    private Boolean userVisible = true;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.REMOVE)
    private final List<PlaylistItem> playlistItemList = new ArrayList<>();

    public Playlist(String name, User user) {
        this.name = name;
        this.user = user;
    }
}
