package com.example.common.exception;

import lombok.Builder;
import lombok.Getter;

/**
 * base common exception.
 * Inherit and implement classes when necessary.
 *
 * @author openlabs
 *
 */
@Getter
public class CommonException extends RuntimeException {

    private static final long serialVersionUID = -4982348093873946553L;

    private final String errorCode;
    private final String[] args;

    @Builder
    public CommonException(String errorCode, String message, Throwable cause, String... args) {
        super(message, cause);
        this.errorCode = errorCode;
        this.args = args;
    }

    public static CommonException create(String errorCode) {
        return CommonException.builder().errorCode(errorCode).build();
    }

    public static CommonException create(String errorCode, Throwable cause) {
        return CommonException.builder().errorCode(errorCode).cause(cause).build();
    }

    public static CommonException create(String errorCode, String... args) {
        return CommonException.builder().errorCode(errorCode).args(args).build();
    }

    public static CommonException create(String errorCode, Throwable cause, String... args) {
        return CommonException.builder().errorCode(errorCode).cause(cause).args(args).build();
    }
}
