package io.hhplus.lecture_apply_service.application.port.in;

import io.hhplus.lecture_apply_service.application.port.out.LectureLock;
import io.hhplus.lecture_apply_service.infrastructure.entity.StudentJpaEntity;
import io.hhplus.lecture_apply_service.infrastructure.entity.StudentLectureJpaEntity;
import io.hhplus.lecture_apply_service.common.UseCase;
import io.hhplus.lecture_apply_service.infrastructure.entity.LectureJpaEntity;
import io.hhplus.lecture_apply_service.exception.CustomException;
import io.hhplus.lecture_apply_service.infrastructure.repository.jpa.StudentLectureJpaRepository;
import io.hhplus.lecture_apply_service.infrastructure.repository.jpa.LectureJpaRepository;
import io.hhplus.lecture_apply_service.infrastructure.repository.jpa.StudentJpaRepository;
import io.hhplus.lecture_apply_service.presentation.dto.res.ApplyLectureAPIResponse;
import io.hhplus.lecture_apply_service.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Semaphore;


@RequiredArgsConstructor
@UseCase
public class ApplyLectureUseCaseImpl implements ApplyLectureUseCase {

    private final LectureLock lectureLock;
    private final LectureJpaRepository lectureRepository;
    private final StudentJpaRepository studentRepository;
    private final StudentLectureJpaRepository studentLectureRepository;

    private final Semaphore semaphore = new Semaphore(1);



    @Transactional
    public ApplyLectureAPIResponse executeNope(ApplyLectureCommand command) {
        try{
            //semaphore.acquire();
            LectureJpaEntity lectureJpaEntity = lectureRepository.findByIdxLock(command.getLectureId())
                .orElseThrow(()->
                    new CustomException(ErrorCode.INVALID_LECTURE_ID));
            if (lectureJpaEntity.getCapacity() <= 0){
                return new ApplyLectureAPIResponse(command.getStudentId(), command.getLectureId(), false);
            }
            lectureJpaEntity.setCapacity(lectureJpaEntity.getCapacity()-1);
            lectureRepository.savexLock(lectureJpaEntity);
            return new ApplyLectureAPIResponse(command.getStudentId(), command.getLectureId(), true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            //semaphore.release();
        }
    }

    @Override
    @Transactional
    public ApplyLectureAPIResponse execute(ApplyLectureCommand command) {
        try{
            //semaphore.acquire();
            LectureJpaEntity lectureJpaEntity = lectureRepository.findById(command.getLectureId())
                    .orElseThrow(()->
                            new CustomException(ErrorCode.INVALID_LECTURE_ID));
            if (lectureJpaEntity.getCapacity() <= 0){
                return new ApplyLectureAPIResponse(command.getStudentId(), command.getLectureId(), false);
            }

            StudentJpaEntity studentJpaEntity = studentRepository.findById(command.getStudentId())
                    .orElseThrow(()->
                            new CustomException(ErrorCode.INVALID_STUDENT_ID));

            lectureJpaEntity.setCapacity(lectureJpaEntity.getCapacity()-1);
            lectureRepository.save(lectureJpaEntity);
            StudentLectureJpaEntity studentLecture = new StudentLectureJpaEntity(studentJpaEntity, lectureJpaEntity);
            studentLectureRepository.save(studentLecture);
            return new ApplyLectureAPIResponse(command.getStudentId(), command.getLectureId(), true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            //semaphore.release();
        }
    }
}
