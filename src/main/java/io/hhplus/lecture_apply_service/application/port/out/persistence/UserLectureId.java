package io.hhplus.lecture_apply_service.application.port.out.persistence;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class UserLectureId implements Serializable {
    private Long studentId;
    private Long courseId;

    public UserLectureId() {
    }
    // Constructor
    public UserLectureId (Long studentId, Long courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }
}
