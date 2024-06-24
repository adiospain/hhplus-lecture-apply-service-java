package io.hhplus.lecture_apply_service.infrastructure;

import io.hhplus.lecture_apply_service.infrastructure.entity.LectureJpaEntity;
import io.hhplus.lecture_apply_service.infrastructure.repository.LectureRepository;
import io.hhplus.lecture_apply_service.infrastructure.repository.jpa.LectureJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class LectureRepositoryImpl implements LectureRepository {

  private final LectureJpaRepository lectureRepository;

  @Override
  public Optional<LectureJpaEntity> findById(Long lectureId) {
    return lectureRepository.findById(lectureId);
  }

  @Override
  public LectureJpaEntity save(LectureJpaEntity lecture) {
    return lectureRepository.save(lecture);
  }

  @Override
  public Optional<LectureJpaEntity> findByIdxLock(Long lectureId) {
    return lectureRepository.findByIdxLock(lectureId);
  }

  @Override
  public LectureJpaEntity savexLock(LectureJpaEntity lecture) {
    return lectureRepository.savexLock(lecture);
  }
}
