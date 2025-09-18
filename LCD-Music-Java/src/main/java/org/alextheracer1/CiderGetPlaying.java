package org.alextheracer1;

import com.google.gson.Gson;
import org.alextheracer1.Music.CiderStatus;
import org.alextheracer1.Music.Song;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class CiderGetPlaying {

    private final int LCDSIZE = 16;
    private String prevTitle = "";
    private String titleArtist = "";
    private String positionLength = "";
    private int stringPosition = -5;
    private int stringPositionBack = 0;
    private final String nowPlayingURL = "http://localhost:10767/api/v1/playback/now-playing";
    private final String isPlayingURL = "http://localhost:10767/api/v1/playback/is-playing";

    public boolean isCiderPlaying() throws URISyntaxException, IOException {

        Gson gson = new Gson();

        CiderStatus status = gson.fromJson((new InputStreamReader(getHttpData(isPlayingURL).getInputStream())), CiderStatus.class);

        return status.isPlaying();
    }

    private HttpURLConnection getHttpData(String url) throws URISyntaxException, IOException {
        URI getSong = new URI(url);

        HttpURLConnection conn = (HttpURLConnection) getSong.toURL().openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP Error code : "
                    + conn.getResponseCode());
        }

        return conn;
    }

    public String getPlaying() throws IOException, URISyntaxException {

        Gson gson = new Gson();

        Song newSong = gson.fromJson((new InputStreamReader(getHttpData(nowPlayingURL).getInputStream())), Song.class);

        return implementScrolling(newSong);
    }

    private String formatMetadata (Song newSong, String title) {
        String metadata;

        titleArtist = title + " - " + newSong.getInfo().getArtistName();
        positionLength = formatTime(newSong.getInfo().getCurrentPlaybackTime()) + "|" + formatTime(newSong.getInfo().getSongLength());
        metadata = titleArtist + "|"  + positionLength + "\n";

        return metadata;
    }

    private String implementScrolling(Song newSong) {

        String returnString = "";


        try {

            String title = newSong.getInfo().getName();
            boolean isNewSong = !Objects.equals(title, prevTitle);

            if (isNewSong) {

                prevTitle = title;
                returnString = formatMetadata(newSong, title);

                System.out.println(titleArtist);

                stringPosition = -5;
            } else {

                formatMetadata(newSong, title);

                String newOutput = titleArtist;
                int size = titleArtist.length();

                if (size > LCDSIZE) {
                    if (stringPosition >= 0) {
                        newOutput = titleArtist.substring(stringPosition, size);
                        stringPosition++;

                        if ((size - stringPosition) <= LCDSIZE) {

                            newOutput = newOutput + "   " + titleArtist.substring(0, stringPositionBack);
                            stringPositionBack++;

                        } else {
                            stringPositionBack = 0;
                        }

                        if (stringPosition == size) {
                            stringPosition = -5;
                        }
                    } else {
                        stringPosition++;
                    }

                    newOutput = newOutput.substring(0, 16);
                }


                returnString = newOutput + "|" + positionLength + "\n";

            }

        } catch (Exception e) {
            System.out.println("Error running playerctl: " + e.getMessage());
        }

        return returnString;
    }


    private static String formatTime(double seconds) {
        int totalSecs = (int) seconds;
        int minutes = totalSecs / 60;
        int secs = totalSecs % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

}
