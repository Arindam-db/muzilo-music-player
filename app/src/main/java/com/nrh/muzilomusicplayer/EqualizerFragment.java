package com.nrh.muzilomusicplayer;

import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class EqualizerFragment extends Fragment {

    private Equalizer equalizer;
    private BassBoost bassBoost;
    private MediaPlayer mediaPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_equalizer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get MediaPlayer instance
        MusicService musicService = MusicService.getInstance();
        if (musicService != null) {
            mediaPlayer = musicService.getMediaPlayer();

            if (mediaPlayer != null) {
                // Initialize equalizer and bass boost
                equalizer = new Equalizer(0, mediaPlayer.getAudioSessionId());
                equalizer.setEnabled(true);

                bassBoost = new BassBoost(0, mediaPlayer.getAudioSessionId());
                bassBoost.setEnabled(true);

                // Set click listeners for different presets
                TextView normalEq = view.findViewById(R.id.normal_eq);
                TextView bassEq = view.findViewById(R.id.bass_eq);
                TextView trebleEq = view.findViewById(R.id.treble_eq);
                TextView rockEq = view.findViewById(R.id.rock_eq);

                normalEq.setOnClickListener(v -> applyNormalPreset());
                bassEq.setOnClickListener(v -> applyBassBoostPreset());
                trebleEq.setOnClickListener(v -> applyTrebleBoostPreset());
                rockEq.setOnClickListener(v -> applyRockMetalPreset());
            } else {
                showError("MediaPlayer is not initialized. Please try again later.");
            }
        } else {
            showError("MusicService is not available. Please start the music service.");
        }
    }

    // Normal Preset - flat equalizer
    private void applyNormalPreset() {
        setEqualizerPreset(0);  // Setting to default preset if available
        bassBoost.setStrength((short) 0);
    }

    // Bass Boost Preset
    private void applyBassBoostPreset() {
        bassBoost.setStrength((short) 1000);  // Boost bass to high level
        setEqualizerPreset(1);  // Use preset 1, which is usually a low-frequency boost
    }

    // Treble Boost Preset
    private void applyTrebleBoostPreset() {
        if (equalizer.getNumberOfBands() > 0) {
            equalizer.setBandLevel((short) 0, (short) -1000);  // Reduce bass band
            equalizer.setBandLevel((short) 4, (short) 1500);  // Boost treble band
        }
        bassBoost.setStrength((short) 0);  // Disable bass boost for treble
    }

    // Rock Metal Preset
    private void applyRockMetalPreset() {
        for (short i = 0; i < equalizer.getNumberOfPresets(); i++) {
            String presetName = equalizer.getPresetName(i);
            if (presetName.equalsIgnoreCase("Rock")) {
                setEqualizerPreset(i);
                bassBoost.setStrength((short) 500);  // Moderate bass boost
                return;
            }
        }
        showError("Rock preset not available");
    }

    private void setEqualizerPreset(int preset) {
        if (preset >= 0 && preset < equalizer.getNumberOfPresets()) {
            equalizer.usePreset((short) preset);
        }
    }

    private void showError(String message) {
        // You can use a Toast, Snackbar, or any other way to inform the user
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (equalizer != null) {
            equalizer.release();
        }
        if (bassBoost != null) {
            bassBoost.release();
        }
    }
}
