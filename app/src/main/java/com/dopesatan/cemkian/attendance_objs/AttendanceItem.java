package com.dopesatan.cemkian.attendance_objs;

public class AttendanceItem {
    private String subject;
    private int hoursTotal;
    private int hoursObtained;
    private float percent;

    String getSubject() {
        return subject;
    }

    public AttendanceItem setSubject(String subjects) {
        this.subject = subjects;
        return this;
    }
    
    Integer getHoursTotal() {
        return hoursTotal;
    }

    public AttendanceItem setHoursTotal(int hoursTotal) {
        this.hoursTotal = hoursTotal;
        return this;
    }
    
    Integer getHoursObtained() {
        return hoursObtained;
    }

    public AttendanceItem setHoursObtained(int hoursObtained) {
        this.hoursObtained = hoursObtained;
        return this;
    }
    
    public Float getPercentInPoint() {
        return percent;
    }

    public AttendanceItem setPercentInPoint(float percent) {
        this.percent = percent;
        return this;
    }
}
