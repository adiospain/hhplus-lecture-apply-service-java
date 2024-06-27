package io.hhplus.lecture_apply_service.infrastructure.entity;

import io.hhplus.lecture_apply_service.application.port.out.persistence.LectureId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lecture")
@Data
@NoArgsConstructor
public class Lecture {

    @EmbeddedId
    private LectureId id;

    @Column (name = "tmp_id")
    private Long tmpId;

    @Column (name = "start_at")
    private LocalDateTime startAt;

    @Column (name = "name")
    private String name;

    @Column (name = "capacity")
    private Integer capacity;
    @Column (name = "open_at")
    private LocalDateTime open_at;


    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StudentLecture> attend = new HashSet<>();

    public Lecture(Long tmpId, LocalDateTime startAt, String name, Integer capacity, LocalDateTime open_at, HashSet attend){
        this.tmpId = tmpId;
        this.startAt = startAt;
        this.name = name;
        this.capacity = capacity;
        this.open_at = open_at;
        this.attend = attend;
        this.id = new LectureId(tmpId, startAt);
    }
}
