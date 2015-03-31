package com.example.mapengineering;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.mapengineering.data.DatabaseHelper;
import com.example.mapengineering.util.CallbackBundle;
import com.example.mapengineering.util.OpenFileDialog;
import com.example.mapengineering.util.constants;

public class ImportFileActivity extends Activity {
	
	static private int openfileDialogId = 0;
	
	private Button openfile;
	private Button startMeasure;
	
	private TextView projectInfo;
	private TextView filePath;
	
	private SharedPreferences mPreferences;
	private String filaPathSave = "";
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importfile);
        mPreferences=getSharedPreferences(constants.UID, MODE_PRIVATE);
        
        filePath = (TextView)findViewById(R.id.filePathShow);
        projectInfo = (TextView)findViewById(R.id.projectShow);
        
        openfile = (Button)findViewById(R.id.button_openfile);
        openfile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDialog(openfileDialogId);
			}
		});
        
        startMeasure = (Button)findViewById(R.id.start);
        startMeasure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				storeZhuanghao();
				Intent intent = new Intent(ImportFileActivity.this, measureInputActivity.class);
				startActivity(intent);
			}
		});
    }

	@Override
	protected Dialog onCreateDialog(int id) {
		if(id==openfileDialogId){
			Map<String, Integer> images = new HashMap<String, Integer>();
			images.put(OpenFileDialog.sRoot, R.drawable.filedialog_root);	
			images.put(OpenFileDialog.sParent, R.drawable.filedialog_folder_up);	
			images.put(OpenFileDialog.sFolder, R.drawable.filedialog_folder);	
			images.put("txt", R.drawable.filedialog_folder);	
			images.put(OpenFileDialog.sEmpty, R.drawable.filedialog_root);
			Dialog dialog = OpenFileDialog.createDialog(id, this, "选择文件", new CallbackBundle() {
				@Override
				public void callback(Bundle bundle) {
					String filepath = bundle.getString("path");
					String path[] = filepath.split("/");
					filePath.setText("选择的文件名是\n" + path[path.length - 1]); 
					filaPathSave = filepath;
					startMeasure.setEnabled(true);
				}
			}, 
			".txt;",
			images);
			return dialog;
		}
		return null;
	}
	
	@SuppressWarnings("finally")
	private String getFileData(){
		String fileName = filaPathSave;
		String res="";     
		try{ 
			FileInputStream fin = new FileInputStream(fileName);
		    int length = fin.available(); 
		    byte [] buffer = new byte[length]; 
		    fin.read(buffer);     
		    res = EncodingUtils.getString(buffer, "UTF-8"); 
		    fin.close();     
		}catch(Exception e){ 
		    e.printStackTrace(); 
		}finally{
			return res;
		}
		
	}
	
	private void storeZhuanghao(){
		int interval = 0;
		
		String zhuanghaoData = getFileData();
		String zhuanghaoList[] = zhuanghaoData.split(",");//桩号
		
		DatabaseHelper databaseHelper = new DatabaseHelper(this);
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		String uid = mPreferences.getString(constants.IDCODER, "");
		
		for (int i = 0; i < zhuanghaoList.length; i++) {
			//去掉空格和换行
			zhuanghaoList[i] = constants.clearSpaceAndLine(zhuanghaoList[i]);		
			if (constants.containLetter(zhuanghaoList[i])) {
				interval ++;
			}
			db.execSQL("insert into measure_data_detail(UID, zhuanghao, isInput, ordernum, interval) " +
					"values(?,?,?,?,?)", new Object[]{uid, zhuanghaoList[i], 0, 0, interval});
		}
		db.close();
	}
}
