package io.hhplus.lecture_apply_service.domain.lecture.application.useCase;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import io.hhplus.lecture_apply_service.application.port.in.ListLectureUseCase;
import io.hhplus.lecture_apply_service.application.port.in.ListLectureUseCaseImpl;
import io.hhplus.lecture_apply_service.application.port.out.LectureLock;
import io.hhplus.lecture_apply_service.exception.CustomException;
import io.hhplus.lecture_apply_service.infrastructure.entity.Lecture;

import io.hhplus.lecture_apply_service.infrastructure.repository.LectureRepository;
import io.hhplus.lecture_apply_service.presentation.dto.res.ListLectureAPIResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ListLectureUseCaseTest {


  private LectureRepository lectureRepository = Mockito.mock(LectureRepository.class);;

  private ListLectureUseCase listLectureUseCase = new ListLectureUseCaseImpl(lectureRepository);

  private List<Lecture> lectures;

  @BeforeEach
  void setUp(){

    lectures = new ArrayList<>();


  }
  @Test
  @DisplayName("강의 목록 조회")
  void listLectures_found(){
    //given
    for (int i=1; i < 35; ++i){
      Lecture lecture = new Lecture((long)i, "클린 아키텍처"+i, i-1, LocalDateTime.of(2024,4,27,13,0) ,new HashSet<>());
      lectures.add(lecture);
    }
    when(lectureRepository.findAll()).thenReturn(lectures);

    //when
    ListLectureAPIResponse response = listLectureUseCase.execute();
    assertThat(response.lectures().size()).isEqualTo(lectures.size());
    verify(lectureRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("강의 목록 조회, 강의 목록 없음")
  void listLectures_notFound(){
    //given
    when(lectureRepository.findAll()).thenReturn(List.of());

    //when
    assertThrows (CustomException.class, ()->listLectureUseCase.execute());

    verify(lectureRepository, times(1)).findAll();
  }
}
