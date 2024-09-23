package com.anarchyadventure.music_dabang_api.service;

import com.anarchyadventure.music_dabang_api.dto.user.KakaoUserDTO;
import com.anarchyadventure.music_dabang_api.exceptions.BaseException;
import com.anarchyadventure.music_dabang_api.exceptions.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoApiHelper {
    private final RestTemplate restTemplate;

    public KakaoUserDTO getUserInfo(String accessToken) {
        // Define the endpoint URL
        String url = "https://kapi.kakao.com/v2/user/me";

        try {
            // Create HttpHeaders and set the necessary headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // Create an HttpEntity with the headers
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Make the GET request with headers using exchange method
            ResponseEntity<KakaoUserDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                KakaoUserDTO.class
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to get user info from Kakao API", e);
            throw new BaseException("Failed to get user info from Kakao API", ErrorCode.USER_LOGIN_FAILED);
        }
    }
}
