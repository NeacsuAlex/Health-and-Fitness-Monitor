package com.example.healthandfitnessapp.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.healthandfitnessapp.R;
import com.example.healthandfitnessapp.activities.AlarmActivity;
import com.example.healthandfitnessapp.activities.HomeActivity;
import com.example.healthandfitnessapp.fragments.NotificationsFragment;
import com.example.healthandfitnessapp.models.Notification;

public class AlarmService extends IntentService {
    private NotificationManager alarmNotificationManager;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        sendNotification("Wake Up! Wake Up!");
    }

    private void sendNotification(String msg) {
        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, AlarmActivity.class), 0);

        Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
        NotificationService notificationService = new NotificationService();
        if (SettingsManager.receiveNotifications) {
            long millis = System.currentTimeMillis();
            java.sql.Date date = new java.sql.Date(millis);
            NotificationsFragment.AddNotification(new Notification("You got a new alarm!", "New alarm! Wake up!", date));
            notificationService.showNotification(getApplicationContext(), "You got a new alarm!", "New alarm! Wake up!", intent, 1);
        }

    }
}