package com.example.mapengineering;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
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
	private Boolean isModifyPoint;//是否是新增点
	
	private SharedPreferences mPreferences;
	DatabaseHelper databaseHelper;
	SQLiteDatabase dbWrite;
	SQLiteDatabase dbRead;
	
	private int count;//计数，当前的桩号计数
	private int countInput;//计数，输入次数
	private int interval;//测量的区间
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
		Cursor cursorOrderNum = dbRead.rawQuery("select ordernum from measure_data_detail where UID=? order by ordernum desc", new String[]{uid});
		if (cursorOrderNum.moveToFirst()) {
			countInput = cursorOrderNum.getInt(cursorOrderNum.getColumnIndex("ordernum")) + 1;	
			if (countInput == 0) {
				countInput = 1;
			}
		} else{
			countInput = 1;
		}
		cursorOrderNum.close();
		
		Cursor cursorInterval = dbRead.rawQuery("select interval from measure_data_detail where UID=? and isInput = 1 order by ID asc", new String[]{uid});
		if (cursorInterval.moveToLast()) {
			interval = cursorInterval.getInt(cursorInterval.getColumnIndex("interval"));
			System.out.println("interval="+interval);
		} else{
			interval = 1;
		}
		cursorInterval.close();
		
		isNewPoint = false;
		isTempPoint = false;
		isModifyPoint = false;
		
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
		addNewPoint.setOnClickListener(new adNewPointListener());
		
		chaKanShuJu = (Button)findViewById(R.id.chakanshuju);
		chaKanShuJu.setOnClickListener(new chaKanShujuListener());
		
		ceZhanInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ceZhanInfo.setVisibility(View.GONE);
				ceDianInput.setVisibility(View.VISIBLE);
				ceDianInput.setHint("请输入测点");
				ceDianInput.setText("");
				inputOne.setHint("请输入中式数据");
				inputOne.setText("");
				inputTwoll.setVisibility(View.GONE);
				isModifyPoint = true;
			}
		});
		
		this.initYYControl();//初始化语音输入的控件
		this.getAllZhuanghao();
		this.initFirstZhuanghao();
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
		zhuanghaoList.clear();
		Cursor cursor = dbRead.rawQuery("select zhuanghao from measure_data_detail where UID=? and isInput=0 and ordernum=0", new String[]{uid});
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
			if (constants.containLetter(zhuanghaoFirst) && count == 0) {
				inputOne.setHint("请输入后视数据");
				inputTwoll.setVisibility(View.GONE);
			}else if(constants.containLetter(zhuanghaoFirst) && count > 0){
				inputOne.setHint("请输入前视数据");
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
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 
        if (keyCode == KeyEvent.KEYCODE_BACK
                 && event.getRepeatCount() == 0) {
        	Intent intent = new Intent(measureInputActivity.this, MainActivity.class);
			startActivity(intent);
             return true;
         }
         return super.onKeyDown(keyCode, event);
     }

//----------------------------class类分割线---------------------------
	class finishInputListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			String houshi = "";
			String qianshi = "";
			String zhongshi = "";
			
			String zhuanghaoCurrent = zhuanghaoList.get(count);

			//如果测站点和取出的点相等，则照常判断存储
			if (!isModifyPoint && !isNewPoint && !isTempPoint) {
				//包含大写字母则为基准点
				if (constants.containLetter(zhuanghaoCurrent) && count == 0) {
					houshi = inputOne.getText().toString();//获取后视数据
					qianshi = "0000";
					zhongshi = "0000";
					if (houshi == null || houshi.length() <= 0) {
						Toast.makeText(getApplicationContext(), "请确保数据输入", Toast.LENGTH_SHORT).show();
						return;
					}
					
				}else if(constants.containLetter(zhuanghaoCurrent) && count > 0){
					qianshi = inputOne.getText().toString();//获取后视数据
					houshi = "0000";
					zhongshi = "0000";
					if (qianshi == null || qianshi.length() <= 0) {
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
					
					String houshiTmp = "";
					String qianshiTmp = "";
					int qianshiAll = 0;//前视和
					int houshiAll = 0;//后视和
					int subtract = 0;//差
					
					Cursor cursor = dbRead.rawQuery("select houshi, qianshi from measure_data_detail where UID=? and interval=? and zhuanghao='0000'", new String[]{uid, interval+""});
					while (cursor.moveToNext()) {
						houshiTmp = cursor.getString(cursor.getColumnIndex("houshi"));
						qianshiTmp = cursor.getString(cursor.getColumnIndex("qianshi"));
						
						qianshiAll += Integer.valueOf(qianshiTmp);
						houshiAll += Integer.valueOf(houshiTmp);
					}
					subtract = houshiAll - qianshiAll;
					
					if (houshiAll == 0 || qianshiAll == 0) {
						gaoChaTextView.setText("暂无");
					}else{
						gaoChaTextView.setText(subtract);
					}

					cursor.close();
					
					
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
					dbWrite.execSQL("insert into measure_data_detail(UID, zhuanghao, qianshi, zhongshi, houshi, isInput, ordernum, interval)" +
							" values(?,?,?,?,?,?,?,?)", new Object[]{uid, zhuanghaoTemp, qianshi, zhongshi, houshi, 1, countInput, interval});
					
					String houshiTmp = "";
					String qianshiTmp = "";
					int qianshiAll = 0;//前视和
					int houshiAll = 0;//后视和
					int subtract = 0;//差
					Cursor cursor = dbRead.rawQuery("select houshi, qianshi from measure_data_detail where UID='" + uid + "' and interval= " + interval + " and zhuanghao='0000'", new String[]{});
					System.out.println("sql = select houshi, qianshi from measure_data_detail where UID='" + uid + "' and interval= " + interval + " and zhuanghao='0000'");
					while (cursor.moveToNext()) {
						houshiTmp = cursor.getString(cursor.getColumnIndex("houshi"));
						qianshiTmp = cursor.getString(cursor.getColumnIndex("qianshi"));
						
						qianshiAll += Integer.valueOf(qianshiTmp);
						houshiAll += Integer.valueOf(houshiTmp);
					}
					subtract = houshiAll - qianshiAll;
					
					if (houshiAll == 0 || qianshiAll == 0) {
						gaoChaTextView.setText("暂无");
					}else{
						gaoChaTextView.setText(subtract+"");
					}
					cursor.close();
					
				}else if (isNewPoint) {
					String zhuanghaoTemp = ceDianInput.getText().toString();
					houshi = "0000";
					qianshi = "0000";
					zhongshi = inputOne.getText().toString();
					if (zhongshi == null || zhongshi.length() <= 0) {
						Toast.makeText(getApplicationContext(), "请确保数据输入", Toast.LENGTH_SHORT).show();
						return;
					}
					dbWrite.execSQL("insert into measure_data_detail(UID, zhuanghao, qianshi, zhongshi, houshi, isInput, ordernum, interval)" +
							"values(?,?,?,?,?,?,?,?)", new Object[]{uid, zhuanghaoTemp, qianshi, zhongshi, houshi, 1, countInput, interval});
				}else if (isModifyPoint){
					//如果不相等，则判断中式是否有数据，然后更新数据
					houshi = "0000";
					qianshi = "0000";
					zhongshi = inputOne.getText().toString();
					String zhuanghaoUpdate = ceDianInput.getText().toString();
					if (zhongshi == null || zhongshi.length() <= 0) {
						Toast.makeText(getApplicationContext(), "请确保数据输入", Toast.LENGTH_SHORT).show();
						return;
					}
					dbWrite.execSQL("update measure_data_detail set zhuanghao=? ,qianshi=?, zhongshi=?, houshi=?, isInput=?, ordernum=? where UID=? and zhuanghao=?", 
							new Object[]{zhuanghaoUpdate, qianshi, zhongshi, houshi, 1, countInput, uid, zhuanghaoCurrent});
				}
			}
			
			//如果满足这个条件，则完成一次输入，存储数据,并输出成txt格式的文件
			if (constants.containLetter(zhuanghaoCurrent) && count > 0 && !isNewPoint && !isTempPoint) {
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
				//把结束时间，起始点，结束点和文件路径插入表
				dbWrite.execSQL("update measure_data set endTime=?, startPoint=?, endPoint=?, flag=? where ID=?",
						new Object[]{endTime, startPoint, endPoint, flag, uid});
				
				//判断是否还有下一组数据要测
				if (zhuanghaoList.size() -1 == count) {
					System.out.println("测量完成");
					Toast.makeText(getApplicationContext(), "测量完成", Toast.LENGTH_SHORT).show();
//					finish();
					Intent intent = new Intent(measureInputActivity.this, MainActivity.class);
					startActivity(intent);
					
				}else{
					interval++;
					count = 0;
					countInput++;
					//重新加载
					getAllZhuanghao();
				}		
				
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
			isModifyPoint = false;
			
			String zhuanghaoNext = zhuanghaoList.get(count);
			//包含大写字母则为基准点
			ceZhanInfo.setVisibility(View.VISIBLE);
			ceDianInput.setVisibility(View.GONE);
			ceZhanInfo.setText(zhuanghaoNext);
			if (constants.containLetter(zhuanghaoNext) && count == 0) {
				inputOne.setHint("请输入后视数据");
				inputOne.setText("");
				inputTwoll.setVisibility(View.GONE);
			}else if(constants.containLetter(zhuanghaoNext) && count > 0){
				inputOne.setHint("请输入前视数据");
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
			String zhuanghaoTep = "";
			
			Cursor cursor = dbRead.rawQuery("select houshi, zhuanghao, ordernum from measure_data_detail where UID=? and interval=? order by ordernum asc", new String[]{UID, interval+""});
			if (cursor.moveToFirst()) {
				houshiString = cursor.getString(cursor.getColumnIndex("houshi"));
				zhuanghaoTep = cursor.getString(cursor.getColumnIndex("zhuanghao"));
				if (constants.containLetter(zhuanghaoTep)) {
					cursor.close();
					return houshiString;
				}else{
					cursor.close();
					return null;
				}
			}else{
				cursor.close();
				return null;
			}
			
		}
		
		//获取终止水准点
		private String getEndPoint(String UID){
			String qianshiString = "";
			String zhuanghaoTep = "";
			
			Cursor cursor = dbRead.rawQuery("select qianshi, zhuanghao from measure_data_detail where UID=? and interval=? order by ordernum asc", new String[]{UID, (interval+1)+""});
			if (cursor.moveToFirst()) {
				qianshiString = cursor.getString(cursor.getColumnIndex("qianshi"));	
				zhuanghaoTep = cursor.getString(cursor.getColumnIndex("zhuanghao"));
				if (constants.containLetter(zhuanghaoTep)) {
					cursor.close();
					return qianshiString;
				}else{
					cursor.close();
					return null;
				}
			}else{
				cursor.close();
				return null;
			}
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
			ceDianInput.setText("");
			inputOne.setHint("请输入中式数据");
			inputOne.setText("");
			inputTwoll.setVisibility(View.GONE);
			isNewPoint = true;
		}
	}
	
	class chaKanShujuListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(measureInputActivity.this, CompleteDetailListAcitivity.class);
			intent.putExtra("ID", uid);
			startActivity(intent);
		}
	}

}
