package com.example.mapengineering;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class myListBaseView extends Activity{
	
	private ListView listview;
	private List<DataModel> data;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		
		listview = (ListView)findViewById(R.id.dataList);
		
		data = (List<DataModel>)getIntent().getSerializableExtra("data"); 
		System.out.println("11="+data.get(0).getZhuanghao());
		dataViewAdapter adapter = new dataViewAdapter(this, data);  
		System.out.println("---12---");
		listview.setAdapter(adapter);  
	}
}
