package io.hhplus.lecture_apply_service.exception;

import lombok.Getter;
import org.springframework.web.ErrorResponse;

@Getter
public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String runtimeValue;

    public CustomException(ErrorCode errorCode) {
        this(errorCode, "runtimeValue가 존재 하지 않습니다.");
    }

    public CustomException(ErrorCode errorCode, String runtimeValue) {
        this.errorCode = errorCode;
        this.runtimeValue = runtimeValue;
    }
}
