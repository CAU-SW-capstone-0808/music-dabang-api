package com.anarchyadventure.music_dabang_api.exceptions;

public class InvalidParameterException extends BaseException {
    private static final ErrorCode code = ErrorCode.INVALID_PARAMETER;

    public InvalidParameterException() {
        super(code);
    }

    public InvalidParameterException(String message) {
        super(message, code);
    }

    public InvalidParameterException(Throwable cause) {
        super(code, cause);
    }
}
