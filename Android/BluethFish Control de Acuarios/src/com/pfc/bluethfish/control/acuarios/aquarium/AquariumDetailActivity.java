package com.pfc.bluethfish.control.acuarios.aquarium;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

import com.pfc.bluethfish.control.acuarios.Constants;
import com.pfc.bluethfish.control.acuarios.adapters.MyListViewAdapter;
import com.pfc.bluethfish.control.acuarios.data.AquariumColumns;
import com.pfc.bluethfish.control.acuarios.data.AquariumFishListColumns;
import com.pfc.bluethfish.control.acuarios.data.AquariumFishListTable;
import com.pfc.bluethfish.control.acuarios.data.AquariumPlantListColumns;
import com.pfc.bluethfish.control.acuarios.data.AquariumPlantListTable;
import com.pfc.bluethfish.control.acuarios.data.AquariumTable;
import com.pfc.bluethfish.control.acuarios.data.DatabaseAdapter;
import com.pfc.bluethfish.control.acuarios.data.DatabaseHelper;
import com.pfc.bluethfish.control.acuarios.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Fermín Conejo
 *
 */

public class AquariumDetailActivity extends Activity implements NumberPicker.OnValueChangeListener{

	SQLiteDatabase db;
	DatabaseAdapter dbAdapter;
	DatabaseHelper dbHelper;
	
	String id;
	String name;
	String height;
	String length;
	String width;
	String liters;
	String tMin;
	String tMax;
	String phMin;
	String phMax;
	String lightOn;
	String lightOff;
	String image;
	
	private ArrayList<Integer> listId;
	private ArrayList<String> listScName;
	private ArrayList<String> listCmName;
	private ArrayList<Integer> listImage;
	private ArrayList<String> listNumber;
	MyListViewAdapter listViewAdapter;
	
	int fishSelectedId = 0;
	int fOldNumber = 0;
	int plantSelectedId = 0; 
	int pOldNumber = 0;
	
	String fishSelectedCmName;
	String plantSelectedCmName;
	
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if (dbAdapter!=null){
			dbAdapter.close();
		}
		setContentView(R.layout.aquarium_detail);
		
		ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        
		String aquariumID = getIntent().getStringExtra("AquariumID");
		
		
		dbAdapter = new DatabaseAdapter(this);        	
    	dbAdapter.open();
    	Cursor cursor = dbAdapter.getCursorAquariumsData(aquariumID); 
    	
		cursor.moveToFirst();
		
		
		id = cursor.getString(0);
		name = cursor.getString(1);
		height = cursor.getString(2);
		length = cursor.getString(3);
		width = cursor.getString(4);
		liters = cursor.getString(5);
		tMin = cursor.getString(6);
		tMax = cursor.getString(7);
		phMin = cursor.getString(8);
		phMax = cursor.getString(9);
		lightOn = cursor.getString(10);
		lightOff = cursor.getString(11);
		image = cursor.getString(12);

		if (cursor != null){
			cursor.close();
			}
		if (dbAdapter !=null){
			dbAdapter.close();
			}
		
		actionBar.setTitle(name);
    	((TextView) findViewById(R.id.textViewAquariumNameDetail)).setText(name);
    	((TextView) findViewById(R.id.textViewAquariumHeightDetail)).setText(height);
        ((TextView) findViewById(R.id.textViewAquariumLengthDetail)).setText(length);
        ((TextView) findViewById(R.id.textViewAquariumWidthDetail)).setText(width);
        ((TextView) findViewById(R.id.textViewAquariumLitersDetail)).setText(liters);
        ((TextView) findViewById(R.id.textViewAquariumTempMinDetail)).setText(tMin);
        ((TextView) findViewById(R.id.textViewAquariumTempMaxDetail)).setText(tMax);
        ((TextView) findViewById(R.id.textViewAquariumPhMinDetail)).setText(phMin);
        ((TextView) findViewById(R.id.textViewAquariumPhMaxDetail)).setText(phMax);
        ((TextView) findViewById(R.id.textViewAquariumLightOnDetail)).setText(lightOn);
        ((TextView) findViewById(R.id.textViewAquariumLightOffDetail)).setText(lightOff);
        
        
        if(image != null){
        	
        	File imgFile = new  File(image);
        	if(imgFile.exists()){

        	    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

        	    ImageView myImage = (ImageView) findViewById(R.id.imageViewAquarium);
        	    myImage.setImageBitmap(myBitmap);

        	}
        }
        else{
        	((ImageView)findViewById(R.id.imageViewAquarium)).setImageResource(R.drawable.bluethfish_logo);
        }		
		
        /**
         * LISTA DE PECES
         */

        fillList(Integer.parseInt(id),Constants.FISH);
        
    	ListView listFish = (ListView) findViewById(R.id.listViewFish);
        listViewAdapter = new MyListViewAdapter(this, listScName, listCmName, listNumber,Constants.FISH);
        listFish.setAdapter(listViewAdapter);
        
        
        /**
         * LISTA DE PLANTAS
         */
        
        fillList(Integer.parseInt(id),Constants.PLANT);
        
        ListView listPlant = (ListView) findViewById(R.id.listViewPlant);
        listViewAdapter = new MyListViewAdapter(this, listScName, listCmName, listNumber, Constants.PLANT);
        listPlant.setAdapter(listViewAdapter);
         
        
        /**
         * BOTONES GESTION LISTAS Y PARAMETROS
         */
        /**
         * Boton añadir pez al acuario
         */
        Button buttonAddFish=(Button)findViewById(R.id.buttonAddFish);
        buttonAddFish.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addFishToList();
			}
		});
        
        /**
         * Boton modificar pez del acuario
         */
        Button buttonUpdateFish=(Button)findViewById(R.id.buttonUpdateFish);
        buttonUpdateFish.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateFishList();
			}
		});
        
        /**
         * Boton eliminar pez del acuario
         */
        Button buttonRemoveFish=(Button)findViewById(R.id.buttonRemoveFish);
        buttonRemoveFish.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				removeFishList();
			}
		});
        
        /**
         * Boton añadir planta al acuario
         */
        Button buttonAddPlant=(Button)findViewById(R.id.buttonAddPlant);
        buttonAddPlant.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addPlantToList();
			}
		});
        
        /**
         * Boton modificar planta del acuario
         */
        Button buttonUpdatePlant=(Button)findViewById(R.id.buttonUpdatePlant);
        buttonUpdatePlant.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updatePlantList();
			}
		});
       
        /**
         * Boton eliminar planta del acuario
         */
        Button buttonRemovePlant=(Button)findViewById(R.id.buttonRemovePlant);
        buttonRemovePlant.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				removePlantList();
			}
		});
        
        /**
         * Boton para calcular los parametros del acuaro en funcion de los peces y plantas dentro de él
         */
        Button buttonCalculateParams=(Button)findViewById(R.id.buttonCalculateParams);
        buttonCalculateParams.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				aquariumCalculateParams();	
			}
		});
     
        
        /**
         * Boton sensores
         */
        Button buttonSensor = (Button) findViewById(R.id.buttonSensor);
        buttonSensor.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AquariumDetailActivity.this,AquariumSensorDetailActivity.class);
				intent.putExtra("AquariumID",""+ id);
				startActivity(intent); 
			}
		});
        
	}
		
	
	@Override
	public void onPause(){
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
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
         Log.i("value is",""+newVal);
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
			NavUtils.navigateUpTo(this, new Intent(this, AquariumsActivity.class));
            return true;
		default:
			return super.onOptionsItemSelected(item);
		}
    	
    }
	/**
	 * Carga la lista de peces o plantas del acuario segun el parametro type
	 * @param aquariumId
	 * @param type puede ser Constants.FISH o Constants.PLANT  
	 */
    public void fillList(int aquariumId,int type){
		
    	listScName = new ArrayList<String>();
        listCmName = new ArrayList<String>();
        listNumber = new ArrayList<String>();
     
        
        String ScName;
        String CmName;
        String Number;
        Cursor cursor = null;
        
        dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();
        switch (type) {
		case Constants.FISH:
			cursor = dbAdapter.getCursorAquariumFishList(aquariumId);
			if(cursor.moveToFirst()){
	        	do{
	        		ScName = cursor.getString(0);
	        		CmName = cursor.getString(1);
	        		Number = cursor.getString(2);
	        		
	        		listScName.add(ScName);
	        		listCmName.add(CmName);
	        		listNumber.add(Number);
	        		
	        	}while(cursor.moveToNext());	
	        }
			
			if(cursor!=null){
	    		cursor.close();
	    		}
	    	if(dbAdapter!=null){
	    		dbAdapter.close();
	    	}
			break;
		case Constants.PLANT:
			cursor = dbAdapter.getCursorAquariumPlantList(aquariumId);
			if(cursor.moveToFirst()){
	        	do{
	        		ScName = cursor.getString(0);
	        		CmName = cursor.getString(1);
	        		Number = cursor.getString(2);
	        		
	        		listScName.add(ScName);
	        		listCmName.add(CmName);
	        		listNumber.add(Number);
	        		
	        	}while(cursor.moveToNext());
	        }
			
			if(cursor!=null){
	    		cursor.close();
	    		}
	    	if(dbAdapter!=null){
	    		dbAdapter.close();
	    	}
			break;
		default:
			break;
		}     
    	
	}
    
    /**
     * Metodo para añadir un pez a la lista
     */
    public void addFishToList(){
    	
    	fishSelectedId=0; //se pone a 0 para no tener ningun elemento seleccionado
    	final Dialog dialogAddFishToList = new Dialog(AquariumDetailActivity.this);
        dialogAddFishToList.setTitle(R.string.dialog_title_add_fish);
        dialogAddFishToList.setContentView(R.layout.dialog_add_specimen_to_list);
        dialogAddFishToList.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        TextView textViewFishNumber = (TextView) dialogAddFishToList.findViewById(R.id.textViewNumber);
        textViewFishNumber.setText(R.string.dialog_fish_number);
        
        final NumberPicker numberPikerAddFishNumber = (NumberPicker) dialogAddFishToList.findViewById(R.id.numberPickerNumber);
        numberPikerAddFishNumber.setMinValue(1);
        numberPikerAddFishNumber.setMaxValue(100);
        numberPikerAddFishNumber.setWrapSelectorWheel(false);
        numberPikerAddFishNumber.setOnValueChangedListener(this);
        
        listId = new ArrayList<Integer>();
        listScName = new ArrayList<String>();
        listCmName = new ArrayList<String>();
        listImage = new ArrayList<Integer>();
        
        int fishid;
        String scName;
        String cmName;
        String imagelist;
                
        dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();
        Cursor cursor = dbAdapter.getCursorFreshwaterFishCustomList();
        if(cursor.moveToFirst()){
        	
        	do{
        		
        		fishid = Integer.parseInt(cursor.getString(0).trim());
        		scName = cursor.getString(1);
        		cmName = cursor.getString(2);
        		imagelist = cursor.getString(3);
        		
        		listId.add(fishid);
        		listScName.add(scName);
        		listCmName.add(cmName);
        		if(imagelist != null){
        			try{
    					Field field = R.drawable.class.getField(imagelist);
                	    int imageRes = field.getInt(field);

                	    listImage.add(imageRes);
    				}
    				catch (Exception e) {
                	    Log.e(Constants.LOGTAG, "Fallo al obtener el id de la imagen.", e);
                	}
    			
    			}else{
    				listImage.add(R.drawable.bluethfish_logo);
    			}	
        		
        	}while(cursor.moveToNext());
        }
        
        if(cursor!=null){
    		cursor.close();
    		}
    	if(dbAdapter!=null){
    		dbAdapter.close();
    	}
    	
        listViewAdapter = new MyListViewAdapter(this, listScName, listCmName, listImage);
        final ListView listViewFish = (ListView) dialogAddFishToList.findViewById(R.id.listViewAdd);
        listViewFish.setAdapter(listViewAdapter);
        listViewFish.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        
        listViewFish.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position,long arg3){
        		
        		fishSelectedId=listId.get(position);
        	}
		});
        
        Button buttonAddFishAdd = (Button) dialogAddFishToList.findViewById(R.id.button_add_to_list_add);
        buttonAddFishAdd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		
				int fishNumber;
				
				if (fishSelectedId != 0 ){
					
					if (fishExist(Integer.parseInt(id), fishSelectedId)){
						
						fishNumber = numberPikerAddFishNumber.getValue();
						
						dbAdapter = new DatabaseAdapter(AquariumDetailActivity.this);
				        dbAdapter.open();
						Cursor cursor = dbAdapter.getCursorAquariumFishNumber(Integer.parseInt(id), fishSelectedId);
						cursor.moveToFirst();
						int newFishNumber = Integer.parseInt(cursor.getString(0)) + fishNumber;
						if (cursor!=null){
							cursor.close();
						}
						if(dbAdapter!=null){
				    		dbAdapter.close();
				    	}

						final ContentValues aquariumFishListValues = new ContentValues();
						
						dbHelper = new DatabaseHelper(AquariumDetailActivity.this);
						db = dbHelper.getWritableDatabase();
						aquariumFishListValues.put(AquariumFishListColumns.ColFishListAquariumId, id);
						aquariumFishListValues.put(AquariumFishListColumns.ColFishListFishId, fishSelectedId);
						aquariumFishListValues.put(AquariumFishListColumns.ColFishListFishNumber, newFishNumber);
						
						db.update(AquariumFishListTable.FishListTable, aquariumFishListValues,
								AquariumFishListColumns.ColFishListAquariumId+" = "+id+ " AND "+ AquariumFishListColumns.ColFishListFishId + " = "+fishSelectedId,
								null);
						
						if(dbHelper!=null){
							dbHelper.close();
						}
						if(db!=null){
							db.close();
						}
						dialogAddFishToList.dismiss();
						onCreate(new Bundle());
					}
					else{
						
						fishNumber = numberPikerAddFishNumber.getValue();
						final ContentValues aquariumFishListValues = new ContentValues();
						
						dbHelper = new DatabaseHelper(AquariumDetailActivity.this);
						db = dbHelper.getWritableDatabase();
						aquariumFishListValues.put(AquariumFishListColumns.ColFishListAquariumId, id);
						aquariumFishListValues.put(AquariumFishListColumns.ColFishListFishId, fishSelectedId);
						aquariumFishListValues.put(AquariumFishListColumns.ColFishListFishNumber, fishNumber);
						
						db.insert(AquariumFishListTable.FishListTable, null, aquariumFishListValues);
						
						if(dbHelper!=null){
							dbHelper.close();
						}
						if(db!=null){
							db.close();
						}
						dialogAddFishToList.dismiss();
						onCreate(new Bundle());
					}
					
				}
				else{
					Toast.makeText(AquariumDetailActivity.this, R.string.dialog_fish_no_selected , Toast.LENGTH_SHORT).show();
				}
				
			}
		});
        
        
        Button buttonAddFishCancel = (Button) dialogAddFishToList.findViewById(R.id.button_add_to_list_cancel);
        buttonAddFishCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogAddFishToList.cancel();
			}
		});
        
        dialogAddFishToList.show();
    }
    
    /**
     * Metodo para acutalizar el numero de ejeplares de un tipo de pez del acuario
     */
    public void updateFishList(){
    	
    	fishSelectedId = 0; //se pone a 0 para no tener ningun elemento seleccionado
    	fOldNumber = 0;
    	final Dialog dialogUpdateFishList = new Dialog(AquariumDetailActivity.this);
    	dialogUpdateFishList.setTitle(R.string.dialog_title_update_fish_list);
    	dialogUpdateFishList.setContentView(R.layout.dialog_update_list);
    	dialogUpdateFishList.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    	TextView textViewFishNewNumber = (TextView) dialogUpdateFishList.findViewById(R.id.textViewNewNumber);
    	textViewFishNewNumber.setText(R.string.dialog_fish_new_number);	
    	
    	final NumberPicker numberPickerUpdateFishNumber = (NumberPicker) dialogUpdateFishList.findViewById(R.id.numberPickerNewNumber);
    	numberPickerUpdateFishNumber.setMinValue(1);
    	numberPickerUpdateFishNumber.setMaxValue(100);
    	numberPickerUpdateFishNumber.setWrapSelectorWheel(false);
    	numberPickerUpdateFishNumber.setOnValueChangedListener(this);
    	
    	fillList(Integer.parseInt(id),Constants.FISH);
        
    	final ListView listUpdateFish = (ListView) dialogUpdateFishList.findViewById(R.id.listViewUpdate);
        listViewAdapter = new MyListViewAdapter(this, listScName, listCmName, listNumber,Constants.FISH);
        listUpdateFish.setAdapter(listViewAdapter);
        listUpdateFish.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    	
        listUpdateFish.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position,long arg3){
        		
        		dbAdapter = new DatabaseAdapter(AquariumDetailActivity.this);
                dbAdapter.open();
                Cursor cursor = dbAdapter.getCursorFishId(listScName.get(position));
        		cursor.moveToFirst();
        		fishSelectedId = Integer.parseInt(cursor.getString(0));
        		if(cursor!=null){
            		cursor.close();
            		}
            	if(dbAdapter!=null){
            		dbAdapter.close();
            	}
        		
        		fOldNumber = Integer.parseInt(listNumber.get(position));  		
        		numberPickerUpdateFishNumber.setValue(fOldNumber);
        		
        	}
		});
        
        Button buttonUpdateFishUpdate = (Button) dialogUpdateFishList.findViewById(R.id.button_update_list_update);
        buttonUpdateFishUpdate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(fishSelectedId != 0){
					int newFishNumber = numberPickerUpdateFishNumber.getValue();
					final ContentValues aquariumFishListUpdateValues = new ContentValues();
					
					dbHelper = new DatabaseHelper(AquariumDetailActivity.this);
					db = dbHelper.getWritableDatabase();
					aquariumFishListUpdateValues.put(AquariumFishListColumns.ColFishListAquariumId, id);
					aquariumFishListUpdateValues.put(AquariumFishListColumns.ColFishListFishId, fishSelectedId);
					aquariumFishListUpdateValues.put(AquariumFishListColumns.ColFishListFishNumber, newFishNumber);
					
					db.update(AquariumFishListTable.FishListTable, aquariumFishListUpdateValues,
							AquariumFishListColumns.ColFishListAquariumId+" = "+id+ " AND "+ AquariumFishListColumns.ColFishListFishId + " = "+fishSelectedId,
							null);
					
					if(dbHelper!=null){
						dbHelper.close();
					}
					if(db!=null){
						db.close();
					}
					
					dialogUpdateFishList.dismiss();
					onCreate(new Bundle());
					
				}
				else{
					Toast.makeText(AquariumDetailActivity.this, R.string.dialog_fish_no_selected , Toast.LENGTH_SHORT).show();
				}

			}
		});
        
        Button buttonUpdateFishCancel = (Button) dialogUpdateFishList.findViewById(R.id.button_update_list_cancel);
        buttonUpdateFishCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogUpdateFishList.cancel();
			}
		});
    	dialogUpdateFishList.show();
    }
    
    /**
     * Metodo para eliminar uno o todos los peces del acuario
     */
    public void removeFishList(){
    	
    	fishSelectedId = 0; //se pone a 0 para no tener ningun elemento seleccionado
    	
    	final Dialog dialogRemoveFishList = new Dialog(AquariumDetailActivity.this);
    	dialogRemoveFishList.setTitle(R.string.dialog_title_remove_fish_list);
    	dialogRemoveFishList.setContentView(R.layout.dialog_remove_list);
    	dialogRemoveFishList.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    	
    	fillList(Integer.parseInt(id),Constants.FISH);
        
    	final ListView listRemoveFishList = (ListView) dialogRemoveFishList.findViewById(R.id.listViewRemove);
        listViewAdapter = new MyListViewAdapter(this, listScName, listCmName, listNumber,Constants.FISH);
        listRemoveFishList.setAdapter(listViewAdapter);
        listRemoveFishList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    	
        listRemoveFishList.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position,long arg3){
        		
        		dbAdapter = new DatabaseAdapter(AquariumDetailActivity.this);
                dbAdapter.open();
                fishSelectedCmName = listCmName.get(position);
                Cursor cursor = dbAdapter.getCursorFishId(listScName.get(position));
        		cursor.moveToFirst();
        		fishSelectedId = Integer.parseInt(cursor.getString(0));
        		if(cursor!=null){
            		cursor.close();
            		}
            	if(dbAdapter!=null){
            		dbAdapter.close();
            	}
        		
        	}
		});
        
        Button buttonRemoveFish = (Button) dialogRemoveFishList.findViewById(R.id.button_remove_element_list);
        buttonRemoveFish.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (fishSelectedId !=0){
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AquariumDetailActivity.this);
					alertDialogBuilder.setTitle(R.string.dialog_title_remove_fish_list);
					alertDialogBuilder
						.setCancelable(true)
						.setMessage(getResources().getString(R.string.dialog_remove_fish) + " " + fishSelectedCmName + "?")
						.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						})
						.setPositiveButton(R.string.button_accept, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
								dbHelper = new DatabaseHelper(AquariumDetailActivity.this);
								db = dbHelper.getWritableDatabase();
								db.delete(AquariumFishListTable.FishListTable,
										AquariumFishListColumns.ColFishListAquariumId + " = " + id 
										+ " AND " + AquariumFishListColumns.ColFishListFishId + " = " + fishSelectedId,
										null);
								if(dbHelper!=null){
									dbHelper.close();
								}
								if(db!=null){
									db.close();
								}
								onCreate(new Bundle());
								dialogRemoveFishList.dismiss();//se cierra el dialog de eliminar de la lista
								removeFishList(); //se vuelve a llamar al metodo para que la lista del dialogo se actualize
							}
						});
					
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				
				}
				else{
					Toast.makeText(AquariumDetailActivity.this, R.string.dialog_fish_no_selected , Toast.LENGTH_SHORT).show();
				}
					
			}
		});
        
        Button buttonRemoveAllFish = (Button) dialogRemoveFishList.findViewById(R.id.button_remove_all_list);
        buttonRemoveAllFish.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AquariumDetailActivity.this);
				alertDialogBuilder.setTitle(R.string.dialog_title_remove_fish_list);
				alertDialogBuilder
					.setCancelable(true)
					.setMessage(getResources().getString(R.string.dialog_remove_all_fish) + " " + name + "?")
					.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					})
					.setPositiveButton(R.string.button_accept, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							dbHelper = new DatabaseHelper(AquariumDetailActivity.this);
							db = dbHelper.getWritableDatabase();
							db.delete(AquariumFishListTable.FishListTable,
									AquariumFishListColumns.ColFishListAquariumId + " = " + id,
									null);
							if(dbHelper!=null){
								dbHelper.close();
							}
							if(db!=null){
								db.close();
							}
							dialogRemoveFishList.dismiss();//se cierra el dialog de eliminar de la lista
							onCreate(new Bundle());
							
							
						}
					});
				
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
				
			}
		});
        
        Button buttonRemoveFishCancel = (Button) dialogRemoveFishList.findViewById(R.id.button_remove_list_cancel);
        buttonRemoveFishCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogRemoveFishList.cancel();
			}
		});
           
        dialogRemoveFishList.show();
    }
    
    /**
     * Comprueba si un pez ya existe en el acuario 
     * @param aquarium_id
     * @param fish_id
     * @return
     */
    public boolean fishExist(int aquarium_id, int fish_id){
    	dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();
        
        Cursor cursor= dbAdapter.getCursorAquariumFishExist(aquarium_id, fish_id);
    	boolean exist = cursor.getCount() > 0;
    	
    	if(cursor!=null){
    		cursor.close();
    		}
    	if(dbAdapter!=null){
    		dbAdapter.close();
    	}
    	
        return exist;
    }
    
    /**
     * Metodo para añadiro una planta al acuario
     */
    public void addPlantToList(){
    	
    	plantSelectedId=0; //se pone a 0 para no tener ningun elemento seleccionado
    	final Dialog dialogAddPlantToList = new Dialog(AquariumDetailActivity.this);
        dialogAddPlantToList.setTitle(R.string.dialog_title_add_plant);
        dialogAddPlantToList.setContentView(R.layout.dialog_add_specimen_to_list);
        dialogAddPlantToList.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        TextView textViewPlantNumber = (TextView)dialogAddPlantToList.findViewById(R.id.textViewNumber);
        textViewPlantNumber.setText(R.string.dialog_plant_number);
        
        final NumberPicker numberPikerAddPlantNumber = (NumberPicker) dialogAddPlantToList.findViewById(R.id.numberPickerNumber);
        numberPikerAddPlantNumber.setMinValue(1);
        numberPikerAddPlantNumber.setMaxValue(100);
        numberPikerAddPlantNumber.setWrapSelectorWheel(false);
        numberPikerAddPlantNumber.setOnValueChangedListener(this);
        
        listId = new ArrayList<Integer>();
        listScName = new ArrayList<String>();
        listCmName = new ArrayList<String>();
        listImage = new ArrayList<Integer>();
        
        int plantid;
        String scName;
        String cmName;
        String imagelist;
                
        dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();
        Cursor cursor = dbAdapter.getCursorFreshwaterPlantCustomList();
        if(cursor.moveToFirst()){
        	
        	do{
        		
        		plantid = Integer.parseInt(cursor.getString(0).trim());
        		scName = cursor.getString(1);
        		cmName = cursor.getString(2);
        		imagelist = cursor.getString(3);
        		
        		listId.add(plantid);
        		listScName.add(scName);
        		listCmName.add(cmName);
        		if(imagelist != null){
        			try{
    					Field field = R.drawable.class.getField(imagelist);
                	    int imageRes = field.getInt(field);

                	    listImage.add(imageRes);
    				}
    				catch (Exception e) {
                	    Log.e(Constants.LOGTAG, "Fallo al obtener el id de la imagen.", e);
                	}
    			
    			}else{
    				listImage.add(R.drawable.bluethfish_logo);
    			}	
        		
        	}while(cursor.moveToNext());
        }
        
        if(cursor!=null){
    		cursor.close();
    		}
    	if(dbAdapter!=null){
    		dbAdapter.close();
    	}
    	
        listViewAdapter = new MyListViewAdapter(this, listScName, listCmName, listImage);
        final ListView listViewPlant = (ListView) dialogAddPlantToList.findViewById(R.id.listViewAdd);
        listViewPlant.setAdapter(listViewAdapter);
        listViewPlant.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        
        listViewPlant.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position,long arg3){
        		
        		plantSelectedId=listId.get(position);
        	}
		});
        
        Button buttonAddPlantAdd = (Button) dialogAddPlantToList.findViewById(R.id.button_add_to_list_add);
        buttonAddPlantAdd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		
				String plantNumber;
				
				if (plantSelectedId != 0 ){
					if (plantExist(Integer.parseInt(id), plantSelectedId)){
						
						plantNumber = Integer.toString(numberPikerAddPlantNumber.getValue());
						dbHelper = new DatabaseHelper(AquariumDetailActivity.this);
						db = dbHelper.getWritableDatabase();
						dbAdapter = new DatabaseAdapter(AquariumDetailActivity.this);
				        dbAdapter.open();
						Cursor cursor = dbAdapter.getCursorAquariumPlantNumber(Integer.parseInt(id), plantSelectedId);
						cursor.moveToFirst();
						int newPlantNumber = Integer.parseInt(cursor.getString(0)) + Integer.parseInt(plantNumber);
						if (cursor!=null){
							cursor.close();
						}
						if(dbAdapter!=null){
				    		dbAdapter.close();
				    	}

						final ContentValues aquariumPlantListValues = new ContentValues();
						
						dbHelper = new DatabaseHelper(AquariumDetailActivity.this);
						db = dbHelper.getWritableDatabase();
						aquariumPlantListValues.put(AquariumPlantListColumns.ColPlantListAquariumId, id);
						aquariumPlantListValues.put(AquariumPlantListColumns.ColPlantListPlantId, plantSelectedId);
						aquariumPlantListValues.put(AquariumPlantListColumns.ColPlantListPlantNumber, newPlantNumber);
						
						db.update(AquariumPlantListTable.PlantListTable, aquariumPlantListValues,
								AquariumPlantListColumns.ColPlantListAquariumId+" = "+id+ " AND "+ AquariumPlantListColumns.ColPlantListPlantId + " = "+plantSelectedId,
								null);
						
						if(dbHelper!=null){
							dbHelper.close();
						}
						if(db!=null){
							db.close();
						}
						dialogAddPlantToList.dismiss();
						onCreate(new Bundle());

						
					}
					else{
						
						plantNumber = Integer.toString(numberPikerAddPlantNumber.getValue());
						final ContentValues aquariumPlantListValues = new ContentValues();
						
						dbHelper = new DatabaseHelper(AquariumDetailActivity.this);
						db = dbHelper.getWritableDatabase();
						aquariumPlantListValues.put(AquariumPlantListColumns.ColPlantListAquariumId, id);
						aquariumPlantListValues.put(AquariumPlantListColumns.ColPlantListPlantId, plantSelectedId);
						aquariumPlantListValues.put(AquariumPlantListColumns.ColPlantListPlantNumber, plantNumber);
						
						db.insert(AquariumPlantListTable.PlantListTable, null, aquariumPlantListValues);
						
						if(dbHelper!=null){
							dbHelper.close();
						}
						if(db!=null){
							db.close();
						}
						dialogAddPlantToList.dismiss();
						onCreate(new Bundle());
					}
					
				}
				else{
					Toast.makeText(AquariumDetailActivity.this, R.string.dialog_plant_no_selected , Toast.LENGTH_SHORT).show();
				}
				
			}
		});
        
        
        Button buttonAddPlantCancel = (Button) dialogAddPlantToList.findViewById(R.id.button_add_to_list_cancel);
        buttonAddPlantCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogAddPlantToList.cancel();
			}
		});
        
        dialogAddPlantToList.show();
    }
    

    /**
     * Metodo para acutalizar el numero de tallos de un tipo de planta del acuario
     */
    public void updatePlantList(){
    	
    	plantSelectedId = 0; //se pone a 0 para no tener ningun elemento seleccionado
    	pOldNumber = 0;
    	final Dialog dialogUpdatePlantList = new Dialog(AquariumDetailActivity.this);
    	dialogUpdatePlantList.setTitle(R.string.dialog_title_update_plant_list);
    	dialogUpdatePlantList.setContentView(R.layout.dialog_update_list);
    	dialogUpdatePlantList.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    	TextView textViewPlantNewNumber = (TextView) dialogUpdatePlantList.findViewById(R.id.textViewNewNumber);
    	textViewPlantNewNumber.setText(R.string.dialog_plant_new_number);	
    	
    	final NumberPicker numberPickerUpdatePlantNumber = (NumberPicker) dialogUpdatePlantList.findViewById(R.id.numberPickerNewNumber);
    	numberPickerUpdatePlantNumber.setMinValue(1);
    	numberPickerUpdatePlantNumber.setMaxValue(100);
    	numberPickerUpdatePlantNumber.setWrapSelectorWheel(false);
    	numberPickerUpdatePlantNumber.setOnValueChangedListener(this);
    	
    	fillList(Integer.parseInt(id),Constants.PLANT);
        
    	final ListView listUpdatePlant = (ListView) dialogUpdatePlantList.findViewById(R.id.listViewUpdate);
        listViewAdapter = new MyListViewAdapter(this, listScName, listCmName, listNumber,Constants.PLANT);
        listUpdatePlant.setAdapter(listViewAdapter);
        listUpdatePlant.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    	
        listUpdatePlant.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position,long arg3){
        		
        		dbAdapter = new DatabaseAdapter(AquariumDetailActivity.this);
                dbAdapter.open();
                Cursor cursor = dbAdapter.getCursorPlantId(listScName.get(position));
        		cursor.moveToFirst();
        		plantSelectedId = Integer.parseInt(cursor.getString(0));
        		if(cursor!=null){
            		cursor.close();
            		}
            	if(dbAdapter!=null){
            		dbAdapter.close();
            	}
        		
        		pOldNumber = Integer.parseInt(listNumber.get(position));  		
        		numberPickerUpdatePlantNumber.setValue(pOldNumber);
        		
        	}
		});
        
        Button buttonUpdatePlantUpdate = (Button) dialogUpdatePlantList.findViewById(R.id.button_update_list_update);
        buttonUpdatePlantUpdate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(plantSelectedId !=0){
					int newPlantNumber = numberPickerUpdatePlantNumber.getValue();
					final ContentValues aquariumPlantListUpdateValues = new ContentValues();
					
					dbHelper = new DatabaseHelper(AquariumDetailActivity.this);
					db = dbHelper.getWritableDatabase();
					aquariumPlantListUpdateValues.put(AquariumPlantListColumns.ColPlantListAquariumId, id);
					aquariumPlantListUpdateValues.put(AquariumPlantListColumns.ColPlantListPlantId, plantSelectedId);
					aquariumPlantListUpdateValues.put(AquariumPlantListColumns.ColPlantListPlantNumber, newPlantNumber);
					
					db.update(AquariumPlantListTable.PlantListTable, aquariumPlantListUpdateValues,
							AquariumPlantListColumns.ColPlantListAquariumId+" = "+id+ " AND "+ AquariumPlantListColumns.ColPlantListPlantId + " = "+ plantSelectedId,
							null);
					
					if(dbHelper!=null){
						dbHelper.close();
					}
					if(db!=null){
						db.close();
					}
					dialogUpdatePlantList.dismiss();
					onCreate(new Bundle());
					
				}
				else{
					Toast.makeText(AquariumDetailActivity.this, R.string.dialog_plant_no_selected , Toast.LENGTH_SHORT).show();
				}

			}
		});
        
        Button buttonUpdatePlantCancel = (Button) dialogUpdatePlantList.findViewById(R.id.button_update_list_cancel);
        buttonUpdatePlantCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogUpdatePlantList.cancel();
			}
		});
    	dialogUpdatePlantList.show();
    }
    
    /**
     * Metodo para eliminar uno o todos los peces del acuario
     */
    public void removePlantList(){
    	
    	plantSelectedId = 0; //se pone a 0 para no tener ningun elemento seleccionado
    	
    	final Dialog dialogRemovePlantList = new Dialog(AquariumDetailActivity.this);
    	dialogRemovePlantList.setTitle(R.string.dialog_title_remove_plant_list);
    	dialogRemovePlantList.setContentView(R.layout.dialog_remove_list);
    	dialogRemovePlantList.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    	
    	fillList(Integer.parseInt(id),Constants.PLANT);
        
    	final ListView listRemovePlantList = (ListView) dialogRemovePlantList.findViewById(R.id.listViewRemove);
        listViewAdapter = new MyListViewAdapter(this, listScName, listCmName, listNumber,Constants.PLANT);
        listRemovePlantList.setAdapter(listViewAdapter);
        listRemovePlantList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    	
        listRemovePlantList.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position,long arg3){
        		
        		dbAdapter = new DatabaseAdapter(AquariumDetailActivity.this);
                dbAdapter.open();
                plantSelectedCmName = listCmName.get(position);
                Cursor cursor = dbAdapter.getCursorPlantId(listScName.get(position));
        		cursor.moveToFirst();
        		plantSelectedId = Integer.parseInt(cursor.getString(0));
        		if(cursor!=null){
            		cursor.close();
            		}
            	if(dbAdapter!=null){
            		dbAdapter.close();
            	}
        		
        	}
		});
        
        Button buttonRemovePlant = (Button) dialogRemovePlantList.findViewById(R.id.button_remove_element_list);
        buttonRemovePlant.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (plantSelectedId !=0){
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AquariumDetailActivity.this);
					alertDialogBuilder.setTitle(R.string.dialog_title_remove_plant_list);
					alertDialogBuilder
						.setCancelable(true)
						.setMessage(getResources().getString(R.string.dialog_remove_plant) + " " + plantSelectedCmName + "?")
						.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						})
						.setPositiveButton(R.string.button_accept, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
								dbHelper = new DatabaseHelper(AquariumDetailActivity.this);
								db = dbHelper.getWritableDatabase();
								db.delete(AquariumPlantListTable.PlantListTable,
										AquariumPlantListColumns.ColPlantListAquariumId + " = " + id 
										+ " AND " + AquariumPlantListColumns.ColPlantListPlantId + " = " + plantSelectedId,
										null);
								if(dbHelper!=null){
									dbHelper.close();
								}
								if(db!=null){
									db.close();
								}
								onCreate(new Bundle());
								dialogRemovePlantList.dismiss();//se cierra el dialog de eliminar de la lista
								removePlantList(); //se vuelve a llamar al metodo para que la lista del dialogo se actualize
							}
						});
					
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				
				}
				else{
					Toast.makeText(AquariumDetailActivity.this, R.string.dialog_plant_no_selected , Toast.LENGTH_SHORT).show();
				}
					
			}
		});
        
        Button buttonRemoveAllPlant = (Button) dialogRemovePlantList.findViewById(R.id.button_remove_all_list);
        buttonRemoveAllPlant.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AquariumDetailActivity.this);
				alertDialogBuilder.setTitle(R.string.dialog_title_remove_plant_list);
				alertDialogBuilder
					.setCancelable(true)
					.setMessage(getResources().getString(R.string.dialog_remove_all_plant) + " " + name + "?")
					.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					})
					.setPositiveButton(R.string.button_accept, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							dbHelper = new DatabaseHelper(AquariumDetailActivity.this);
							db = dbHelper.getWritableDatabase();
							db.delete(AquariumPlantListTable.PlantListTable,
									AquariumPlantListColumns.ColPlantListAquariumId + " = " + id,
									null);
							if(dbHelper!=null){
								dbHelper.close();
							}
							if(db!=null){
								db.close();
							}
							dialogRemovePlantList.dismiss();//se cierra el dialog de eliminar de la lista
							onCreate(new Bundle());
							
							
						}
					});
				
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
				
			}
		});
        
        Button buttonRemovePlantCancel = (Button) dialogRemovePlantList.findViewById(R.id.button_remove_list_cancel);
        buttonRemovePlantCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogRemovePlantList.cancel();
			}
		});
        
        
        dialogRemovePlantList.show();
    }
    
    /**
     * Comprueba si una planta ya existe en el acuario
     * @param aquarium_id
     * @param plant_id
     * @return
     */
    public boolean plantExist(int aquarium_id, int plant_id){
    	dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();
        
        Cursor cursor= dbAdapter.getCursorAquariumPlantExist(aquarium_id, plant_id);
    	boolean exist = cursor.getCount() > 0;
    	
    	if(cursor!=null){
    		cursor.close();
    		}
    	if(dbAdapter!=null){
    		dbAdapter.close();
    	}
        return exist;
    }
    
    public void aquariumCalculateParams(){
    	
    	ContentValues aquariumParamsValues = new ContentValues();
    	//valores medios de Ph y temperatura de la lista de peces del acuario
    	String avgFishPhMin = null;
    	String avgFishPhMax = null;
    	String avgFishTMin = null;
    	String avgFishTMax = null;
    	
    	//valores medios de Ph y temperatura de la lista de plantas del acuario
    	String avgPlantPhMin = null;
    	String avgPlantPhMax = null;
    	String avgPlantTMin = null;
    	String avgPlantTMax = null;
    	
    	//valores medios de Ph y temperatura del acuario
    	float avgPhMin;
    	float avgPhMax;
    	float avgTMin;
    	float avgTMax;
    	
    	
    	//conseguimos los valores medios de la lista de peces
    	dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();
        
        Cursor cursorFish= dbAdapter.getCursorAquariumAvgFishList(Integer.parseInt(id));
    	cursorFish.moveToFirst();
    	avgFishPhMin = cursorFish.getString(0);
    	avgFishPhMax = cursorFish.getString(1);
    	avgFishTMin = cursorFish.getString(2);
    	avgFishTMax = cursorFish.getString(3);
        
    	if(cursorFish!=null){
    		cursorFish.close();
    		}
    	if(dbAdapter!=null){
    		dbAdapter.close();
    	}
    	
    	//conseguimos los valores medios de la lista de plantas
    	dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();
        
        Cursor cursorPlant= dbAdapter.getCursorAquariumAvgPlantList(Integer.parseInt(id));
    	cursorPlant.moveToFirst();
    	avgPlantPhMin = cursorPlant.getString(0);
    	avgPlantPhMax = cursorPlant.getString(1);
    	avgPlantTMin = cursorPlant.getString(2);
    	avgPlantTMax = cursorPlant.getString(3);
        
    	if(cursorPlant!=null){
    		cursorPlant.close();
    		}
    	if(dbAdapter!=null){
    		dbAdapter.close();
    	}
    	
    	//conseguimos los valores medios generales y los ponemos en la base de datos
    	if(avgFishPhMin == null && avgPlantPhMin == null){
    		Toast.makeText(AquariumDetailActivity.this, R.string.aquarium_no_fish_no_plant, Toast.LENGTH_SHORT).show();
    	
    	}
    	else if(avgFishPhMin == null || avgPlantPhMin == null){
    		if(avgPlantPhMin == null){
        		
        		avgPhMin = Float.parseFloat(avgFishPhMin);
    	    	avgPhMax = Float.parseFloat(avgFishPhMax);
    	    	avgTMin = Float.parseFloat(avgFishTMin);
    	    	avgTMax = Float.parseFloat(avgFishTMax);
    	    	
    	    	aquariumParamsValues.put(AquariumColumns.ColAquariumPhMin, avgPhMin);
    			aquariumParamsValues.put(AquariumColumns.ColAquariumPhMax, avgPhMax);
    			aquariumParamsValues.put(AquariumColumns.ColAquariumTmin, avgTMin);
    			aquariumParamsValues.put(AquariumColumns.ColAquariumTmax, avgTMax);
    			
    			dbHelper = new DatabaseHelper(AquariumDetailActivity.this);
    			db = dbHelper.getWritableDatabase();
    			db.update(AquariumTable.AquariumTable, aquariumParamsValues,AquariumColumns._ID+" = "+id,null);
    			if(dbHelper!=null){
    				dbHelper.close();
    			}
    			if(db!=null){
    				db.close();
    			}
    			
        	}
    		if(avgFishPhMin == null){
    			
    			avgPhMin = Float.parseFloat(avgPlantPhMin);
    	    	avgPhMax = Float.parseFloat(avgPlantPhMax);
    	    	avgTMin = Float.parseFloat(avgPlantTMin);
    	    	avgTMax = Float.parseFloat(avgPlantTMax);
    	    	
    	    	aquariumParamsValues.put(AquariumColumns.ColAquariumPhMin, avgPhMin);
    			aquariumParamsValues.put(AquariumColumns.ColAquariumPhMax, avgPhMax);
    			aquariumParamsValues.put(AquariumColumns.ColAquariumTmin, avgTMin);
    			aquariumParamsValues.put(AquariumColumns.ColAquariumTmax, avgTMax);
    			
    			dbHelper = new DatabaseHelper(AquariumDetailActivity.this);
    			db = dbHelper.getWritableDatabase();
    			db.update(AquariumTable.AquariumTable, aquariumParamsValues,AquariumColumns._ID+" = "+id,null);
    			if(dbHelper!=null){
    				dbHelper.close();
    			}
    			if(db!=null){
    				db.close();
    			}
    			
    		}
    	}
    	else{
	    	avgPhMin = (Float.parseFloat(avgFishPhMin) + Float.parseFloat(avgPlantPhMin))/2;
	    	avgPhMax = (Float.parseFloat(avgFishPhMax) + Float.parseFloat(avgPlantPhMax))/2;
	    	avgTMin = (Float.parseFloat(avgFishTMin) + Float.parseFloat(avgPlantTMin))/2;
	    	avgTMax = (Float.parseFloat(avgFishTMax) + Float.parseFloat(avgPlantTMax))/2;
	    	
	    	aquariumParamsValues.put(AquariumColumns.ColAquariumPhMin, avgPhMin);
			aquariumParamsValues.put(AquariumColumns.ColAquariumPhMax, avgPhMax);
			aquariumParamsValues.put(AquariumColumns.ColAquariumTmin, avgTMin);
			aquariumParamsValues.put(AquariumColumns.ColAquariumTmax, avgTMax);
			
			dbHelper = new DatabaseHelper(AquariumDetailActivity.this);
			db = dbHelper.getWritableDatabase();
			db.update(AquariumTable.AquariumTable, aquariumParamsValues,AquariumColumns._ID+" = "+id,null);
			if(dbHelper!=null){
				dbHelper.close();
			}
			if(db!=null){
				db.close();
			}
			
    	}
    	
    	onCreate(new Bundle());
    }
    
}
