package io.hhplus.lecture_apply_service.domain.lecture.application.useCase;

import io.hhplus.lecture_apply_service.application.port.in.ApplyLectureUseCase;
import io.hhplus.lecture_apply_service.application.port.in.ApplyLectureUseCaseImpl;
import io.hhplus.lecture_apply_service.exception.CustomException;
import io.hhplus.lecture_apply_service.application.port.in.ApplyLectureCommand;
import io.hhplus.lecture_apply_service.application.port.out.LectureLock;

import io.hhplus.lecture_apply_service.exception.ErrorCode;
import io.hhplus.lecture_apply_service.infrastructure.entity.Lecture;
import io.hhplus.lecture_apply_service.infrastructure.entity.Student;
import io.hhplus.lecture_apply_service.infrastructure.entity.StudentLecture;
import io.hhplus.lecture_apply_service.infrastructure.repository.LectureRepository;
import io.hhplus.lecture_apply_service.infrastructure.repository.StudentLectureRepository;
import io.hhplus.lecture_apply_service.infrastructure.repository.StudentRepository;
import io.hhplus.lecture_apply_service.presentation.dto.res.ApplyLectureAPIResponse;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ApplyLectureUseCaseTest {


    private final LectureRepository lectureRepository = Mockito.mock(LectureRepository.class);
    private final StudentRepository studentRepository = Mockito.mock(StudentRepository.class);
    private final StudentLectureRepository studentLectureRepository = Mockito.mock(StudentLectureRepository.class);
    private final ApplyLectureUseCase applyLectureUseCase = new ApplyLectureUseCaseImpl(lectureRepository, studentRepository, studentLectureRepository);

    private List<Student> students = new ArrayList<>();
    private List<Lecture> lectures = new ArrayList<>();

    @Test
    @DisplayName("특강 신청 성공")
    void applyLectureServiceSuccess() {
        //given
        for (int i=1; i < 35; ++i){
            LocalDateTime localDateTime = LocalDateTime.now();
            Lecture lecture = new Lecture((long)i, localDateTime, "클린아키텍처", 30, LocalDateTime.of(2024,4,27,13,0), new HashSet<>());
            lectures.add(lecture);
            lectureRepository.save(lecture);
        }
        for (int i = 1; i < 40; ++i) {
            Student student = new Student((long) i, "정현우" + i, new HashSet<>());
            students.add(student);
            studentRepository.save(student);
        }
        Student student = students.get(0);
        Lecture lecture = lectures.get(1); //정원이 1명 남은 특강

        ApplyLectureCommand command = ApplyLectureCommand.builder()
            .studentId(student.getId())
            .lectureId(lecture.getId().getLectureId())
            .startAt(lecture.getId().getStartAt())
            .requestAt(LocalDateTime.now())
            .build();


        //when
        when(lectureRepository.findByIdxLock(lecture.getId())).thenReturn(Optional.of(lecture));
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));
        ApplyLectureAPIResponse response = applyLectureUseCase.execute(command);

        //then
        assertThat(response.status()).isTrue();
        //성공 히스토리 기록했는지 확인
        verify(studentLectureRepository, times(1)).save(any(StudentLecture.class));
    }

    @Test
    @DisplayName("이미 수강한 특강을 다시 신청한 경우")
    void applyLectureAlreadyApplied() {
        for (int i=1; i < 35; ++i){
            LocalDateTime localDateTime = LocalDateTime.now();
            Lecture lecture = new Lecture((long)i, localDateTime, "클린아키텍처", 30, LocalDateTime.of(2024,4,27,13,0), new HashSet<>());
            lectures.add(lecture);
            lectureRepository.save(lecture);
        }
        for (int i = 1; i < 40; ++i) {
            Student student = new Student((long) i, "정현우" + i, new HashSet<>());
            students.add(student);
            studentRepository.save(student);
        }
        Student student = students.get(0);
        Lecture lecture = lectures.get(28);

        ApplyLectureCommand command = ApplyLectureCommand.builder()
            .studentId(student.getId())
            .lectureId(lecture.getId().getLectureId())
            .startAt(lecture.getId().getStartAt())
            .requestAt(LocalDateTime.now())
            .build();

        when(studentLectureRepository.existsByStudentIdAndLectureIdAndEnrollmentIsTrue(student.getId(), lecture.getId())).thenReturn(true);
        when(lectureRepository.findByIdxLock(lecture.getId())).thenReturn(Optional.of(lecture));
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

        CustomException studentException = assertThrows(CustomException.class, ()->applyLectureUseCase.execute(command));
        assertThat(studentException.getErrorCode().name()).isEqualTo("ALREADY_APPLIED");
    }

    @Test
    @DisplayName("수강 인원 초과, 특강 신청 실패")
    void applyLectureServiceFail() {

        //given
        for (int i=1; i < 35; ++i){
            LocalDateTime localDateTime = LocalDateTime.now();
            Lecture lecture = new Lecture((long)i, localDateTime, "클린아키텍처", i-1, LocalDateTime.of(2024,4,27,13,0), new HashSet<>());
            lectures.add(lecture);
            lectureRepository.save(lecture);
        }
        for (int i = 1; i < 40; ++i) {
            Student student = new Student((long) i, "정현우" + i, new HashSet<>());
            students.add(student);
            studentRepository.save(student);
        }
        Student student = students.get(3);
        Lecture lecture = lectures.get(0);
        ApplyLectureCommand command = ApplyLectureCommand.builder()
                .studentId(student.getId())
                .lectureId(lecture.getId().getLectureId())
            .startAt(lecture.getId().getStartAt())
            .requestAt(LocalDateTime.now())
                .build();
        when(lectureRepository.findByIdxLock(lecture.getId())).thenReturn(Optional.of(lecture));
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(studentLectureRepository.existsByStudentIdAndLectureIdAndEnrollmentIsTrue(student.getId(), lecture.getId())).thenReturn(false);

        //when
        ApplyLectureAPIResponse response = applyLectureUseCase.execute(command);

        //then
        StudentLecture studentLecture = new StudentLecture(student, lecture, false);
        //실패 히스토리 기록 했는지 확인
        verify(studentLectureRepository, times(1)).save(any(StudentLecture.class));
        assertThat(response.status()).isFalse();

    }

    @Test
    @DisplayName("특강 신청 동시성 테스트 : 동시적이 아닌 순차적으로 실행")
    void applyLectureServiceSequential() throws InterruptedException {
        //given
        long studentId = 1L;
        long lectureId = 10L;
        int trial = 36;

        ExecutorService executorService =Executors.newFixedThreadPool(trial);
        CountDownLatch latch = new CountDownLatch(trial);
        List<Future<ApplyLectureAPIResponse>> futures = new ArrayList<>();
        LocalDateTime localDateTime = LocalDateTime.now();
        Lecture lecture = new Lecture(lectureId, localDateTime, "클린아키텍처", 30, LocalDateTime.of(2024,4,27,13,0), new HashSet<>());

        //when
        when(lectureRepository.findByIdxLock(lecture.getId())).thenReturn(Optional.of(lecture));
        when(studentRepository.findById(anyLong())).thenAnswer(invocation ->{
            long id = invocation.getArgument(0);
            return Optional.of(new Student(id, "정현우"+id, new HashSet<>()));
        });
        when(lectureRepository.save(any(Lecture.class))).thenAnswer(invocation -> invocation.getArgument(0));
        for (long i = studentId; i < studentId + trial; ++i){
            ApplyLectureCommand command = ApplyLectureCommand.builder()
                    .studentId(i)
                    .lectureId(lectureId)
                .startAt(lecture.getId().getStartAt())
                .requestAt(LocalDateTime.now())
                    .build();

            ApplyLectureAPIResponse response = applyLectureUseCase.execute(command);
            if (response.studentId() >= studentId + 30){
                assertThat(response.status()).isFalse();
            }
            else {
                assertThat(response.status()).isTrue();
            }
        }
    }



    @Test
    @DisplayName("특강 신청 동시성 30명")
    void applyLectureServiceConcurrencySecond() throws InterruptedException {

        long studentId = 21L;
        long lectureId = 10L;
        int capacity = 20;
        int trial = 30;

        List<Student> students = new ArrayList<>();
        LocalDateTime localDateTime = LocalDateTime.now();
        for (long i = studentId; i < studentId + trial; ++i){
            students.add(new Student(i, "정현우"+i, new HashSet<>()));
        }
        Lecture lecture = new Lecture(lectureId, localDateTime,"클린아키텍처", capacity, LocalDateTime.of(2024,4,27,13,0), new HashSet<>());

        when(lectureRepository.findByIdxLock(lecture.getId())).thenReturn(Optional.of(lecture));


        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (Student student : students){
            CompletableFuture<Void> future = CompletableFuture.runAsync(()->{
                ApplyLectureCommand command = ApplyLectureCommand.builder()
                        .studentId(student.getId())
                        .lectureId(lecture.getId().getLectureId())
                    .startAt(lecture.getId().getStartAt())
                    .requestAt(LocalDateTime.now())
                        .build();
                when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
                applyLectureUseCase.execute(command);
            });
            futures.add(future);
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }
}
