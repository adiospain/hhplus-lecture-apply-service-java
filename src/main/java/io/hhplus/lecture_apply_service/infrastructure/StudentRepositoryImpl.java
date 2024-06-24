package io.hhplus.lecture_apply_service.infrastructure;


import io.hhplus.lecture_apply_service.infrastructure.entity.Student;
import io.hhplus.lecture_apply_service.infrastructure.repository.StudentRepository;

import io.hhplus.lecture_apply_service.infrastructure.repository.jpa.StudentJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class StudentRepositoryImpl implements StudentRepository {

  private final StudentJpaRepository studentRepository;


  @Override
  public Optional<Student> findById(Long studentId) {
    return studentRepository.findById(studentId);
  }
}
