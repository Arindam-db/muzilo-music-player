package com.nrh.muzilomusicplayer;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;
import java.io.IOException;
import java.util.List;

public class MusicPlayerActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private TextView titleTextView, artistTextView;
    private SeekBar seekBar, volumeBar;
    private Handler handler = new Handler();
    private Runnable runnable;
    private List<MusicData> musicList;
    private int currentPosition;
    private AudioManager audioManager;  // AudioManager for volume control

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        titleTextView = findViewById(R.id.titleTextView);
        artistTextView = findViewById(R.id.artistTextView);
        seekBar = findViewById(R.id.seekbar_music);
        volumeBar = findViewById(R.id.volume_bar);  // Volume control SeekBar
        ImageView playPauseButton = findViewById(R.id.playPauseButton);
        ImageView nextButton = findViewById(R.id.nextButton);
        ImageView previousButton = findViewById(R.id.previousButton);
        ImageView backButton = findViewById(R.id.back_button);

        // Get the music list and current position from the intent
        Intent intent = getIntent();
        musicList = intent.<MusicData>getParcelableArrayListExtra("music_list");
        currentPosition = intent.getIntExtra("position", 0);

        // Get the current song and set title and artist in the views
        MusicData currentMusic = musicList.get(currentPosition);
        titleTextView.setText(currentMusic.getTitle());
        artistTextView.setText(currentMusic.getArtist());

        // Initialize media player and start playing the current song
        playMusic(currentMusic.getPath());

        // Initialize AudioManager for volume control
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        setupVolumeControl();  // Method to setup volume SeekBar

        // Play/pause button functionality
        playPauseButton.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                playPauseButton.setImageResource(R.drawable.baseline_play_circle_24);
            } else {
                mediaPlayer.start();
                playPauseButton.setImageResource(R.drawable.baseline_pause_circle);
            }
        });

        // Next/previous buttons functionality
        nextButton.setOnClickListener(v -> playNextSong());
        previousButton.setOnClickListener(v -> playPreviousSong());

        // Back button functionality
        backButton.setOnClickListener(v -> {
            Intent intent1 = new Intent(MusicPlayerActivity.this, MainActivity.class);
            startActivity(intent1);
            finish();
        });

        // SeekBar functionality for track progress
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Update SeekBar when media player is ready
        mediaPlayer.setOnPreparedListener(mp -> seekBar.setMax(mediaPlayer.getDuration()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        startSeekBarUpdate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSeekBarUpdate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        stopSeekBarUpdate();
    }

    // Method to play the next song
    private void playNextSong() {
        if (currentPosition < musicList.size() - 1) {
            currentPosition++;
        } else {
            currentPosition = 0;  // Loop back to the first song
        }
        MusicData nextMusic = musicList.get(currentPosition);
        playMusic(nextMusic.getPath());
        updateUI(nextMusic);
    }

    // Method to play the previous song
    private void playPreviousSong() {
        if (currentPosition > 0) {
            currentPosition--;
        } else {
            currentPosition = musicList.size() - 1;  // Loop to the last song
        }
        MusicData previousMusic = musicList.get(currentPosition);
        playMusic(previousMusic.getPath());
        updateUI(previousMusic);
    }

    // Helper method to start playing music
    private void playMusic(String path) {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to update the UI
    private void updateUI(MusicData musicData) {
        titleTextView.setText(musicData.getTitle());
        artistTextView.setText(musicData.getArtist());
        ImageView playPauseButton = findViewById(R.id.playPauseButton);
        playPauseButton.setImageResource(R.drawable.baseline_pause_circle);
    }

    // Method to setup volume control
    private void setupVolumeControl() {
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        volumeBar.setMax(maxVolume);
        volumeBar.setProgress(currentVolume);

        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    // Set the device's media volume
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    // Method to start updating the SeekBar for track progress
    private void startSeekBarUpdate() {
        runnable = () -> {
            if (mediaPlayer != null) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(currentPosition);
            }
            handler.postDelayed(runnable, 1000);  // Update every 1 second
        };
        handler.postDelayed(runnable, 1000);
    }

    // Method to stop updating the SeekBar
    private void stopSeekBarUpdate() {
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }
}