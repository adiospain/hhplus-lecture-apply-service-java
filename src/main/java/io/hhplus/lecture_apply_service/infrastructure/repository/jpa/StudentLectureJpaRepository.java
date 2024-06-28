package io.hhplus.lecture_apply_service.infrastructure.repository.jpa;

import io.hhplus.lecture_apply_service.application.port.out.persistence.LectureId;
import io.hhplus.lecture_apply_service.application.port.out.persistence.StudentLectureId;
import io.hhplus.lecture_apply_service.infrastructure.entity.StudentLecture;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentLectureJpaRepository extends JpaRepository<StudentLecture, StudentLectureId> {


  boolean existsByStudentIdAndLectureIdAndEnrollmentIsTrue(Long studentId, LectureId lectureId);

  List<StudentLecture> findAllByStudentId(Long studentId);
}
