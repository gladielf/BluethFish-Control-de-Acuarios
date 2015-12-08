package com.pfc.bluethfish.control.acuarios.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pfc.bluethfish.control.acuarios.data.DatabaseAdapter;

import android.content.Context;
import android.database.Cursor;



/**
 * @author Fermín Conejo
 *
 */

public class MyFragmentContent {

    public static class FreshwaterItem {

        public String id;
        public String stringA;
        public String stringB;

        
        public FreshwaterItem(String id, String stringA ,String stringB){
        	this.id = id;
        	this.stringA = stringA;
        	this.stringB = stringB;
        }
        
           
        public String toString(String param) {
            return param;
        }
    }

    public static List<FreshwaterItem> ITEMS = new ArrayList<FreshwaterItem>();
    public static Map<String, FreshwaterItem> ITEM_MAP = new HashMap<String, FreshwaterItem>();

    static DatabaseAdapter dbAdapter;
    public static void setContextFishName(Context context,String family) {
    		
    		ITEMS.clear();
    	 	dbAdapter = new DatabaseAdapter(context);        	
        	dbAdapter.open();
            Cursor cursor = dbAdapter.getCursorFreshwaterFishName(family); // database query

            if (cursor.moveToFirst()) {
                do {
                	FreshwaterItem item = new FreshwaterItem(cursor.getString(0), cursor.getString(1), cursor.getString(2));
                    addItem(item);
                } while (cursor.moveToNext());
            }
            
            if(cursor != null){
            	cursor.close();
            }
            if (dbAdapter !=null){
				dbAdapter.close();
				}    
            
    }
    
    
    public static void setContextPlantName(Context context,String family) {
		
		ITEMS.clear();
	 	dbAdapter = new DatabaseAdapter(context);        	
    	dbAdapter.open();
        Cursor cursor = dbAdapter.getCursorFreshwaterPlantName(family); // database query

        if (cursor.moveToFirst()) {
            do {
            	FreshwaterItem item = new FreshwaterItem(cursor.getString(0), cursor.getString(1), cursor.getString(2));
                addItem(item);
            } while (cursor.moveToNext());
        }
        
        if(cursor != null){
        	cursor.close();
        }
        if (dbAdapter !=null){
			dbAdapter.close();
			}    
        
    }


    private static void addItem(FreshwaterItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }
}
