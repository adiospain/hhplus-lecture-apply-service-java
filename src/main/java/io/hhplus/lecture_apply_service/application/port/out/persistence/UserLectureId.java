package io.hhplus.lecture_apply_service.application.port.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class UserLectureId implements Serializable {
    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "lecture_id")
    private Long lectureId;
    public UserLectureId() {
    }
    // Constructor
    public UserLectureId (Long studentId, Long lectureId) {
        this.studentId = studentId;
        this.lectureId = lectureId;
    }
}
