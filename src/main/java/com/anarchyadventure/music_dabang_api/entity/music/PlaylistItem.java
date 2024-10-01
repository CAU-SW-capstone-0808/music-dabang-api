package com.anarchyadventure.music_dabang_api.entity.music;

import com.anarchyadventure.music_dabang_api.entity.BaseEntity;
import com.anarchyadventure.music_dabang_api.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    /**
     * 재생목록 내 순서
     * 1. 기본적으로 마지막 아이템의 orderingNum + 1000
     * 2. 클라이언트가 순서를 변경할 경우, 해당 순서로 변경
     * 3. 계산은 클라이언트 내에서 이루어짐
     *  - 클라이언트에서 orderingNum을 직접 지정
     *  - 만약 orderingNum이 겹치는 경우, 연쇄적으로 변경(변경이 없을 때까지)
     */
    @Setter
    @Column(nullable = false)
    @ColumnDefault("0")
    private Long orderingNum = 0L;

    public PlaylistItem(User user, MusicContent musicContent, Playlist playlist) {
        this.user = user;
        this.musicContent = musicContent;
        this.playlist = playlist;
    }
}
