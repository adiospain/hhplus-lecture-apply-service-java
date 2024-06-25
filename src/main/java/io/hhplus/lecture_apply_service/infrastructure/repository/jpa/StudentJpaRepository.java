package io.hhplus.lecture_apply_service.infrastructure.repository.jpa;

import io.hhplus.lecture_apply_service.infrastructure.entity.Student;

import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentJpaRepository extends JpaRepository<Student, Long> {
}
