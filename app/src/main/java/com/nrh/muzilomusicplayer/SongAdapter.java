package com.nrh.muzilomusicplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private List<String> availableSongs;
    private List<String> selectedSongs;

    public SongAdapter(List<String> availableSongs, List<String> selectedSongs) {
        this.availableSongs = availableSongs;
        this.selectedSongs = selectedSongs;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_multiple_choice, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        String song = availableSongs.get(position);
        holder.songTextView.setText(song);

        holder.songCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedSongs.add(song);
            } else {
                selectedSongs.remove(song);
            }
        });
    }

    @Override
    public int getItemCount() {
        return availableSongs.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView songTextView;
        CheckBox songCheckBox;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            songTextView = itemView.findViewById(android.R.id.text1);
            songCheckBox = itemView.findViewById(android.R.id.checkbox);
        }
    }
}
