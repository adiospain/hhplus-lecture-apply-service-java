package io.hhplus.lecture_apply_service.domain.lecture.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.lecture_apply_service.application.port.in.ApplyLectureUseCase;
import io.hhplus.lecture_apply_service.application.port.in.ApplyLectureCommand;
import io.hhplus.lecture_apply_service.application.port.in.EnrolledLectureUseCase;
import io.hhplus.lecture_apply_service.application.port.in.ListLectureUseCase;
import io.hhplus.lecture_apply_service.infrastructure.entity.Lecture;
import io.hhplus.lecture_apply_service.presentation.dto.LectureController;
import io.hhplus.lecture_apply_service.presentation.dto.req.ApplyLectureAPIRequest;
import io.hhplus.lecture_apply_service.presentation.dto.res.ApplyLectureAPIResponse;
import io.hhplus.lecture_apply_service.presentation.dto.res.EnrolledLectureAPIResponse;
import io.hhplus.lecture_apply_service.presentation.dto.res.ListLectureAPIResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = LectureController.class)
public class LectureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ApplyLectureUseCase applyLectureUseCase;

    @MockBean
    private ListLectureUseCase listLectureUseCase;

    @MockBean
    private EnrolledLectureUseCase checkLectureEnrollmentUseCase;

    @Test
    @DisplayName("특강 목록 조회")
    void listAllLecturesSuccess() throws Exception{
        //given
        List<Lecture> lectures = new ArrayList<>();


        for (int i = 0; i < 3; ++i){
            Lecture lecture = new Lecture();
            lecture.setTmpId((long)i);
            lecture.setName("클린 아키텍처"+i);
            lecture.setCapacity(30);
            lecture.setOpen_at(LocalDateTime.of(2024,4,27,13,i+30));
            lectures.add(lecture);
        }
        ListLectureAPIResponse response = new ListLectureAPIResponse(lectures);
        when(listLectureUseCase.execute()).thenReturn(response);

        mockMvc.perform(get("/lectures"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.lectures[0].name").value(lectures.get(0).getName()))
            .andExpect(jsonPath("$.lectures[1].name").value(lectures.get(1).getName()))
            .andExpect(jsonPath("$.lectures[2].name").value(lectures.get(2).getName()));
    }


    @Test
    @DisplayName("특강 신청 성공")
    void applyForLectureSuccess() throws Exception{
        //given
        Long userId = 1L;
        Long lectureId = 100L;
        boolean applySuccess = true;
        LocalDateTime localDateTime = LocalDateTime.now();
        ApplyLectureAPIResponse APIresponse = new ApplyLectureAPIResponse(userId, lectureId, localDateTime, applySuccess);
        //when
        when(applyLectureUseCase.execute(any(ApplyLectureCommand.class))).thenReturn(APIresponse);

        ApplyLectureAPIRequest request = new ApplyLectureAPIRequest(userId, lectureId, localDateTime, LocalDateTime.now());
        mockMvc.perform(post("/lectures/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

        //then
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.studentId").value(userId))
                        .andExpect(jsonPath("$.lectureId").value(lectureId))
                        .andExpect(jsonPath("$.status").value(applySuccess));
    }

    @Test
    @DisplayName("특강 신청 실패")
    void applyForLectureFail() throws Exception{
        //given
        Long userId = 1L;
        Long lectureId = 100L;
        boolean applySuccess = false;
        LocalDateTime localDateTime = LocalDateTime.now();
        ApplyLectureAPIResponse APIresponse = new ApplyLectureAPIResponse(userId, lectureId, localDateTime, applySuccess);
        when(applyLectureUseCase.execute(any(ApplyLectureCommand.class))).thenReturn(APIresponse);

        //when
        ApplyLectureAPIRequest request = new ApplyLectureAPIRequest(userId, lectureId, localDateTime, LocalDateTime.now());
        mockMvc.perform(post("/lectures/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(userId))
                .andExpect(jsonPath("$.lectureId").value(lectureId))
                .andExpect(jsonPath("$.status").value(applySuccess));
    }

    @Test
    @DisplayName("특강 신청 여부 조회, 수강 신청 성공")
    void checkLectureEnrollment_true() throws Exception{
        //given
        long studentId = 2L;
        long lectureId = 3L;

        EnrolledLectureAPIResponse response = new EnrolledLectureAPIResponse(List.of(),true);
        when(checkLectureEnrollmentUseCase.execute(studentId)).thenReturn(response);

        mockMvc.perform(get("/lectures/application/"+studentId)
                .param("lectureId", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(true));
    }

    @Test
    @DisplayName("특강 신청 여부 조회, 수강 신청 실패")
    void checkLectureEnrollment_false() throws Exception{
        //given
        long studentId = 2L;
        long lectureId = 3L;

        EnrolledLectureAPIResponse response = new EnrolledLectureAPIResponse(List.of(),false);
        when(checkLectureEnrollmentUseCase.execute(studentId)).thenReturn(response);

        mockMvc.perform(get("/lectures/application/"+studentId)
                .param("lectureId", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(false));
    }
}
