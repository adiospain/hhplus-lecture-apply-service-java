package io.hhplus.lecture_apply_service.application.port.in;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApplyLectureCommand {

    private Long userId;
    private Long lectureId;

}
