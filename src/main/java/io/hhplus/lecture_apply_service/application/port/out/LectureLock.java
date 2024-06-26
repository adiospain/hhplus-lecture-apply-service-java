package io.hhplus.lecture_apply_service.application.port.out;

public interface LectureLock {

    void lockLecture (long lectureId);
    void releaseLecture (long lectureId);
}
