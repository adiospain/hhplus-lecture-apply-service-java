package io.hhplus.lecture_apply_service.infrastructure.repository.jpa;

import io.hhplus.lecture_apply_service.infrastructure.entity.StudentLectureJpaEntity;
import jakarta.persistence.LockModeType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface StudentLectureJpaRepository extends JpaRepository<StudentLectureJpaEntity, Long> {


  boolean existsByStudentIdAndLectureIdAndEnrollmentIsTrue(Long studentId, Long lectureId);

  List<StudentLectureJpaEntity> findAllByStudentId(Long studentId);
}
