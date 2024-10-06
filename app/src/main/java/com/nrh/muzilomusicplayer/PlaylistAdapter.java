package com.nrh.muzilomusicplayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {

    private List<String> playlistNames;
    private OnPlaylistClickListener listener;

    // Constructor that accepts an OnPlaylistClickListener
    public PlaylistAdapter(List<String> playlistNames, OnPlaylistClickListener listener) {
        this.playlistNames = playlistNames;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        String playlistName = playlistNames.get(position);
        holder.playlistNameTextView.setText(playlistName);

        // Set click listener to handle playlist clicks
        holder.itemView.setOnClickListener(v -> listener.onPlaylistClick(playlistName));
    }

    @Override
    public int getItemCount() {
        return playlistNames.size();
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        TextView playlistNameTextView;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            playlistNameTextView = itemView.findViewById(android.R.id.text1);
        }
    }

    // Interface for playlist click handling
    public interface OnPlaylistClickListener {
        void onPlaylistClick(String playlistName);
    }
}
