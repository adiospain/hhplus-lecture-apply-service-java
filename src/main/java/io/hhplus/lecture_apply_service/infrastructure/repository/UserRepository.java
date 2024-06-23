package io.hhplus.lecture_apply_service.infrastructure.repository;

import io.hhplus.lecture_apply_service.application.port.out.persistence.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserJpaEntity, Long> {

}
