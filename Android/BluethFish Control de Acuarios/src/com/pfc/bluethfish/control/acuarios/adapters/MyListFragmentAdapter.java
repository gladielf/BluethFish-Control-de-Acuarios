package com.pfc.bluethfish.control.acuarios.adapters;

import java.util.List;

import com.pfc.bluethfish.control.acuarios.adapters.MyFragmentContent.FreshwaterItem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author Fermín Conejo
 *
 */

public class MyListFragmentAdapter extends ArrayAdapter<FreshwaterItem> {

	private Context context;
	private int layoutResourceId;   
	private List<FreshwaterItem> objects = null;

	public MyListFragmentAdapter(Context context, int layoutResourceId, List<FreshwaterItem> objects) {
	    super(context, layoutResourceId, objects);
	    this.context = context;
	    this.layoutResourceId = layoutResourceId;
	    this.objects = objects;
	}

	public View getView(int position, View convertView, ViewGroup parent)  {
	    if(convertView == null)
	    {
	        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	        convertView = inflater.inflate(layoutResourceId, parent, false);
	    }

	    FreshwaterItem item = objects.get(position);
	    TextView titletext = (TextView)convertView.findViewById(android.R.id.text1);
	    titletext.setText(item.toString(item.stringA));
	    TextView titletext2 = (TextView)convertView.findViewById(android.R.id.text2);
	    titletext2.setText(item.toString(item.stringB));
	    return convertView;
	}

}

