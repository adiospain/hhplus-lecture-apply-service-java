package io.hhplus.lecture_apply_service.application.port.in;

import io.hhplus.lecture_apply_service.application.port.out.LectureLock;
import io.hhplus.lecture_apply_service.infrastructure.entity.UserJpaEntity;
import io.hhplus.lecture_apply_service.infrastructure.entity.UserLectureJpaEntity;
import io.hhplus.lecture_apply_service.common.UseCase;
import io.hhplus.lecture_apply_service.infrastructure.entity.LectureJpaEntity;
import io.hhplus.lecture_apply_service.exception.CustomException;
import io.hhplus.lecture_apply_service.infrastructure.repository.UserJpaLectureRepository;
import io.hhplus.lecture_apply_service.infrastructure.repository.LectureJpaRepository;
import io.hhplus.lecture_apply_service.infrastructure.repository.UserJpaRepository;
import io.hhplus.lecture_apply_service.presentation.dto.res.ApplyLectureAPIResponse;
import io.hhplus.lecture_apply_service.exception.ErrorCode;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@UseCase
public class ApplyLectureUseCaseImpl implements ApplyLectureUseCase {

    private final LectureLock lectureLock;
    private final LectureJpaRepository lectureRepository;
    private final UserJpaRepository userRepository;
    private final UserJpaLectureRepository userLectureRepository;

    @Override
    public ApplyLectureAPIResponse execute(ApplyLectureCommand command) {

        LectureJpaEntity lectureJpaEntity = lectureRepository.findById(command.getLectureId())
                .orElseThrow(()->
                        new CustomException(ErrorCode.INVALID_LECTURE_ID));
        UserJpaEntity userJpaEntity = userRepository.findById(command.getUserId())
                .orElseThrow(()->
                        new CustomException(ErrorCode.INVALID_USER_ID));


        UserLectureJpaEntity userLecture = new UserLectureJpaEntity(userJpaEntity, lectureJpaEntity);
        userLectureRepository.save(userLecture);
        return new ApplyLectureAPIResponse(command.getUserId(), command.getUserId(), true);
    }
}
