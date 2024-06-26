package io.hhplus.lecture_apply_service.application.port.out;

import io.hhplus.lecture_apply_service.application.port.out.LectureLock;
import org.springframework.stereotype.Component;

@Component
public class NoOpLectureLock implements LectureLock {


    @Override
    public void lockLecture(long lectureId) {
        //temporary
    }

    @Override
    public void releaseLecture(long lectureId) {
        //temporary
    }
}
