package io.hhplus.lecture_apply_service.domain.lecture.application.useCase;

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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

    @Test
    @DisplayName("특강 신청 동시성 31명")
    void applyLectureServiceConcurrency() throws InterruptedException {

        long userId = 21L;
        long lectureId = 10L;

        int trial = 31;
        ExecutorService executorService =Executors.newFixedThreadPool(trial);
        CountDownLatch latch = new CountDownLatch(trial);
        List<Future<ApplyLectureAPIResponse>> futures = new ArrayList<>();

        LectureJpaEntity lecture = new LectureJpaEntity(lectureId, "클린아키텍처", 30, LocalDateTime.of(2024,4,27,13,0), new HashSet<>());
        long longTrial = (long) trial;
        for (long i = userId; i < userId + longTrial; ++i){
            ApplyLectureCommand command = ApplyLectureCommand.builder()
                    .userId(i)
                    .lectureId(lectureId)
                    .build();
            StudentJpaEntity user = new StudentJpaEntity(i, "정현우"+i, new HashSet<>());
            when(lectureRepository.findById(anyLong())).thenReturn(Optional.of(lecture));
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
            when(lectureRepository.save(any(LectureJpaEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Future<ApplyLectureAPIResponse> future =
            executorService.submit(()-> applyLectureUseCase.execute(command));
            futures.add(future);
        }
        for (Future<ApplyLectureAPIResponse> future : futures) {
            try {
                ApplyLectureAPIResponse result = future.get();
                assertThat(result.success()).isTrue();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            finally {
                latch.countDown();
            }
        }
        latch.await();
        executorService.shutdown();
    }

    @Test
    @DisplayName("특강 신청 동시성 31명")
    void applyLectureServiceConcurrencySecond() throws InterruptedException {

        long userId = 21L;
        long lectureId = 10L;

        int trial = 30;

        List<StudentJpaEntity> users = new ArrayList<>();
        for (long i = userId; i < userId + trial; ++i){
            users.add(new StudentJpaEntity(i, "정현우"+i, new HashSet<>()));
        }
        LectureJpaEntity lecture = new LectureJpaEntity(lectureId, "클린아키텍처", 0, LocalDateTime.of(2024,4,27,13,0), new HashSet<>());

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (StudentJpaEntity user : users){
            CompletableFuture<Void> future = CompletableFuture.runAsync(()->{
                ApplyLectureCommand command = ApplyLectureCommand.builder()
                        .userId(user.getId())
                        .lectureId(lectureId)
                        .build();
                when(lectureRepository.findById(anyLong())).thenReturn(Optional.of(lecture));
                when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
                applyLectureUseCase.execute(command);
            });
            futures.add(future);
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }
}
