package io.hhplus.lecture_apply_service.infrastructure.repository.jpa;

import io.hhplus.lecture_apply_service.infrastructure.entity.LectureJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureJpaRepository extends JpaRepository<LectureJpaEntity, Long> {

}