package io.hhplus.lecture_apply_service.application.port.in;

import io.hhplus.lecture_apply_service.application.port.out.LectureLock;
import io.hhplus.lecture_apply_service.infrastructure.entity.Student;

import io.hhplus.lecture_apply_service.common.UseCase;
import io.hhplus.lecture_apply_service.infrastructure.entity.Lecture;
import io.hhplus.lecture_apply_service.exception.CustomException;
import io.hhplus.lecture_apply_service.infrastructure.entity.StudentLecture;
import io.hhplus.lecture_apply_service.infrastructure.repository.LectureRepository;
import io.hhplus.lecture_apply_service.infrastructure.repository.StudentLectureRepository;
import io.hhplus.lecture_apply_service.infrastructure.repository.StudentRepository;
import io.hhplus.lecture_apply_service.presentation.dto.res.ApplyLectureAPIResponse;
import io.hhplus.lecture_apply_service.exception.ErrorCode;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;



@RequiredArgsConstructor
@UseCase
public class ApplyLectureUseCaseImpl implements ApplyLectureUseCase {

    private final LectureLock lectureLock;
    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;
    private final StudentLectureRepository studentLectureRepository;

    @Transactional
    @Override
    public ApplyLectureAPIResponse execute (ApplyLectureCommand command) {
      //유효한 학생ID 확인
      Student student = validateStudent(command.getStudentId());
      //이미 수강한 특강 확인
      validateNotAlreadyApplied(student.getId(), command.getLectureId());
      //유효한 특강ID 확인
      Lecture lecture = validateLecture(command.getLectureId());

      //수강 인원 확인
      ApplyLectureAPIResponse response = handleLectureCapacity(student, lecture);
      if (response != null) {
        //수강 인원 초과로 수강 실패
        saveApplyHistory(student,lecture,false);
        return response;
      }

      if (LocalDateTime.now().isBefore(lecture.getOpen_at())){
        throw new CustomException(ErrorCode.INVALID_TIME);
      }

      //수강인원 감소
      lecture.setCapacity(lecture.getCapacity()-1);
      lectureRepository.savexLock(lecture);

      //수강신청 성공 히스토리 추가
      saveApplyHistory(student,lecture,true);
      return new ApplyLectureAPIResponse(command.getStudentId(), command.getLectureId(), true);
    }

  private void saveApplyHistory(Student student, Lecture lecture, boolean enrollment) {
    StudentLecture studentLecture = new StudentLecture(student, lecture, enrollment);
    studentLectureRepository.save(studentLecture);
  }

  private Lecture validateLecture(Long lectureId) {
    return lectureRepository.findByIdxLock(lectureId)
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_LECTURE_ID));
  }
  private void validateNotAlreadyApplied(Long studentId, Long lectureId) {
    if (studentLectureRepository.existsByStudentIdAndLectureIdAndEnrollmentIsTrue(studentId, lectureId)) {
      throw new CustomException(ErrorCode.ALREADY_APPLIED);
    }
  }

  private Student validateStudent(Long studentId) {
    return studentRepository.findById(studentId)
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_STUDENT_ID));
  }

  private ApplyLectureAPIResponse handleLectureCapacity(Student student, Lecture lecture) {
      return lecture.getCapacity() == 0 ? new ApplyLectureAPIResponse(student.getId(), lecture.getId(), false) : null;
  }


}
