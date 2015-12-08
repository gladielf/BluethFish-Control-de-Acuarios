package com.pfc.bluethfish.control.acuarios.library.fish;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.pfc.bluethfish.control.acuarios.Constants;
import com.pfc.bluethfish.control.acuarios.MainActivity;
import com.pfc.bluethfish.control.acuarios.adapters.MyGridViewAdapter;
import com.pfc.bluethfish.control.acuarios.data.DatabaseAdapter;
import com.pfc.bluethfish.control.acuarios.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * @author Fermín Conejo
 *
 */

public class FishOrderActivity extends Activity {
//En este activity ira la gridview para los Order de los peces
	
	private MyGridViewAdapter adapter;
	private ArrayList<String> listorder;
	private ArrayList<Integer> listimage;
	DatabaseAdapter dbAdapter;
	
	private GridView gridView;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if (dbAdapter!=null){
			dbAdapter.close();
		}
		setContentView(R.layout.gridview_layout);
			
		ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.action_bar_fish_library_order);
        
		fillGrid();
		
		adapter = new MyGridViewAdapter(this, listorder, listimage, null);
		
		gridView = (GridView) findViewById(R.id.gridView);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				
				Intent intent = new Intent(FishOrderActivity.this,FishFamilyActivity.class);
				intent.putExtra("order", adapter.getItem(position));
				
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
	    
	    
	public void fillGrid() {
		// llenamos el gridview con los datos de la base de datos
		
		
		listorder = new ArrayList<String>();
		listimage = new ArrayList<Integer>();
		
		String nombre;
		String image;
		
		dbAdapter = new DatabaseAdapter(this);        	
    	dbAdapter.open();
    	Cursor cursor= dbAdapter.getCursorFreshwaterFishOrder();
    	
    	if(cursor.moveToFirst()){
    		
    		do{
    			nombre= cursor.getString(1);
    			image = cursor.getString(2);
    			
    			listorder.add(nombre);
    					
    			if(image!=null){
    				try{
    					Field field = R.drawable.class.getField(image);
                	    int imageRes = field.getInt(field);

                	    listimage.add(imageRes);
    				}
    				catch (Exception e) {
                	    Log.e(Constants.LOGTAG, "Fallo al obtener el id de la imagen.", e);
                	}
    			
    			}else{
    				listimage.add(R.drawable.bluethfish_logo);
    			}
    			
    		}while(cursor.moveToNext());
    	}
    	
    	if(cursor!=null){
    		cursor.close();
    		}
    	if(dbAdapter!=null){
    		dbAdapter.close();
    	}
	}
}