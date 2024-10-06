package com.nrh.muzilomusicplayer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nrh.muzilomusicplayer.PlaylistAdapter;
import com.nrh.muzilomusicplayer.R;

import java.util.ArrayList;
import java.util.List;

public class PlaylistFragment extends Fragment implements PlaylistAdapter.OnPlaylistClickListener {

    private RecyclerView recyclerView;
    private PlaylistAdapter adapter;
    private List<String> playlistNames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        FloatingActionButton fabAddPlaylist = view.findViewById(R.id.fabAddPlaylist);

        // Setup RecyclerView
        playlistNames = new ArrayList<>();
        adapter = new PlaylistAdapter(playlistNames, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Add new playlist
        fabAddPlaylist.setOnClickListener(v -> showAddPlaylistDialog());

        return view;
    }

    // Handle playlist click
    @Override
    public void onPlaylistClick(String playlistName) {
        // Open song selection screen (you can use a fragment or an activity)
        Bundle bundle = new Bundle();
        bundle.putString("playlist_name", playlistName);

        SongSelectionFragment songSelectionFragment = new SongSelectionFragment();
        songSelectionFragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, songSelectionFragment) // Replace with your container ID
                .addToBackStack(null)
                .commit();
    }

    private void showAddPlaylistDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Playlist");

        final EditText input = new EditText(getContext());
        input.setHint("Enter playlist name");
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String playlistName = input.getText().toString();
            if (!playlistName.isEmpty()) {
                playlistNames.add(playlistName);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Playlist name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
