package io.hhplus.lecture_apply_service.infrastructure.repository;

import io.hhplus.lecture_apply_service.infrastructure.entity.StudentLecture;
import java.util.List;
public interface StudentLectureRepository {
  StudentLecture save (StudentLecture lecture);

  boolean existsByStudentIdAndLectureIdAndEnrollmentIsTrue(Long studentId, Long lectureId);

  List<StudentLecture> findAllByStudentId(Long studentId);
}
