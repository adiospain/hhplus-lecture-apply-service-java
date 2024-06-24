package io.hhplus.lecture_apply_service.infrastructure;

import io.hhplus.lecture_apply_service.infrastructure.entity.StudentLecture;
import io.hhplus.lecture_apply_service.infrastructure.repository.StudentLectureRepository;
import io.hhplus.lecture_apply_service.infrastructure.repository.jpa.StudentLectureJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class StudentLectureRepositoryImpl implements StudentLectureRepository {

  private StudentLectureJpaRepository studentLectureRepository;

  @Override
  public StudentLecture save(StudentLecture lecture) {
    return studentLectureRepository.save(lecture);
  }

  @Override
  public boolean existsByStudentIdAndLectureIdAndEnrollmentIsTrue(Long studentId, Long lectureId) {
    return studentLectureRepository.existsByStudentIdAndLectureIdAndEnrollmentIsTrue(studentId, lectureId);
  }

  @Override
  public List<StudentLecture> findAllByStudentId(Long studentId) {
    return studentLectureRepository.findAllByStudentId(studentId);
  }
}
