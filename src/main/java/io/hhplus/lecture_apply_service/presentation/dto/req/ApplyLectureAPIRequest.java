package io.hhplus.lecture_apply_service.presentation.dto.req;

public record ApplyLectureAPIRequest(
        long studentId,
        long lectureId
) {
}
