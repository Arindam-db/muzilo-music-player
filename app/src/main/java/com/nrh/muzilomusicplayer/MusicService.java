package com.nrh.muzilomusicplayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import java.io.IOException;

public class MusicService extends Service {

    private MediaPlayer mediaPlayer;
    private static final String CHANNEL_ID = "MuziloMusicPlayerChannel";
    private static MusicService instance;

    // Singleton pattern to access the MediaPlayer instance
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public static MusicService getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mediaPlayer = new MediaPlayer();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String musicPath = intent.getStringExtra("music_path");

        if (musicPath == null || musicPath.isEmpty()) {
            // Log or handle the error: musicPath is null or empty
            stopSelf();  // Stop the service if the path is invalid
            return START_NOT_STICKY;
        }

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musicPath); // Set the data source
            mediaPlayer.prepare(); // Prepare the media player
            mediaPlayer.start(); // Start playing the music
        } catch (IOException e) {
            e.printStackTrace();
            stopSelf(); // Stop the service if there's an error
            return START_NOT_STICKY;
        }

        // Show notification
        Intent notificationIntent = new Intent(this, MusicPlayerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Muzilo Music Player")
                .setContentText("Music is playing")
                .setSmallIcon(R.drawable.baseline_play_circle_24) // Ensure you have the correct icon here
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        startForeground(1, notification); // Start the service in foreground

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop(); // Stop playback
            mediaPlayer.release(); // Release resources
            mediaPlayer = null; // Nullify the media player
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // Not binding this service
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Muzilo Music Player Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = this.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
}
