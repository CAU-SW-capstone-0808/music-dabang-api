package com.anarchyadventure.music_dabang_api.entity.user;

import com.anarchyadventure.music_dabang_api.entity.BaseEntity;
import com.anarchyadventure.music_dabang_api.entity.music.PlaylistItem;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(
    name = "usr",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"oauth_provider", "oauth_id"})
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private OAuthProvider oauthProvider;

    private String oauthId;

    @Column(nullable = false)
    private String nickname;

    @Setter
    private String profileImageUrl;

    @Column(length = 20, unique = true)
    private String phone;

    private String password; // not null if oauthProvider is NONE

    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 5)
    @ColumnDefault("'USER'")
    private final UserRole role = UserRole.USER;

    private LocalDateTime lastLoginAt;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private final List<PlaylistItem> playlistItems = new ArrayList<>();

    public User(OAuthProvider oauthProvider, String oauthId, String nickname, String profileImageUrl, String phone, LocalDate birthday, Gender gender) {
        this.oauthProvider = oauthProvider;
        this.oauthId = oauthId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.phone = phone;
        this.birthday = birthday;
        this.gender = gender;
    }

    public void onLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }
}
