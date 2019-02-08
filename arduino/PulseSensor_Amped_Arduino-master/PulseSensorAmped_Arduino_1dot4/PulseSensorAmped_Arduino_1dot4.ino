#include <SoftwareSerial.h>
#define TxD 14;
#define RxD 15;
#define BTSerial Serial3;
/*  Pulse Sensor Amped 1.4    by Joel Murphy and Yury Gitman   http://www.pulsesensor.com

----------------------  Notes ----------------------  ---------------------- 
This code:
1) Blinks an LED to User's Live Heartbeat   PIN 13
2) Fades an LED to User's Live HeartBeat
3) Determines BPM
4) Prints All of the Above to Serial

Read Me:
https://github.com/WorldFamousElectronics/PulseSensor_Amped_Arduino/blob/master/README.md   
 ----------------------       ----------------------  ----------------------
*/

//  Variables
int pulsePin = 0;                 // Pulse Sensor purple wire connected to analog pin 0

// Volatile Variables, used in the interrupt service routine!
volatile int BPM;                   // int that holds raw Analog in 0. updated every 2mS
volatile int Signal;                // holds the incoming raw data
volatile int IBI = 600;             // int that holds the time interval between beats! Must be seeded! 
volatile boolean Pulse = false;     // "True" when User's live heartbeat is detected. "False" when not a "live beat". 
volatile boolean QS = false;        // becomes true when Arduoino finds a beat.

// Regards Serial OutPut  -- Set This Up to your needs
static boolean serialVisual =  true;   // Set to 'false' by Default.  Re-set to 'true' to see Arduino Serial Monitor ASCII Visual Pulse 

String myString;
int buzzer = 8;
int led = 12; 
int blueled = 11;
boolean emergency = false;
int emergencyNumber = 0;

void setup(){
  Serial.begin(9600);             // we agree to talk fast!
  Serial3.begin(9600);
  pinMode(buzzer,OUTPUT);
  pinMode(led,OUTPUT);
  pinMode(blueled,OUTPUT);
  interruptSetup();                 // sets up to read Pulse Sensor signal every 2mS 
   // IF YOU ARE POWERING The Pulse Sensor AT VOLTAGE LESS THAN THE BOARD VOLTAGE, 
   // UN-COMMENT THE NEXT LINE AND APPLY THAT VOLTAGE TO THE A-REF PIN
//   analogReference(EXTERNAL);   
}

//  Where the Magic Happens
void loop(){
 if(emergency){
    emergencyNumber++;
    if(emergencyNumber%2 != 0){
        digitalWrite(buzzer, HIGH); // Buzzer on
        digitalWrite(led,HIGH);
        digitalWrite(blueled,LOW);
        delay(500);
    }else{
          digitalWrite(buzzer, LOW); // Buzzer off
          digitalWrite(led,LOW);
          digitalWrite(blueled,HIGH);
          delay(500);
     }
  }     
     
  while(Serial3.available()){  
    if(Serial3.available()){
      char data = (char)Serial3.read();
      myString+=data;
      if(myString.equals("emergency")){
        int i=0;
        emergency = true;
      }
      if(myString.equals("stop")){
        emergency = false;
        digitalWrite(buzzer, LOW); // Buzzer off
        digitalWrite(led,LOW);
        digitalWrite(blueled,LOW);
        delay(500);
      }
   }
  }
  myString="";
  
  if (QS == true){     // A Heartbeat Was Found
                       // BPM and IBI have been Determined
                       // Quantified Self "QS" true when arduino finds a heartbeat
        serialOutputWhenBeatHappens();   // A Beat Happened, Output that to serial.     
        QS = false;                      // reset the Quantified Self flag for next time    
  }
  delay(100);                             //  take a break
}
