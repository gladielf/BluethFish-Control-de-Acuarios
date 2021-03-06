package com.pfc.bluethfish.control.acuarios.library.plant;

import com.pfc.bluethfish.control.acuarios.data.DatabaseAdapter;
import com.pfc.bluethfish.control.acuarios.R;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

/**
 * @author Fermín Conejo
 *
 */

public class PlantDetailActivity extends FragmentActivity {

	DatabaseAdapter dbAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	if (dbAdapter!=null){
			dbAdapter.close();
		}
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail);
        
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.action_bar_plant_library_plant_detail));

        
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(PlantDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(PlantDetailFragment.ARG_ITEM_ID));
            PlantDetailFragment fragment = new PlantDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.plant_detail_container, fragment)
                    .commit();
        }
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
			NavUtils.navigateUpTo(this, new Intent(this, PlantListActivity.class));
            return true;
		default:
			return super.onOptionsItemSelected(item);
		}
    }	
    
	
}
