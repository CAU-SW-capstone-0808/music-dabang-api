package com.anarchyadventure.music_dabang_api.exceptions;

public class EntityAlreadyExistException extends BaseException {
    private static final ErrorCode code = ErrorCode.ALREADY_EXIST;

    public EntityAlreadyExistException() {
        super(code);
    }

    public EntityAlreadyExistException(String message) {
        super(message, code);
    }

    public EntityAlreadyExistException(Throwable cause) {
        super(code, cause);
    }
}
