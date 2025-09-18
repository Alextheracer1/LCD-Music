package org.alextheracer1.Music;

import com.google.gson.annotations.SerializedName;

public class CiderStatus {

    @SerializedName("is_playing")
    private boolean isPlaying;

    public boolean isPlaying() {
        return isPlaying;
    }

}
