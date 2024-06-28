package io.hhplus.lecture_apply_service.application.port.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@Embeddable
public class StudentLectureId implements Serializable {
    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "lecture_id")
    private LectureId lectureId;
}
