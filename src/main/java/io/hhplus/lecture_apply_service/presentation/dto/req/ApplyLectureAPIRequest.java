package io.hhplus.lecture_apply_service.presentation.dto.req;

import io.hhplus.lecture_apply_service.application.port.out.persistence.LectureId;
import io.hhplus.lecture_apply_service.infrastructure.entity.Lecture;
import java.time.LocalDateTime;

public record ApplyLectureAPIRequest(
        long studentId,
        long lectureId,
        LocalDateTime startAt,
        LocalDateTime requestAt
) {
}
