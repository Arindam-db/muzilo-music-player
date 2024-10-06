package com.nrh.muzilomusicplayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SongSelectionFragment extends Fragment {

    private String playlistName;
    private List<String> availableSongs;
    private List<String> selectedSongs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_selection, container, false);

        // Retrieve the playlist name from arguments
        if (getArguments() != null) {
            playlistName = getArguments().getString("playlist_name");
        }

        // Sample list of available songs (you can replace this with real data)
        availableSongs = new ArrayList<>();
        availableSongs.add("Song 1");
        availableSongs.add("Song 2");
        availableSongs.add("Song 3");

        selectedSongs = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewSongs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SongAdapter songAdapter = new SongAdapter(availableSongs, selectedSongs);
        recyclerView.setAdapter(songAdapter);

        Button btnAddSongs = view.findViewById(R.id.btnAddSongs);
        btnAddSongs.setOnClickListener(v -> {
            if (!selectedSongs.isEmpty()) {
                Toast.makeText(getContext(), "Songs added to " + playlistName, Toast.LENGTH_SHORT).show();
                // Logic to add the songs to the playlist (e.g., store them in a database)
            } else {
                Toast.makeText(getContext(), "No songs selected", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
