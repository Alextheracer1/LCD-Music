package org.alextheracer1;

import jssc.*;

import java.util.Objects;

import static jssc.SerialPort.*;

public class Main {

    public static void main(String[] args) {

        CurrentlyPlaying player = new CurrentlyPlaying();
        CurrentTime time = new CurrentTime();


        for (String port : SerialPortList.getPortNames()) {
            if (port.equals("/dev/ttyUSB0")) {
                try {
                    SerialPort arduinoPort = new SerialPort(port);
                    arduinoPort.openPort();
                    arduinoPort.setParams(BAUDRATE_9600, DATABITS_8, STOPBITS_1, PARITY_NONE);
                    Thread.sleep(5000);
                    for (; ; ) {
                        if (player.getPlaying() == 1) {

                            arduinoPort.writeBytes(player.getMetadata().getBytes());
                            Thread.sleep(1000);

                        } else if (player.getPlaying() == 0) {
                            arduinoPort.writeBytes("Player paused\n".getBytes());
                            System.out.println("Player currently paused");
                            Thread.sleep(2000);
                        } else if (player.getPlaying() == 2) {
                            System.out.println("Player currently stopped. Try playing a song.");
                            arduinoPort.writeBytes("Player stopped\n".getBytes());
                            arduinoPort.writeBytes(time.getCurrentTime().getBytes());
                            Thread.sleep(2000);

                        }
                    }

                } catch (SerialPortException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }


    }
}