package io.hhplus.lecture_apply_service.infrastructure.repository;

import io.hhplus.lecture_apply_service.infrastructure.entity.StudentLectureJpaEntity;
import java.util.List;
public interface StudentLectureRepository {
  StudentLectureJpaEntity save (StudentLectureJpaEntity lecture);

  boolean existsByStudentIdAndLectureIdAndEnrollmentIsTrue(Long studentId, Long lectureId);

  List<StudentLectureJpaEntity> findAllByStudentId(Long studentId);
}
