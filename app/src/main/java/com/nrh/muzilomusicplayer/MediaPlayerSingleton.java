package com.nrh.muzilomusicplayer;

import android.media.MediaPlayer;

public class MediaPlayerSingleton {

    // Declare mediaPlayer as a static variable
    private static MediaPlayer mediaPlayer;

    // Method to get the instance of MediaPlayer
    public static synchronized MediaPlayer getInstance() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        return mediaPlayer;
    }

    // Method to release the mediaPlayer and clean up resources
    public static void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // Method to play a media source
    public static void play() {
        if (mediaPlayer != null) {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        }
    }

    // Method to pause the current playback
    public static void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    // Method to stop the current playback
    public static void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            try {
                // Preparing the player again, so it's ready for reuse
                mediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Method to reset the mediaPlayer (useful for setting new data sources)
    public static void reset() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        }
    }

    // Method to set the data source
    public static void setDataSource(String path) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.reset(); // Always reset before setting a new data source
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare(); // Synchronous call to prepare the player
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to set a listener for completion events
    public static void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        if (mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(listener);
        }
    }
}
