/*
 *  BluethFishLibrary.c
 *  
 *  Created by Fermín Conejo Baltasar
 *
 */

#include "BluethFishLibrary.h"


/*
 *
 * Función para conseguir el nombre del archivo segun el día
 *
 */
void getFileName(char * fileName,int actualDay, int actualMonth,int actualYear ){
	
	fileName[0]='/';
	fileName[1]='d';
	fileName[2]='a';
	fileName[3]='t';
	fileName[4]='a';
	fileName[5]='/';
	fileName[6] = actualDay/10 +'0';
	fileName[7] = actualDay%10 +'0';
	fileName[8] = actualMonth/10 + '0';
	fileName[9] = actualMonth%10 +'0';
	fileName[10] = '2';
	fileName[11] = '0';
	fileName[12] = (actualYear-2000)/10 +'0';
	fileName[13] = actualYear%10 +'0';
	fileName[14] = '.';
	fileName[15] = 't';
	fileName[16] = 'x';  
	fileName[17] = 't';
	return;
}


/*
 *
 * Función para leer el valor de la sonda de PH
 *
 */
float getPhValue(int phsensorpin){
	
	unsigned long int avgValue;  //Store the average value of the sensor feedback  
	float b;
	int buf[10],temp;
	
	for(int i=0; i<10;i++){
		buf[i]= analogRead(phsensorpin);
		delay(10);
	}
	for(int i=0;i<9;i++)        //sort the analog from small to large
	{
		for(int j=i+1;j<10;j++)
		{
			if(buf[i]>buf[j])
			{
				temp=buf[i];
				buf[i]=buf[j];
				buf[j]=temp;
			}
		}
	}
	avgValue=0;
	for(int i=2;i<8;i++)                      //take the average value of 6 center sample
		avgValue+=buf[i];
	float phvalue=(float)avgValue*5.0/1024/6; //convert the analog into millivolt
	phvalue=3.5*phvalue; 
	return phvalue;
}



/*
 * Función para leer la configuración del archivo config.txt
 */

void getConfig(int *aquariumID,int *lightOnHour, int *lightOnMinute,
			   int *lightOffHour, int *lightOffMinute, float *tMin,float *tMax,
			   float *phMin,float *phMax, int *myDelay){
	
	char bufferAq[20];
	char aquariumId[5];
	char bufferLightOn[20];
	char lightOn_Hour[5];
	char lightOn_Minutes[5];	
	char bufferLightOff[20];
	char lightOff_Hour[5];
	char lightOff_Minutes[5];
	char bufferTMin[20];
	char tempMin[5];
	char bufferTMax[20];
	char tempMax[5];
	char bufferPhMin[20];
	char ph_Min[5];
	char bufferPhMax[20];
	char ph_Max[5];
	char bufferDelay[20];
	char delay[10];
	bool EOL = false;
	int idx = 0;
	char c;
	int i = 0;
	int j = 0;
	
	File configFile = SD.open("/config/CONFIG.TXT");
	
	if(configFile){
		//LINEA 1 ID ACUARIO
		EOL = false;
		while(!EOL){
			c = configFile.read();
			if(c=='\n' || idx ==19){
				bufferAq[idx] =0;
				idx = 0;
				EOL = true;
			}
			else{
				bufferAq[idx] = c;
				idx++;
			}
		}
		
		i = 0;
		while(bufferAq[i] != ' '){
			i++;
		}
		for (int j=0; j<5 ; j++ , i++){
			aquariumId[j]=bufferAq[i];
		}
		*aquariumID = atoi(aquariumId);
		
		//Linea 2 Hora encendido
		EOL = false;
		idx = 0;
		while(!EOL){
			c = configFile.read();
			if(c=='\n' || idx == 19){
				bufferLightOn[idx] = 0;
				idx = 0;
				EOL = true;
			}
			else{
				bufferLightOn[idx] = c;
				idx++;
			}
			
		}
		
		//Hora de encendido de la luz
		lightOn_Hour[0]=bufferLightOn[8];
		lightOn_Hour[1]=bufferLightOn[9];
		lightOn_Minutes[0]=bufferLightOn[11];
		lightOn_Minutes[1]=bufferLightOn[12];
		
		*lightOnHour = atoi(lightOn_Hour);
		*lightOnMinute = atoi(lightOn_Minutes);
		
		//LINEA 3 HORA APAGADO
		EOL = false;
		idx = 0;
		while(!EOL){
			c = configFile.read();
			if(c=='\n' || idx == 19){
				bufferLightOff[idx] = 0;
				idx = 0;
				EOL = true;
			}
			else{
				bufferLightOff[idx] = c;
				idx++;
			}
			
		}
		
		//Hora de apagado de la luz
		lightOff_Hour[0]=bufferLightOff[9];
		lightOff_Hour[1]=bufferLightOff[10];
		lightOff_Minutes[0]=bufferLightOff[12];
		lightOff_Minutes[1]=bufferLightOff[13];
		
		*lightOffHour = atoi(lightOff_Hour);
		*lightOffMinute = atoi(lightOff_Minutes);
		
		//LINEA 4 Temp Min
		EOL = false;
		idx = 0;
		while(!EOL){
			c = configFile.read();
			if(c=='\n' || idx == 19){
				bufferTMin[idx] = 0;
				idx = 0;
				EOL = true;
			}
			else{
				bufferTMin[idx] = c;
				idx++;
			}
		}
		
		i = 0;
		while(bufferTMin[i] != ' '){
			i++;
		}
		for (j=0; j<5 ; j++ , i++){
			tempMin[j]=bufferTMin[i];
		}
		
		*tMin = atof(tempMin);
		
		
		//LINEA 5 Temp Max
		EOL = false;
		idx = 0;
		while(!EOL){
			c = configFile.read();
			if(c=='\n' || idx == 19){
				bufferTMax[idx] = 0;
				idx = 0;
				EOL = true;
			}
			else{
				bufferTMax[idx] = c;
				idx++;
			}
		}
		
		i = 0;
		while(bufferTMax[i] != ' '){
			i++;
		}
		for (int j=0; j<5 ; j++ , i++){
			tempMax[j]=bufferTMax[i];
		}
		
		*tMax = atof(tempMax);
		
		//LINEA 6 PH Min
		EOL = false;
		idx = 0;
		while(!EOL){
			c = configFile.read();
			if(c=='\n' || idx == 19){
				bufferPhMin[idx] = 0;
				idx = 0;
				EOL = true;
			}
			else{
				bufferPhMin[idx] = c;
				idx++;
			}
			
		}
		
		i = 0;
		while(bufferPhMin[i] != ' '){
			i++;
		}
		for (int j=0; j<5 ; j++ , i++){
			ph_Min[j]=bufferPhMin[i];
		}
		
		*phMin = atof(ph_Min);
		
		//LINEA 7 Ph Max
		EOL = false;
		idx = 0;
		while(!EOL){
			c = configFile.read();
			if(c=='\n' || idx == 19){
				bufferPhMax[idx] = 0;
				idx = 0;
				EOL = true;
			}
			else{
				bufferPhMax[idx] = c;
				idx++;
			}
			
		}
		
		i = 0;
		while(bufferPhMax[i] != ' '){
			i++;
		}
		for (int j=0; j<5 ; j++ , i++){
			ph_Max[j]=bufferPhMax[i];
		}
		
		*phMax = atof(ph_Max);
		
		//LINEA 8 My Delay
		EOL = false;
		idx = 0;
		while(!EOL){
			c = configFile.read();
			if(c=='\n' || idx == 19){
				bufferDelay[idx] = 0;
				idx = 0;
				EOL = true;
			}
			else{
				bufferDelay[idx] = c;
				idx++;
			}
		}
		
		i = 0;
		j = 0;
		while(bufferDelay[i] != ' '){
			i++;
		}
		for (int j=0; j<9 ; j++ , i++){
			delay[j]=bufferDelay[i];
		}
		
		*myDelay = atoi(delay);
		
		
		configFile.close();
		
	}else{
		Serial.println("ERROR");
	}
	return;
}


/*
 * Función para establecer una nueva configuración 
 */

void setConfig(char incomingData[50]){

	File configFile;
	char newAqId[10] ="";
	char newLightOn[10]="";
	char newLightOff[10]="";
	char newTmin[10]="";
	char newTmax[10]="";
	char newPhmin[10]="";
	char newPhmax[10]="";
	char newDelay[10]=""; 

	int i;
	int j;
	
	//nuevo id del acuario
	i=0;
	j=0;
	while (incomingData[i] != '/') {
		newAqId[j]=incomingData[i];
		i++;
		j++;
	}
	
	//nueva hora de encendido
	i++;
	j=0;
	while (incomingData[i] != '/') {
		
		newLightOn[j]=incomingData[i];
		i++;
		j++;
	}
			
	//nueva hora de apagado
	i++;
	j=0;
	while (incomingData[i] != '/') {
		newLightOff[j]=incomingData[i];
		i++;
		j++;
	}
			
	//nueva temperatura minima
	i++;
	j=0;
	while (incomingData[i] != '/' ){
		newTmin[j] = incomingData[i];
		i++;
		j++;
	}
			
	//nueva temperatura maxima
	i++;
	j=0;
	while (incomingData[i] != '/' ){
		newTmax[j] = incomingData[i];
		i++;
		j++;
	}
			
	//nueva PH minimo
	i++;
	j=0;
	while (incomingData[i] != '/' ){
		newPhmin[j] = incomingData[i];
		i++;
		j++;
	}
	
	//nueva PH maximo
	i++;
	j=0;
	while (incomingData[i] != '/') {
		newPhmax[j] = incomingData[i];
		i++;
		j++;
	}

	//nueva retardo
	i++;
	j=0;
	while (incomingData[i] != '/') {
		newDelay[j] = incomingData[i];
		i++;
		j++;
	}
	
	
	if (SD.exists("/config/CONFIG.TXT")){
		SD.remove("/config/CONFIG.TXT");
	}
	
	configFile = SD.open("/config/CONFIG.TXT", FILE_WRITE);
	
	if(configFile){
		
		configFile.print("AquariumID ");
		configFile.print(newAqId);
		configFile.print("\n");
		configFile.print("LightOn ");
		configFile.print(newLightOn);
		configFile.print("\n");
		configFile.print("LightOff ");
		configFile.print(newLightOff);
		configFile.print("\n");
		configFile.print("TempMin ");
		configFile.print(newTmin);
		configFile.print("\n");
		configFile.print("TempMax ");
		configFile.print(newTmax);
		configFile.print("\n");
		configFile.print("PhMin ");
		configFile.print(newPhmin);
		configFile.print("\n");
		configFile.print("PhMax ");
		configFile.print(newPhmax);
		configFile.print("\n");
		configFile.print("Delay ");
		configFile.print(newDelay);
		configFile.print("000");//así se pasa de segundos a milisegundos
		configFile.print("\n");
		
		configFile.close();
	
	}else{
		Serial.println("ERROR");
	}
	return;
}


/*
 * Función para listar los archivos de la tarjeta SD
 */

void listFile(){

	char fileList[512]="";
	File root;
	
	root = SD.open("/data/");
	strncat(fileList,"f",512);
	while(true) {
		
		File entry =  root.openNextFile();
		if (! entry) {
			break;
		}

		strncat(fileList,entry.name(),512);
		strncat(fileList,"/",512);
		
		entry.close();
	}
	Serial.println(fileList);
	root.close();
	return;
}

/*
 * Función para leer los datos del fichero 
 * cuyo nombre se ha pasado por parámetro y enviarlos por bluetooth 
 *
 */
void getFileData(char incomingData[50]) {

	File dataFile;
	char fileName[25]="";
	int i;
	int j;
	bool EOL = false;
	char c;
	int idx;
	char buffer[50];
	char time[10]="";
	char temp[10]="";
	char ph[10]="";
	//char datas[10240]="";

	
	strncat(fileName,"/data/",25);
	i = 0;
	j = 6;
	while (incomingData[i] != '/') {
		fileName[j]=incomingData[i];
		i++;
		j++;

	}
	//Serial.println(fileName);
	dataFile = SD.open(fileName);
	if (dataFile) {
		Serial.write("d");
		//strncat(datas,"d",1024);
		while (dataFile.available()) {
			EOL = false;
			while(!EOL){
				c = dataFile.read();
				if(c=='\n' || idx == 49){
					buffer[idx] =0;
					
					EOL = true;
				}
				else{
					buffer[idx] = c;
					idx++;
				}
			}
			 
			//Serial.print("d");
			/*for (i=0; i<8; i++) {
				time[i]=buffer[i];
			}
			strncat(datas,time,1024);
			strncat(datas,"/",1024); */
			//Serial.print(time);
			//Serial.print("/");
			char datas[50]="";
			i=15;
			j=0;
			while (buffer[i]!= ' ') {
				temp[j]=buffer[i];
				i++;
				j++;
			}
			strncat(datas,temp,50);
			strncat(datas,"/",50);
			//Serial.print(temp);
			//Serial.print("/");			
			i= i+7;
			
			for (j=0; j<idx; i++,j++) {
				ph[j]=buffer[i];
			}
			
			strncat(datas,ph,50);
			strncat(datas,"/",50);
			Serial.write(datas);
			idx = 0;
			

			delay(100);
			//Serial.print(ph);
			//Serial.println("/");
			
			
		}
		//strncat(datas,"|",1024);
		dataFile.close();
		
		Serial.write("|");
	}else {
		Serial.println("ERROR");
	}
	//Serial.println("");
	return;
}