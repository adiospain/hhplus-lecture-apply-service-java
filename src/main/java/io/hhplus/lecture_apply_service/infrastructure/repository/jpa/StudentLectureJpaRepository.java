package io.hhplus.lecture_apply_service.infrastructure.repository.jpa;

import io.hhplus.lecture_apply_service.infrastructure.entity.StudentLecture;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentLectureJpaRepository extends JpaRepository<StudentLecture, Long> {


  boolean existsByStudentIdAndLectureIdAndEnrollmentIsTrue(Long studentId, Long lectureId);

  List<StudentLecture> findAllByStudentId(Long studentId);
}
