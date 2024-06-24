package io.hhplus.lecture_apply_service.infrastructure.repository;

import io.hhplus.lecture_apply_service.infrastructure.entity.StudentLectureJpaEntity;

public interface StudentLectureRepository {
  StudentLectureJpaEntity save (StudentLectureJpaEntity lecture);

  boolean existsByStudentIdAndLectureIdAndEnrollmentIsTrue(Long studentId, Long lectureId);
}
