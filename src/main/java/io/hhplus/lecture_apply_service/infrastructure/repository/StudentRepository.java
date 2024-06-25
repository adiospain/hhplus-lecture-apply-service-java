package io.hhplus.lecture_apply_service.infrastructure.repository;

import io.hhplus.lecture_apply_service.infrastructure.entity.Student;
import java.util.Optional;

public interface StudentRepository {
  Optional<Student> findById(Long studentId);
}
