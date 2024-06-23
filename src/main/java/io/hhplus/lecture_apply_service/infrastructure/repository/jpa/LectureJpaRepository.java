package io.hhplus.lecture_apply_service.infrastructure.repository.jpa;

import io.hhplus.lecture_apply_service.infrastructure.entity.LectureJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LectureJpaRepository extends JpaRepository<LectureJpaEntity, Long> {

    @Query("select l from lecture b where b.id = :id")
    Optional<LectureJpaEntity> findById (Long id);
}