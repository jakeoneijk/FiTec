#include <SoftwareSerial.h>

#define TxD 14;
#define RxD 15;
#define BTSerial Serial3;

int bufferPosition;
boolean temp=0;
byte buffer[1024];
String myString;

int buzzer = 8;
int led = 12; 
int blueled = 11;
void setup() {
  Serial3.begin(9600);
  Serial.begin(9600);
  pinMode(buzzer,OUTPUT);
  pinMode(led,OUTPUT);
  pinMode(blueled,OUTPUT);
  bufferPosition=0;
  
}

void loop() {
   while(Serial3.available()){
    if(Serial3.available()){
      char data = (char)Serial3.read();
      myString+=data;
      if(myString=='emergency'){
        int i=0;
        while(i<30){
        digitalWrite(buzzer, HIGH); // Buzzer on
        digitalWrite(led,HIGH);
        digitalWrite(blueled,LOW);
        delay(500);
        digitalWrite(buzzer, LOW); // Buzzer off
        digitalWrite(led,LOW);
        digitalWrite(blueled,HIGH);
        delay(500);
        i++;
        }
      }
   }
  }
}
