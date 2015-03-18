package com.example.mapengineering;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.mapengineering.data.DatabaseHelper;
import com.example.mapengineering.model.DataDetailModel;
import com.example.mapengineering.util.constants;

public class measureInputActivity extends Activity {

	private EditText zhuanghaoEdit;
	private EditText qianshiEdit;
	private EditText zhongshiEdit;
	private EditText houshiEdit;
	private Button nextButton;
	private Button finishMeasure;
	private Button chakanshuju;
	
	private SharedPreferences mPreferences;
	private List<DataDetailModel> dataList;
	DatabaseHelper databaseHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_measure);
		mPreferences=getSharedPreferences(constants.UID, MODE_PRIVATE);
		
		dataList = new ArrayList<DataDetailModel>();
		
		zhuanghaoEdit = (EditText)findViewById(R.id.zhuanghao);
		qianshiEdit = (EditText)findViewById(R.id.qianshi);
		zhongshiEdit = (EditText)findViewById(R.id.zhongshi);
		houshiEdit = (EditText)findViewById(R.id.houshi);
		
		nextButton = (Button)findViewById(R.id.nextPoint);
		nextButton.setOnClickListener(new nextButton());
		
		finishMeasure = (Button)findViewById(R.id.finishMeasure);
		finishMeasure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				databaseHelper = new DatabaseHelper(getApplicationContext());
				SQLiteDatabase db = databaseHelper.getWritableDatabase();
				String uid = mPreferences.getString(constants.IDCODER, "");
				
				//获取日期和当前时间
				Date now = new Date(); 
				DateFormat d1 = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM);
				String DateFormat = d1.format(now);
				String dateSplit[] = DateFormat.split(" ");
				String endTime = dateSplit[1].substring(0, dateSplit[1].length() - 3);
				
				//取出起始水准点
				String startPoint = getStartPoint(uid);
				//取出结束水准点
				String endPoint = getEndPoint(uid);
				//把结束时间，起始点，结束点插入表
				db.execSQL("update measure_data set endTime=?, startPoint=?, endPoint=? where ID=?",
						new Object[]{endTime, startPoint, endPoint, uid});
			}
		});
		
		chakanshuju = (Button)findViewById(R.id.chakanshuju);
		chakanshuju.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				Intent intent = new Intent(measureInputActivity.this, ListBaseAcitivity.class);
//				intent.putExtra("data", (Serializable)dataList); 
//				startActivity(intent);
			}
		});
	}

	//获取起始水准点
	private String getStartPoint(String UID){
		String houshiString = "";
		
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select houshi from measure_data_detail where UID=? limit 0,1", new String[]{UID});
		if (cursor.moveToFirst()) {
			houshiString = cursor.getString(cursor.getColumnIndex("houshi"));
		}
		cursor.close();
		return houshiString;
	}
	
	//获取终止水准点
	private String getEndPoint(String UID){
		String qianshiString = "";
		
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select qianshi from measure_data_detail where UID=?", new String[]{UID});
		if (cursor.moveToLast()) {
			qianshiString = cursor.getString(cursor.getColumnIndex("qianshi"));
		}
		cursor.close();
		return qianshiString;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class nextButton implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			DataDetailModel dataModel = new DataDetailModel();
			dataModel.setZhuanghao(zhuanghaoEdit.getText().toString());
			dataModel.setQianshi(qianshiEdit.getText().toString());
			dataModel.setZhongshi(zhongshiEdit.getText().toString());
			dataModel.setHoushi(houshiEdit.getText().toString());
			
			zhuanghaoEdit.setText("");
			qianshiEdit.setText("");
			zhongshiEdit.setText("");
			houshiEdit.setText("");
			
			dataList.add(dataModel);
		}
		
	}
}
