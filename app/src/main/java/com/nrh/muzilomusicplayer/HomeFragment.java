package com.nrh.muzilomusicplayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.search.SearchBar;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private MusicAdapter musicAdapter;
    private List<MusicData> musicList = new ArrayList<>();  // Full list of music
    private List<MusicData> filteredMusicList = new ArrayList<>();  // Filtered list for search results
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize SearchView
        searchView = view.findViewById(R.id.searchView);

        // Load music files
        loadMusic();

        // Set up SearchView to filter music list
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Filter the music list when search is submitted
                filterMusic(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the music list as the user types
                filterMusic(newText);
                return false;
            }
        });

        return view;
    }

    // Method to load music from device storage
    private void loadMusic() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int pathColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do {
                String title = cursor.getString(titleColumn);
                String artist = cursor.getString(artistColumn);
                String path = cursor.getString(pathColumn);

                // Create a new MusicData object and add it to the list
                MusicData music = new MusicData(title, artist, path);
                musicList.add(music);

            } while (cursor.moveToNext());

            cursor.close();
        }

        // Initialize adapter with the full music list and context
        musicAdapter = new MusicAdapter(musicList, getContext());
        recyclerView.setAdapter(musicAdapter);
    }

    // Method to filter the music list based on search query
    private void filterMusic(String query) {
        filteredMusicList.clear();
        if (query.isEmpty()) {
            filteredMusicList.addAll(musicList);  // If query is empty, show all music
        } else {
            for (MusicData music : musicList) {
                if (music.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        music.getArtist().toLowerCase().contains(query.toLowerCase())) {
                    filteredMusicList.add(music);
                }
            }
        }
        musicAdapter.updateData(filteredMusicList);  // Update the adapter with the filtered list
    }
}