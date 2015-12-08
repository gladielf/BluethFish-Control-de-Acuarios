package com.pfc.bluethfish.control.acuarios;

import com.pfc.bluethfish.control.acuarios.aquarium.AquariumsActivity;
import com.pfc.bluethfish.control.acuarios.data.DatabaseAdapter;
import com.pfc.bluethfish.control.acuarios.library.fish.FishOrderActivity;
import com.pfc.bluethfish.control.acuarios.library.plant.PlantOrderActivity;
import com.pfc.bluethfish.control.acuarios.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


/**
 * @author Fermín Conejo
 *
 *	Activity principal de la aplicación
 */
public class MainActivity extends Activity {
	DatabaseAdapter dbAdapter;
	Boolean mBound;
	Button buttonAquarium;
	Button buttonPlant;
	Button buttonFish;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        
        //Se abre la base de datos si no existe se crea y se cierra
        dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();
        if(dbAdapter!=null){
        	dbAdapter.close();
        }
        
    /* ****** MIS ACUARIOS ****** */
    buttonAquarium = (Button) findViewById(R.id.buttonAquarium);
    buttonAquarium.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			startActivity(new Intent(MainActivity.this,AquariumsActivity.class));
		}
	});
    
    /* ****** BIBLIOTECA DE PLANTAS ****** */
    buttonPlant = (Button) findViewById(R.id.buttonPlant);
    buttonPlant.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			startActivity(new Intent(MainActivity.this,PlantOrderActivity.class));
		}
	});
    
    
    /* ****** BIBLIOTECA DE PECES ****** */
    buttonFish = (Button) findViewById(R.id.buttonFish);
    buttonFish.setOnClickListener(new View.OnClickListener(){
    	@Override
    	public void onClick(View v){
    		startActivity(new Intent(MainActivity.this,FishOrderActivity.class));
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
		
		default:
			return super.onOptionsItemSelected(item);
		}	
    }
}