package com.anarchyadventure.music_dabang_api.exceptions;

public class InvalidAccessException extends BaseException {
    private static final ErrorCode code = ErrorCode.INVALID_ACCESS;

    public InvalidAccessException() {
        super(code);
    }

    public InvalidAccessException(String message) {
        super(message, code);
    }

    public InvalidAccessException(Throwable cause) {
        super(code, cause);
    }
}
