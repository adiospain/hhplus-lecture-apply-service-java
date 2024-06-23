package io.hhplus.lecture_apply_service.domain.lecture.application.service;

import io.hhplus.lecture_apply_service.application.port.in.ApplyLectureUseCase;
import io.hhplus.lecture_apply_service.application.port.in.ApplyLectureUseCaseImpl;
import io.hhplus.lecture_apply_service.infrastructure.entity.Student;
import io.hhplus.lecture_apply_service.infrastructure.entity.Lecture;
import io.hhplus.lecture_apply_service.application.port.in.ApplyLectureCommand;
import io.hhplus.lecture_apply_service.application.port.out.LectureLock;
import io.hhplus.lecture_apply_service.infrastructure.repository.LectureRepository;
import io.hhplus.lecture_apply_service.infrastructure.repository.StudentLectureRepository;
import io.hhplus.lecture_apply_service.infrastructure.repository.StudentRepository;

import io.hhplus.lecture_apply_service.infrastructure.repository.jpa.StudentLectureJpaRepository;
import io.hhplus.lecture_apply_service.presentation.dto.res.ApplyLectureAPIResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class ApplyLectureUseCaseTest {

    private final LectureLock lectureLock = Mockito.mock(LectureLock.class);
    private final LectureRepository lectureRepository = Mockito.mock(LectureRepository.class);
    private final StudentRepository studentRepository = Mockito.mock(StudentRepository.class);
    private final StudentLectureRepository studentLectureRepository = Mockito.mock(StudentLectureRepository.class);
    private final ApplyLectureUseCase applyLectureUseCase = new ApplyLectureUseCaseImpl(lectureLock, lectureRepository, studentRepository, studentLectureRepository);

    @Test
    @DisplayName("특강 신청 성공")
    void applyLectureServicestatus() {
        //given
        long studentId = 21L;
        long lectureId = 100L;
        ApplyLectureCommand command = ApplyLectureCommand.builder()
                .studentId(studentId)
                .lectureId(lectureId)
                .build();
        Lecture lecture = new Lecture(lectureId, "클린아키텍처", 30, LocalDateTime.of(2024,4,27,13,0), new HashSet<>());
        Student student = new Student(studentId, "정현우", new HashSet<>());

        //when
        when(lectureRepository.findByIdxLock(lectureId)).thenReturn(Optional.of(lecture));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        ApplyLectureAPIResponse response = applyLectureUseCase.execute(command);

        //then
        assertThat(response.status()).isTrue();
    }

    @Test
    @DisplayName("특강 신청 실패")
    void applyLectureServiceFail() {

        long studentId = 21L;
        long lectureId = 100L;
        ApplyLectureCommand command = ApplyLectureCommand.builder()
                .studentId(studentId)
                .lectureId(lectureId)
                .build();
        Lecture lecture = new Lecture(lectureId, "클린아키텍처", 30, LocalDateTime.of(2024,4,27,13,0), new HashSet<>());
        Student student = new Student(studentId, "정현우", new HashSet<>());
        when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(lecture));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        ApplyLectureAPIResponse response = applyLectureUseCase.execute(command);
        assertThat(response.status()).isFalse();
    }
}
