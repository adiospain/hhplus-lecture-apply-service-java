package io.hhplus.lecture_apply_service.infrastructure.repository;

import io.hhplus.lecture_apply_service.infrastructure.entity.StudentJpaEntity;
import java.util.Optional;

public interface StudentRepository {
  Optional<StudentJpaEntity> findById(Long studentId);
}
