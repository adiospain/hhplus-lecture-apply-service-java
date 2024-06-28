package io.hhplus.lecture_apply_service.infrastructure.entity;

import io.hhplus.lecture_apply_service.application.port.out.persistence.StudentLectureId;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "student_lecture")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentLecture {

  @EmbeddedId
  private StudentLectureId id;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Student student;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "lecture_id", referencedColumnName = "id", insertable = false, updatable = false),
        @JoinColumn(name = "start_at", referencedColumnName = "start_at", insertable = false, updatable = false),
    })
    private Lecture lecture;

    private Boolean enrollment;

    public StudentLecture(Student student, Lecture lecture, Boolean enrollment){
        this.student = student;
        this.lecture = lecture;
        this.id = new StudentLectureId(student.getId(), lecture.getId());
        this.enrollment = enrollment;
    }

}
