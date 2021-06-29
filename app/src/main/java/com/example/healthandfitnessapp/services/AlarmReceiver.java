package com.example.healthandfitnessapp.services;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.legacy.content.WakefulBroadcastReceiver;
import androidx.preference.PreferenceManager;

import com.example.healthandfitnessapp.activities.AlarmActivity;
import com.example.healthandfitnessapp.activities.HomeActivity;
import com.example.healthandfitnessapp.fragments.NotificationsFragment;
import com.example.healthandfitnessapp.models.Notification;

import java.util.Timer;
import java.util.TimerTask;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    AlarmActivity inst;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onReceive(final Context context, Intent intent) {
        //this will update the UI with message
        inst = AlarmActivity.instance();
        inst.setAlarmText("Alarm! Wake up! Wake up!");

        //this will sound the alarm tone
        //this will sound the alarm once, if you wish to
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        //Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        float volume = (float)(prefs.getInt("volume_alarm", 70))/100f;
        ringtone.setVolume(volume);
        ringtone.play();
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                ringtone.stop();

                Intent intent = new Intent(context, AlarmActivity.class);
                NotificationService notificationService=new NotificationService();
                if(SettingsManager.receiveNotifications)
                {
                    long millis=System.currentTimeMillis();
                    java.sql.Date date=new java.sql.Date(millis);
                    NotificationsFragment.AddNotification(new Notification("Alarm stopped!","Alarm stopped!",date));
                    notificationService.showNotification(context, "Alarm stopped!", "Alarm stopped!", intent, 1);
                }
                t.cancel();
            }
        }, 15000);

        //this will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}