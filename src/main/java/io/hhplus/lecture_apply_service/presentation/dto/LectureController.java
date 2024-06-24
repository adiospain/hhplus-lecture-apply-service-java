package io.hhplus.lecture_apply_service.presentation.dto;

import io.hhplus.lecture_apply_service.application.port.in.ApplyLectureUseCase;
import io.hhplus.lecture_apply_service.application.port.in.EnrolledLectureUseCase;
import io.hhplus.lecture_apply_service.application.port.in.ListLectureUseCase;
import io.hhplus.lecture_apply_service.presentation.dto.req.ApplyLectureAPIRequest;
import io.hhplus.lecture_apply_service.application.port.in.ApplyLectureCommand;
import io.hhplus.lecture_apply_service.presentation.dto.res.ApplyLectureAPIResponse;

import io.hhplus.lecture_apply_service.presentation.dto.res.EnrolledLectureAPIResponse;
import io.hhplus.lecture_apply_service.presentation.dto.res.ListLectureAPIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lectures")
public class LectureController {

    private final ListLectureUseCase listLectureUseCase;
    private final ApplyLectureUseCase applyLectureUseCase;
    private final EnrolledLectureUseCase checkLectureEnrollmentUseCase;


    @PostMapping("/apply")
    public ResponseEntity<ApplyLectureAPIResponse> applyLecture (@RequestBody ApplyLectureAPIRequest request)
    {
        ApplyLectureCommand command = ApplyLectureCommand.builder()
                .studentId(request.studentId())
                .lectureId(request.lectureId())
                .build();

        ApplyLectureAPIResponse response = applyLectureUseCase.execute(command);
        //ApplyLectureAPIResponse response = new ApplyLectureAPIResponse(true);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ListLectureAPIResponse> listLecture(){
        ListLectureAPIResponse response = listLectureUseCase.execute();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/application/{userId}")
    public ResponseEntity<EnrolledLectureAPIResponse> checkLectureEnrollment(@PathVariable Long userId) {
        EnrolledLectureAPIResponse response = checkLectureEnrollmentUseCase.execute(userId);
        return ResponseEntity.ok(response);
    }


}
