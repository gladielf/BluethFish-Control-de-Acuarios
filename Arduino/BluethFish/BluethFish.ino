#include <BluethFishLibrary.h>
#include <Wire.h> 
#include "RTClib.h"
#include <OneWire.h>
#include <DallasTemperature.h>
#include <SD.h>
#include <String.h>

#define TempSensorPin 2
#define PhSensorPin 0

OneWire ourWire(TempSensorPin);
DallasTemperature sensors (&ourWire);

RTC_DS1307 RTC;

File myFile;
File myTestFile;
int aquariumID;
int lightOnHour,lightOnMinute,lightOffHour,lightOffMinute;
int lightOn,lightOff;
float tMin,tMax;
float phMin,phMax;
int myDelay;
int actualSecond, actualMinute, actualHour;
int actualTime;
char fileName[]= "/data/00000000.txt";
float phValue;

int ledTemp =5;
int ledPh = 6;
int light = 7;
int ledTest=8;
char incomingByte;
char incomingData[50];
int i;

void setup () {
  Wire.begin(); // Inicia el puerto I2C
  RTC.begin(); // Inicia la comunicación con el RTC
  //RTC.adjust(DateTime(__DATE__, __TIME__)); // Establece la fecha y hora (Comentar una vez establecida la hora)
  Serial.begin(9600); // Establece la velocidad de datos del puerto serie
  sensors.begin();

  //Pines de los led
  pinMode(ledTemp, OUTPUT);
  pinMode(ledPh, OUTPUT);  
  pinMode(light, OUTPUT);
  
  pinMode(53, OUTPUT);//Inicia la tarjeta SD
  if (!SD.begin(4)) {
    return;
  }
  
  if(!SD.exists("/data/")){
    SD.mkdir("/data/");
  }
  if(!SD.exists("/config/")){
    SD.mkdir("/config/");
    setConfig("1/10:00/22:00/25.0/30.0/5/7/30/");
  }
  //se abre el archivo config y se ponen los datos en los int pasados por referencia
  getConfig(&aquariumID,&lightOnHour, &lightOnMinute, &lightOffHour,&lightOffMinute, &tMin,&tMax,&phMin,&phMax, &myDelay);

}
void loop () {
  sensors.requestTemperatures();
  float temp = sensors.getTempCByIndex(0);
  DateTime now = RTC.now(); // Obtiene la fecha y hora del RTC
  
  if(Serial.available() > 0){
      
      incomingByte = Serial.read();
      switch(incomingByte){
        case 'c':
                for(i=0;i<49;i++){
                  incomingData[i] = Serial.read();
                } 
                setConfig(incomingData);
                getConfig(&aquariumID,&lightOnHour, &lightOnMinute, &lightOffHour,&lightOffMinute, &tMin,&tMax,&phMin,&phMax, &myDelay);
                break;
        case 'g':
                listFile();
                break;
        case 's':
                for(i=0;i<49;i++){
                  incomingData[i] = Serial.read();
                } 
                getFileData(incomingData);
                break;         
      }
      
  }
  
  actualSecond = now.second();
  actualMinute = now.minute();
  actualHour = now.hour();
  
    //comprobacion temperatura
  if(tMin > temp || tMax < temp){
      digitalWrite(ledTemp,HIGH);
    }else{ 
      digitalWrite(ledTemp,LOW);
    }
  
  //comprobacion ph
  phValue = getPhValue(PhSensorPin);
  if(phMin > phValue || phMax < phValue){
      digitalWrite(ledPh,HIGH);
    }else{ 
      digitalWrite(ledPh,LOW);
    }
    
  //CODIGO PARA LA COMPROBACION DE LA LUZ
  lightOn=lightOnHour*100+lightOnMinute;
  lightOff=lightOffHour*100+lightOffMinute;
  actualTime = actualHour*100+actualMinute;
  if((lightOn <= actualTime) && (lightOff >= actualTime)){
    digitalWrite(light,HIGH);
  }else{
    digitalWrite(light,LOW);
  }
 
  
      
  //Envio de valores actuales
  char tChar[5]="";
  char phChar[5]="";
  char valores[20]="";

  strncat(valores,"a",20);
  dtostrf(temp,2,2,tChar);
  strncat(valores,tChar,20);
  strncat(valores,"/",20);
  dtostrf(phValue,2,2,phChar);
  strncat(valores,phChar,20);
  Serial.println(valores);
        
  //Obtencion del nombre del fichero      
  getFileName(fileName,now.day(),now.month(),now.year());
  myFile = SD.open(fileName, FILE_WRITE);  
  
  if (myFile) {
    
    // Horas
    if(actualHour < 10 ){
      myFile.print("0");
    }
    myFile.print(actualHour); 
    myFile.print(':');
    // Minutos
    if(actualMinute < 10 ){
      myFile.print("0");
    }
    myFile.print(actualMinute); 
    myFile.print(':');
    // Segundos
    if(actualSecond < 10 ){
      myFile.print("0");
    }
    myFile.print(actualSecond); 
    myFile.print(' ');
    
    //temperatura
    myFile.print("Temp: ");
    myFile.print(temp);
    myFile.print(" C ");
    
    //PH
    myFile.print("PH: ");
    myFile.println(phValue,2);
  
        
    myFile.close();

    
  } else {
    Serial.println("error opening file");
  }
  
  delay(myDelay); 
}
