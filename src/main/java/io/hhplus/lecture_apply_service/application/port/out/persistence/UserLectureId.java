package io.hhplus.lecture_apply_service.application.port.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class UserLectureId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "lecture_id")
    private Long lectureId;
    public UserLectureId() {
    }
    // Constructor
    public UserLectureId (Long userId, Long lectureId) {
        this.userId = userId;
        this.lectureId = lectureId;
    }
}
