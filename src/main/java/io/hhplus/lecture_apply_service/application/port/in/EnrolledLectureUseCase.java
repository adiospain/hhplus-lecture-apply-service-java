package io.hhplus.lecture_apply_service.application.port.in;


import io.hhplus.lecture_apply_service.presentation.dto.res.EnrolledLectureAPIResponse;

public interface EnrolledLectureUseCase {

  EnrolledLectureAPIResponse execute(long userId);
}
