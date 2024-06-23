package io.hhplus.lecture_apply_service.application.port.out.persistence;

import io.hhplus.lecture_apply_service.infrastructure.repository.UserLectureRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lecture")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureJpaEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer capacity;
    private LocalDateTime open_at;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserLectureJpaEntity> attend = new HashSet<>();
}
