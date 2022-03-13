package com.dopesatan.cemkian.workers;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.dopesatan.cemkian.R;
import com.dopesatan.cemkian.ui_access.MainActivity;

public class LocalNotificationManager {
    private Context mContext;
    private NotificationManager mNotificationManager;
    private String title;
    private String body;
    private static final String CHANNEL_ID = "_channel";
    private static final String CHANNEL_NAME = "Attendance Checker Notification";
    private NotificationChannel notificationChannel;

    private LocalNotificationManager(Context context) {
        mContext = context;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static LocalNotificationManager getInstance(Context context) {
        return new LocalNotificationManager(context);
    }

    public LocalNotificationManager setTitle(String title) {
        this.title = title;
        return this;
    }

    public LocalNotificationManager setBody(String body) {
        this.body = body;
        return this;
    }

    public void show() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelsAndNotify();
            Notification notification = new Notification.Builder(mContext, notificationChannel.getId())
                    .setContentTitle(title)
                    .setAutoCancel(false)
                    .setStyle(new Notification.BigTextStyle().bigText(body))
                    .setContentIntent(PendingIntent.getActivity(mContext, 0,
                            new Intent(mContext, MainActivity.class), 0))
                    .setSmallIcon(R.drawable.ic_assignment)
                    .setShowWhen(false)
                    .setLocalOnly(false)
                    .build();
            mNotificationManager.notify(233, notification);
        } else {
            Notification notification = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                    .setContentTitle(title)
                    .setAutoCancel(false)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                    .setContentIntent(PendingIntent.getActivity(mContext, 0,
                            new Intent(mContext, MainActivity.class), 0))
                    .setSmallIcon(R.drawable.ic_assignment)
                    .setShowWhen(true)
                    .setLocalOnly(false)
                    .build();
            mNotificationManager.notify(233, notification);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannelsAndNotify() {
        notificationChannel = new NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);

        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.setShowBadge(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        mNotificationManager.createNotificationChannel(notificationChannel);
    }
}
