package com.dopesatan.cemkian.workers;

import com.dopesatan.cemkian.attendance_objs.AttendanceData;

public interface Runner {
    void onComplete(AttendanceData data);

    void onCheckComplete(boolean isNull);

    void onError();
}
