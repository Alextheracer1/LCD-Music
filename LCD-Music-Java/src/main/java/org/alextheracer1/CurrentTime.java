package org.alextheracer1;

import java.sql.Date;
import java.sql.Time;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class CurrentTime {

    CurrentTime(){
    }

    public String getCurrentTime() {

        String currentTime = "";

        currentTime = ZonedDateTime.now(ZoneId.of("Europe/Vienna")).toString().substring(11, 19);

        return currentTime;
    }
}
