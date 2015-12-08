package com.pfc.bluethfish.control.acuarios.aquarium;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;
import com.pfc.bluethfish.control.acuarios.Constants;
import com.pfc.bluethfish.control.acuarios.adapters.MyListViewAdapter;
import com.pfc.bluethfish.control.acuarios.data.AquariumColumns;
import com.pfc.bluethfish.control.acuarios.data.AquariumTable;
import com.pfc.bluethfish.control.acuarios.data.DatabaseAdapter;
import com.pfc.bluethfish.control.acuarios.data.DatabaseHelper;
import com.pfc.bluethfish.control.acuarios.R;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AquariumSensorDetailActivity extends Activity {

	SQLiteDatabase db;
	DatabaseAdapter dbAdapter;
	DatabaseHelper dbHelper;
	
	private BluetoothAdapter myBluetoothAdapter;
	private BluetoothSocket myBluetoothSocket;
	static Handler handler;
	private StringBuilder sb = new StringBuilder();
	private ConnectedThread myConnectedThread;
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	int aquariumIDAssociated;
	int configdelay;
	
	String aquariumID;

	
	String sbprintFiles;
	String fileSelected;
	ArrayList<String> fileList;
	MyListViewAdapter listViewAdapter;
	
	ArrayList<String> arrayTime;
	ArrayList<Float> arrayTemp;
	ArrayList<Float> arrayPh;
	
	@SuppressLint("HandlerLeak")
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if (dbAdapter!=null){
			dbAdapter.close();
		}
		setContentView(R.layout.aquarium_sensor);
		
		ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
		aquariumID = getIntent().getStringExtra("AquariumID");
		
		SharedPreferences prefs = getSharedPreferences("MyPreferences",AquariumSensorDetailActivity.MODE_PRIVATE);
		aquariumIDAssociated = prefs.getInt("aqIdAssociated", 0);

		final TextView textViewActualTemp =  (TextView) findViewById(R.id.textViewActualTempDetail);
		final TextView textViewActualPh = (TextView) findViewById(R.id.textViewActualPhDetail);
		
		drawGraphic("d|");
		
		handler = new Handler(){
			
			public void handleMessage(android.os.Message msg){
				switch (msg.what) {
				case 1:
					byte[] readBuf = (byte[]) msg.obj;
	                String strIncom = new String(readBuf, 0, msg.arg1);                 

	                sb.append(strIncom);
	                if(sb.substring(0, 1).equals("a")){   
		                int middleOfLineIndex = sb.indexOf("/");
		                int endOfLineIndex = sb.indexOf("\r\n");
		                if(middleOfLineIndex < endOfLineIndex){
			                if (middleOfLineIndex > 0) {                                            
			                    String sbprintTemp = sb.substring(1, middleOfLineIndex);               
			                    textViewActualTemp.setText(sbprintTemp);            
			                }
			                if (endOfLineIndex > 0) {                                            
			                    String sbprintPh = sb.substring(middleOfLineIndex+1, endOfLineIndex);
			                    sb.delete(0, sb.length());    
			                    textViewActualPh.setText(sbprintPh);
			                }
		                }
	                }else if(sb.substring(0, 1).equals("f")){
	                	fileList = new ArrayList<String>();
		                int endOfLineIndex = sb.indexOf("\r\n");

		                for(int i = 1;i<endOfLineIndex; i=i+12){
		                	sbprintFiles = sb.substring(i,i+12);
		                	fileList.add(sbprintFiles);
		                	i++;
		                };
		                if (endOfLineIndex > 0) {                                            
		                    sb.delete(0, sb.length());    
		                    createGraphic();
		                }
	                }else if(sb.substring(0, 1).equals("d")){
		                int endOfLineIndex = sb.indexOf("\r\n");
		                if (endOfLineIndex > 0) {     
		                	int endOfdata = sb.indexOf("|");
		                
		                	String fileData;
		                	fileData = sb.substring(0,endOfdata+1);

		                	Log.i(Constants.LOGTAG, "El dato recibido es:"+sb);
		                    sb.delete(0, sb.length());   
		                    
		                    drawGraphic(fileData);
		                }
	                }else{
	                	sb.delete(0, sb.length());    
	                }

					break;
				}
			};
		};
		
		myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (myBluetoothAdapter == null) {
			Toast.makeText(getApplicationContext(), "Bluetooth no soportado.", Toast.LENGTH_SHORT).show();
		}
		else {
			if (!myBluetoothAdapter.isEnabled()) {
		         Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		         startActivityForResult(turnOnIntent, Constants.REQUEST_ENABLE_BT);
		      }
		      else{
		      }
		}
		
		
		/**
		 * Botón Establecer acuario asociado
		 */
		Button buttonAquariumAssociate = (Button) findViewById(R.id.buttonsAqAssociate);
		buttonAquariumAssociate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(aquariumIDAssociated != Integer.parseInt(aquariumID)){
					new AlertDialog.Builder(AquariumSensorDetailActivity.this).setTitle(getResources()
							.getString(R.string.dialog_title_confirm_aquarium_associate))
							.setMessage(R.string.dialog_message_confirm_aquarium_associate)
							.setNegativeButton(R.string.button_cancel,new OnClickListener(){
								public void onClick(DialogInterface dialog,int arg1){
									Log.i(Constants.LOGTAG, "Cancelar asociacion");
								}
							})
							.setPositiveButton(R.string.button_accept,new OnClickListener() {
								public void onClick(DialogInterface dialog,int arg1){
									Log.i(Constants.LOGTAG, "Aceptar asociacion");
									SharedPreferences prefs = getSharedPreferences("MyPreferences",AquariumSensorDetailActivity.MODE_PRIVATE);
									SharedPreferences.Editor editor = prefs.edit();
									editor.putInt("aqIdAssociated",Integer.parseInt(aquariumID));
									editor.commit();
									onCreate(new Bundle());
									onResume();
									new AlertDialog.Builder(AquariumSensorDetailActivity.this)
											.setMessage(R.string.dialog_message_reminder_config)
											.setPositiveButton(R.string.button_accept,new OnClickListener() {
												public void onClick(DialogInterface dialog,int arg1){
												
												}
											}).show();
								}
							}).show();
					
				}else{
					new AlertDialog.Builder(AquariumSensorDetailActivity.this).setTitle(getResources()
							.getString(R.string.dialog_title_aquarium_associated))
							.setMessage(R.string.dialog_message_aquarium_associated)
							.setPositiveButton(R.string.button_accept,new OnClickListener() {
								public void onClick(DialogInterface dialog,int arg1){
								
								}
							}).show();
				}
			}
		});
		
		
		
		/**
		 * Botón configurar sensores
		 */
		Button buttonConfig = (Button) findViewById(R.id.buttonCongif);
		buttonConfig.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(aquariumIDAssociated == Integer.parseInt(aquariumID)){
					dialogConfig(Integer.parseInt(aquariumID));
				}else{
					new AlertDialog.Builder(AquariumSensorDetailActivity.this).setTitle(getResources()
							.getString(R.string.dialog_title_wrong_aquarium))
							.setMessage(R.string.dialog_message_wrong_aquarium)
							.setPositiveButton(R.string.button_accept,new OnClickListener() {
								public void onClick(DialogInterface dialog,int arg1){
								
								}
							}).show();
				}
				
			}
		});
		
		
		/**
		 * Botón Crear Gráfica
		 */
		Button buttonGraphic = (Button) findViewById(R.id.buttonGraphic);
		buttonGraphic.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(aquariumIDAssociated == Integer.parseInt(aquariumID)){
					myConnectedThread.write("g");
				}else{
					new AlertDialog.Builder(AquariumSensorDetailActivity.this).setTitle(getResources()
							.getString(R.string.dialog_title_wrong_aquarium))
							.setMessage(R.string.dialog_message_wrong_aquarium)
							.setPositiveButton(R.string.button_accept,new OnClickListener() {
								public void onClick(DialogInterface dialog,int arg1){
								
								}
							}).show();
				}
			}
		});
	}
	
	
	 private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
	      if(Build.VERSION.SDK_INT >= 10){
	          try {
	              final Method  m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
	              return (BluetoothSocket) m.invoke(device, MY_UUID);
	          } catch (Exception e) {
	              Log.e(Constants.LOGTAG, "Could not create Insecure RFComm Connection",e);
	          }
	      }
	      return  device.createRfcommSocketToServiceRecord(MY_UUID);
	  }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	       if(requestCode == Constants.REQUEST_ENABLE_BT){
	
	           if(myBluetoothAdapter.isEnabled()) {
	        	  
	           } else {  
	        	
	           }
	       }
	   }
 
	@Override
	public void onResume(){
		super.onResume();
		if(Integer.parseInt(aquariumID) == aquariumIDAssociated){
			
			BluetoothDevice myBluetoothDevice = myBluetoothAdapter.getRemoteDevice(Constants.BLUETOOTH_MAC_ADDRESS);
			
			try {
				myBluetoothSocket = createBluetoothSocket(myBluetoothDevice);
			} catch (Exception e) {
				Log.e(Constants.LOGTAG, "Error al crear el socket del Bluetooth");
			}
			myBluetoothAdapter.cancelDiscovery();
			
			try {
				myBluetoothSocket.connect();
				Log.i(Constants.LOGTAG, "Socket conectado");
			} catch (Exception e) {
				try {
					myBluetoothSocket.close();
				} catch (Exception e2) {
					Log.e(Constants.LOGTAG, "Error al conectar el socket");
				}
			}
			
			myConnectedThread = new ConnectedThread(myBluetoothSocket);
			myConnectedThread.start();
			
		}
		else {
			new AlertDialog.Builder(AquariumSensorDetailActivity.this).setTitle(getResources()
					.getString(R.string.dialog_title_wrong_aquarium))
					.setMessage(R.string.dialog_message_wrong_aquarium)
					.setPositiveButton(R.string.button_accept,new OnClickListener() {
						public void onClick(DialogInterface dialog,int arg1){
						
						}
					}).show();
		}
	}
	
	
	@Override
	public void onPause(){
		try {
			myBluetoothSocket.close();
		} catch (Exception e) {
			Log.e(Constants.LOGTAG, "Error al cerrar el socket del Bluetooth en onPause");
		}
		if (dbAdapter !=null){
			dbAdapter.close();
		}
		super.onPause();
	}
	@Override
	public void onStop(){
		if (dbAdapter !=null){
			dbAdapter.close();
		}
		super.onStop();
	}
	
	@Override
	public void onDestroy(){
		if (dbAdapter !=null){
			dbAdapter.close();
		}
		super.onDestroy();
	}	
	
	
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
		case R.id.menu_about:
			new AlertDialog.Builder(this).setTitle(getResources()
			.getString(R.string.dialog_title_about))
			.setMessage(R.string.dialog_message_about)
			.setPositiveButton(R.string.button_accept,new OnClickListener() {
				public void onClick(DialogInterface dialog,int arg1){
					
				}
			}).show();
			return true;
		

		case android.R.id.home:
			Intent intent = new Intent(AquariumSensorDetailActivity.this,AquariumDetailActivity.class);
			intent.putExtra("AquariumID",""+ aquariumID);
			NavUtils.navigateUpTo(this, intent);
            return true;
		default:
			return super.onOptionsItemSelected(item);
		}
    	
    }
    
    private class ConnectedThread extends Thread {
    	private final InputStream mmInStream;
        private final OutputStream mmOutStream;
      
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
      
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }
      
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
      
        public void run() {
            byte[] buffer = new byte[512];  
            int bytes; 
 
            
            while (true) {
                try {

                    bytes = mmInStream.read(buffer);        
                    handler.obtainMessage(1, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
      
       
        public void write(String message) {
            Log.d(Constants.LOGTAG, "...Data to send: " + message + "...");
            byte[] msgBuffer = message.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                Log.d(Constants.LOGTAG, "...Error data send: " + e.getMessage() + "...");     
              }
        }
    }
    
    public void dialogConfig(final int id) {
    	
    	final Dialog dialogConfig = new Dialog(AquariumSensorDetailActivity.this);
    	dialogConfig.setTitle(R.string.dialog_title_config);
    	dialogConfig.setContentView(R.layout.dialog_config);
    	dialogConfig.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    	Button buttonConfigAccept = (Button) dialogConfig.findViewById(R.id.button_config_accept);
    	Button buttonConfigCancel = (Button) dialogConfig.findViewById(R.id.button_config_cancel);
    	
    	TextView dialogConfigTmin = (TextView) dialogConfig.findViewById(R.id.textViewConfigTempMinDetail);
    	TextView dialogConfigTmax = (TextView) dialogConfig.findViewById(R.id.textViewConfigTempMaxDetail);
    	TextView dialogConfigPhmin = (TextView) dialogConfig.findViewById(R.id.textViewConfigPhMinDetail);
    	TextView dialogConfigPhmax = (TextView) dialogConfig.findViewById(R.id.textViewConfigPhMaxDetail);
    	
    	dbHelper = new DatabaseHelper(this);
    	final ContentValues configValues = new ContentValues();
    	
    	final EditText dialogConfigdelay = (EditText) dialogConfig.findViewById(R.id.editTextdelay);
    	SharedPreferences prefs = getSharedPreferences("MyPreferences",AquariumSensorDetailActivity.MODE_PRIVATE);
		configdelay = prefs.getInt("customdelay", 0);
		if(id == aquariumIDAssociated){
			dialogConfigdelay.setText(String.valueOf(configdelay));
		}
    	final TimePicker dialogConfigLightOn = (TimePicker) dialogConfig.findViewById(R.id.timePickerDialogConfigLightOn);
    	final TimePicker dialogConfigLightOff = (TimePicker) dialogConfig.findViewById(R.id.timePickerDialogConfigLightOff);
    	
    	SimpleDateFormat sdf= new SimpleDateFormat("HH:mm",java.util.Locale.getDefault());
        Date oldDateOn = null;
        Date oldDateOff = null;
    	
        final String tMin;
        final String tMax;
        final String phMin;
        final String phMax;
        String oldLightOn;
        String oldLightOff;
        
        dbAdapter = new DatabaseAdapter(this);        	
    	dbAdapter.open();
    	Cursor cursor = dbAdapter.getCursorConfig(Integer.toString(id)); 
    	
		cursor.moveToFirst();
			
		tMin = cursor.getString(1);
		tMax = cursor.getString(2);
		phMin = cursor.getString(3);
		phMax = cursor.getString(4);
		oldLightOn = cursor.getString(5);
		oldLightOff = cursor.getString(6);
		
		if (cursor != null){
			cursor.close();
			}
		if (dbAdapter !=null){
			dbAdapter.close();
			}


		dialogConfigTmin.setText(tMin);
    	dialogConfigTmax.setText(tMax);
    	dialogConfigPhmin.setText(phMin);
    	dialogConfigPhmax.setText(phMax);
        
        
    	dialogConfigLightOn.setIs24HourView(true);
    	dialogConfigLightOn.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
    	try {
            oldDateOn = sdf.parse(oldLightOn);
        } catch (ParseException e) {
        }
        Calendar calendarOn = Calendar.getInstance();
        calendarOn.setTime(oldDateOn);
        dialogConfigLightOn.setCurrentHour(calendarOn.get(Calendar.HOUR_OF_DAY));
        dialogConfigLightOn.setCurrentMinute(calendarOn.get(Calendar.MINUTE));
    	
    	dialogConfigLightOff.setIs24HourView(true);
    	dialogConfigLightOff.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
    	try {
            oldDateOff = sdf.parse(oldLightOff);
        } catch (ParseException e) {
        }
        Calendar calendarOff = Calendar.getInstance();
        calendarOff.setTime(oldDateOff);
        dialogConfigLightOff.setCurrentHour(calendarOff.get(Calendar.HOUR_OF_DAY));
        dialogConfigLightOff.setCurrentMinute(calendarOff.get(Calendar.MINUTE));
    	
    	
    	
    	/**
    	 * Botón aceptar configuracion
    	 */
    	buttonConfigAccept.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String editTextValue = dialogConfigdelay.getText().toString();
				if(editTextValue == null || editTextValue.isEmpty())
				{
					Toast.makeText(AquariumSensorDetailActivity.this, R.string.dialog_config_empty_file , Toast.LENGTH_SHORT).show();
				}
				else if(tMin == null || tMin.isEmpty() || tMax == null || tMax.isEmpty() || phMin == null || phMin.isEmpty() || phMax == null || phMax.isEmpty()){
					Toast.makeText(AquariumSensorDetailActivity.this, R.string.dialog_config_empty_data , Toast.LENGTH_SHORT).show();
				}
				else{
					configdelay = Integer.parseInt(editTextValue);
					
					String newLightOn = String.format(Locale.getDefault(),"%02d:%02d" ,dialogConfigLightOn.getCurrentHour() , dialogConfigLightOn.getCurrentMinute());
					String newLightOff =  String.format(Locale.getDefault(),"%02d:%02d",dialogConfigLightOff.getCurrentHour(), dialogConfigLightOff.getCurrentMinute());
					
					db = dbHelper.getWritableDatabase();
					configValues.put(AquariumColumns.ColAquariumLightOn,newLightOn);
					configValues.put(AquariumColumns.ColAquariumLightOff,newLightOff);
					db.update(AquariumTable.AquariumTable, configValues, AquariumColumns._ID +" = "+ id, null);
					if(dbHelper!=null){
						dbHelper.close();
					}
					if(db!=null){
						db.close();
					}
					
					
					myConnectedThread.write("c");
					
					myConnectedThread.write(id+"/"+newLightOn+"/"+newLightOff+"/"+tMin+"/"+tMax+"/"+phMin+"/"+phMax+"/"+configdelay+"/");
					
					// Los datos de id y delay se almacenan mediante shared preferences
					
					SharedPreferences prefs = getSharedPreferences("MyPreferences",AquariumSensorDetailActivity.MODE_PRIVATE);
					SharedPreferences.Editor editor = prefs.edit();
					editor.putInt("aqIdAssociated", id);
					editor.putInt("customdelay", configdelay);
					editor.commit();
				
					dialogConfig.dismiss();
				}
				
			}
		});

    	
    	/**
    	 * Botón cancelar configuracion
    	 */
    	buttonConfigCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogConfig.cancel();
			}
		});
    	
    	dialogConfig.show();
    }
    

    public void createGraphic(){
    	fileSelected = null;
    	final Dialog dialogGraphics = new Dialog(AquariumSensorDetailActivity.this);
    	dialogGraphics.setTitle(R.string.dialog_title_graphics);
    	dialogGraphics.setContentView(R.layout.dialog_graphic);
    	dialogGraphics.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    	Button buttonGraphicsAccept = (Button) dialogGraphics.findViewById(R.id.button_graphics_accept);
    	Button buttonGraphicsCancel = (Button) dialogGraphics.findViewById(R.id.button_graphics_cancel);
    	final ListView listViewFileList = (ListView) dialogGraphics.findViewById(R.id.listViewFileList);
    	
		listViewAdapter = new MyListViewAdapter(this, fileList,Constants.GRAPHIC);
		listViewFileList.setAdapter(listViewAdapter);
		listViewFileList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		listViewFileList.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position,long arg3){
        		fileSelected = fileList.get(position);
        		
        	}
		});
		
    	/**
    	 * Botón Aceptar crear grafica
    	 */
    	buttonGraphicsAccept.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(fileSelected != null){
					myConnectedThread.write("s");
					myConnectedThread.write(fileSelected+"/");
					dialogGraphics.dismiss();
				}else{
					Toast.makeText(AquariumSensorDetailActivity.this,R.string.dialog_graphics_no_file_selected, Toast.LENGTH_SHORT).show();
				}
			}
		});
    	
    	
    	/**
    	 * Botón cancelar crear grafica
    	 */
    	buttonGraphicsCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogGraphics.cancel();
			}
		});
    	
    	dialogGraphics.show();
    }
    
    public void drawGraphic(String fileData) {
    	XYPlot plot;

    	String temp;
    	String ph;

    	arrayTime = new ArrayList<String>();
    	arrayTemp = new ArrayList<Float>();
    	arrayPh  = new ArrayList<Float>();
    	int i = 1;
    	int j = 0;
    	while(fileData.charAt(i)!='|'){
        	

            j=i;
            while(fileData.charAt(i)!='/'){
            	i++;
            }
            temp = fileData.substring(j,i);
            arrayTemp.add(Float.parseFloat(temp));
            i++;
            j=i;
            while(fileData.charAt(i)!='/'){
            	i++;
            }
            
            ph = fileData.substring(j,i);
            arrayPh.add(Float.parseFloat(ph));
          i++;
        }
    	
    	plot = (XYPlot) findViewById(R.id.pot);
    	plot.clear();
    	plot.setTitle(fileSelected);
    	XYSeries seriesTemp = new SimpleXYSeries(arrayTemp,SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,"Temperatura");
    	
    	LineAndPointFormatter seriesTempFormat = new LineAndPointFormatter();
    	seriesTempFormat.configure(getApplicationContext(),R.xml.line_temp_format);
    	plot.addSeries(seriesTemp, seriesTempFormat);
    	
    	XYSeries seriesPh = new SimpleXYSeries(arrayPh,SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,"Ph");
    	
    	LineAndPointFormatter seriesPhFormat = new LineAndPointFormatter();
    	seriesPhFormat.configure(getApplicationContext(),R.xml.line_ph_format);
    	
    	
    	plot.addSeries(seriesPh, seriesPhFormat);
    	
    	plot.setTicksPerRangeLabel(1);
        plot.setTicksPerDomainLabel(10);
        plot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 1);
        plot.setDomainStepValue(1);
        plot.getLayoutManager().remove(plot.getDomainLabelWidget());
        plot.setRangeBoundaries(0, 40,BoundaryMode.FIXED);
        plot.redraw();
        
	}
}
