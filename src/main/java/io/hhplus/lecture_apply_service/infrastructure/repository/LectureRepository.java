package io.hhplus.lecture_apply_service.infrastructure.repository;

import io.hhplus.lecture_apply_service.infrastructure.entity.LectureJpaEntity;
import java.util.List;
import java.util.Optional;

public interface LectureRepository {

    Optional<LectureJpaEntity> findById(Long lectureId);
    LectureJpaEntity save (LectureJpaEntity lecture);

    Optional<LectureJpaEntity> findByIdxLock(Long lectureId);

    LectureJpaEntity savexLock(LectureJpaEntity lectureJpaEntity);

    List<LectureJpaEntity> findAll();
}
