package io.hhplus.lecture_apply_service.presentation.dto.res;

import io.hhplus.lecture_apply_service.infrastructure.entity.Lecture;
import java.util.List;

public record ListLectureAPIResponse
    (List<Lecture> lectures){

}
