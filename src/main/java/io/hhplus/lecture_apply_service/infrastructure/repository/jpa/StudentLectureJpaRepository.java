package io.hhplus.lecture_apply_service.infrastructure.repository.jpa;

import io.hhplus.lecture_apply_service.infrastructure.entity.StudentLectureJpaEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface StudentLectureJpaRepository extends JpaRepository<StudentLectureJpaEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    <S extends StudentLectureJpaEntity> S save(S entity);
}
