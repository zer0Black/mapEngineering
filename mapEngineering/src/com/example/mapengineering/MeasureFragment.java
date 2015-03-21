package com.example.mapengineering;

import java.text.DateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mapengineering.data.DatabaseHelper;
import com.example.mapengineering.util.JsonParser;
import com.example.mapengineering.util.Uid;
import com.example.mapengineering.util.constants;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

public class MeasureFragment extends Fragment {
	
	// 语音听写UI
    private RecognizerDialog iatDialog;
    private Toast mToast;
    
	private EditText manCodeOneEdit;
	private EditText manCodeTwoEdit;
	private EditText manCodeThreeEdit;
	private EditText instrumentCodeEdit;
	private CheckBox checkBox;
	private Button startMeasureButton;
	
	private Button manCodeOneYYButton;
	private Button manCodeTwoYYButton;
	private Button manCodeThreeYYButton;
	private Button instrumentYYButton;
	
	private View layoutView;
	FragmentManager fManager;
	private SharedPreferences mPreferences;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fManager = getChildFragmentManager();
		MainActivity.fragmentStacks.add(new ActivityStack(fManager));
		mPreferences=getActivity().getSharedPreferences(constants.UID, getActivity().MODE_PRIVATE);
	
		// 初始化听写Dialog,如果只使用有UI听写功能,无需创建SpeechRecognizer
		iatDialog = new RecognizerDialog(getActivity(),mInitListener);
		mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);	
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layoutView = inflater.inflate(R.layout.measure, null);
		
		manCodeOneEdit = (EditText)layoutView.findViewById(R.id.manCode_one);
		manCodeTwoEdit = (EditText)layoutView.findViewById(R.id.manCode_two);
		manCodeThreeEdit = (EditText)layoutView.findViewById(R.id.manCode_three);
		instrumentCodeEdit = (EditText)layoutView.findViewById(R.id.instrumentCode);
		checkBox = (CheckBox)layoutView.findViewById(R.id.checkBox1);
		
		startMeasureButton = (Button)layoutView.findViewById(R.id.startMeasure);
		startMeasureButton.setOnClickListener(new startMeasureListener());
		
		manCodeOneYYButton = (Button)layoutView.findViewById(R.id.manCode_One_yy);
		manCodeOneYYButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				manCodeOneEdit.setText("");
				iatDialog.setListener(recognizerDialogListenerManCodeOne);
				iatDialog.show();
				showTip("开始识别");
			}
		});
		
		manCodeTwoYYButton = (Button)layoutView.findViewById(R.id.manCode_two_yy);
		manCodeTwoYYButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				manCodeTwoEdit.setText("");
				iatDialog.setListener(recognizerDialogListenerManCodeTwo);
				iatDialog.show();
				showTip("开始识别");
			}
		});
		
		manCodeThreeYYButton = (Button)layoutView.findViewById(R.id.manCode_three_yy);
		manCodeThreeYYButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				manCodeThreeEdit.setText("");
				iatDialog.setListener(recognizerDialogListenerManCodeThree);
				iatDialog.show();
				showTip("开始识别");
			}
		});
		
		instrumentYYButton = (Button)layoutView.findViewById(R.id.instrumentCode_yy);
		instrumentYYButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				instrumentCodeEdit.setText("");
				iatDialog.setListener(recognizerDialogListenerInstrumentCode);
				iatDialog.show();
				showTip("开始识别");
			}
		});
		
		return layoutView;
	}
	
	/**
	 * 听写UI监听器
	 */
	private RecognizerDialogListener recognizerDialogListenerManCodeOne=new RecognizerDialogListener(){
		
		public void onResult(RecognizerResult results, boolean isLast) {
			String text = JsonParser.parseIatResult(results.getResultString());
			manCodeOneEdit.append(text);
			manCodeOneEdit.setSelection(manCodeOneEdit.length());
		}

		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}
	};
	
	private RecognizerDialogListener recognizerDialogListenerManCodeTwo=new RecognizerDialogListener(){
		
		public void onResult(RecognizerResult results, boolean isLast) {
			String text = JsonParser.parseIatResult(results.getResultString());
			manCodeTwoEdit.append(text);
			manCodeTwoEdit.setSelection(manCodeTwoEdit.length());
		}

		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}
	};
	
	private RecognizerDialogListener recognizerDialogListenerManCodeThree=new RecognizerDialogListener(){
		
		public void onResult(RecognizerResult results, boolean isLast) {
			String text = JsonParser.parseIatResult(results.getResultString());
			manCodeThreeEdit.append(text);
			manCodeThreeEdit.setSelection(manCodeThreeEdit.length());
		}

		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}
	};
	
	private RecognizerDialogListener recognizerDialogListenerInstrumentCode=new RecognizerDialogListener(){
		
		public void onResult(RecognizerResult results, boolean isLast) {
			String text = JsonParser.parseIatResult(results.getResultString());
			instrumentCodeEdit.append(text);
			instrumentCodeEdit.setSelection(instrumentCodeEdit.length());
		}

		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}
	};

	class startMeasureListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			//弹出确认窗口
			new AlertDialog.Builder(getActivity())
			.setTitle("开始测量")
			.setMessage("请检查人员代码，仪器代码，复测与否的完整性，开始测量后这些数据将不能更改")
			.setCancelable(false)
			.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(
								DialogInterface dialog,
								int which) {
							storeDataToSql();
						}
					})
			.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(
								DialogInterface dialog,
								int which) {
							// TODO Auto-generated method stub
							dialog.cancel();
						}
					}).show();
		}
	}
	
	private void storeDataToSql(){
		String manCodeOne = manCodeOneEdit.getText().toString();
		String manCodeTwo = manCodeTwoEdit.getText().toString();
		String manCodeThree = manCodeThreeEdit.getText().toString();
		String instrumentCode = instrumentCodeEdit.getText().toString();
		Boolean isAgainMeasure = checkBox.isChecked();
		int againMeasure;
		if (isAgainMeasure) {
			againMeasure = 1;
		}else{
			againMeasure = 0;
		}
		
		if (manCodeOne == null || manCodeOne.length() == 0
				|| manCodeTwo == null || manCodeTwo.length() == 0
				|| manCodeThree == null || manCodeThree.length() == 0) {
			Toast.makeText(getActivity(), "人员代码输入不完整，请检查", Toast.LENGTH_LONG).show();
			return;
		}
		
		if (instrumentCode == null || instrumentCode.length() == 0) {
			Toast.makeText(getActivity(), "请输入仪器代码", Toast.LENGTH_LONG).show();
			return;
		}
		
		//获取日期和当前时间
		Date now = new Date(); 
		DateFormat d1 = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM);
		String DateFormat = d1.format(now);
		String dateSplit[] = DateFormat.split(" ");
		String date = dateSplit[0];
		String startTime = dateSplit[1].substring(0, dateSplit[1].length() - 3);
		
		long uid = Uid.next();
		String uidString = uid+"";
		
		int measureType = 6;//代表中平
		int flag = 0;//0代表测量未完成
		
		//把ID存入首选项
		Editor editor=mPreferences.edit();
		editor.putString(constants.IDCODER, uidString).commit();
		//存入数据库
		this.save(uidString, date, startTime, manCodeOne, manCodeTwo, manCodeThree, measureType, againMeasure, flag);
	
		Intent intent = new Intent(getActivity(), measureInputActivity.class);
		startActivity(intent);
	}
	
	private void save(String uid, String date, String startTime, String mancodeOne, 
			String mancodeTwo, String mancodeThree, int measureType,
			int againMeasure, int flag){
		DatabaseHelper databaseHelper = new DatabaseHelper(this.getActivity());
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		db.execSQL("insert into measure_data(ID, date, startTime ,mancodeOne," +
				"mancodeTwo, mancodeThree, measureType, againMeasure, flag)" +
				"values(?,?,?,?,?,?,?,?,?)", new Object[]{uid,
			date, startTime, mancodeOne, mancodeTwo, mancodeThree, 
			measureType, againMeasure, flag});
		
		db.close();
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
}
