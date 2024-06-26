package io.hhplus.lecture_apply_service.application.port.in;

import io.hhplus.lecture_apply_service.common.UseCase;
import io.hhplus.lecture_apply_service.infrastructure.entity.StudentLecture;
import io.hhplus.lecture_apply_service.infrastructure.repository.StudentLectureRepository;
import io.hhplus.lecture_apply_service.presentation.dto.res.EnrolledLectureAPIResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@UseCase
public class EnrolledLectureUseCaseImpl implements EnrolledLectureUseCase {
  private final StudentLectureRepository studentLectureRepository;
  @Override
  public EnrolledLectureAPIResponse execute(long studentId) {
    List<StudentLecture> enrolledLectures = studentLectureRepository.findAllByStudentId(studentId);
    EnrolledLectureAPIResponse response = new EnrolledLectureAPIResponse(enrolledLectures, !enrolledLectures.isEmpty());
    return response;
  }
}
