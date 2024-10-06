package com.nrh.muzilomusicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MusicPlayerActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private String title, artist, path;
    private TextView titleTextView, artistTextView;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        // Initialize views
        titleTextView = findViewById(R.id.titleTextView);
        artistTextView = findViewById(R.id.artistTextView);
        seekBar = findViewById(R.id.seekbar_music);
        ImageButton playPauseButton = findViewById(R.id.playPauseButton);
        ImageButton nextButton = findViewById(R.id.nextButton);
        ImageButton previousButton = findViewById(R.id.previousButton);
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
        new Thread(() -> {
            while (mediaPlayer != null && mediaPlayer.isPlaying()) {
                try {
                    Thread.sleep(1000);
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

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
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MusicPlayerActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Next/previous buttons
        nextButton.setOnClickListener(v -> playNextSong());
        previousButton.setOnClickListener(v -> playPreviousSong());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // Placeholder methods for next/previous functionality
    private void playNextSong() {
        // Logic for playing next song
    }

    private void playPreviousSong() {
        // Logic for playing previous song
    }
}
