package io.hhplus.lecture_apply_service.infrastructure.repository;

import io.hhplus.lecture_apply_service.application.port.out.persistence.LectureJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<LectureJpaEntity, Long> {

}