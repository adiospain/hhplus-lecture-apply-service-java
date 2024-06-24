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
    INVALID_TIME(BAD_REQUEST, "특강 신청 기간이 아닙니다."),

    //404
    LECTURE_NOT_FOUND(NOT_FOUND, "특강을 찾을 수 없습니다."),

    //409

    //500

    ALREADY_APPLIED(BAD_REQUEST, "해당 특강을 이미 수강하고 있습니다."),
    INVALID_TIME(BAD_REQUEST,"아직 특강 신청 기간이 아닙니다.");

    private final HttpStatus httpStatus;
    private final String message;

}