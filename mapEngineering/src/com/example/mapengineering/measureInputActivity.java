package com.example.mapengineering;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapengineering.data.DatabaseHelper;
import com.example.mapengineering.model.DataDetailModel;
import com.example.mapengineering.util.JsonParser;
import com.example.mapengineering.util.constants;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

public class measureInputActivity extends Activity {	
	
	// 语音听写UI
    private RecognizerDialog iatDialog;
    private Toast mToast;
	
//	private EditText zhuanghaoEdit;
//	private EditText qianshiEdit;
//	private EditText zhongshiEdit;
//	private EditText houshiEdit;
    
    private TextView ceZhanInfo;
    private TextView gaoChaTextView;//高差的展示
    
    private EditText ceDianInput;//测站点修改框
    private EditText inputOne;//输入框1
    private EditText inputTwo;//输入框2
    
	private Button finishInput;
	private Button chaKanShuJu;
	
	private Button inputOneYY;
	private Button inputTwoYY;
	
	private SharedPreferences mPreferences;
	DatabaseHelper databaseHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_measure);
		mPreferences=getSharedPreferences(constants.UID, MODE_PRIVATE);
		
		databaseHelper = new DatabaseHelper(getApplicationContext());
		
		ceZhanInfo = (TextView)findViewById(R.id.cezhandian);
		gaoChaTextView = (TextView)findViewById(R.id.gaocha);
		
		ceDianInput = (EditText)findViewById(R.id.cezhandian_edit);
		inputOne = (EditText)findViewById(R.id.inputOne);
		inputTwo = (EditText)findViewById(R.id.inputTwo);
		
		finishInput = (Button)findViewById(R.id.finishInput);
		finishInput.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				
			}
		});
		
//		finishMeasure = (Button)findViewById(R.id.finishMeasure);
//		finishMeasure.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				SQLiteDatabase db = databaseHelper.getWritableDatabase();
//				String uid = mPreferences.getString(constants.IDCODER, "");
//				
//				//获取日期和当前时间
//				Date now = new Date(); 
//				DateFormat d1 = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM);
//				String DateFormat = d1.format(now);
//				String dateSplit[] = DateFormat.split(" ");
//				String endTime = dateSplit[1].substring(0, dateSplit[1].length() - 3);
//				int flag = 1;
//				
//				//取出起始水准点
//				String startPoint = getStartPoint(uid);
//				//取出结束水准点
//				String endPoint = getEndPoint(uid);
//				//把结束时间，起始点，结束点插入表
//				db.execSQL("update measure_data set endTime=?, startPoint=?, endPoint=?, flag=? where ID=?",
//						new Object[]{endTime, startPoint, endPoint, flag, uid});
//				
//				db.close();
//				
//				Intent intent = new Intent(measureInputActivity.this, MainActivity.class);
//				startActivity(intent);
//			}
//		});
//		
//		chakanshuju = (Button)findViewById(R.id.chakanshuju);
//		chakanshuju.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
////				Intent intent = new Intent(measureInputActivity.this, ListBaseAcitivity.class);
////				intent.putExtra("data", (Serializable)dataList); 
////				startActivity(intent);
//			}
//		});
		
		// 初始化听写Dialog,如果只使用有UI听写功能,无需创建SpeechRecognizer
		iatDialog = new RecognizerDialog(this,mInitListener);
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);	
				
		inputOneYY = (Button)findViewById(R.id.inputOne_yy);
		inputOneYY.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						inputOneYY.setText("");
						iatDialog.setListener(recognizerDialogListenerInputOne);
						iatDialog.show();
						showTip("开始识别");
					}
				});	
			
		inputTwoYY = (Button)findViewById(R.id.inputTwo_yy);
		inputTwoYY.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					inputTwo.setText("");
					iatDialog.setListener(recognizerDialogListenerInputTwo);
					iatDialog.show();
					showTip("开始识别");
				}
			});
			
//			zhongshiYY = (Button)findViewById(R.id.zhongshi_yy);
//			zhongshiYY.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					zhongshiEdit.setText("");
//					iatDialog.setListener(recognizerDialogListenerZhongshi);
//					iatDialog.show();
//					showTip("开始识别");
//				}
//			});
//			
//			houshiYY = (Button)findViewById(R.id.houshi_yy);
//			houshiYY.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					houshiEdit.setText("");
//					iatDialog.setListener(recognizerDialogListenerHoushi);
//					iatDialog.show();
//					showTip("开始识别");
//				}
//			});
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
	
//	class nextButton implements OnClickListener{
//
//		@Override
//		public void onClick(View arg0) {
//			
//			String zhuanghao = zhuanghaoEdit.getText().toString();
//			String qianshi = qianshiEdit.getText().toString();
//			String zhongshi = zhongshiEdit.getText().toString();
//			String houshi = houshiEdit.getText().toString();
//			
//			if (zhuanghao.equals("")&&qianshi.equals("")&&zhongshi.equals("")&&houshi.equals("")) {
//				Toast.makeText(getApplicationContext(), "没有任何数据输入，请检查", Toast.LENGTH_SHORT).show();
//				return;
//			}
//			
//			if (zhuanghao.equals("") || zhuanghao.length() == 0) {
//				zhuanghao = "0000";
//			}
//			
//			if (qianshi.equals("") || qianshi.length() == 0) {
//				qianshi = "0000";
//			}
//			
//			if (zhongshi.equals("") || zhongshi.length() == 0) {
//				zhongshi = "0000";
//			}
//			
//			if (houshi.equals("") || houshi.length() == 0) {
//				houshi = "0000";
//			}
//			
//			SQLiteDatabase db = databaseHelper.getWritableDatabase();
//			String uid = mPreferences.getString(constants.IDCODER, "");
//			
//			db.execSQL("insert into measure_data_detail(UID, zhuanghao, qianshi ,zhongshi," +
//					"houshi) values(?,?,?,?,?)", new Object[]{uid, 
//					zhuanghao, qianshi, zhongshi, houshi});
//			
//			db.close();
//			
//			zhuanghaoEdit.setText("");
//			qianshiEdit.setText("");
//			zhongshiEdit.setText("");
//			houshiEdit.setText("");
//		}
//	}
	
	/**
	 * 初始化监听器。
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			if (code != ErrorCode.SUCCESS) {
        		showTip("初始化失败,错误码："+code);
        	}
		}
	};
	
	private void showTip(final String str)
	{
		mToast.setText(str);
		mToast.show();
	}
	
	private RecognizerDialogListener recognizerDialogListenerInputOne=new RecognizerDialogListener(){
		
		public void onResult(RecognizerResult results, boolean isLast) {
			String text = JsonParser.parseIatResult(results.getResultString());
			inputOne.append(text);
			inputOne.setSelection(inputOne.length());
		}

		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}
	};
	
	private RecognizerDialogListener recognizerDialogListenerInputTwo=new RecognizerDialogListener(){
		
		public void onResult(RecognizerResult results, boolean isLast) {
			String text = JsonParser.parseIatResult(results.getResultString());
			inputTwo.append(text);
			inputTwo.setSelection(inputTwo.length());
		}

		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}
	};
	
//	private RecognizerDialogListener recognizerDialogListenerZhongshi=new RecognizerDialogListener(){
//		
//		public void onResult(RecognizerResult results, boolean isLast) {
//			String text = JsonParser.parseIatResult(results.getResultString());
//			zhongshiEdit.append(text);
//			zhongshiEdit.setSelection(zhongshiEdit.length());
//		}
//
//		public void onError(SpeechError error) {
//			showTip(error.getPlainDescription(true));
//		}
//	};
//	
//	private RecognizerDialogListener recognizerDialogListenerHoushi=new RecognizerDialogListener(){
//		
//		public void onResult(RecognizerResult results, boolean isLast) {
//			String text = JsonParser.parseIatResult(results.getResultString());
//			houshiEdit.append(text);
//			houshiEdit.setSelection(houshiEdit.length());
//		}
//
//		public void onError(SpeechError error) {
//			showTip(error.getPlainDescription(true));
//		}
//	};
}
