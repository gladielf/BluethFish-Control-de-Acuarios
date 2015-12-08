/*
 *  BluethFishLibrary.h
 *  
 *  Created by Fermín Conejo Baltasar
 *  
 */


//#include "RTClib.h"
#include <stdlib.h>
#include <SD.h>

#if defined(ARDUINO) && ARDUINO >= 100
#include "Arduino.h"
#else
#include "WProgram.h"
#endif

/*
 * Función para conseguir el nombre del archivo segun el día
 */
void getFileName(char * fileName,int actualDay, int actualMonth,int actualYear );


/*
 * Función para leer el valor de la sonda de PH
 */
float getPhValue(int phsensorpin);


/*
 * Función para leer la configuración del archivo config.txt
 */

void getConfig(int *aquariumID,int *lightOnHour, int *lightOnMinute,
			   int *lightOffHour, int *lightOffMinute, float *tMin,float *tMax,
			   float *phMin,float *phMax, int *myDelay);


/*
 * Función para establecer una nueva configuración 
 */

void setConfig(char incomingData[50]);


/*
 * Función para listar los archivos de la tarjeta SD
 */

void listFile();


/*
 * Función para leer los datos del fichero 
 * cuyo nombre se ha pasado por parámetro y enviarlos por bluetooth 
 *
 */
void getFileData(char incomingData[50]);