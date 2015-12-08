package com.pfc.bluethfish.control.acuarios.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Fermín Conejo
 *
 */

public class DatabaseHelper extends SQLiteOpenHelper {

	private static String DB_PATH;
	private static String DB_NAME = "dbfish.db";
	
	private SQLiteDatabase myDataBase;
	private final Context myContext;
	
	private static CursorFactory cursorFactory = null;
	private static int DB_VERSION = 1;
	
	public DatabaseHelper(Context context){
		 super(context,DB_NAME,cursorFactory, DB_VERSION);
		 this.myContext = context;
		 
		 DB_PATH = context.getFilesDir().getParentFile().getPath() + "/databases/";
		}
	// METODO DE CREACION DE BASE DE DATOS EXTERNA A LA APLICACION	

	
	/**
	 * Crea una base de datos vacía en el sistema y la reescribe con nuestro
	 * fichero de base de datos.
	 * */
	public void createDataBase() throws IOException{

	boolean dbExist = checkDataBase();
	 
	if(dbExist){
		//la base de datos existe y no hacemos nada.
	}else{
		//Llamando a este método se crea la base de datos vacía en la ruta por defecto del sistema
		//de nuestra aplicación por lo que podremos sobreescribirla con nuestra base de datos.
		this.getReadableDatabase();
				try {
				copyDataBase();
				} catch (IOException e) {
				throw new Error("Error copiando Base de Datos");
				}
			}
	}
	
	/**
	* Comprueba si la base de datos existe para evitar copiar siempre el 
	* fichero cada vez que se abra la aplicación.
	* 
	* @return true si existe, false si no existe
	*/
	public boolean checkDataBase(){
	 
	 File dbFile = new File(DB_PATH + DB_NAME);
	    return dbFile.exists();
	}	
	
	/**
	* Copia nuestra base de datos desde la carpeta assets a la recién creada
	* base de datos en la carpeta de sistema, desde dónde podremos acceder a ella.
	* Esto se hace con bytestream.
	* */
	private void copyDataBase() throws IOException{
	 
	//Abrimos el fichero de base de datos como entrada
	InputStream myInput = myContext.getAssets().open(DB_NAME);
	 
	//Ruta a la base de datos vacía recién creada
	String outFileName = DB_PATH + DB_NAME;
	 
	//Abrimos la base de datos vacía como salida
	OutputStream myOutput = new FileOutputStream(outFileName);
	 
	//Transferimos los bytes desde el fichero de entrada al de salida
	byte[] buffer = new byte[1024];
	int length;
	while ((length = myInput.read(buffer))>0){
				myOutput.write(buffer, 0, length);
	}
	 
	//Liberamos los streams
	myOutput.flush();
	myOutput.close();
	myInput.close();
	 
	}
	
	/**
	 * Inicia el proceso de copia del fichero de base de datos, o crea una base
	 * de datos vacía en su lugar
	 * */
	
	public SQLiteDatabase getDataBase() {
		// Abre la base de datos

		try {
			createDataBase();
		} catch (IOException e) {
			throw new Error("Ha sido imposible crear la Base de Datos");
		}
		String myPath = DB_PATH + DB_NAME;
		return SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	
	@Override
	public void close(){
		if(myDataBase != null){
		    myDataBase.close();
		}
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
	
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}	
}