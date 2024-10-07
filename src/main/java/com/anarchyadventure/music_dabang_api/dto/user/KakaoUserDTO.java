package com.anarchyadventure.music_dabang_api.dto.user;

import com.anarchyadventure.music_dabang_api.entity.user.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class KakaoUserDTO {
    private Long id;

    // 외부 API 필드명에 의존하지 않도록 @JsonProperty 사용
    private KakaoUserInfo kakao_account;

    @Data
    public static class KakaoUserInfo {
        private String phone_number; // "+82 10-1234-5678"
        private String birthday; // "0101"
        private String birthyear; // "2000"
        private String birthday_type; // SOLAR, LUNAR
        private String gender; // "male", "female"
        private Profile profile;

        public LocalDate getBirth() {
            // StringUtils 사용
            if (birthday == null || birthyear == null) {
                return null;
            }
            if (birthday.isBlank() || birthyear.isBlank()) {
                return null;
            }

            int year = Integer.parseInt(birthyear);
            int month = Integer.parseInt(birthday.substring(0, 2));
            int day = Integer.parseInt(birthday.substring(2, 4));
            return LocalDate.of(year, month, day);
        }

        public Gender getGender() {
            // null 보다는 UNKNOWN 고려
            if (gender == null) {
                return null;
            }
            if (gender.equals("male")) {
                return Gender.M;
            } else {
                return Gender.F;
            }
        }

        @Data
        public static class Profile {
            private String nickname;
            private String thumbnail_image_url;
            private String profile_image_url;
        }
    }
}
