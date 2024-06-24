package io.hhplus.lecture_apply_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //400

    INVALID_LECTURE_ID(BAD_REQUEST, "유효하지 않은 특강 ID 입니다."),
    INVALID_STUDENT_ID(BAD_REQUEST, "유효하지 않은 학생 ID 입니다."),

    //404


    //409

    //500

    DUMMY(BAD_REQUEST, "Dummy");
    private final HttpStatus httpStatus;
    private final String message;

}