package com.pfc.bluethfish.control.acuarios.aquarium;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.pfc.bluethfish.control.acuarios.Constants;
import com.pfc.bluethfish.control.acuarios.MainActivity;
import com.pfc.bluethfish.control.acuarios.adapters.MyGridViewAdapter;
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
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author Fermín Conejo
 *
 */

public class AquariumsActivity extends Activity {

	private MyGridViewAdapter adapter;
	private ArrayList<String> listname;
	private ArrayList<String> listimage;
	ArrayList<Integer> listid;
	SQLiteDatabase db;
	DatabaseAdapter dbAdapter;
	DatabaseHelper dbHelper;
	private GridView gridView;
	int aquariumId;
	ImageView imageAquarium;
	String picturePath = null;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if (dbAdapter!=null){
			dbAdapter.close();
		}
		setContentView(R.layout.gridview_layout);
		
		listid = new ArrayList<Integer>();
		
		ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.action_bar_aquarium);
		
		
		fillGrid();
		
		adapter = new MyGridViewAdapter(this, listname, null, listimage);
		
		gridView = (GridView) findViewById(R.id.gridView);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {	
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				
				if(adapter.getItem(position) == getResources().getString(R.string.button_add_aquarium)){
				
					addAquarium();
					
				}else{
					aquariumId= listid.get(position);
					Intent intent = new Intent(AquariumsActivity.this,AquariumDetailActivity.class);
					intent.putExtra("AquariumID",""+ aquariumId);
					startActivity(intent); 
				}
			}
		});

		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0,View argi1, int position, long arg3){
				
				if(adapter.getItem(position) == getResources().getString(R.string.button_add_aquarium)){
					
					addAquarium();
					
				}else{
					aquariumId=listid.get(position);
					
					String[] options ={getResources().getString(R.string.dialog_title_update_aquarium),
									   getResources().getString(R.string.dialog_title_remove_aquarium)};
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AquariumsActivity.this);
					alertDialogBuilder.setTitle(R.string.dialog_title_aquarium_options);
					alertDialogBuilder
						.setCancelable(true)
						.setItems(options,new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int pos) {
								
								switch (pos) {
								case 0:
									updateAquarium(aquariumId);
									break;
								case 1:
									removeAquarium(aquariumId);
									break;

								default:
									break;
								}
								
							}
						} ).setNeutralButton(R.string.button_cancel,new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();								
							}
						});
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				}
				return true;
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
			NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
            return true;
		default:
			return super.onOptionsItemSelected(item);
		}
    	
    }
   
	@Override
   	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
   		super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 
   		switch(requestCode) {
   		case Constants.IMAGE_FROM_GALLERY:
   		    if(resultCode == RESULT_OK){  
   		        Uri selectedImage = imageReturnedIntent.getData();
   		        String[] filePathColumn = { MediaStore.Images.Media.DATA };
   		        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
   		        cursor.moveToFirst();
   		        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
   		        picturePath = cursor.getString(columnIndex);
   		        cursor.close();
   		        imageAquarium.setImageURI(selectedImage);
   		    }
   		    break; 
   		case Constants.IMAGE_FROM_CAMERA:	
   		    if(resultCode == RESULT_OK){  
   		        Uri selectedPhoto = null;
   		        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] {Media.DATA, Media.DATE_ADDED, MediaStore.Images.ImageColumns.ORIENTATION}, Media.DATE_ADDED, null, "date_added DESC");
   		        cursor.moveToFirst();
   		        selectedPhoto = Uri.parse(cursor.getString(cursor.getColumnIndex(Media.DATA)));
   		        picturePath = selectedPhoto.toString();
   		        cursor.close();
   		        imageAquarium.setImageURI(selectedPhoto);
   		    }
   		    break;
   		}
   	}
    
    public void fillGrid() {
    	
    	listname = new ArrayList<String>();
		listimage = new ArrayList<String>();
		
		String name;
		String image;
		int id;
		
		listname.clear();
		listimage.clear();
		
		dbAdapter = new DatabaseAdapter(this);        	
    	dbAdapter.open();
    	Cursor cursor= dbAdapter.getCursorAquariumsName();
    	
    	if(cursor.moveToFirst()){
    		
    		do{
    			id = Integer.parseInt(cursor.getString(0).trim());
    			name= cursor.getString(1);
    			image = cursor.getString(2);
    			
    			listid.add(id);
    			listname.add(name);
    			
    			if(image!=null){
    				try{
    					listimage.add(image);
    				}
    				catch (Exception e) {
                	    Log.e(Constants.LOGTAG, "Fallo al obtener el id de la imagen.", e);
                	}
    			
    			}else{
    				listimage.add(null);
    			}
    			
    		}while(cursor.moveToNext());
    	}
    	
    	if(cursor!=null){
    		cursor.close();
    		}
    	if(dbAdapter!=null){
    		dbAdapter.close();
    	}
    	
    	/**
    	 * tras listar todos los acuarios se añade un elemento mas que sera 
    	 * el boton para añadir un nuevo acuario
    	 */
    	listname.add(getResources().getString(R.string.button_add_aquarium));
    	listimage.add(Constants.BLUETHFISH_ADD);
    }
   
    
    public void addAquarium(){
    	
    	final Dialog dialogAddAquarium = new Dialog(AquariumsActivity.this);
        dialogAddAquarium.setTitle(R.string.dialog_title_add_aquarium);
        dialogAddAquarium.setContentView(R.layout.dialog_add_update_aquarium);
        dialogAddAquarium.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ImageButton buttonAddFromGallery = (ImageButton) dialogAddAquarium.findViewById(R.id.imageButtonGallery);
        ImageButton buttonAddFromCamera = (ImageButton) dialogAddAquarium.findViewById(R.id.imageButtonCamera);
        Button buttonAquariumAccept = (Button) dialogAddAquarium.findViewById(R.id.button_add_aquarium_accept);
        Button buttonAquariumCancel = (Button) dialogAddAquarium.findViewById(R.id.button_add_aquarium_cancel);
        
        picturePath = null;
        imageAquarium = (ImageView) dialogAddAquarium.findViewById(R.id.dialogImageView);
        dbHelper = new DatabaseHelper(this);
        
        final ContentValues aquariumValues = new ContentValues();
        
        final EditText aquariumName = (EditText)dialogAddAquarium.findViewById(R.id.editTextDialogAquariumName);
        final EditText aquariumHeight = (EditText)dialogAddAquarium.findViewById(R.id.editTextDialogAquariumHeight);
        final EditText aquariumLength = (EditText)dialogAddAquarium.findViewById(R.id.editTextDialogAquariumLength);
        final EditText aquariumWidth = (EditText)dialogAddAquarium.findViewById(R.id.editTextDialogAquariumWidth);
        final TimePicker aquariumLightOn = (TimePicker)dialogAddAquarium.findViewById(R.id.timePickerDialogAquariumLightOn);
        final TimePicker aquariumLightOff = (TimePicker)dialogAddAquarium.findViewById(R.id.timePickerDialogAquariumLightOff);
        
       	aquariumLightOn.setIs24HourView(true);
       	aquariumLightOn.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
       	aquariumLightOn.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
       
       	aquariumLightOff.setIs24HourView(true);
       	aquariumLightOff.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
       	aquariumLightOff.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
       
       	buttonAddFromGallery.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				Intent intentGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intentGallery, Constants.IMAGE_FROM_GALLERY);	
			}
		});
       	
       	
       	buttonAddFromCamera.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intentCamera, Constants.IMAGE_FROM_CAMERA);
			}
		});

     	
        /**
         * BOTON ACEPTAR el dialog se cierra y guarda los datos añadidos por el usuario
         * a la base de datos 
         * 
         * para sacar los listros 1º se sacan los cm3 (Alto*Largo*Ancho) y se divide por 1000 eso da los litros
         */
        buttonAquariumAccept.setOnClickListener(new View.OnClickListener() 
        {	
			@Override
			public void onClick(View v) {
				
				String name = aquariumName.getText().toString();
				String aqHeight = aquariumHeight.getText().toString();
				String aqLength = aquariumLength.getText().toString();
				String aqWidth = aquariumWidth.getText().toString();
				String lightOn = String.format(Locale.getDefault(),"%02d:%02d",aquariumLightOn.getCurrentHour() , aquariumLightOn.getCurrentMinute());
				String lightOff = String.format(Locale.getDefault(),"%02d:%02d", aquariumLightOff.getCurrentHour() , aquariumLightOff.getCurrentMinute());
				
				if(name == null || name.isEmpty() || aqHeight == null ||aqHeight.isEmpty() || aqLength == null ||aqLength.isEmpty() || aqWidth == null ||aqWidth.isEmpty()){
					Toast.makeText(AquariumsActivity.this, R.string.dialog_add_aquarium_empty_fields , Toast.LENGTH_SHORT).show();
				}
				else if (picturePath == null){
					Toast.makeText(AquariumsActivity.this, R.string.dialog_add_aquarium_empty_picture , Toast.LENGTH_SHORT).show();
				}
				else{
					
					float height = Integer.parseInt(aqHeight.trim());
					float length = Integer.parseInt(aqLength.trim());
					float width = Integer.parseInt(aqWidth.trim());
					
					db = dbHelper.getWritableDatabase();
					aquariumValues.put(AquariumColumns.ColAquariumName, name);
					aquariumValues.put(AquariumColumns.ColAquariumHeight,aqHeight);
					aquariumValues.put(AquariumColumns.ColAquariumLength,aqLength);
					aquariumValues.put(AquariumColumns.ColAquariumWidth,aqWidth);
					float liters= height * length * width / Constants.cm3toL ;
					aquariumValues.put(AquariumColumns.ColAquariumLiters,liters );
					aquariumValues.put(AquariumColumns.ColAquariumLightOn,lightOn);
					aquariumValues.put(AquariumColumns.ColAquariumLightOff,lightOff);
					aquariumValues.put(AquariumColumns.ColAquariumImage, picturePath);
						
					db.insert(AquariumTable.AquariumTable,null , aquariumValues);
					
					if(dbHelper!=null){
						dbHelper.close();
					}
					if(db!=null){
						db.close();
					}
					dialogAddAquarium.dismiss();
					onCreate(new Bundle());
				}
			}
		});
        
        /**
         * BOTON CANCELAR el dialog se cierra sin guardar ningun cambio
         */
        buttonAquariumCancel.setOnClickListener(new View.OnClickListener() 
        {	
			@Override
			public void onClick(View v) {
				dialogAddAquarium.cancel();
			}
		});
        dialogAddAquarium.show();
    }
 
    
    public void updateAquarium(final int id) {
    	
    	final Dialog dialogUpdateAquarium = new Dialog(AquariumsActivity.this);
        dialogUpdateAquarium.setTitle(R.string.dialog_title_update_aquarium);
        dialogUpdateAquarium.setContentView(R.layout.dialog_add_update_aquarium);
        dialogUpdateAquarium.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ImageButton buttonUpdateFromGallery = (ImageButton) dialogUpdateAquarium.findViewById(R.id.imageButtonGallery);
        ImageButton buttonUpdateFromCamera = (ImageButton) dialogUpdateAquarium.findViewById(R.id.imageButtonCamera);
        Button buttonAquariumUpdateAccept = (Button) dialogUpdateAquarium.findViewById(R.id.button_add_aquarium_accept);
        Button buttonAquariumUpdateCancel = (Button) dialogUpdateAquarium.findViewById(R.id.button_add_aquarium_cancel);
        
        dbHelper = new DatabaseHelper(this);
        imageAquarium = (ImageView) dialogUpdateAquarium.findViewById(R.id.dialogImageView);
        
        final ContentValues aquariumUpdateValues = new ContentValues();
        
        
		final EditText aquariumName = (EditText)dialogUpdateAquarium.findViewById(R.id.editTextDialogAquariumName);
        final EditText aquariumHeight = (EditText)dialogUpdateAquarium.findViewById(R.id.editTextDialogAquariumHeight);
        final EditText aquariumLength = (EditText)dialogUpdateAquarium.findViewById(R.id.editTextDialogAquariumLength);
        final EditText aquariumWidth = (EditText)dialogUpdateAquarium.findViewById(R.id.editTextDialogAquariumWidth);
        final TimePicker aquariumLightOn = (TimePicker)dialogUpdateAquarium.findViewById(R.id.timePickerDialogAquariumLightOn);
        final TimePicker aquariumLightOff = (TimePicker)dialogUpdateAquarium.findViewById(R.id.timePickerDialogAquariumLightOff);
    	
        SimpleDateFormat sdf= new SimpleDateFormat("HH:mm",java.util.Locale.getDefault());
        Date oldDateOn = null;
        Date oldDateOff = null;
        
    	String oldName;
    	String oldHeight;
		String oldLength;
		String oldWidth;
		String oldLightOn;
		String oldLightOff;
		String oldImage;
		
		dbAdapter = new DatabaseAdapter(this);        	
    	dbAdapter.open();
    	Cursor cursor = dbAdapter.getCursorAquariumUpdate(Integer.toString(id)); 
    	
		cursor.moveToFirst();
			
		oldName = cursor.getString(1);
		oldHeight = cursor.getString(2);
		oldLength = cursor.getString(3);
		oldWidth = cursor.getString(4);
		oldLightOn = cursor.getString(5);
		oldLightOff = cursor.getString(6);
		oldImage = cursor.getString(7);

		if (cursor != null){
			cursor.close();
			}
		if (dbAdapter !=null){
			dbAdapter.close();
			}
		
		aquariumName.setText(oldName);
		aquariumHeight.setText(oldHeight);
		aquariumLength.setText(oldLength);
		aquariumWidth.setText(oldWidth);
		
       	aquariumLightOn.setIs24HourView(true);
       	aquariumLightOn.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
       	
        try {
            oldDateOn = sdf.parse(oldLightOn);
        } catch (ParseException e) {
        }
        Calendar calendarOn = Calendar.getInstance();
        calendarOn.setTime(oldDateOn);
       	aquariumLightOn.setCurrentHour(calendarOn.get(Calendar.HOUR_OF_DAY));
       	aquariumLightOn.setCurrentMinute(calendarOn.get(Calendar.MINUTE));
       
       
       	aquariumLightOff.setIs24HourView(true);
       	aquariumLightOff.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
       	try {
            oldDateOff = sdf.parse(oldLightOff);
        } catch (ParseException e) {
        }
        Calendar calendarOff = Calendar.getInstance();
        calendarOff.setTime(oldDateOff);
       	aquariumLightOff.setCurrentHour(calendarOff.get(Calendar.HOUR_OF_DAY));
       	aquariumLightOff.setCurrentMinute(calendarOff.get(Calendar.MINUTE));
       	imageAquarium.setImageURI(Uri.parse(oldImage));

       	buttonUpdateFromGallery.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intentGallery, Constants.IMAGE_FROM_GALLERY);
			}
		});
       	
       	
       	buttonUpdateFromCamera.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intentCamera, Constants.IMAGE_FROM_CAMERA);
			}
		});

    	
        /**
         * BOTON ACEPTAR el dialog se cierra y se actualizan los datos de la base datos
         */
        buttonAquariumUpdateAccept.setOnClickListener(new View.OnClickListener() 
        {	
			@Override
			public void onClick(View v) {
				
				String newName = aquariumName.getText().toString();
				String newHeight = aquariumHeight.getText().toString();
				String newLength = aquariumLength.getText().toString();
				String newWidth = aquariumWidth.getText().toString();
				String newLightOn = String.format(Locale.getDefault(),"%02d:%02d" ,aquariumLightOn.getCurrentHour() , aquariumLightOn.getCurrentMinute());
				String newLightOff =  String.format(Locale.getDefault(),"%02d:%02d",aquariumLightOff.getCurrentHour(), aquariumLightOff.getCurrentMinute());

				if(newName == null || newName.isEmpty() || newHeight == null ||newHeight.isEmpty() || newLength == null ||newLength.isEmpty() || newWidth == null ||newWidth.isEmpty()){
					Toast.makeText(AquariumsActivity.this, R.string.dialog_add_aquarium_empty_fields , Toast.LENGTH_SHORT).show();
				}
				else{
					
					float height = Integer.parseInt(newHeight.trim());
					float length = Integer.parseInt(newLength.trim());
					float width = Integer.parseInt(newWidth.trim());
					
					db = dbHelper.getWritableDatabase();
					aquariumUpdateValues.put(AquariumColumns.ColAquariumName, newName);
					aquariumUpdateValues.put(AquariumColumns.ColAquariumHeight,newHeight);
					aquariumUpdateValues.put(AquariumColumns.ColAquariumLength,newLength);
					aquariumUpdateValues.put(AquariumColumns.ColAquariumWidth,newWidth);
					float newLiters= height * length * width / Constants.cm3toL ;
					aquariumUpdateValues.put(AquariumColumns.ColAquariumLiters, newLiters );
					aquariumUpdateValues.put(AquariumColumns.ColAquariumLightOn,newLightOn);
					aquariumUpdateValues.put(AquariumColumns.ColAquariumLightOff,newLightOff);
					aquariumUpdateValues.put(AquariumColumns.ColAquariumImage, picturePath);
					db.update(AquariumTable.AquariumTable, aquariumUpdateValues, AquariumColumns._ID +" = "+ id,null);
					
					if(dbHelper!=null){
						dbHelper.close();
					}
					if(db!=null){
						db.close();
					}
				
				
				dialogUpdateAquarium.dismiss();
				onCreate(new Bundle());
				}
			}
		});
        
        
        
        /**
         * BOTON CANCELAR el dialog se cierra sin guardar ningun cambio
         */
        buttonAquariumUpdateCancel.setOnClickListener(new View.OnClickListener() 
        {	
			@Override
			public void onClick(View v) {
				dialogUpdateAquarium.cancel();
			}
		});
        dialogUpdateAquarium.show();
	}
    
    
    
    public void removeAquarium(final int id) {
    	
    	String aquariumName;
    	
        dbHelper = new DatabaseHelper(this);
		dbAdapter = new DatabaseAdapter(this);        	
    	dbAdapter.open();
    	Cursor cursor = dbAdapter.getCursorAquariumDeleteName(Integer.toString(id)); 
    	
		cursor.moveToFirst();
			
		aquariumName = cursor.getString(1);

		if (cursor != null){
			cursor.close();
			}
		if (dbAdapter !=null){
			dbAdapter.close();
			}
    	
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AquariumsActivity.this);
		alertDialogBuilder.setTitle(R.string.dialog_title_remove_aquarium);
		alertDialogBuilder
			.setCancelable(true)
			.setMessage(getResources().getString(R.string.dialog_remove_aquarium).toString() + " " + aquariumName +" ?")
			.setNegativeButton(R.string.button_cancel,new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();								
				}
			})
			.setPositiveButton(R.string.button_accept, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					db=dbHelper.getWritableDatabase();
					//Se borra la lista de peces del aucario
					db.delete(AquariumFishListTable.FishListTable,
							AquariumFishListColumns.ColFishListAquariumId + " = " + id,
							null);
					//Se borra la lista de plantas del aucario
					db.delete(AquariumPlantListTable.PlantListTable,
							AquariumPlantListColumns.ColPlantListAquariumId + " = " + id,
							null);
					//se borra el acuario
					db.delete(AquariumTable.AquariumTable, AquariumColumns._ID +" = "+ id, null);
					if(dbHelper!=null){
						dbHelper.close();
					}
					if(db!=null){
						db.close();
					}
					onCreate(new Bundle());
				}
			});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();	
	}
}