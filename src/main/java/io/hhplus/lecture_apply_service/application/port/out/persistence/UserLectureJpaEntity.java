package io.hhplus.lecture_apply_service.application.port.out.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_lecture")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLectureJpaEntity {
    @EmbeddedId
    private UserLectureId id;

    @ManyToOne
    @MapsId("user_id")
    private UserJpaEntity user;

    @ManyToOne
    @MapsId("lecture_id")
    private LectureJpaEntity lecture;

    public UserLectureJpaEntity (UserJpaEntity user, LectureJpaEntity lecture){
        this.user = user;
        this.lecture = lecture;
        this.id = new UserLectureId(user.getId(), lecture.getId());
    }

}
