package com.pfc.bluethfish.control.acuarios.adapters;

import java.util.ArrayList;

import com.pfc.bluethfish.control.acuarios.Constants;
import com.pfc.bluethfish.control.acuarios.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class MyGridViewAdapter extends BaseAdapter {
	
	private ArrayList<String> list;
	private ArrayList<Integer> listimage;
	private ArrayList<String> listExternalImage;
	private Activity activity;
	
	public MyGridViewAdapter(Activity activity, ArrayList<String> list, ArrayList<Integer> listimage, ArrayList<String> listexternalimage){
		super();
		this.activity=activity;
		this.list=list;
		this.listimage=listimage;
		this.listExternalImage = listexternalimage;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public String getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public static class ViewHolder{
		public ImageView imgview;
		public TextView txtview;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder view;
		LayoutInflater inflater = activity.getLayoutInflater();
		
		if(convertView == null){
			
			view = new ViewHolder();
			convertView = inflater.inflate(R.layout.gridview_row, null);
			
			view.imgview = (ImageView) convertView.findViewById(R.id.gridViewImage);
			view.txtview = (TextView) convertView.findViewById(R.id.gridViewText);
			
			
			convertView.setTag(view);
			
		}
		else{
			
			view= (ViewHolder) convertView.getTag();
		}
		
		view.txtview.setText(list.get(position));
		if (listimage != null){
			view.imgview.setImageResource(listimage.get(position));
		}else if(listExternalImage != null){
			
			if(listExternalImage.get(position) == Constants.BLUETHFISH_ADD){
				Bitmap b = BitmapFactory.decodeResource(convertView.getResources(), R.drawable.bluethfish_add);
				view.imgview.setImageBitmap(b);
			} else{
				Bitmap bm = BitmapFactory.decodeFile(listExternalImage.get(position));
				view.imgview.setImageBitmap(bm);
			}
		}
		return convertView;
	}

}
