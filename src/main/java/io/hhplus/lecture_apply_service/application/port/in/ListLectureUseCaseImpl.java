package io.hhplus.lecture_apply_service.application.port.in;

import io.hhplus.lecture_apply_service.common.UseCase;
import io.hhplus.lecture_apply_service.exception.CustomException;
import io.hhplus.lecture_apply_service.exception.ErrorCode;
import io.hhplus.lecture_apply_service.infrastructure.entity.Lecture;
import io.hhplus.lecture_apply_service.infrastructure.repository.LectureRepository;
import io.hhplus.lecture_apply_service.presentation.dto.res.ListLectureAPIResponse;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@UseCase
public class ListLectureUseCaseImpl implements ListLectureUseCase{
  private final LectureRepository lectureRepository;

  @Override
  public ListLectureAPIResponse execute() {
    List<Lecture> lectures = lectureRepository.findAll();
    return Optional.ofNullable(lectures)
        .filter(list -> !list.isEmpty())
        .map(list -> new ListLectureAPIResponse(list))
        .orElseThrow(()->new CustomException(ErrorCode.LECTURE_NOT_FOUND));
  }

}
