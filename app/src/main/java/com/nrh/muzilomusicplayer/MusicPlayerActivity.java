package com.nrh.muzilomusicplayer;

import android.content.Intent;
import android.media.MediaPlayer;import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import androidx.activity.EdgeToEdge;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class MusicPlayerActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private String title, artist, path;
    private TextView titleTextView, artistTextView;
    private SeekBar seekBar;
    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_music_player);
        EdgeToEdge.enable(this);
        // Now you can safely access the view
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);return insets;
        });

        // Initialize views
        titleTextView = findViewById(R.id.titleTextView);artistTextView = findViewById(R.id.artistTextView);
        seekBar = findViewById(R.id.seekbar_music);
        ImageView playPauseButton = findViewById(R.id.playPauseButton);
        ImageView nextButton = findViewById(R.id.nextButton);
        ImageView previousButton = findViewById(R.id.previousButton);
        ImageView backButton = findViewById(R.id.back_button);

        // Get data from intent
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        artist = intent.getStringExtra("artist");
        path = intent.getStringExtra("path");

        // Set title and artist in the views
        titleTextView.setText(title);
        artistTextView.setText(artist);

        // Initialize media player
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();  // Start playing music
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Play/pause button
        playPauseButton.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                playPauseButton.setImageResource(R.drawable.baseline_play_circle_24);
            } else {
                mediaPlayer.start();
                playPauseButton.setImageResource(R.drawable.baseline_pause_circle);
            }
        });

        // SeekBar functionality
        mediaPlayer.setOnPreparedListener(mp -> seekBar.setMax(mediaPlayer.getDuration()));
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

        //back button working
        backButton.setOnClickListener(v -> {
            Intent intent1 = new Intent(MusicPlayerActivity.this, MainActivity.class);
            startActivity(intent1);
            finish();
        });

        // Next/previous buttons
        nextButton.setOnClickListener(v -> playNextSong());
        previousButton.setOnClickListener(v -> playPreviousSong());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start updating SeekBar when activity resumes
        startSeekBarUpdate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop updating SeekBar when activity pauses
        stopSeekBarUpdate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        stopSeekBarUpdate(); // Ensure runnable is stopped
    }

    // Placeholder methods for next/previous functionality
    private void playNextSong() {
        // Logic for playing next song
    }

    private void playPreviousSong() {
        // Logic for playing previous song
    }

    // Start updating SeekBar
    private void startSeekBarUpdate() {
        runnable = () -> {
            if (mediaPlayer != null) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(currentPosition);
            }
            handler.postDelayed(runnable, 1000); // Update every 1 second
        };
        handler.postDelayed(runnable, 1000);
    }

    // Stop updating SeekBar
    private void stopSeekBarUpdate() {
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }
}