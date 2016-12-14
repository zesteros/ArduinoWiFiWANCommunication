/*
   sketch for xPico Wifi application note
   Lantronix Inc.
   xPico Wifi Arduino Demo

   Simple serial protocol to turn on, turn off or Blink the LED
     1 - turn on
     0 - turn off

      written by Gary Marrs, Lantronix FAE
      02-21-14
*/
const int ledPin = 13; // pin the LED is connected to
int num = 0;
void setup(){
  Serial.begin(9600); // Initialize serial port to send and receive at 9600 baud
  pinMode(ledPin, OUTPUT); // set this pin as output
  delay(5000);            // delay 5 secs to allow xPico Wifi to boot up
  Serial.println("setup complete");
}
void loop(){
  if (Serial.available()){ // Check to see if at least one character is avail
    //Serial.println("read available");
    char ch = Serial.read();
    if ( isDigit(ch) ){ // is this an ascii digit between 0 and 9
      if (ch == '0') {
        digitalWrite(ledPin, LOW);
        Serial.println("0");
      }
      if (ch == '1') { // 1 turn on LED
        digitalWrite(ledPin, HIGH);
        Serial.println("1");
      }
    } // end of if
  } // end of serial available if
}
