package com.example.mapengineering;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapengineering.data.DatabaseHelper;
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
    
    private ProgressDialog progressDialog;

    private TextView ceZhanInfo;
    private TextView gaoChaTextView;//高差的展示
    
    private EditText ceDianInput;//测站点修改框
    private EditText inputOne;//输入框1
    private EditText inputTwo;//输入框2
    
	private Button finishInput;
	private Button chaKanShuJu;
	private Button setTempPoint;
	private Button addNewPoint;
	
	private Button inputOneYY;
	private Button inputTwoYY;
	
	private LinearLayout inputTwoll;
	private Boolean isTempPoint;//是否是转点
	private Boolean isNewPoint;//是否是新增点
	
	private SharedPreferences mPreferences;
	DatabaseHelper databaseHelper;
	SQLiteDatabase dbWrite;
	SQLiteDatabase dbRead;
	
	private int count;//计数，当前的桩号计数
	private int countInput;//计数，输入次数
	private List<String> zhuanghaoList;//桩号列表
	
	private String uid = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_measure);
		mPreferences=getSharedPreferences(constants.UID, MODE_PRIVATE);
		uid = mPreferences.getString(constants.IDCODER, "");
		databaseHelper = new DatabaseHelper(getApplicationContext());
		dbWrite = databaseHelper.getWritableDatabase();
		dbRead = databaseHelper.getReadableDatabase();
		
		count = 0;//初始化为0
		countInput = 1;
		isNewPoint = false;
		isTempPoint = false;
		
		zhuanghaoList = new ArrayList<String>();
		
		inputTwoll = (LinearLayout)findViewById(R.id.inputTwoll);
		
		ceZhanInfo = (TextView)findViewById(R.id.cezhandian);
		gaoChaTextView = (TextView)findViewById(R.id.gaocha);
		
		ceDianInput = (EditText)findViewById(R.id.cezhandian_edit);
		inputOne = (EditText)findViewById(R.id.inputOne);
		inputTwo = (EditText)findViewById(R.id.inputTwo);
		
		finishInput = (Button)findViewById(R.id.finishInput);
		finishInput.setOnClickListener(new finishInputListener());
		
		setTempPoint = (Button)findViewById(R.id.setTempPoint);
		setTempPoint.setOnClickListener(new setTempPointListener());

		addNewPoint = (Button)findViewById(R.id.addNewPoint);
//		addNewPoint.setOnClickListener(new setTempPointListener());
		
		this.initYYControl();//初始化语音输入的控件
		this.initFirstZhuanghao();
		this.getAllZhuanghao();
	
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
	}

	private void initYYControl(){
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
	}
	
	private void getAllZhuanghao(){
		Cursor cursor = dbRead.rawQuery("select zhuanghao from measure_data_detail where UID=?, isInput=0, ordernum=0", new String[]{uid});
		while(cursor.moveToNext()){
			zhuanghaoList.add(cursor.getString(cursor.getColumnIndex("zhuanghao")));
		}
		cursor.close();
	}
	
	private void initFirstZhuanghao(){
		if (zhuanghaoList != null && zhuanghaoList.size() > 0) {
			String zhuanghaoFirst = zhuanghaoList.get(count);
			//包含大写字母则为基准点
			ceZhanInfo.setVisibility(View.VISIBLE);
			ceDianInput.setVisibility(View.GONE);
			ceZhanInfo.setText(zhuanghaoFirst);
			if (constants.containLetter(zhuanghaoFirst)) {
				inputOne.setHint("请输入后视数据");
				inputTwoll.setVisibility(View.GONE);
			}else if(zhuanghaoFirst.equals("0000")){
				inputOne.setHint("请输入前视数据");
				inputTwoll.setVisibility(View.VISIBLE);
				inputTwo.setHint("请输入后视数据");
			}else {
				inputOne.setHint("请输入中式数据");
				inputTwoll.setVisibility(View.GONE);
			}
		}
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

//----------------------------class类分割线---------------------------
	class finishInputListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			String houshi = "";
			String qianshi = "";
			String zhongshi = "";
			
			String zhuanghaoCurrent = zhuanghaoList.get(count);

			//如果测站点和取出的点相等，则照常判断存储
			if (zhuanghaoCurrent.equals(ceZhanInfo.getText().toString())) {
				//包含大写字母则为基准点
				if (constants.containLetter(zhuanghaoCurrent)) {
					houshi = inputOne.getText().toString();//获取后视数据
					qianshi = "0000";
					zhongshi = "0000";
					if (houshi == null || houshi.length() <= 0) {
						Toast.makeText(getApplicationContext(), "请确保数据输入", Toast.LENGTH_SHORT).show();
						return;
					}
					
				}else if(zhuanghaoCurrent.equals("0000")){
					qianshi = inputOne.getText().toString();//获取前视数据
					zhongshi = "0000";
					houshi = inputTwo.getText().toString();//获取后视数据
					if (houshi == null || houshi.length() <= 0 || qianshi == null || qianshi.length() <= 0) {
						Toast.makeText(getApplicationContext(), "请确保数据输入", Toast.LENGTH_SHORT).show();
						return;
					}
					
				}else {
					houshi = "0000";
					qianshi = "0000";
					zhongshi = inputOne.getText().toString();
					if (zhongshi == null || zhongshi.length() <= 0) {
						Toast.makeText(getApplicationContext(), "请确保数据输入", Toast.LENGTH_SHORT).show();
						return;
					}
				}
				//存储数据到数据库
				dbWrite.execSQL("update measure_data_detail set qianshi=?, zhongshi=?, houshi=?, isInput=?, ordernum=? where UID=? and zhuanghao=?", 
						new Object[]{qianshi, zhongshi, houshi, 1, countInput, uid, zhuanghaoCurrent});
				
			}else{
				//如果是临时的转点
				if (isTempPoint) {
					String zhuanghaoTemp = "0000";
					qianshi = inputOne.getText().toString();//获取前视数据
					zhongshi = "0000";
					houshi = inputTwo.getText().toString();//获取后视数据
					if (houshi == null || houshi.length() <= 0 || qianshi == null || qianshi.length() <= 0) {
						Toast.makeText(getApplicationContext(), "请确保数据输入", Toast.LENGTH_SHORT).show();
						return;
					}
					dbWrite.execSQL("insert into measure_data_detail(UID, zhuanghao, qianshi, zhongshi, houshi, isInput, ordernum)" +
							"values(?,?,?,?,?,?,?)", new Object[]{uid, zhuanghaoTemp, qianshi, zhongshi, houshi, 1, countInput});
				}else if (isNewPoint) {
					String zhuanghaoTemp = ceDianInput.getText().toString();
					houshi = "0000";
					qianshi = "0000";
					zhongshi = inputOne.getText().toString();
					if (zhongshi == null || zhongshi.length() <= 0) {
						Toast.makeText(getApplicationContext(), "请确保数据输入", Toast.LENGTH_SHORT).show();
						return;
					}
					dbWrite.execSQL("insert into measure_data_detail(UID, zhuanghao, qianshi, zhongshi, houshi, isInput, ordernum)" +
							"values(?,?,?,?,?,?,?)", new Object[]{uid, zhuanghaoTemp, qianshi, zhongshi, houshi, 1, countInput});
				}else{
					//如果不相等，则判断中式是否有数据，然后更新数据
					houshi = "0000";
					qianshi = "0000";
					zhongshi = inputOne.getText().toString();
					String zhuanghaoUpdate = ceZhanInfo.getText().toString();
					if (zhongshi == null || zhongshi.length() <= 0) {
						Toast.makeText(getApplicationContext(), "请确保数据输入", Toast.LENGTH_SHORT).show();
						return;
					}
					dbWrite.execSQL("update measure_data_detail set zhuanghao=? ,qianshi=?, zhongshi=?, houshi=?, isInput=?, ordernum=? where UID=? and zhuanghao=?", 
							new Object[]{zhuanghaoUpdate, qianshi, zhongshi, houshi, 1, countInput, uid, zhuanghaoCurrent});
				}
			}
			
			//如果满足这个条件，则完成一次输入，存储数据,并输出成txt格式的文件
			if (constants.containLetter(zhuanghaoCurrent) && count > 0) {
				progressDialog = ProgressDialog.show(measureInputActivity.this, "", "完成一个测回，正在处理数据...");
				//获取日期和当前时间
				Date now = new Date(); 
				DateFormat d1 = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM);
				String DateFormat = d1.format(now);
				String dateSplit[] = DateFormat.split(" ");
				String endTime = dateSplit[1].substring(0, dateSplit[1].length() - 3);
				int flag = 1;
				
				//取出起始水准点
				String startPoint = getStartPoint(uid);
				//取出结束水准点
				String endPoint = getEndPoint(uid);
				//把结束时间，起始点，结束点插入表
				dbWrite.execSQL("update measure_data set endTime=?, startPoint=?, endPoint=?, flag=? where ID=?",
						new Object[]{endTime, startPoint, endPoint, flag, uid});
				
				progressDialog.dismiss();
				return;
			}
			
			if (isTempPoint || isNewPoint) {
				countInput++;
			}else{
				count++;
				countInput++;
			}
			
			isTempPoint = false;
			isNewPoint = false;
			
			String zhuanghaoNext = zhuanghaoList.get(count);
			//包含大写字母则为基准点
			ceZhanInfo.setVisibility(View.VISIBLE);
			ceDianInput.setVisibility(View.GONE);
			ceZhanInfo.setText(zhuanghaoNext);
			if (constants.containLetter(zhuanghaoNext)) {
				inputOne.setHint("请输入后视数据");
				inputOne.setText("");
				inputTwoll.setVisibility(View.GONE);
			}else if(zhuanghaoNext.equals("0000")){
				inputOne.setHint("请输入前视数据");
				inputTwoll.setVisibility(View.VISIBLE);
				inputTwo.setHint("请输入后视数据");
				inputOne.setText("");
				inputTwo.setText("");
			}else {
				inputOne.setHint("请输入中式数据");
				inputOne.setText("");
				inputTwoll.setVisibility(View.GONE);
			}
		}
		
		//获取起始水准点
		private String getStartPoint(String UID){
			String houshiString = "";
			
			Cursor cursor = dbRead.rawQuery("select houshi from measure_data_detail where UID=? and ordernum=1", new String[]{UID});
			while (cursor.moveToNext()) {
				houshiString = cursor.getString(cursor.getColumnIndex("houshi"));	
			}	
			cursor.close();
			return houshiString;
		}
		
		//获取终止水准点
		private String getEndPoint(String UID){
			String qianshiString = "";
			
			Cursor cursor = dbRead.rawQuery("select qianshi from measure_data_detail where UID=? and ordernum=?", new String[]{UID, countInput+""});
			while (cursor.moveToNext()) {
				qianshiString = cursor.getString(cursor.getColumnIndex("qianshi"));	
			}	
			cursor.close();
			return qianshiString;
		}
	}
	
	class setTempPointListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			ceDianInput.setVisibility(View.GONE);
			ceZhanInfo.setVisibility(View.VISIBLE);
			ceZhanInfo.setText("转点");
			inputOne.setHint("请输入前视数据");
			inputTwoll.setVisibility(View.VISIBLE);
			inputTwo.setHint("请输入后视数据");
			inputOne.setText("");
			inputTwo.setText("");
			isTempPoint = true;
		}
	}
	
	class adNewPointListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			ceZhanInfo.setVisibility(View.GONE);
			ceDianInput.setVisibility(View.VISIBLE);
			ceDianInput.setHint("请输入测点");
			inputOne.setHint("请输入中式数据");
			inputOne.setText("");
			inputTwoll.setVisibility(View.GONE);
			isNewPoint = true;
		}
	}
}
