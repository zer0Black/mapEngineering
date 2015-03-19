package com.example.mapengineering.view;

import java.util.List;

import com.example.mapengineering.R;
import com.example.mapengineering.model.DataDetailModel;
import com.example.mapengineering.model.DataModel;
import com.example.mapengineering.view.CompleteDetaildataViewAdapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CompleteDataAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private List<DataModel> dataList;
	
	public CompleteDataAdapter(Context context, List<DataModel> data){
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
             holder.AgainMeasure = (TextView)convertView.findViewById(R.id.again_measure);
             holder.measureStyle = (TextView)convertView.findViewById(R.id.measure_style);
         
             convertView.setTag(holder);
              
         }else {    
             holder = (ViewHolder)convertView.getTag();
         }
		 
		 String startTime = dataList.get(position).getStartTime();
		 String measureAgain = "";
		 if (dataList.get(position).getIsAgainMeasure()) {
			 measureAgain="往测";
		}else{
			 measureAgain=" 返测";
		}
		 
		 String measureStyle = "";
		 if (dataList.get(position).getMeasureType() == 6) {
			 measureStyle = "中平";
		}
		 
		 holder.startTime.setText(startTime);
		 holder.AgainMeasure.setText(measureAgain);
		 holder.measureStyle.setText(measureStyle);
		 
		 return convertView;
	}

	  public final class ViewHolder{
	        public TextView startTime;
	        public TextView AgainMeasure;
	        public TextView measureStyle;
	    }
}
