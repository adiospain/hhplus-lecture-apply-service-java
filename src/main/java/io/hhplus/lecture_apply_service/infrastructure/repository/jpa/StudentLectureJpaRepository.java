package io.hhplus.lecture_apply_service.infrastructure.repository.jpa;

import io.hhplus.lecture_apply_service.infrastructure.entity.StudentLectureJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentLectureJpaRepository extends JpaRepository<StudentLectureJpaEntity, Long> {


}
