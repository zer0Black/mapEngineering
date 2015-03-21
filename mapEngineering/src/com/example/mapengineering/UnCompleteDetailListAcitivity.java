package com.example.mapengineering;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.example.mapengineering.data.DatabaseHelper;
import com.example.mapengineering.model.DataDetailModel;
import com.example.mapengineering.util.constants;
import com.example.mapengineering.view.DetaildataViewAdapter;

public class UnCompleteDetailListAcitivity extends Activity{
	
	private SharedPreferences mPreferences;
	
	private Button continueMeasure;
	
	private ListView listview;
	private List<DataDetailModel> dataList =  new ArrayList<DataDetailModel>();
	DatabaseHelper databaseHelper;
	String ID = "";
	
	DetaildataViewAdapter dataAdapter;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uncomplete_record_detail);
		mPreferences=getSharedPreferences(constants.UID, MODE_PRIVATE);
		
		databaseHelper = new DatabaseHelper(this);
		
		listview = (ListView)findViewById(R.id.uncomplete_detail_dataList);
		ID = getIntent().getStringExtra("ID");
		
		System.out.println("ID="+ID);
		
		//从数据库取出数据，加载到data里，然后设置给adapter
		dataAdapter = new DetaildataViewAdapter(this, dataList);
		listview.setAdapter(dataAdapter);
		
		continueMeasure = (Button)findViewById(R.id.continueMeasure);
		continueMeasure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//把ID存入首选项
				Editor editor=mPreferences.edit();
				editor.putString(constants.IDCODER, ID).commit();
				
				Intent intent = new Intent(UnCompleteDetailListAcitivity.this, measureInputActivity.class);
				startActivity(intent);
			}
		});

	}
	
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.getDataFromSql(ID);
	}
	
	private void getDataFromSql(String ID){
		dataList.clear();
		
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select zhuanghao, qianshi, zhongshi, houshi from measure_data_detail where UID=?", new String[]{ID});
		while(cursor.moveToNext()){
			DataDetailModel dataDetailModel = new DataDetailModel();
			String zhuanghao = cursor.getString(cursor.getColumnIndex("zhuanghao"));
			String qianshi = cursor.getString(cursor.getColumnIndex("qianshi"));
			String zhongshi = cursor.getString(cursor.getColumnIndex("zhongshi"));
			String houshi = cursor.getString(cursor.getColumnIndex("houshi"));
			
			dataDetailModel.setZhuanghao(zhuanghao);
			dataDetailModel.setQianshi(qianshi);
			dataDetailModel.setZhongshi(zhongshi);
			dataDetailModel.setHoushi(houshi);
			
			dataList.add(dataDetailModel);	
		}
		cursor.close();
		dataAdapter.notifyDataSetChanged();
	}
}
