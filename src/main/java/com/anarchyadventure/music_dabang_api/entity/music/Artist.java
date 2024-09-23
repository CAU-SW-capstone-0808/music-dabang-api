package com.anarchyadventure.music_dabang_api.entity.music;

import com.anarchyadventure.music_dabang_api.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Artist extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "artist_id")
    private Long id;

    private String name;
    
    @OneToMany(mappedBy = "artist", cascade = CascadeType.REMOVE)
    private List<MusicContent> musicContentList = new ArrayList<>();

    public Artist(String name) {
        this.name = name;
    }
}
