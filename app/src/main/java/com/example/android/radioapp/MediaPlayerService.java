package com.example.android.radioapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;

/**
 * Created by jotten on 14.12.17.
 */

public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener {
    MediaPlayer mediaPlayer;

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mediaPlayer.stop();
        Log.i("MediaPlayerService","Stop");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("MediaPlayerService", "Received start id " + startId + ": " + intent);
        String url = intent.getStringExtra("url");
        play(url);
        return START_NOT_STICKY;
    }

    public void play(String url){
        try {
            Uri radioUri = Uri.parse(url);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(getApplicationContext(), radioUri);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
        }catch (IOException e){
            Log.d("MediaPlayerService","Could not start audio streaming: "+e.getMessage());
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

    }

}
