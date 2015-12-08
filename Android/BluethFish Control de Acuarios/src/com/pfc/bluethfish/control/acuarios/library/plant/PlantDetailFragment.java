package com.pfc.bluethfish.control.acuarios.library.plant;


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

public class PlantDetailFragment extends Fragment {
	
	public static final String ARG_ITEM_ID = "item_id";
	
    DatabaseAdapter dbAdapter;
    MyFragmentContent.FreshwaterItem mItem;
    
    String id;
    String scientificName;
	String commonName; 
	String description;
	String habitat;
	String maintenance; 
	String substratum;
	String aquariumPosition;
	String reproduction;
	String light;
	String size;
	String waterCondition;
	String gHmin; 
	String gHmax; 
	String pHmin; 
	String pHmax;
	String cO2min;
	String cO2max;
	String Tmin; 
	String Tmax;
	String complexity; 
	String image;

    public PlantDetailFragment(){
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
        View rootView = inflater.inflate(R.layout.plant_detail, container, false);
        //Aqui se ponen los datos detalle del fragment    
        if (mItem != null) {
        	dbAdapter = new DatabaseAdapter(getActivity());        	
        	dbAdapter.open();
        	Cursor cursor = dbAdapter.getCursorFreshwaterPlantData(mItem.id); 
        	
			cursor.moveToFirst();
			
			id = cursor.getString(0);
			scientificName = cursor.getString(1);
			commonName = cursor.getString(2);
			description = cursor.getString(3);
			habitat = cursor.getString(4);
			maintenance = cursor.getString(5); 
			substratum = cursor.getString(6);
			aquariumPosition = cursor.getString(7);
			reproduction = cursor.getString(8);
			light = cursor.getString(9);
			size = cursor.getString(10);
			waterCondition = cursor.getString(11);
			gHmin = cursor.getString(12); 
			gHmax = cursor.getString(13); 
			pHmin = cursor.getString(14); 
			pHmax = cursor.getString(15);
			cO2min = cursor.getString(16);
			cO2max = cursor.getString(17);
			Tmin = cursor.getString(18); 
			Tmax = cursor.getString(19);
			complexity = cursor.getString(20); 
			image = cursor.getString(21);
			
			
			if (cursor != null){
				cursor.close();
				}
			if (dbAdapter !=null){
				dbAdapter.close();
				}
			
			((TextView) rootView.findViewById(R.id.textViewPlantScientificNameDetail)).setText(scientificName);
			((TextView) rootView.findViewById(R.id.textViewPlantCommonNameDetail)).setText(commonName);
			((TextView) rootView.findViewById(R.id.textViewPlantDescriptionDetail)).setText(description);
			((TextView) rootView.findViewById(R.id.textViewPlantHabitatDetail)).setText(habitat);
			((TextView) rootView.findViewById(R.id.textViewPlantMaintenanceDetail)).setText(maintenance);
			((TextView) rootView.findViewById(R.id.textViewPlantSubstratumDetail)).setText(substratum);
			((TextView) rootView.findViewById(R.id.textViewPlantAquariumPositionDetail)).setText(aquariumPosition);
			((TextView) rootView.findViewById(R.id.textViewPlantReproductionDetail)).setText(reproduction);
			((TextView) rootView.findViewById(R.id.textViewPlantLightDetail)).setText(light);
			((TextView) rootView.findViewById(R.id.textViewPlantSizeDetail)).setText(size);
			((TextView) rootView.findViewById(R.id.textViewPlantWaterDetail)).setText(waterCondition);
			((TextView) rootView.findViewById(R.id.textViewPlantGhMinDetail)).setText(gHmin);
			((TextView) rootView.findViewById(R.id.textViewPlantGhMaxDetail)).setText(gHmax);
			((TextView) rootView.findViewById(R.id.textViewPlantPhMinDetail)).setText(pHmin);
			((TextView) rootView.findViewById(R.id.textViewPlantPhMaxDetail)).setText(pHmax);
			((TextView) rootView.findViewById(R.id.textViewPlantCo2MinDetail)).setText(cO2min);
			((TextView) rootView.findViewById(R.id.textViewPlantCo2MaxDetail)).setText(cO2max);
			((TextView) rootView.findViewById(R.id.textViewPlantTempMinDetail)).setText(Tmin);
			((TextView) rootView.findViewById(R.id.textViewPlantTempMaxDetail)).setText(Tmax);
			((TextView) rootView.findViewById(R.id.textViewPlantComplexityDetail)).setText(complexity);


			if(image != null){
            	
            	try {
            	    Field field = R.drawable.class.getField(image);
            	    int imageRes = field.getInt(null);
                    ((ImageView)rootView.findViewById(R.id.imageViewPlant)).setImageResource(imageRes);
            	}
            	catch (Exception e) {
            	    Log.e(Constants.LOGTAG, "Fallo al obtener el id de la imagen.", e);
            	}    	

            }
            else{
            	((ImageView)rootView.findViewById(R.id.imageViewPlant)).setImageResource(R.drawable.bluethfish_logo);
            }

			
        }
        return rootView;
    }
}
