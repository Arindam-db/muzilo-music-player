package com.nrh.muzilomusicplayer;

import android.app.Service;
import android.media.MediaPlayer;

abstract class MusicService extends Service {

    private MediaPlayer mediaPlayer;

    // Singleton pattern to access media player instance
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public static MusicService getInstance() {
        return instance;
    }

    private static MusicService instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mediaPlayer = new MediaPlayer();
    }
}
