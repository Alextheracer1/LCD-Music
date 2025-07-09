package org.alextheracer1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;

public class CurrentlyPlaying {

    CurrentlyPlaying() {
    }

    private final int LCDSIZE = 16;
    private String prevTitle = "";
    private String titleArtist = "";
    private String positionLength = "";
    private int stringPosition = -5;
    private int stringPositionBack = 0;


    public int getPlaying() {
        try {
            String status = runCommand("playerctl status");

            if (status.equals("Playing")) {
                return 1;
            } else if (status.equals("Paused")) {
                return 0;
            } else {
                return 2;
            }


        } catch (Exception e) {
            e.getMessage();
            return 2;
        }

    }

    public String getMetadata() {

        String returnString = "Error";

        try {

            String title = runCommand("playerctl metadata title");
            boolean newSong = !Objects.equals(title, prevTitle);

            if (newSong) {

                prevTitle = title;
                returnString = getOtherMetadata(title);

                System.out.println(titleArtist);

                stringPosition = -5;
            } else {

                getOtherMetadata(title);

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

                }


                returnString = newOutput + "|" + positionLength + "\n";

            }

        } catch (Exception e) {
            System.out.println("Error running playerctl: " + e.getMessage());
        }

        return returnString;
    }

    private String getOtherMetadata(String title) {
        String returnString = "Error";
        try {
            String artist = runCommand("playerctl metadata artist");

            double position = Double.parseDouble(runCommand("playerctl position").trim());
            long lengthMicros = Long.parseLong(runCommand("playerctl metadata mpris:length").trim());
            double lengthSeconds = lengthMicros / 1_000_000.0;

            titleArtist = title + " - " + artist;
            positionLength = formatTime(position) + "|" + formatTime(lengthSeconds);

            returnString = titleArtist + "|" + positionLength + "\n";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return returnString;
    }


    private static String runCommand(String command) throws Exception {
        Process process = Runtime.getRuntime().exec(command);
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            return reader.readLine();
        }
    }

    private static String formatTime(double seconds) {
        int totalSecs = (int) seconds;
        int minutes = totalSecs / 60;
        int secs = totalSecs % 60;
        return String.format("%02d:%02d", minutes, secs);
    }
}

