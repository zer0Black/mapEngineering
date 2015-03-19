package com.example.mapengineering;

import java.util.List;

import com.example.mapengineering.model.DataDetailModel;
import com.example.mapengineering.view.CompleteDetaildataViewAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class CompleteDetailListAcitivity extends Activity{
	
	private ListView listview;
	private List<DataDetailModel> data;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		
		listview = (ListView)findViewById(R.id.dataList);
		
//		data = (List<DataDetailModel>)getIntent().getSerializableExtra("data"); 
//		System.out.println("11="+data.get(0).getZhuanghao());
		
//		dataViewAdapter adapter = new dataViewAdapter(this, data);  

//		listview.setAdapter(adapter);  
	}
}
