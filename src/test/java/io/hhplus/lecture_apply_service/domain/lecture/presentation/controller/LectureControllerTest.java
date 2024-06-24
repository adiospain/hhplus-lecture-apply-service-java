package io.hhplus.lecture_apply_service.domain.lecture.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.lecture_apply_service.application.port.in.ApplyLectureUseCase;
import io.hhplus.lecture_apply_service.application.port.in.ApplyLectureCommand;
import io.hhplus.lecture_apply_service.presentation.dto.LectureController;
import io.hhplus.lecture_apply_service.presentation.dto.req.ApplyLectureAPIRequest;
import io.hhplus.lecture_apply_service.presentation.dto.res.ApplyLectureAPIResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    @DisplayName("특강 신청 성공")
    void applyForLectureSuccess() throws Exception{
        //given
        Long userId = 1L;
        Long lectureId = 100L;
        boolean applySuccess = true;
        ApplyLectureAPIResponse APIresponse = new ApplyLectureAPIResponse(userId, lectureId, applySuccess);
        //when
        when(applyLectureUseCase.applyLecture(any(ApplyLectureCommand.class))).thenReturn(APIresponse);

        ApplyLectureAPIRequest request = new ApplyLectureAPIRequest(userId, lectureId);
        mockMvc.perform(post("/lectures/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

        //then
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.studentId").value(userId))
                        .andExpect(jsonPath("$.lectureId").value(lectureId))
                        .andExpect(jsonPath("$.success").value(applySuccess));
    }

    @Test
    @DisplayName("특강 신청 실패")
    void applyForLectureFail() throws Exception{
        //given
        Long userId = 1L;
        Long lectureId = 100L;
        boolean applySuccess = false;
        ApplyLectureAPIResponse APIresponse = new ApplyLectureAPIResponse(userId, lectureId, applySuccess);
        when(applyLectureUseCase.applyLecture(any(ApplyLectureCommand.class))).thenReturn(APIresponse);

        //when
        ApplyLectureAPIRequest request = new ApplyLectureAPIRequest(userId, lectureId);
        mockMvc.perform(post("/lectures/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(userId))
                .andExpect(jsonPath("$.lectureId").value(lectureId))
                .andExpect(jsonPath("$.success").value(applySuccess))
                .andExpect(jsonPath("$.success").value(applySuccess));
    }
}
