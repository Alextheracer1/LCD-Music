package org.alextheracer1;

import jssc.*;

import java.io.IOException;
import java.net.URISyntaxException;

import static jssc.SerialPort.*;

public class Main {

    public static void main(String[] args) {

        CurrentlyPlaying player = new CurrentlyPlaying();
        CiderGetPlaying ciderPlayer =  new CiderGetPlaying();


        for (String port : SerialPortList.getPortNames()) {
            if (port.equals("/dev/ttyUSB0")) {
                try {
                    SerialPort arduinoPort = new SerialPort(port);
                    arduinoPort.openPort();
                    arduinoPort.setParams(BAUDRATE_9600, DATABITS_8, STOPBITS_1, PARITY_NONE);
                    Thread.sleep(5000);

                    while (true) {

                        if (ciderPlayer.isCiderPlaying()){
                            arduinoPort.writeBytes(ciderPlayer.getPlaying().getBytes());
                            Thread.sleep(1000);
                        } else {
                            arduinoPort.writeBytes("Player paused\n".getBytes());
                            System.out.println("Player currently paused");
                            Thread.sleep(2000);
                        }
                    }

                    /*
                    for (; ; ) {
                        if (player.getPlaying() == 1) {

                            arduinoPort.writeBytes(ciderPlayer.getPlaying().getBytes());
                            Thread.sleep(1000);

                        } else if (player.getPlaying() == 0) {
                            arduinoPort.writeBytes("Player paused\n".getBytes());
                            System.out.println("Player currently paused");
                            Thread.sleep(2000);
                        } else if (player.getPlaying() == 2) {
                            System.out.println("Player currently stopped. Try playing a song.");
                            arduinoPort.writeBytes("Player stopped\n".getBytes());
                            Thread.sleep(2000);
                        }
                    }
                    */


                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }


    }
}