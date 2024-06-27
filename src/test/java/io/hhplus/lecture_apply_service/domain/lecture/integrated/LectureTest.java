package io.hhplus.lecture_apply_service.domain.lecture.integrated;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.lecture_apply_service.application.port.in.ApplyLectureCommand;
import io.hhplus.lecture_apply_service.application.port.in.ApplyLectureUseCase;
import io.hhplus.lecture_apply_service.application.port.in.EnrolledLectureUseCase;
import io.hhplus.lecture_apply_service.exception.CustomException;
import io.hhplus.lecture_apply_service.exception.ErrorCode;
import io.hhplus.lecture_apply_service.infrastructure.entity.Lecture;
import io.hhplus.lecture_apply_service.infrastructure.entity.Student;
import io.hhplus.lecture_apply_service.infrastructure.entity.StudentLecture;
import io.hhplus.lecture_apply_service.infrastructure.repository.LectureRepository;
import io.hhplus.lecture_apply_service.infrastructure.repository.StudentLectureRepository;
import io.hhplus.lecture_apply_service.infrastructure.repository.StudentRepository;
import io.hhplus.lecture_apply_service.presentation.dto.req.ApplyLectureAPIRequest;
import io.hhplus.lecture_apply_service.presentation.dto.res.ApplyLectureAPIResponse;
import io.hhplus.lecture_apply_service.presentation.dto.res.EnrolledLectureAPIResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
public class LectureTest {

  private List<Lecture> lectures = new ArrayList<>();
  private List<Student> students = new ArrayList<>();



  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private LectureRepository lectureRepository;

  @Autowired
  private StudentLectureRepository studentLectureRepository;

  @Autowired
  private ApplyLectureUseCase applyLectureUseCase;
  @Autowired
  private EnrolledLectureUseCase enrolledLectureUseCase;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");


  @Test
  @DisplayName("특강 신청 테스트 - 성공")
  public void applyLecture_success() throws Exception {

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
    //given
    Student student = students.get(2);
    Lecture lecture = lectures.get(3);
    boolean applySuccess = true;
    ApplyLectureAPIRequest request = new ApplyLectureAPIRequest(student.getId(), lecture.getId().getLectureId(), lecture.getStartAt(), LocalDateTime.now());

    //when
    ResultActions resultActions = mockMvc.perform(post("/lectures/apply")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))

        //then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.studentId").value(student.getId()))
        .andExpect(jsonPath("$.lectureId").value(lecture.getId()))
        .andExpect(jsonPath("$.status").value(applySuccess));
  }

  @Test
  @DisplayName("특강 신청 테스트 - 실패")
  public void applyLecture_Fail() throws Exception {

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
    //given
    Student student = students.get(2);
    Lecture lecture = lectures.get(0);
    boolean applySuccess = false;
    ApplyLectureAPIRequest request = new ApplyLectureAPIRequest(student.getId(), lecture.getId().getLectureId(), lecture.getStartAt(), LocalDateTime.now());

    //when
    ResultActions resultActions = mockMvc.perform(post("/lectures/apply")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))

        //then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.studentId").value(student.getId()))
        .andExpect(jsonPath("$.lectureId").value(lecture.getId()))
        .andExpect(jsonPath("$.status").value(applySuccess));
  }

  @Test
  @DisplayName("수강하는 특강 목록 조회 테스트 - 성공")
  public void listAllLecture_success() throws Exception {
    for (int i=1; i < 35; ++i){
      LocalDateTime localDateTime = LocalDateTime.now();
      Lecture lecture = new Lecture((long)i, localDateTime,"클린 아키텍처"+i, i-1, LocalDateTime.of(2024,4,27,13,0) ,new HashSet<>());
      lectures.add(lecture);
      lectureRepository.save(lecture);
    }
    for (int i = 1; i < 40; ++i) {
      Student student = new Student((long) i, "정현우" + i, new HashSet<>());
      students.add(student);
      studentRepository.save(student);
    }
    //given
    Student student = students.get(2);
    Lecture lecture = lectures.get(4);
    StudentLecture studentLecture = new StudentLecture(student, lecture, true);
    studentLectureRepository.save(studentLecture);


    //when
    EnrolledLectureAPIResponse response = enrolledLectureUseCase.execute(student.getId());

    //then
    assertThat(response.status()).isTrue();

        //.andExpect(jsonPath("$.lectures.length()").value(greaterThan(0)));
  }

  @Test
  @DisplayName("특강 목록 테스트 - 성공")
  public void enrolledLecture_success() throws Exception {
    //given
    for (int i=1; i < 35; ++i){
      LocalDateTime localDateTime = LocalDateTime.now();
      Lecture lecture = new Lecture((long)i, localDateTime,"클린 아키텍처"+i, i-1, LocalDateTime.of(2024,4,27,13,0) ,new HashSet<>());
      lectures.add(lecture);
      lectureRepository.save(lecture);
    }
    for (int i = 1; i < 40; ++i) {
      Student student = new Student((long) i, "정현우" + i, new HashSet<>());
      students.add(student);
      studentRepository.save(student);
    }

    //when
    ResultActions resultActions = mockMvc.perform(get("/lectures"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lectures").isArray())
        .andExpect(jsonPath("$.lectures.length()").value(lectures.size()));

    // Optionally, perform more detailed assertions on individual lectures
    for (int i = 0; i < lectures.size(); i++) {
      Lecture expectedLecture = lectures.get(i);
      resultActions.andExpect(jsonPath("$.lectures[" + i + "].id").value(expectedLecture.getId()))
          .andExpect(jsonPath("$.lectures[" + i + "].name").value(expectedLecture.getName()))
          .andExpect(jsonPath("$.lectures[" + i + "].capacity").value(expectedLecture.getCapacity()))
          .andExpect(jsonPath("$.lectures[" + i + "].open_at").value(expectedLecture.getOpen_at().toString()+":00"));
      // Add more assertions as needed
    }
  }

  @Test
  @DisplayName("특강 신청 동시성 테스트 : 수강인원 이후 부터는 실패")
  void applyLectureServiceConcurrency() throws InterruptedException {
    //given
    long studentId = 1L;
    long lectureId = 10L;
    int capacity = 30;
    int trial = 40;

    ExecutorService executorService =Executors.newFixedThreadPool(trial);
    CountDownLatch latch = new CountDownLatch(trial);
    List<Future<ApplyLectureAPIResponse>> futures = new ArrayList<>();LocalDateTime localDateTime = LocalDateTime.now();
    Lecture lecture = new Lecture(lectureId, localDateTime, "클린아키텍처", capacity, LocalDateTime.of(2024,4,27,13,0), new HashSet<>());

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
          .startAt(lecture.getStartAt())
          .requestAt(LocalDateTime.now())
          .build();

      Future<ApplyLectureAPIResponse> future =
          executorService.submit(()-> applyLectureUseCase.execute(command));
      futures.add(future);
    }

    //then
    AtomicInteger yes = new AtomicInteger();
    AtomicInteger no = new AtomicInteger();
    AtomicInteger applied = new AtomicInteger();
    futures.stream().forEach(future ->{
      try {
        ApplyLectureAPIResponse response = future.get();
        if (response.status() == true){
          yes.getAndIncrement();
        }
        else {
          no.getAndIncrement();
        }
      } catch (InterruptedException | ExecutionException e) {
        if (!(((CustomException) e.getCause()).getErrorCode() == ErrorCode.ALREADY_APPLIED)){
          throw new RuntimeException(e);
        }
        else {
          applied.getAndIncrement();
        }
      }
      finally {
        latch.countDown();
      }
    });
    latch.await();
    executorService.shutdown();
    List<StudentLecture> history = studentLectureRepository.findAll();
    List<StudentLecture> success = history.stream().filter(his->his.getEnrollment() == true).toList();
    assertThat(history.size()).isEqualTo(trial);
    assertThat(yes.get()).isEqualTo(lecture.getCapacity());
  }

  @Test
  @DisplayName("특강 신청 동시성 테스트 - 성공")
  public void applyLectureConcurrency_success() throws Exception {
    //given
    for (int i=1; i < 50; ++i){
      LocalDateTime localDateTime = LocalDateTime.now();
      Lecture lecture = new Lecture((long)i, localDateTime,"클린 아키텍처"+i, i-1, LocalDateTime.of(2024,4,27,13,0) ,new HashSet<>());
      lectures.add(lecture);
      lectureRepository.save(lecture);
    }
    for (int i = 1; i < 100; ++i) {
      Student student = new Student((long) i+1, "정현우" + i, new HashSet<>());
      students.add(student);
      studentRepository.save(student);
    }
    Lecture lecture = lectures.get(38);

    ExecutorService executorService = Executors.newFixedThreadPool(students.size());
    CountDownLatch latch = new CountDownLatch(students.size());
    List<Future<ApplyLectureAPIResponse>> futures = new ArrayList<>();

    //when
    for (int i = 0; i < students.size(); ++i){
      ApplyLectureCommand command = ApplyLectureCommand.builder()
          .studentId(students.get(i).getId())
          .lectureId(lecture.getId().getLectureId())
          .startAt(lecture.getStartAt())
          .requestAt(LocalDateTime.now())
          .build();
      Future<ApplyLectureAPIResponse> future =
          executorService.submit(()->
              applyLectureUseCase.execute(command)
          );
      futures.add(future);
    }

    AtomicInteger yes = new AtomicInteger();
    AtomicInteger no = new AtomicInteger();
    futures.stream().forEach(future ->{
      try {
        ApplyLectureAPIResponse response = future.get();
        if (response.status() == true){
          yes.getAndIncrement();
        }
        else {
          no.getAndIncrement();
        }
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      catch ( CustomException e){
        int a = 0;
      } catch (ExecutionException e) {
        throw new RuntimeException(e);
      } finally {
        latch.countDown();
      }
    });
    //then
    latch.await();
    executorService.shutdown();
    List<StudentLecture> history = studentLectureRepository.findAll();
    List<StudentLecture> success = history.stream().filter(his->his.getEnrollment() == true).toList();
    assertThat(success.size()).isEqualTo(lecture.getCapacity());
    assertThat(yes.get()).isEqualTo(lecture.getCapacity());
  }

  @Test
  @DisplayName("특강 신청 동시성 테스트 신청자 중 이미 수강한 특강을 신청함 - 성공")
  public void applyAlreadyDidLectureConcurrency_success() throws Exception {
    //given
    int studentNum = 100;
    int lectureNum = 50;
    int alreadyApplied = 12;
    for (int i=0; i < lectureNum; ++i){
      LocalDateTime localDateTime = LocalDateTime.now();
      Lecture lecture = new Lecture((long)i, localDateTime,"클린 아키텍처"+i, i-1, LocalDateTime.of(2024,4,27,13,0) ,new HashSet<>());
      lectures.add(lecture);
      lectureRepository.save(lecture);
    }
    for (int i = 0; i < studentNum; ++i) {
      Student student = new Student((long) i+1, "정현우" + i, new HashSet<>());
      students.add(student);
      studentRepository.save(student);
    }
    Lecture lecture = lectures.get(38);
    for (int i = 0; i < alreadyApplied; ++i){
      StudentLecture studentLecture = new StudentLecture(students.get(i), lecture, true);
      studentLectureRepository.save(studentLecture);
    }


    ExecutorService executorService = Executors.newFixedThreadPool(students.size());
    CountDownLatch latch = new CountDownLatch(students.size());
    List<Future<ApplyLectureAPIResponse>> futures = new ArrayList<>();

    //when
    for (int i = 0; i < students.size(); ++i){
      ApplyLectureCommand command = ApplyLectureCommand.builder()
          .studentId(students.get(i).getId())
          .lectureId(lecture.getId().getLectureId())
          .requestAt(LocalDateTime.now())
          .build();
      Future<ApplyLectureAPIResponse> future =
          executorService.submit(()->
              applyLectureUseCase.execute(command)
          );
      futures.add(future);
    }

    AtomicInteger yes = new AtomicInteger();
    AtomicInteger no = new AtomicInteger();
    AtomicInteger applied = new AtomicInteger();
    futures.stream().forEach(future ->{
            try {
                ApplyLectureAPIResponse response = future.get();
                if (response.status() == true){
                    yes.getAndIncrement();
                }
                else {
                    no.getAndIncrement();
                }
            } catch (InterruptedException | ExecutionException e) {
              if (!(((CustomException) e.getCause()).getErrorCode() == ErrorCode.ALREADY_APPLIED)){
                throw new RuntimeException(e);
              }
              else {
                applied.getAndIncrement();
              }
            }
            finally {
                latch.countDown();
            }
        });
    //then
    latch.await();
    executorService.shutdown();
    List<StudentLecture> history = studentLectureRepository.findAll();
    List<StudentLecture> success = history.stream().filter(his->his.getEnrollment() == true).toList();
    assertThat(history.size()).isEqualTo(studentNum);
    assertThat(yes.get()).isEqualTo(lecture.getCapacity());
    assertThat(applied.get()).isEqualTo(alreadyApplied);
  }
}
