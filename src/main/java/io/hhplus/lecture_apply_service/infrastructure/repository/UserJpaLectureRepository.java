package io.hhplus.lecture_apply_service.infrastructure.repository;

import io.hhplus.lecture_apply_service.infrastructure.entity.UserLectureJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaLectureRepository extends JpaRepository<UserLectureJpaEntity, Long> {


}
