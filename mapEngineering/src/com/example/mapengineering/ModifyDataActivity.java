package com.example.mapengineering;

import com.example.mapengineering.data.DatabaseHelper;
import com.example.mapengineering.model.DataModel;
import com.example.mapengineering.util.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ModifyDataActivity extends Activity{

	private EditText zhuanghaoEdit;
	private EditText qianshiEdit;
	private EditText zhongshiEdit;
	private EditText houshiEdit;
	private Button save;
	
	private Button zhuanghaoYY;
	private Button qianshiYY;
	private Button zhongshiYY;
	private Button houshiYY;
	
	String ID = "";
	DatabaseHelper databaseHelper;
	
	// 语音听写UI
    private RecognizerDialog iatDialog;
    private Toast mToast;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify);
	
		ID = getIntent().getStringExtra("ID");
		
		databaseHelper = new DatabaseHelper(getApplicationContext());
		
		iatDialog = new RecognizerDialog(this,mInitListener);
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);	
		
		zhuanghaoEdit = (EditText)findViewById(R.id.zhuanghao_modify);
		qianshiEdit = (EditText)findViewById(R.id.qianshi_modify);
		zhongshiEdit = (EditText)findViewById(R.id.zhongshi_modify);
		houshiEdit = (EditText)findViewById(R.id.houshi_modify);
		
		save = (Button)findViewById(R.id.save_modify);
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SQLiteDatabase db = databaseHelper.getWritableDatabase();
				db.execSQL("update measure_data_detail set zhuanghao=?, qianshi=?, zhongshi=?, houshi=? where ID=?", 
						new Object[]{zhuanghaoEdit.getText().toString()
						, qianshiEdit.getText().toString()
						, zhongshiEdit.getText().toString()
						, houshiEdit.getText().toString()
						, ID});
				finish();
			}
		});
		
		zhuanghaoYY = (Button)findViewById(R.id.zhuanghao_yy_modify);
		zhuanghaoYY.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					zhuanghaoEdit.setText("");
					iatDialog.setListener(recognizerDialogListenerZhuanghao);
					iatDialog.show();
					showTip("开始识别");
				}
			});	
		
		qianshiYY = (Button)findViewById(R.id.qianshi_yy_modify);
		qianshiYY.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				qianshiEdit.setText("");
				iatDialog.setListener(recognizerDialogListenerQianshi);
				iatDialog.show();
				showTip("开始识别");
			}
		});
		
		zhongshiYY = (Button)findViewById(R.id.zhongshi_yy_modify);
		zhongshiYY.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				zhongshiEdit.setText("");
				iatDialog.setListener(recognizerDialogListenerZhongshi);
				iatDialog.show();
				showTip("开始识别");
			}
		});
		
		houshiYY = (Button)findViewById(R.id.houshi_yy_modify);
		houshiYY.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				houshiEdit.setText("");
				iatDialog.setListener(recognizerDialogListenerHoushi);
				iatDialog.show();
				showTip("开始识别");
			}
		});
		
		this.initData();
	}
	
	private void initData(){
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select zhuanghao, qianshi, zhongshi, houshi from measure_data_detail where ID = ?", new String[]{ID});
	
		while(cursor.moveToNext()){
			String zhuanghao = cursor.getString(cursor.getColumnIndex("zhuanghao"));
			String qianshi = cursor.getString(cursor.getColumnIndex("qianshi"));
			String zhongshi = cursor.getString(cursor.getColumnIndex("zhongshi"));
			String houshi = cursor.getString(cursor.getColumnIndex("houshi"));
			
			zhuanghaoEdit.setText(zhuanghao);
			qianshiEdit.setText(qianshi);
			zhongshiEdit.setText(zhongshi);
			houshiEdit.setText(houshi);
		}
		cursor.close();
	}
	
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
	
private RecognizerDialogListener recognizerDialogListenerZhuanghao=new RecognizerDialogListener(){
		
		public void onResult(RecognizerResult results, boolean isLast) {
			String text = JsonParser.parseIatResult(results.getResultString());
			zhuanghaoEdit.append(text);
			zhuanghaoEdit.setSelection(zhuanghaoEdit.length());
		}

		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}
	};
	
	private RecognizerDialogListener recognizerDialogListenerQianshi=new RecognizerDialogListener(){
		
		public void onResult(RecognizerResult results, boolean isLast) {
			String text = JsonParser.parseIatResult(results.getResultString());
			qianshiEdit.append(text);
			qianshiEdit.setSelection(qianshiEdit.length());
		}

		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}
	};
	
	private RecognizerDialogListener recognizerDialogListenerZhongshi=new RecognizerDialogListener(){
		
		public void onResult(RecognizerResult results, boolean isLast) {
			String text = JsonParser.parseIatResult(results.getResultString());
			zhongshiEdit.append(text);
			zhongshiEdit.setSelection(zhongshiEdit.length());
		}

		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}
	};
	
	private RecognizerDialogListener recognizerDialogListenerHoushi=new RecognizerDialogListener(){
		
		public void onResult(RecognizerResult results, boolean isLast) {
			String text = JsonParser.parseIatResult(results.getResultString());
			houshiEdit.append(text);
			houshiEdit.setSelection(houshiEdit.length());
		}

		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}
	};
}
