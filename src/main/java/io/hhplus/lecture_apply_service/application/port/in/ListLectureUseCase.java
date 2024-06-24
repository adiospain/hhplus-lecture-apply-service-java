package io.hhplus.lecture_apply_service.application.port.in;

import io.hhplus.lecture_apply_service.presentation.dto.res.ApplyLectureAPIResponse;
import io.hhplus.lecture_apply_service.presentation.dto.res.ListLectureAPIResponse;
import org.springframework.transaction.annotation.Transactional;

public interface ListLectureUseCase {

  ListLectureAPIResponse execute();
}
