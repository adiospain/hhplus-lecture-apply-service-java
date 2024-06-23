package io.hhplus.lecture_apply_service.presentation.dto.res;

public record ApplyLectureAPIResponse(
        long userId,
        long lectureId,
        boolean success) {
}
