package io.hhplus.lecture_apply_service.infrastructure.repository.jpa;

import io.hhplus.lecture_apply_service.infrastructure.entity.LectureJpaEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import org.springframework.data.repository.query.Param;

public interface LectureJpaRepository extends JpaRepository<LectureJpaEntity, Long> {


    Optional<LectureJpaEntity> findById (Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM LectureJpaEntity l WHERE l.id = :lectureId")
    Optional<LectureJpaEntity> findByIdxLock(Long lectureId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("UPDATE LectureJpaEntity l SET l.name = :#{#lectureJpaEntity.name}, l.capacity = :#{#lectureJpaEntity.capacity} WHERE l.id = :#{#lectureJpaEntity.id}")
    LectureJpaEntity savexLock(@Param("lectureJpaEntity") LectureJpaEntity lectureJpaEntity);
}