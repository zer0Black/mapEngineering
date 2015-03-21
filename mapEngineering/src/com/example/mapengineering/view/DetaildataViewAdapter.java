package com.example.mapengineering.view;

import java.util.List;

import com.example.mapengineering.R;
import com.example.mapengineering.R.id;
import com.example.mapengineering.R.layout;
import com.example.mapengineering.model.DataDetailModel;
import com.example.mapengineering.view.CompleteDataAdapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DetaildataViewAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<DataDetailModel> dataList;
	
	public DetaildataViewAdapter(Context context, List<DataDetailModel> data){
		this.mInflater = LayoutInflater.from(context);
		dataList = data;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	  
	  @Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			 if (convertView == null) {
	            
	            holder=new ViewHolder();  
	             
	            convertView = mInflater.inflate(R.layout.measure_data_detail, null);
	            holder.cedian = (TextView)convertView.findViewById(R.id.cedianT);
	            holder.qianshi = (TextView)convertView.findViewById(R.id.qianshiT);
	            holder.zhongshi = (TextView)convertView.findViewById(R.id.zhongshiT);
	            holder.houshi = (TextView)convertView.findViewById(R.id.houshiT);
	            convertView.setTag(holder);
	             
	        }else {    
	            holder = (ViewHolder)convertView.getTag();
	        }
			 
			 String zhuanghao = dataList.get(position).getZhuanghao();
			 String zhongshi = dataList.get(position).getZhongshi();
			 String qianshi = dataList.get(position).getQianshi();
			 String houshi = dataList.get(position).getHoushi();
			 
			 holder.cedian.setText(zhuanghao);
			 holder.qianshi.setText(qianshi);
			 holder.zhongshi.setText(zhongshi);
			 holder.houshi.setText(houshi);
			 
			return convertView;
		}
		
		 public final class ViewHolder{
		        public TextView cedian;
		        public TextView qianshi;
		        public TextView zhongshi;
		        public TextView houshi;
		    }
	
}
