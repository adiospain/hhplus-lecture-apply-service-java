package io.hhplus.lecture_apply_service.application.port.in;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApplyLectureCommand {

    private Long studentId;
    private Long lectureId;
    private LocalDateTime startAt;
    private LocalDateTime requestAt;

}
