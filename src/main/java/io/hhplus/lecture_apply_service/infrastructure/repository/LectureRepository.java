package io.hhplus.lecture_apply_service.infrastructure.repository;

import io.hhplus.lecture_apply_service.application.port.out.persistence.LectureId;
import io.hhplus.lecture_apply_service.infrastructure.entity.Lecture;
import java.util.List;
import java.util.Optional;

public interface LectureRepository {

    Optional<Lecture> findById(LectureId lectureId);
    Lecture save (Lecture lecture);

    Optional<Lecture> findByIdxLock(LectureId lectureId);



    List<Lecture> findAll();
}
