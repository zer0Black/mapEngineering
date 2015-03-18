package com.example.mapengineering;

import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class MeasureFragment extends Fragment {
	
	
	private EditText manCodeOneEdit;
	private EditText manCodeTwoEdit;
	private EditText manCodeThreeEdit;
	private EditText instrumentCodeEdit;
	private CheckBox checkBox;
	private Button startMeasureButton;
	
	private View layoutView;
	FragmentManager fManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fManager = getChildFragmentManager();
		MainActivity.fragmentStacks.add(new ActivityStack(fManager));
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
		
		if (manCodeOne == null || manCodeOne.length() == 0
				|| manCodeTwo == null || manCodeTwo.length() == 0
				|| manCodeThree == null || manCodeThree.length() == 0) {
			Toast.makeText(getActivity(), "人员代码输入不完整，请检查", Toast.LENGTH_LONG).show();
		}
		
		if (instrumentCode == null || instrumentCode.length() == 0) {
			Toast.makeText(getActivity(), "请输入仪器代码", Toast.LENGTH_LONG).show();
		}
		
		Date now = new Date(); 
//		String Date = ;
		//存入数据库
	}
	
}
