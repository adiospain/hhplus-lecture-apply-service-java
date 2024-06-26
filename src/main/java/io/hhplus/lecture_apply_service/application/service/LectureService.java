package io.hhplus.lecture_apply_service.application.service;

import io.hhplus.lecture_apply_service.application.port.in.ApplyLectureCommand;
import io.hhplus.lecture_apply_service.application.port.in.ApplyLectureUseCase;
import io.hhplus.lecture_apply_service.presentation.dto.res.ApplyLectureAPIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LectureService {

    private final ApplyLectureUseCase applyLectureUseCase;

    public ApplyLectureAPIResponse applyLecture (ApplyLectureCommand command){
        return applyLectureUseCase.execute(command);
    }
}
