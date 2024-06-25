package io.hhplus.lecture_apply_service.infrastructure;

import io.hhplus.lecture_apply_service.infrastructure.entity.Lecture;
import io.hhplus.lecture_apply_service.infrastructure.repository.LectureRepository;
import io.hhplus.lecture_apply_service.infrastructure.repository.jpa.LectureJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class LectureRepositoryImpl implements LectureRepository {

  private final LectureJpaRepository lectureRepository;

  @Override
  public Optional<Lecture> findById(Long lectureId) {
    return lectureRepository.findById(lectureId);
  }

  @Override
  public Lecture save(Lecture lecture) {
    return lectureRepository.save(lecture);
  }

  @Override
  public Optional<Lecture> findByIdxLock(Long lectureId) {
    return lectureRepository.findByIdxLock(lectureId);
  }

  @Override
  public Lecture savexLock(Lecture lecture) {
    return lectureRepository.savexLock(lecture);
  }

  @Override
  public List<Lecture> findAll() {
    return lectureRepository.findAll();
  }
}
