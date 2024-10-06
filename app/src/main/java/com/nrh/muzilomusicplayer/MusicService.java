package com.nrh.muzilomusicplayer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MusicService extends Service {

    private static final String CHANNEL_ID = "MuziloMusicPlayerChannel";
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the media player
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if ("PLAY".equals(action)) {
            String musicPath = intent.getStringExtra("music_path");
            playMusic(musicPath);
            showNotification("Playing Song", "Artist Name");
        } else if ("PAUSE".equals(action)) {
            pauseMusic();
        } else if ("STOP".equals(action)) {
            stopMusic();
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;  // Return null as this is a started service, not bound service.
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    private void playMusic(String path) {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    private void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        stopForeground(true);  // Stop the service and remove the notification
        stopSelf();
    }

    // Create a notification with controls
    private void showNotification(String songTitle, String artistName) {
        createNotificationChannel();

        // Intent for playing the song
        Intent playIntent = new Intent(this, MusicService.class);
        playIntent.setAction("PLAY");
        PendingIntent playPendingIntent = PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Intent for pausing the song
        Intent pauseIntent = new Intent(this, MusicService.class);
        pauseIntent.setAction("PAUSE");
        PendingIntent pausePendingIntent = PendingIntent.getService(this, 1, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Intent for stopping the song
        Intent stopIntent = new Intent(this, MusicService.class);
        stopIntent.setAction("STOP");
        PendingIntent stopPendingIntent = PendingIntent.getService(this, 2, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Build the notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(songTitle)
                .setContentText(artistName)
                .setSmallIcon(R.drawable.baseline_music_note_24)
                .addAction(R.drawable.baseline_play_circle_24, "Play", playPendingIntent)  // Add play button
                .addAction(R.drawable.baseline_pause_circle, "Pause", pausePendingIntent)  // Add pause button
                .addAction(R.drawable.baseline_stop_24, "Stop", stopPendingIntent)  // Add stop button
                .setOngoing(true)  // Ensures the notification is not dismissable
                .setPriority(NotificationCompat.PRIORITY_LOW);

        // Start the service in the foreground
        startForeground(1, notificationBuilder.build());
    }

    // Create a notification channel for Android 8.0 and higher
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Muzilo Music Player Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
}
