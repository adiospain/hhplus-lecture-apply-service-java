package io.hhplus.lecture_apply_service.infrastructure.repository;

import io.hhplus.lecture_apply_service.application.port.out.persistence.UserLectureJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLectureRepository extends JpaRepository<UserLectureJpaEntity, Long> {


}
