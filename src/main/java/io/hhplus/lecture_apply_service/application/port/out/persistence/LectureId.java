package io.hhplus.lecture_apply_service.application.port.out.persistence;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@Embeddable
public class LectureId implements Serializable {
  @Column(name = "id")
  private Long lectureId;

  @Column(name = "start_at")
  private LocalDateTime startAt;
}