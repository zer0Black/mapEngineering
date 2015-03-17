package com.example.mapengineering;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.mapengineering.model.DataDetailModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class measureInputActivity extends Activity {

	private EditText zhuanghaoEdit;
	private EditText qianshiEdit;
	private EditText zhongshiEdit;
	private EditText houshiEdit;
	private Button nextButton;
	private Button chakanshuju;
	
	private List<DataDetailModel> dataList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_measure);
		
		dataList = new ArrayList<DataDetailModel>();
		
		zhuanghaoEdit = (EditText)findViewById(R.id.zhuanghao);
		qianshiEdit = (EditText)findViewById(R.id.qianshi);
		zhongshiEdit = (EditText)findViewById(R.id.zhongshi);
		houshiEdit = (EditText)findViewById(R.id.houshi);
		
		nextButton = (Button)findViewById(R.id.nextPoint);
		nextButton.setOnClickListener(new nextButton());
		
		chakanshuju = (Button)findViewById(R.id.chakan);
		chakanshuju.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(measureInputActivity.this, ListBaseAcitivity.class);
				intent.putExtra("data", (Serializable)dataList); 
				startActivity(intent);
			}
		});
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
