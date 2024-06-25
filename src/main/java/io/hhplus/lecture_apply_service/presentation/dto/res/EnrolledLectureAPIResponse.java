package io.hhplus.lecture_apply_service.presentation.dto.res;

import io.hhplus.lecture_apply_service.infrastructure.entity.StudentLecture;
import java.util.List;

public record EnrolledLectureAPIResponse (
    List<StudentLecture> enrolledLectures,
    boolean status
){

}
