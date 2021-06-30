package com.example.healthandfitnessapp.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.preference.PreferenceManager;

public class SoundService {

    public SoundService() {

    }

    public android.net.Uri playSound(Context context, int ringtoneManagerID, boolean isAlarm) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int volume;
        if (!isAlarm)
            volume = prefs.getInt("volume_notifications", 70);
        else
            volume = prefs.getInt("volume_alarm", 70);

        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        manager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);

        Uri notification = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        MediaPlayer player = MediaPlayer.create(context, notification);
        player.start();
        return notification;
    }

    public void playSound(Context context, android.net.Uri soundUri) {
        final MediaPlayer mp = MediaPlayer.create(context, soundUri);
        mp.start();
    }

}
