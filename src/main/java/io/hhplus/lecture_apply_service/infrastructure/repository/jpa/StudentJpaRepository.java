package io.hhplus.lecture_apply_service.infrastructure.repository.jpa;

import io.hhplus.lecture_apply_service.infrastructure.entity.Lecture;
import io.hhplus.lecture_apply_service.infrastructure.entity.Student;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;


public interface StudentJpaRepository extends JpaRepository<Student, Long> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT s FROM Student s WHERE s.id = :studentId")
  Optional<Student> findByIdxLock(Long studentId);


}
