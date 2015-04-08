package com.example.mapengineering.view;

import java.util.List;

import com.example.mapengineering.R;
import com.example.mapengineering.model.DataDetailModel;
import com.example.mapengineering.model.DataModel;
import com.example.mapengineering.view.DetaildataViewAdapter.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UnCompleteDataAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private List<DataModel> dataList;
	private Context context;
	
	public UnCompleteDataAdapter(Context context, List<DataModel> data){
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		dataList = data;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 ViewHolder holder = null;
		 if (convertView == null) {
             
             holder=new ViewHolder();  
              
             convertView = mInflater.inflate(R.layout.unmeasure_data, null);
             holder.startTime = (TextView)convertView.findViewById(R.id.unstart_time);
             holder.startPoint = (TextView)convertView.findViewById(R.id.unstart_point);
             holder.delete = (TextView)convertView.findViewById(R.id.delete_unmeasureData);
         
             convertView.setTag(holder);
              
         }else {    
             holder = (ViewHolder)convertView.getTag();
         }
		 
		 final String ID = dataList.get(position).getID();
		 String startTime = dataList.get(position).getStartTime();
		 String startPoint = dataList.get(position).getStartPoint();
		 
		 holder.startPoint.setText(startPoint);
		 holder.startTime.setText(startTime);
		 
		 holder.delete.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent("UNCOMPLETE_DELETE");
					intent.putExtra("CompleteID", ID);
					context.sendBroadcast(intent);
				}
			});
		 
		 return convertView;
	}

	  public final class ViewHolder{
	        public TextView startTime;
	        public TextView startPoint;
	        public TextView delete;
	    }
}
