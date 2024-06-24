package io.hhplus.lecture_apply_service.infrastructure.entity;

import io.hhplus.lecture_apply_service.application.port.out.persistence.UserLectureId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "student_lecture")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentLectureJpaEntity {
    @EmbeddedId
    private UserLectureId id;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id", insertable = false, updatable = false)
    private StudentJpaEntity student;

    @ManyToOne
    @JoinColumn(name = "lecture_id", referencedColumnName = "id", insertable = false, updatable = false)
    private LectureJpaEntity lecture;

    private Boolean enrollment;

    public StudentLectureJpaEntity(StudentJpaEntity student, LectureJpaEntity lecture, Boolean enrollment){
        this.student = student;
        this.lecture = lecture;
        this.id = new UserLectureId(student.getId(), lecture.getId());
        this.enrollment = enrollment;
    }

}
