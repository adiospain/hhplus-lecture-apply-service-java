package io.hhplus.lecture_apply_service.presentation.dto.res;

import io.hhplus.lecture_apply_service.application.port.out.persistence.LectureId;
import java.time.LocalDateTime;


public record ApplyLectureAPIResponse(
        long studentId,
        long lectureId,
        LocalDateTime startAt,
        boolean status) {
}
