//Code for displaying Music on an LCD

#include <Arduino.h>
#include <LiquidCrystal_I2C.h>
#include <avr/wdt.h>

#define DISPLAYCOLS 16
#define DISPLAYROWS 2
#define TIMEOUT 10

LiquidCrystal_I2C lcd(0x27,DISPLAYCOLS,DISPLAYROWS);  // set the LCD address to 0x27 for a 16 chars and 2 line display

String inputString = "";         // a String to hold incoming data
bool stringComplete = false;
int stringLength = 0;
int counter = 0;
int currentTimeout = 0;
String prevCurrentTime = "";

byte musicNote[] = {
  B00001,
  B00011,
  B00101,
  B01001,
  B01001,
  B01011,
  B11011,
  B11000
};

byte customAless[] = {
  B00011,
  B00110,
  B00100,
  B00100,
  B00100,
  B01100,
  B11100,
  B11100
};

void scrollTest();

void setup()
{

  // initialize serial:
  Serial.begin(9600);
  // reserve 200 bytes for the inputString:
  inputString.reserve(500);


  lcd.init();                      // initialize the lcd

  // Print a message to the LCD.
  lcd.backlight();
  lcd.setCursor(0,0);
  lcd.print("Waiting for");

  lcd.setCursor(0,1);
  lcd.print("connection... ");

  lcd.createChar(0, musicNote);
  lcd.createChar(1, customAless);

  lcd.setCursor(15, 0);
  lcd.write(0);

}

void loop()
{

  String currentTime = "";

  if (stringComplete) {


    String input = inputString;

    int firstSep = input.indexOf('|');
    int secondSep = input.indexOf('|', firstSep + 1);

    String title = input.substring(0, firstSep);

    currentTime = input.substring(firstSep + 1, secondSep);

    String endTime = input.substring(secondSep + 1);

    Serial.println("Title: " + title);
    Serial.println("Start Time: " + currentTime);
    Serial.println("End Time: " + endTime);

    stringLength = title.length();

    lcd.clear();
    lcd.home();
    lcd.print(title);
    lcd.setCursor(0,1);
    lcd.print(currentTime + " | " + endTime);
    if (stringLength >= DISPLAYCOLS) {
      //scrollTest();
    }

    if (counter <= stringLength) {
      lcd.setCursor(15, 1);
      lcd.write(1);
    } else {
      lcd.setCursor(15, 1);
      lcd.write(0);
    }

    counter++;

    if (stringLength*2 == counter) {
      counter = 0;
    }


    // clear the string:
    inputString = "";
    stringComplete = false;

  }

}


void serialEvent() {
  while (Serial.available()) {
    // get the new byte:
    char inChar = (char)Serial.read();
    // add it to the inputString:

    // if the incoming character is a newline, set a flag so the main loop can
    // do something about it:
    if (inChar == '\n') {
      stringComplete = true;
    } else {
      inputString += inChar;
     }
  }
}

void scrollTest(){

  // to move it offscreen right:

  for (int positionCounter = 0; positionCounter < stringLength+DISPLAYCOLS-2; positionCounter++) {

    // scroll one position right:

    lcd.scrollDisplayLeft();

    // wait a bit:

    delay(250);

  }


  // scroll 16 positions (display length + string length) to the left

  // to move it back to center:

  for (int positionCounter = 0; positionCounter < stringLength+DISPLAYCOLS; positionCounter++) {

    // scroll one position left:

    //lcd.scrollDisplayRight();

    // wait a bit:

    delay(150);

  }
}