package com.anarchyadventure.music_dabang_api.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /**
     * COMMON-ERROR: 공통적으로 처리되는 오류 목록
     */
    // 500 에러로, 집중 모니터링 대상
    SYSTEM_ERROR("일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", 500),
    INVALID_PARAMETER("요청한 값이 올바르지 않습니다.", 400),
    ENTITY_NOT_FOUND("리소스를 찾을 수 없습니다", 404),
    FORBIDDEN("잘못된 접근입니다", 403),
    UNVERIFIED("검증되지 않은 필드가 있습니다", 403),
    UNAUTHORIZED("권한이 없습니다.", 401),
    UNAUTHENTICATED("인증에 실패하였습니다", 401),
    INVALID_ACCESS("잘못된 접근입니다.", 400),
    INVALID_TOKEN("유효하지 않은 토큰값입니다.", 401),
    INVALID_STATE("잘못된 상태입니다.", 409),
    ALREADY_EXIST("이미 존재하는 객체입니다.", 422),
    REQUEST_NUM_OVERFLOW("요청횟수를 초과하였습니다", 429),

    /**
     * User 관련 오류
     */
    USER_PHONE_OVERLAPPED("이미 존재하는 휴대폰 번호입니다", 422),
    USER_LOGIN_FAILED("로그인에 실패하였습니다.", 401);

    private final String description;
    private final int statusCode;
}
