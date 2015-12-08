package com.pfc.bluethfish.control.acuarios.library.fish;

import com.pfc.bluethfish.control.acuarios.adapters.MyFragmentContent;
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

public class FishListActivity extends FragmentActivity
        implements FishListFragment.Callbacks {

    private boolean mTwoPane;
    DatabaseAdapter dbAdapter;
       
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	if (dbAdapter!=null){
			dbAdapter.close();
		}
    	String family = getIntent().getStringExtra("family");
    	MyFragmentContent.setContextFishName(this, family);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fish_list);
        
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.action_bar_fish_library_fish) + " " + family);

        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
            ((FishListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.item_list))
                    .setActivateOnItemClick(true);
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
			NavUtils.navigateUpTo(this, new Intent(this, FishFamilyActivity.class));
            return true;
		default:
			return super.onOptionsItemSelected(item);
		}
    	
    }

    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(FishDetailFragment.ARG_ITEM_ID, id);
            FishDetailFragment fragment = new FishDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();



        } else {
            Intent detailIntent = new Intent(this, FishDetailActivity.class);
            detailIntent.putExtra(FishDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
    
	

}
