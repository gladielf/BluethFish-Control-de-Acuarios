package com.pfc.bluethfish.control.acuarios.adapters;

import java.util.ArrayList;

import com.pfc.bluethfish.control.acuarios.Constants;
import com.pfc.bluethfish.control.acuarios.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * @author Fermín Conejo
 *
 */

public class MyListViewAdapter extends BaseAdapter {

	private ArrayList<String> listScName;
	private ArrayList<String> listCmName;
	private ArrayList<Integer> listImage;
	private ArrayList<String> listNumber;
	
	private Activity activity;
	private int type;
	
	public MyListViewAdapter(Activity activity, ArrayList<String> scName, ArrayList<String> cmName, ArrayList<Integer> image){
		super();
		this.activity = activity;
		this.listScName = scName;
		this.listCmName = cmName; 
		this.listImage = image;	
	}
	
	public MyListViewAdapter(Activity activity, ArrayList<String> scName,ArrayList<String> cmName, ArrayList<String> number, int type){
		super();
		this.activity = activity;
		this.listScName = scName;
		this.listCmName = cmName; 	
		this.listNumber = number;
		this.type = type;
	}
	
	public MyListViewAdapter(Activity activity, ArrayList<String> file,int type){
		super();
		this.activity = activity;
		this.listScName = file; // El array con la lista de fichero es asignado al array llamado listScName ya que es el array principal
		this.type = type;
		
	}
	@Override
	public int getCount() {
		return listScName.size();
	}

	@Override
	public String getItem(int position) {
		return listScName.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	
	public static class ViewHolder{
		public TextView txtviewScName;
		public TextView txtviewCmName;
		public ImageView imgview;
		public TextView txtviewNumber;
		public TextView txtviewFile;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder view;
		LayoutInflater inflater = activity.getLayoutInflater();
		if(listImage != null){
		
			if(convertView == null){
				
				view = new ViewHolder();
				convertView = inflater.inflate(R.layout.listview_add_row, null);
				
				view.txtviewScName = (TextView)convertView.findViewById(R.id.textViewAddScName);
				view.txtviewCmName = (TextView)convertView.findViewById(R.id.textViewAddCmName);
				view.imgview = (ImageView)convertView.findViewById(R.id.imageViewList);
				
				convertView.setTag(view);
			}
			else {
				
				view = (ViewHolder) convertView.getTag();
			}
			
			view.txtviewScName.setText(listScName.get(position));
			view.txtviewCmName.setText(listCmName.get(position));
			view.imgview.setImageResource(listImage.get(position));
		}
		else if(listNumber != null){
			
			if (convertView == null){
			
				view = new ViewHolder();
				convertView = inflater.inflate(R.layout.listview_list_row, null);
				
				view.txtviewScName = (TextView)convertView.findViewById(R.id.textViewListScName);
				view.txtviewCmName = (TextView)convertView.findViewById(R.id.textViewListCmName);
				view.txtviewNumber = (TextView)convertView.findViewById(R.id.textViewListNumber);
				convertView.setTag(view);
			}
			else {
				
				view = (ViewHolder) convertView.getTag();
			}
			
			view.txtviewScName.setText(listScName.get(position));
			view.txtviewCmName.setText(listCmName.get(position));
			switch(type){
				case Constants.FISH:
					view.txtviewNumber.setText(activity.getString(R.string.dialog_fish_number) + " " + listNumber.get(position));
					break;
				case Constants.PLANT:
					view.txtviewNumber.setText(activity.getString(R.string.dialog_plant_number) + " " + listNumber.get(position));
					break;
				default:
					break;
			}
			
			
		}else if(type == Constants.GRAPHIC){
		
			if(convertView == null){
				
				view = new ViewHolder();
				convertView = inflater.inflate(R.layout.listview_graphic_row, null);
				
				view.txtviewFile= (TextView)convertView.findViewById(R.id.textViewGraphicRow);
				
				convertView.setTag(view);
			}
			else {
				
				view = (ViewHolder) convertView.getTag();
			}
			
			view.txtviewFile.setText(listScName.get(position));

		}
		
		return convertView;
	}
	
}