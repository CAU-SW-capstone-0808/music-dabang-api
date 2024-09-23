package com.anarchyadventure.music_dabang_api.exceptions;

public class UnauthorizedException extends BaseException {
    private static final ErrorCode code = ErrorCode.UNAUTHORIZED;

    public UnauthorizedException() {
        super(code);
    }

    public UnauthorizedException(String message) {
        super(message, code);
    }

    public UnauthorizedException(Throwable cause) {
        super(code, cause);
    }
}
