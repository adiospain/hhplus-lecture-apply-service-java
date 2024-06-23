package io.hhplus.lecture_apply_service.domain.lecture.presentation.controller.dto.req;

public record ApplyLectureAPIRequest(
        long userId,
        long lectureId
) {
}
