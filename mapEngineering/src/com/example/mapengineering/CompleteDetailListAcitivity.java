package com.example.mapengineering;

import java.util.ArrayList;
import java.util.List;

import com.example.mapengineering.data.DatabaseHelper;
import com.example.mapengineering.model.DataDetailModel;
import com.example.mapengineering.model.DataModel;
import com.example.mapengineering.view.CompleteDataAdapter;
import com.example.mapengineering.view.DetaildataViewAdapter;
import com.example.mapengineering.view.UnCompleteDataAdapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class CompleteDetailListAcitivity extends Activity{
	
	private ListView listview;
	private List<DataDetailModel> dataList =  new ArrayList<DataDetailModel>();
	DatabaseHelper databaseHelper;
	String ID = "";
	
	DetaildataViewAdapter dataAdapter;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.complete_record_detail);
		
		databaseHelper = new DatabaseHelper(this);
		
		listview = (ListView)findViewById(R.id.complete_detail_dataList);
		ID = getIntent().getStringExtra("ID");
		
		System.out.println("ID="+ID);
		
		//从数据库取出数据，加载到data里，然后设置给adapter
		dataAdapter = new DetaildataViewAdapter(this, dataList);
		listview.setAdapter(dataAdapter);
		listview.setOnItemClickListener(new modifyItemClick());
		
//		data = (List<DataDetailModel>)getIntent().getSerializableExtra("data"); 
//		System.out.println("11="+data.get(0).getZhuanghao());
		
//		dataViewAdapter adapter = new dataViewAdapter(this, data);  

//		listview.setAdapter(adapter);  
	}
	
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.getDataFromSql(ID);
	}
	
	private void getDataFromSql(String ID){
		dataList.clear();
		
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select ID, zhuanghao, qianshi, zhongshi, houshi from measure_data_detail where UID=? and ordernum != 0 order by ordernum asc", new String[]{ID});
		while(cursor.moveToNext()){
			DataDetailModel dataDetailModel = new DataDetailModel();
			String IDD = cursor.getString(cursor.getColumnIndex("ID"));
			String zhuanghao = cursor.getString(cursor.getColumnIndex("zhuanghao"));
			String qianshi = cursor.getString(cursor.getColumnIndex("qianshi"));
			String zhongshi = cursor.getString(cursor.getColumnIndex("zhongshi"));
			String houshi = cursor.getString(cursor.getColumnIndex("houshi"));
			
			dataDetailModel.setID(IDD);
			dataDetailModel.setZhuanghao(zhuanghao);
			dataDetailModel.setQianshi(qianshi);
			dataDetailModel.setZhongshi(zhongshi);
			dataDetailModel.setHoushi(houshi);
			
			dataList.add(dataDetailModel);	
		}
		System.out.println("ComDetaildataList="+dataList);
		cursor.close();
		dataAdapter.notifyDataSetChanged();
	}
	
	class modifyItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			String ID = dataList.get(position).getID();
			Intent intent = new Intent(CompleteDetailListAcitivity.this, ModifyDataActivity.class);
			intent.putExtra("ID", ID);
			startActivity(intent);
		}
	}
}
