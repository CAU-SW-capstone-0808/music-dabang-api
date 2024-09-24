package com.anarchyadventure.music_dabang_api.entity.music;

import com.anarchyadventure.music_dabang_api.entity.BaseEntity;
import com.anarchyadventure.music_dabang_api.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

/**
 * 추후 확장 -> PlayList 추가
 * playList == null -> 기본 재생목록
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaylistItem extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "playlist_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "music_content_id")
    private MusicContent musicContent;

    @ManyToOne(fetch = FetchType.LAZY)
    private Playlist playlist;

    @Column(nullable = false)
    @ColumnDefault("1")
    private Integer orderingNum = 1;

    public PlaylistItem(User user, MusicContent musicContent, Playlist playlist) {
        this.user = user;
        this.musicContent = musicContent;
        this.playlist = playlist;
    }
}
