package org.alextheracer1;

import com.google.gson.annotations.SerializedName;

public class Song {

    private Info info;

    public Info getInfo() {
        return info;
    }

    public static class Info {
        @SerializedName("name")
        private String name;

        @SerializedName("artistName")
        private String artistName;

        // JSON has "durationInMillis" → map to songLength (seconds)
        @SerializedName("durationInMillis")
        private float songLength;

        @SerializedName("currentPlaybackTime")
        private float currentPlaybackTime;

        public String getName() {
            return name;
        }

        public String getArtistName() {
            return artistName;
        }

        // Return duration in seconds instead of millis
        public float getSongLength() {
            return songLength / 1000f;
        }

        public float getCurrentPlaybackTime() {
            return currentPlaybackTime;
        }
    }
}