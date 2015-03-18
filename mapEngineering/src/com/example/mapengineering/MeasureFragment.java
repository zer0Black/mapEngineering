package com.example.mapengineering;

import java.text.DateFormat;
import java.util.Date;

import com.example.mapengineering.data.DatabaseHelper;
import com.example.mapengineering.util.Uid;
import com.example.mapengineering.util.constants;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.style.TtsSpan.MeasureBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MeasureFragment extends Fragment {
	
	
	private EditText manCodeOneEdit;
	private EditText manCodeTwoEdit;
	private EditText manCodeThreeEdit;
	private EditText instrumentCodeEdit;
	private CheckBox checkBox;
	private Button startMeasureButton;
	
	private View layoutView;
	FragmentManager fManager;
	private SharedPreferences mPreferences;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fManager = getChildFragmentManager();
		MainActivity.fragmentStacks.add(new ActivityStack(fManager));
		mPreferences=getActivity().getSharedPreferences(constants.UID, getActivity().MODE_PRIVATE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layoutView = inflater.inflate(R.layout.measure, null);
		
		manCodeOneEdit = (EditText)manCodeOneEdit.findViewById(R.id.manCode_one);
		manCodeTwoEdit = (EditText)manCodeTwoEdit.findViewById(R.id.manCode_two);
		manCodeThreeEdit = (EditText)manCodeThreeEdit.findViewById(R.id.manCode_three);
		instrumentCodeEdit = (EditText)instrumentCodeEdit.findViewById(R.id.instrumentCode);
		checkBox = (CheckBox)checkBox.findViewById(R.id.checkBox1);
		
		startMeasureButton = (Button)startMeasureButton.findViewById(R.id.startMeasure);
		startMeasureButton.setOnClickListener(new startMeasureListener());
		
		return layoutView;
	}

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
		}
		
		if (instrumentCode == null || instrumentCode.length() == 0) {
			Toast.makeText(getActivity(), "请输入仪器代码", Toast.LENGTH_LONG).show();
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
	
}
