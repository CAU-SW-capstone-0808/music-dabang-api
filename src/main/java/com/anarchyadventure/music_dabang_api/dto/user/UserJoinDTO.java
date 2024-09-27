package com.anarchyadventure.music_dabang_api.dto.user;

import com.anarchyadventure.music_dabang_api.entity.user.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class UserJoinDTO {
    @Length(min = 11, max = 11)
    private String phone; // 01012345678
    @Length(min = 2, max = 10)
    private String nickname; // 홍길동
    @Length(min = 4, max = 20)
    private String password; // 1234
    @NotNull
    private Gender gender; // m, f
    @Length(min = 6, max = 6)
    private String birth; // 000410

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
