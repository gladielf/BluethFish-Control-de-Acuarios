package com.pfc.bluethfish.control.acuarios.library.fish;


import java.lang.reflect.Field;

import com.pfc.bluethfish.control.acuarios.Constants;
import com.pfc.bluethfish.control.acuarios.adapters.MyFragmentContent;
import com.pfc.bluethfish.control.acuarios.data.DatabaseAdapter;
import com.pfc.bluethfish.control.acuarios.R;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Fermín Conejo
 *
 */

public class FishDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    
    DatabaseAdapter dbAdapter;
    MyFragmentContent.FreshwaterItem mItem;

    String id;
	String scientificName;
	String commonName; 
	String description; 
	String size; 
	String habitat;
	String maintenance; 
	String food;
	String aquariumSize;
	String genderDifference; 
	String reproduction; 
	String association;
	String waterCondition;
	String gHmin; 
	String gHmax; 
	String pHmin; 
	String pHmax;
	String Tmin; 
	String Tmax;
	String complexity; 
	String image;
    
    public FishDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	if (dbAdapter!=null){
			dbAdapter.close();
		}
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = MyFragmentContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fish_detail, container, false);
        //Aqui se ponen los datos detalle del fragment    
        if (mItem != null) {
        	
       	
        	dbAdapter = new DatabaseAdapter(getActivity());        	
        	dbAdapter.open();
        	Cursor cursor = dbAdapter.getCursorFreshwaterFishData(mItem.id); 
        	
			cursor.moveToFirst();
			
			id = cursor.getString(0);
			scientificName = cursor.getString(1);
			commonName = cursor.getString(2);
			description = cursor.getString(3); 
			size = cursor.getString(4); 
			habitat = cursor.getString(5);
			maintenance = cursor.getString(6); 
			food = cursor.getString(7);
			aquariumSize = cursor.getString(8);
			genderDifference = cursor.getString(9); 
			reproduction = cursor.getString(10); 
			association = cursor.getString(11);
			waterCondition = cursor.getString(12);
			gHmin = cursor.getString(13); 
			gHmax = cursor.getString(14);
			pHmin = cursor.getString(15);
			pHmax = cursor.getString(16);
			Tmin = cursor.getString(17);
			Tmax = cursor.getString(18);
			complexity = cursor.getString(19);
			image = cursor.getString(20);
			
			if (cursor != null){
				cursor.close();
				}
			if (dbAdapter !=null){
				dbAdapter.close();
				}
        			
        	((TextView) rootView.findViewById(R.id.textViewScientificNameDetail)).setText(scientificName);
        	((TextView) rootView.findViewById(R.id.textViewCommonNameDetail)).setText(commonName);
            ((TextView) rootView.findViewById(R.id.textViewDescriptionDetail)).setText(description);
            ((TextView) rootView.findViewById(R.id.textViewSizeDetail)).setText(size);
            ((TextView) rootView.findViewById(R.id.textViewHabitatDetail)).setText(habitat);
            ((TextView) rootView.findViewById(R.id.textViewMaintenanceDetail)).setText(maintenance);
            ((TextView) rootView.findViewById(R.id.textViewFoodDetail)).setText(food);
            ((TextView) rootView.findViewById(R.id.textViewAquSizeDetail)).setText(aquariumSize);
            ((TextView) rootView.findViewById(R.id.textViewGenderDifferenceDetail)).setText(genderDifference);
            ((TextView) rootView.findViewById(R.id.textViewReproductionDetail)).setText(reproduction);
            ((TextView) rootView.findViewById(R.id.textViewAssociationDetail)).setText(association);
            ((TextView) rootView.findViewById(R.id.textViewWaterDetail)).setText(waterCondition);
            ((TextView) rootView.findViewById(R.id.textViewGhMinDetail)).setText(gHmin);
            ((TextView) rootView.findViewById(R.id.textViewGhMaxDetail)).setText(gHmax);
            ((TextView) rootView.findViewById(R.id.textViewPhMinDetail)).setText(pHmin);
            ((TextView) rootView.findViewById(R.id.textViewPhMaxDetail)).setText(pHmax);
            ((TextView) rootView.findViewById(R.id.textViewTempMinDetail)).setText(Tmin);
            ((TextView) rootView.findViewById(R.id.textViewTempMaxDetail)).setText(Tmax);
            ((TextView) rootView.findViewById(R.id.textViewComplexityDetail)).setText(complexity);
            
            
            if(image != null){
            	
            	try {
            	    Field field = R.drawable.class.getField(image);
            	    int imageRes = field.getInt(null);
                    ((ImageView)rootView.findViewById(R.id.imageViewFish)).setImageResource(imageRes);
            	}
            	catch (Exception e) {
            	    Log.e(Constants.LOGTAG, "Fallo al obtener el id de la imagen.", e);
            	}    	

            }
            else{
            	((ImageView)rootView.findViewById(R.id.imageViewFish)).setImageResource(R.drawable.bluethfish_logo);
            }
            
            
        }
        return rootView;
    }
}
