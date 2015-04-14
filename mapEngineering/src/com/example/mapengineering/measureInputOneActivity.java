package com.example.mapengineering;

import com.iflytek.cloud.ui.RecognizerDialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class measureInputOneActivity extends Activity{

	private Button finishInput;
	private Button chaKanShuJu;
	
    private EditText zhuanghao;//输入框1
    private EditText qianshi;//输入框2
    private EditText zhongshi;//输入框1
    private EditText houshi;//输入框2
	
    private Button zhuanghaoYY;
	private Button qianshiYY;
	private Button zhongshiYY;
	private Button houshiYY;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_measure1);
		
		zhuanghao = (EditText)findViewById(R.id.zhuanghao);
		qianshi = (EditText)findViewById(R.id.qianshi);
		zhongshi = (EditText)findViewById(R.id.zhongshi);
		houshi = (EditText)findViewById(R.id.houshi);
		
		
		
	}
	
//	private void initYYControl(){
//		// 初始化听写Dialog,如果只使用有UI听写功能,无需创建SpeechRecognizer
//				iatDialog = new RecognizerDialog(this,mInitListener);
//				mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);	
//						
//				inputOneYY = (Button)findViewById(R.id.inputOne_yy);
//				inputOneYY.setOnClickListener(new View.OnClickListener() {
//							
//							@Override
//							public void onClick(View v) {
//								inputOneYY.setText("");
//								iatDialog.setListener(recognizerDialogListenerInputOne);
//								iatDialog.show();
//								showTip("开始识别");
//							}
//						});	
//					
//				inputTwoYY = (Button)findViewById(R.id.inputTwo_yy);
//				inputTwoYY.setOnClickListener(new View.OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//							inputTwo.setText("");
//							iatDialog.setListener(recognizerDialogListenerInputTwo);
//							iatDialog.show();
//							showTip("开始识别");
//						}
//					});
//	}
	
}
