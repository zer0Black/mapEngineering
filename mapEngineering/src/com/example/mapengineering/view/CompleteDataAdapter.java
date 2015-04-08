package com.example.mapengineering.view;

import java.util.List;

import com.example.mapengineering.R;
import com.example.mapengineering.model.DataDetailModel;
import com.example.mapengineering.model.DataModel;
import com.example.mapengineering.view.DetaildataViewAdapter.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CompleteDataAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private List<DataModel> dataList;
	Context context;
	
	public CompleteDataAdapter(Context context, List<DataModel> data){
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
              
             convertView = mInflater.inflate(R.layout.measure_data, null);
             holder.startTime = (TextView)convertView.findViewById(R.id.start_time);
             holder.startPoint = (TextView)convertView.findViewById(R.id.start_point);
             holder.update_statu = (TextView)convertView.findViewById(R.id.update_statu);
             holder.upload_measureData = (TextView)convertView.findViewById(R.id.upload_measureData);
             holder.delete_measureData = (TextView)convertView.findViewById(R.id.delete_measureData);
             convertView.setTag(holder);
              
         }else {    
             holder = (ViewHolder)convertView.getTag();
         }
		 
		 final String ID = dataList.get(position).getID();
		 String startTime = dataList.get(position).getStartTime();
		 String startPoint = dataList.get(position).getStartPoint();
		 int isUpload = dataList.get(position).getIsUpload();
		 
		 if (isUpload == 0) {
			holder.update_statu.setText("未上传");
		}else{
			holder.update_statu.setText("已上传");
			holder.update_statu.setTextColor(Color.GREEN);
		}
		 
		 holder.startPoint.setText(startPoint);
		 holder.startTime.setText(startTime);
		 
		 holder.upload_measureData.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent("COMPLETE_UPLOAD");
				intent.putExtra("CompleteID", ID);
				context.sendBroadcast(intent);
			}
		});
		 
		 holder.delete_measureData.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent("COMPLETE_DELETE");
					intent.putExtra("CompleteID", ID);
					context.sendBroadcast(intent);
				}
			});
		 
		 return convertView;
	}

	  public final class ViewHolder{
		  public TextView startTime;
		  public TextView startPoint;
	      public TextView update_statu;
	      public TextView upload_measureData;
	      public TextView delete_measureData;
	    }
}
