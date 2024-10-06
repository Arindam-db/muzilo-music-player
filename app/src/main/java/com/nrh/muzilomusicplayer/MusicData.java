package com.nrh.muzilomusicplayer;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class MusicData implements Parcelable {
    private String title;
    private String artist;
    private String path;

    public MusicData(String title, String artist, String path) {
        this.title = title;
        this.artist = artist;
        this.path = path;
    }

    protected MusicData(Parcel in) {
        title = in.readString();
        artist = in.readString();
        path = in.readString();
    }

    public static final Creator<MusicData> CREATOR = new Creator<MusicData>() {
        @Override
        public MusicData createFromParcel(Parcel in) {
            return new MusicData(in);
        }

        @Override
        public MusicData[] newArray(int size) {
            return new MusicData[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getPath() {
        return path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(artist);
        parcel.writeString(path);
    }
}
