// filename: OpenFileDemo.java
package com.example.mapengineering;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
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
					filePath.setText(filepath); 
				}
			}, 
			".txt;",
			images);
			return dialog;
		}
		return null;
	}
	
	private void storeZhuanghao(){
		DatabaseHelper databaseHelper = new DatabaseHelper(this);
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		String uid = mPreferences.getString(constants.IDCODER, "");
		
//		db.execSQL("insert into measure_data_detail(UID, zhuanghao, isInput)" +
//				"values(UID,)", new Object[]{});
		
		db.close();
	}
	
	
}
