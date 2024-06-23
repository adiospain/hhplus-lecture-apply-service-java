package io.hhplus.lecture_apply_service.domain.lecture.presentation.controller.dto.res;

public record ApplyLectureAPIResponse(
        long userId,
        long lectureId,
        boolean success) {
}
