package com.dopesatan.cemkian.workers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;

import java.util.Objects;

import com.dopesatan.cemkian.attendance_objs.AttendanceData;

public class DTM {

    private static final String UNAME = "_un";
    private static final String PWD = "_pw";
    private static final String ATD = "_atd";
    private static final String LSTCHK = "_lastcheck";
    private static final String CYCLE_T_HOUR = "_cycle_t_hr";
    private static final String CYCLE_T_MINUTE = "_cycle_t_min";
    private static final String CYCLE_STATE = "_C_s";
    private static final String NEWSER = "_new";

    private static DTM instance;
    private SharedPreferences sharedPref;

    private DTM(Context context) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static DTM getInstance(Context context) {
        if (instance != null)
            return instance;
        else {
            instance = new DTM(context);
            return instance;
        }
    }

    public String getUname() {
        return sharedPref.getString(UNAME, "");
    }

    public void setUname(String uname) {
        sharedPref.edit().putString(UNAME, uname).apply();
    }

    public String getPWD() {
        return Objects.requireNonNull(sharedPref.getString(PWD, "")).trim();
    }

    public void setPwd(String pwd) {
        sharedPref.edit().putString(PWD, pwd).apply();
    }

    @Nullable
    public AttendanceData getLastSavedATD() {
        String dataGet = sharedPref.getString(ATD, "");
        AttendanceData data = new AttendanceData();
        data.setCached(true);
        data.setAvailable(true);
        if (dataGet != null && !dataGet.equals("")) {
            try {
                String[] dataMod = dataGet.split(",");
                for (String lcd : dataMod) {
                    data.addSub(lcd.split(":")[0]);
                    data.addHourObt(Integer.parseInt(lcd.split(":")[1]));
                    data.addHourTotal(Integer.parseInt(lcd.split(":")[2]));
                    data.addPercent(Float.parseFloat(lcd.split(":")[3]));
                }
                return data;
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /*
     * Access Asynchronously
     * */
    public void updateAttendanceData(AttendanceData data) {
        StringBuilder dataMake = new StringBuilder();
        for (int i = 0; i < data.getSubLen(); i++) {
            if (i != data.getSubLen() - 1) {
                dataMake.append(data.getSub(i))
                        .append(":")
                        .append(data.getHourObt(i))
                        .append(":")
                        .append(data.getHourTotal(i))
                        .append(":")
                        .append(data.getPercentInPoint(i))
                        .append(",");
            } else {
                dataMake.append(data.getSub(i))
                        .append(":")
                        .append(data.getHourObt(i))
                        .append(":")
                        .append(data.getHourTotal(i))
                        .append(":")
                        .append(data.getPercentInPoint(i));
            }
        }
        sharedPref.edit().putString(ATD, dataMake.toString()).apply();
    }

    public long getLastCheckTimeStamp() {
        return sharedPref.getLong(LSTCHK, 0L);
    }

    public void setLastCheckTimeStamp(long mills) {
        sharedPref.edit().putLong(LSTCHK, mills).apply();
    }

    public int getCycleT_Hour() {
        return sharedPref.getInt(CYCLE_T_HOUR, 8);
    }

    public void setCycleT_Hour(int nanoTime) {
        sharedPref.edit().putInt(CYCLE_T_HOUR, nanoTime).apply();
    }

    public int getCycleT_Min() {
        return sharedPref.getInt(CYCLE_T_MINUTE, 0);
    }

    public void setCycleT_Min(int nanoTime) {
        sharedPref.edit().putInt(CYCLE_T_MINUTE, nanoTime).apply();
    }

    public boolean isCycleEnabled() {
        return sharedPref.getBoolean(CYCLE_STATE, true);
    }

    public void setCycleState(boolean what) {
        sharedPref.edit().putBoolean(CYCLE_STATE, what).apply();
    }

    public boolean isNewUser() {
        return sharedPref.getBoolean(NEWSER, true);
    }

    public void setNewUserNoMore() {
        sharedPref.edit().putBoolean(NEWSER, false).apply();
    }
}
