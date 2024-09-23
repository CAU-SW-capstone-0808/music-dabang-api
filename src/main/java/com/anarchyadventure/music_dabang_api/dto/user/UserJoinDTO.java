package com.anarchyadventure.music_dabang_api.dto.user;

import com.anarchyadventure.music_dabang_api.entity.user.Gender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class UserJoinDTO {
    private final String phone; // 01012345678
    private final String nickname; // 홍길동
    private final String password; // 1234
    private final Gender gender; // m, f
    private final String birth; // 000410

    public LocalDate getBirthday() {
        String p1 = birth.substring(0, 2);
        String p2 = birth.substring(2, 4);
        String p3 = birth.substring(4, 6);
        int pi1 = Integer.parseInt(p1);
        int pi2 = Integer.parseInt(p2);
        int pi3 = Integer.parseInt(p3);
        return LocalDate.of(pi1 < 20 ? 2000 + pi1 : 1900 + pi1, pi2, pi3);
    }
}
