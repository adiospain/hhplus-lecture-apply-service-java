package io.hhplus.lecture_apply_service.presentation.dto.res;

public record ApplyLectureAPIResponse(
        long studentId,
        long lectureId,
        boolean status) {
}
