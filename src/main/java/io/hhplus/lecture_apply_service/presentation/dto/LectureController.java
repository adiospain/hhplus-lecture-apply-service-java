package io.hhplus.lecture_apply_service.presentation.dto;

import io.hhplus.lecture_apply_service.application.port.in.ApplyLectureUseCase;
import io.hhplus.lecture_apply_service.application.port.in.ListLectureUseCase;
import io.hhplus.lecture_apply_service.presentation.dto.req.ApplyLectureAPIRequest;
import io.hhplus.lecture_apply_service.application.port.in.ApplyLectureCommand;
import io.hhplus.lecture_apply_service.presentation.dto.res.ApplyLectureAPIResponse;
import io.hhplus.lecture_apply_service.presentation.dto.res.ListLectureAPIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lectures")
public class LectureController {

    private final ListLectureUseCase listLectureUseCase;
    public final ApplyLectureUseCase applyLectureUseCase;


    @PostMapping("/apply")
    public ResponseEntity<ApplyLectureAPIResponse> applyLecture (@RequestBody ApplyLectureAPIRequest request)
    {
        ApplyLectureCommand command = ApplyLectureCommand.builder()
                .studentId(request.userId())
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
}
