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
    private final StudentJpaRepository userRepository;
    private final StudentLectureJpaRepository studentLectureRepository;

    private final Semaphore semaphore = new Semaphore(1);

    @Override
    @Transactional
    public ApplyLectureAPIResponse execute(ApplyLectureCommand command) {
        try{
            //semaphore.acquire();
            LectureJpaEntity lectureJpaEntity = lectureRepository.findById(command.getLectureId())
                    .orElseThrow(()->
                            new CustomException(ErrorCode.INVALID_LECTURE_ID));
            if (lectureJpaEntity.getCapacity() <= 0){
                return new ApplyLectureAPIResponse(command.getUserId(), command.getLectureId(), false);
            }

            StudentJpaEntity userJpaEntity = userRepository.findById(command.getUserId())
                    .orElseThrow(()->
                            new CustomException(ErrorCode.INVALID_USER_ID));

            lectureJpaEntity.setCapacity(lectureJpaEntity.getCapacity()-1);
            lectureRepository.save(lectureJpaEntity);
            StudentLectureJpaEntity userLecture = new StudentLectureJpaEntity(userJpaEntity, lectureJpaEntity);
            studentLectureRepository.save(userLecture);
            return new ApplyLectureAPIResponse(command.getUserId(), command.getLectureId(), true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            //semaphore.release();
        }

    }
}
