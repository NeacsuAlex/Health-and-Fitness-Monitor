package com.example.healthandfitnessapp.services;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;

public class SoundService {

    public SoundService()
    {

    }

  public android.net.Uri playSound(int ringtoneManagerID)
    {
        return RingtoneManager.getDefaultUri(ringtoneManagerID);
    }

    public void playSound(Context context, android.net.Uri soundUri)
    {
        final MediaPlayer mp = MediaPlayer.create(context, soundUri);
        mp.start();
    }

}
