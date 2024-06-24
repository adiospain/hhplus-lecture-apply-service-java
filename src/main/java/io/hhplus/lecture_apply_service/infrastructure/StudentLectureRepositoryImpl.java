package io.hhplus.lecture_apply_service.infrastructure;

import io.hhplus.lecture_apply_service.infrastructure.entity.StudentLectureJpaEntity;
import io.hhplus.lecture_apply_service.infrastructure.repository.StudentLectureRepository;
import io.hhplus.lecture_apply_service.infrastructure.repository.jpa.StudentLectureJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class StudentLectureRepositoryImpl implements StudentLectureRepository {

  private StudentLectureJpaRepository studentLectureRepository;

  @Override
  public StudentLectureJpaEntity save(StudentLectureJpaEntity lecture) {
    return studentLectureRepository.save(lecture);
  }

  @Override
  public boolean existsByStudentIdAndLectureIdAndEnrollmentIsTrue(Long studentId, Long lectureId) {
    return studentLectureRepository.existsByStudentIdAndLectureIdAndEnrollmentIsTrue(studentId, lectureId);
  }
}
