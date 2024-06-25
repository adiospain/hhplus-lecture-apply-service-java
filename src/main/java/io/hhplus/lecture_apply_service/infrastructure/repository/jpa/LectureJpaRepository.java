package io.hhplus.lecture_apply_service.infrastructure.repository.jpa;

import io.hhplus.lecture_apply_service.infrastructure.entity.Lecture;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import org.springframework.data.repository.query.Param;

public interface LectureJpaRepository extends JpaRepository<Lecture, Long> {


    Optional<Lecture> findById (Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM Lecture l WHERE l.id = :lectureId")
    Optional<Lecture> findByIdxLock(Long lectureId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("UPDATE Lecture l SET l.name = :#{#Lecture.name}, l.capacity = :#{#Lecture.capacity} WHERE l.id = :#{#Lecture.id}")
    Lecture savexLock(@Param("Lecture") Lecture Lecture);
}