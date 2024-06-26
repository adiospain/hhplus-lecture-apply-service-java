package io.hhplus.lecture_apply_service.application.port.in;

import io.hhplus.lecture_apply_service.presentation.dto.res.ApplyLectureAPIResponse;


public interface ApplyLectureUseCase {
    ApplyLectureAPIResponse execute(ApplyLectureCommand command);
}
