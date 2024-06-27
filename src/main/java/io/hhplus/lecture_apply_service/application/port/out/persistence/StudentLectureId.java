package io.hhplus.lecture_apply_service.application.port.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class StudentLectureId implements Serializable {
    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "lecture_id")
    private LectureId lectureId;
    public StudentLectureId() {
    }
    // Constructor
    public StudentLectureId(Long studentId, LectureId lectureId) {
        this.studentId = studentId;
        this.lectureId = lectureId;
    }
}
