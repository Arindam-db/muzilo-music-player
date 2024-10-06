package com.nrh.muzilomusicplayer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    private final List<MusicData> musicDataList;
    private List<MusicData> filteredList;
    private Context context;  // Add context

    public MusicAdapter(List<MusicData> musicDataList, Context context) {
        this.musicDataList = musicDataList;
        this.filteredList = new ArrayList<>(musicDataList);
        this.context = context; // Correct assignment
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        MusicData musicData = filteredList.get(position);
        holder.title.setText(musicData.getTitle());
        holder.artist.setText(musicData.getArtist());

        // Handle item click to open music player
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MusicPlayerActivity.class);
            intent.putExtra("music_list", new ArrayList<>(filteredList));  // Pass the filtered music list
            intent.putExtra("position", position);  // Pass the current position
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    // Method to filter the music list based on query
    public void filter(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(musicDataList); // Show all items if query is empty
        } else {
            for (MusicData musicData : musicDataList) {
                if (musicData.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        musicData.getArtist().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(musicData);
                }
            }
        }
        notifyDataSetChanged();
    }

    // Method to update the list with a new filtered music list
    public void updateData(List<MusicData> newMusicList) {
        this.filteredList = new ArrayList<>(newMusicList);
        notifyDataSetChanged();
    }

    public static class MusicViewHolder extends RecyclerView.ViewHolder {
        TextView title, artist;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            artist = itemView.findViewById(R.id.artist);
        }
    }
}
