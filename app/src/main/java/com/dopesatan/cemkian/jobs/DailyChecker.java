package com.dopesatan.cemkian.jobs;

import android.content.Context;

import androidx.annotation.NonNull;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.JobRequest;

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.dopesatan.cemkian.attendance_objs.AttendanceData;
import com.dopesatan.cemkian.workers.DTM;
import com.dopesatan.cemkian.workers.LocalNotificationManager;
import com.dopesatan.cemkian.workers.Runner;
import com.dopesatan.cemkian.workers.WebParser;
import com.google.firebase.crashlytics.internal.common.CrashlyticsCore;

public class DailyChecker extends DailyJob {

    static final String TAG = "daily_";
    private static final StringBuilder builder = new StringBuilder();
    private CrashlyticsCore Crashlytics;

    public static void schedule(Context context) {
        long start = TimeUnit.HOURS.toMillis(DTM.getInstance(context).getCycleT_Hour())
                + TimeUnit.MINUTES.toMillis(DTM.getInstance(context).getCycleT_Min());
        long end = start + TimeUnit.HOURS.toMillis(1);

       DailyJob.schedule(new JobRequest.Builder(TAG)
                .setUpdateCurrent(true), start, end);
    }

    @NonNull
    @Override
    protected DailyJobResult onRunDailyJob(@NonNull Params params) {
        try {
            new WebParser().start(
                    DTM.getInstance(getContext()).getUname(),
                    DTM.getInstance(getContext()).getPWD(),
                    new Runner() {
                        @Override
                        public void onComplete(AttendanceData data) {
                            AttendanceData old = DTM.getInstance(getContext()).getLastSavedATD();
                            if (old != null) {
                                for (int i = 0; i < data.getSubLen(); i++) {
                                    builder.append(data.getSub(i))
                                            .append(" : ")
                                            .append(String.format(Locale.US, "%.2f", data.getPercentInPoint(i) - old.getPercentInPoint(i))
                                            )
                                            .append("%")
                                            .append(
                                                    i != data.getSubLen() - 1 ? " | " : ""
                                            );
                                }
                                Logger.getLogger(builder.toString());
                                giveNotification();
                            }

                            DTM.getInstance(getContext()).updateAttendanceData(data);
                        }

                        @Override
                        public void onCheckComplete(boolean isNull) {
                        }

                        @Override
                        public void onError() {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        return DailyJobResult.SUCCESS;
    }

    private void giveNotification() {
        LocalNotificationManager.getInstance(getContext())
                .setTitle("Percentage Change :")
                .setBody(builder.toString())
                .show();
    }
}
