package com.anarchyadventure.music_dabang_api.entity.music;

import com.anarchyadventure.music_dabang_api.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MusicContent extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "music_content_id")
    private Long id;

    @Column(length = 500)
    private String thumbnailUrl;
    @Column(length = 500)
    private String musicContentUrl;
    @Column(length = 500)
    private String videoContentUrl;

    @Column(length = 200)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'MUSIC'")
    private MusicContentType musicContentType = MusicContentType.MUSIC;

    public MusicContent(String thumbnailUrl, String musicContentUrl, String videoContentUrl, String title, Artist artist) {
        // 필수값, 길이 제한 등에 대한 validation (+ dto 보다는 생성자에 기록)
        this.thumbnailUrl = thumbnailUrl;
        this.musicContentUrl = musicContentUrl;
        this.videoContentUrl = videoContentUrl;
        this.title = title;
        this.artist = artist;
    }

    public void edit(String thumbnailUrl, String musicContentUrl, String videoContentUrl, String title) {
        this.thumbnailUrl = thumbnailUrl;
        this.musicContentUrl = musicContentUrl;
        this.videoContentUrl = videoContentUrl;
        this.title = title;
    }
}
