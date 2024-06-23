package io.hhplus.lecture_apply_service.infrastructure.repository.jpa;

import io.hhplus.lecture_apply_service.infrastructure.entity.LectureJpaEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LectureJpaRepository extends JpaRepository<LectureJpaEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<LectureJpaEntity> findById (Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    <S extends LectureJpaEntity> S save(S entity);
}