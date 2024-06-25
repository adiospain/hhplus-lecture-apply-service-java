package io.hhplus.lecture_apply_service.infrastructure.repository;

import io.hhplus.lecture_apply_service.infrastructure.entity.Lecture;
import java.util.List;
import java.util.Optional;

public interface LectureRepository {

    Optional<Lecture> findById(Long lectureId);
    Lecture save (Lecture lecture);

    Optional<Lecture> findByIdxLock(Long lectureId);

    Lecture savexLock(Lecture lectureJpaEntity);

    List<Lecture> findAll();
}
