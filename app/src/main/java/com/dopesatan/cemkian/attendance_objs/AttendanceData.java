package com.dopesatan.cemkian.attendance_objs;

import java.io.Serializable;
import java.util.ArrayList;

public class AttendanceData implements Serializable {

    private ArrayList<String> subjects;
    private ArrayList<Integer> hoursTotal;
    private ArrayList<Integer> hoursObtained;
    private ArrayList<Float> percent;
    private boolean isCached;
    private boolean isAvailable;

    public AttendanceData() {
        subjects = new ArrayList<>();
        hoursTotal = new ArrayList<>();
        hoursObtained = new ArrayList<>();
        percent = new ArrayList<>();
    }

    public void addSub(String sub) {
        subjects.add(sub);
    }

    public void addHourTotal(int tHrs) {
        hoursTotal.add(tHrs);
    }

    public void addHourObt(int oHrs) {
        hoursObtained.add(oHrs);
    }

    public String getSub(int index) {
        return subjects.get(index);
    }

    public int getHourTotal(int index) {
        return hoursTotal.get(index);
    }

    public int getHourObt(int index) {
        return hoursObtained.get(index);
    }

    public void addPercent(float per) {
        percent.add(per);
    }

    public float getPercentInPoint(int index) {
        return percent.get(index);
    }

    public int getSubLen() {
        return subjects.size();
    }

    public void setCached(boolean cached) {
        isCached = cached;
    }

    public boolean isCached() {
        return isCached;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
