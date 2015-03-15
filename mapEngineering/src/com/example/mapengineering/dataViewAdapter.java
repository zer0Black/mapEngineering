package com.example.mapengineering;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class dataViewAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<DataModel> dataList;
	
	public dataViewAdapter(Context context, List<DataModel> data){
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
              
             convertView = mInflater.inflate(R.layout.testview, null);
             holder.cedian = (TextView)convertView.findViewById(R.id.cedianT);
             holder.qianshi = (TextView)convertView.findViewById(R.id.qianshiT);
             holder.zhongshi = (TextView)convertView.findViewById(R.id.zhongshiT);
             holder.houshi = (TextView)convertView.findViewById(R.id.houshiT);
             convertView.setTag(holder);
              
         }else {    
             holder = (ViewHolder)convertView.getTag();
         }
		 
		 System.out.println("---1---");
		 
		 System.out.println((String)dataList.get(position).getZhuanghao());
		 
		 holder.cedian.setText((String)dataList.get(position).getZhuanghao());
		 holder.qianshi.setText((String)dataList.get(position).getQianshi());
		 holder.zhongshi.setText((String)dataList.get(position).getZhongshi());
		 holder.houshi.setText((String)dataList.get(position).getHoushi());
		 
		 return convertView;
	}

	  public final class ViewHolder{
	        public TextView cedian;
	        public TextView qianshi;
	        public TextView zhongshi;
	        public TextView houshi;
	    }
	
}
