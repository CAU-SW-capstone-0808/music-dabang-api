package com.anarchyadventure.music_dabang_api.exceptions;

public class UnauthenticatedException extends BaseException {
    private static final ErrorCode code = ErrorCode.UNAUTHENTICATED;

    public UnauthenticatedException() {
        super(code);
    }

    public UnauthenticatedException(String message) {
        super(message, code);
    }

    public UnauthenticatedException(Throwable cause) {
        super(code, cause);
    }
}
