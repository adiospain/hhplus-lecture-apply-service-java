package io.hhplus.lecture_apply_service.domain.lecture.application.useCase;

import io.hhplus.lecture_apply_service.application.port.in.EnrolledLectureUseCase;
import io.hhplus.lecture_apply_service.application.port.in.EnrolledLectureUseCaseImpl;
import io.hhplus.lecture_apply_service.infrastructure.entity.Lecture;
import io.hhplus.lecture_apply_service.infrastructure.entity.Student;
import io.hhplus.lecture_apply_service.infrastructure.entity.StudentLecture;
import io.hhplus.lecture_apply_service.infrastructure.repository.StudentLectureRepository;
import io.hhplus.lecture_apply_service.presentation.dto.res.EnrolledLectureAPIResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EnrolledLectureUseCaseTest {
  private final StudentLectureRepository studentLectureRepository = Mockito.mock(
      StudentLectureRepository.class);
  private EnrolledLectureUseCase enrolledLectureUseCase = new EnrolledLectureUseCaseImpl(studentLectureRepository);

  private List<Student> students;
  private List<Lecture> lectures;
  private List<StudentLecture> studentLectures;

  @BeforeEach
  void setUp() {
    students = new ArrayList<>();
    lectures = new ArrayList<>();
    studentLectures = new ArrayList<>();

    for (int i = 1; i < 40; ++i) {
      Student student = new Student((long) i, "정현우" + i, new HashSet<>());
      students.add(student);
    }
    for (int i=1; i < 35; ++i){
      Lecture lecture = new Lecture((long)i, "클린 아키텍처"+i, i-1, LocalDateTime.of(2024,4,27,13,0) ,new HashSet<>());
      lectures.add(lecture);
    }
    for (int i = 0; i < 10; ++i){
      StudentLecture studentLecture = new StudentLecture(students.get(i), lectures.get(i), i % 2 == 0);
      studentLectures.add(studentLecture);
    }
  }
  @Test
  @DisplayName("강의 신청 완료 여부 확인 테스트, 수강 성공")
  public void checkLectureEnrollment_success() {
    //Given
    Student student = students.get(5);
    List<StudentLecture> filtered = studentLectures.stream()
            .filter(studentLecture -> studentLecture.getStudent().getId().equals(student.getId()))
            .collect(Collectors.toList());

    when(studentLectureRepository.findAllByStudentId(student.getId())).thenReturn(filtered);

    EnrolledLectureAPIResponse response = enrolledLectureUseCase.execute(student.getId());
    assertThat(response.enrolledLectures()).isNotEmpty();
    assertThat(response.status()).isTrue();
    verify(studentLectureRepository, times(1)).findAllByStudentId(student.getId());
  }

  @Test
  @DisplayName("강의 신청 완료 여부 확인 테스트, 수강 실패")
  public void checkLectureEnrollment_fail() {
    //Given
    Student student = students.get(2);
    when(studentLectureRepository.findAllByStudentId(student.getId())).thenReturn(List.of());

    EnrolledLectureAPIResponse response = enrolledLectureUseCase.execute(student.getId());
    assertThat(response.status()).isFalse();
    verify(studentLectureRepository, times(1)).findAllByStudentId(student.getId());
  }
}
