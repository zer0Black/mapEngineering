package com.example.mapengineering;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapengineering.data.DatabaseHelper;
import com.example.mapengineering.ftp.FTP;
import com.example.mapengineering.ftp.FTP.UploadProgressListener;
import com.example.mapengineering.model.DataModel;
import com.example.mapengineering.view.CompleteDataAdapter;
import com.example.mapengineering.view.UnCompleteDataAdapter;
import com.example.mapengineering.view.ViewPagerAdapter;

public class RecordFragment extends Fragment {
	
	private static final String TAG = "RecordFragment";  
    
    public static final String FTP_CONNECT_SUCCESSS = "ftp连接成功";  
    public static final String FTP_CONNECT_FAIL = "ftp连接失败";  
    public static final String FTP_DISCONNECT_SUCCESS = "ftp断开连接";  
    public static final String FTP_FILE_NOTEXISTS = "ftp上文件不存在";  
      
    public static final String FTP_UPLOAD_SUCCESS = "ftp文件上传成功";  
    public static final String FTP_UPLOAD_FAIL = "ftp文件上传失败";  
    public static final String FTP_UPLOAD_LOADING = "ftp文件正在上传";  
  
    public static final String FTP_DOWN_LOADING = "ftp文件正在下载";  
    public static final String FTP_DOWN_SUCCESS = "ftp文件下载成功";  
    public static final String FTP_DOWN_FAIL = "ftp文件下载失败";  
      
    public static final String FTP_DELETEFILE_SUCCESS = "ftp文件删除成功";  
    public static final String FTP_DELETEFILE_FAIL = "ftp文件删除失败"; 
	
	
	private View layoutView;
	FragmentManager fManager;
	List<View> views;// Tab页面列表
	int offset = 0;// 动画图片偏移量
	int currIndex = 0;// 当前页卡编号
	int bmpw;// 动画图片宽度

	ViewPager viewPager;// 页卡内容
	ImageView imageView;// 动画图片
	TextView textView, textView2;
	View view, view2;// 各个页卡
	RelativeLayout rltitle1,rltitle2;
	ViewPagerAdapter viewPagerAdapter;
	
	CompleteDataAdapter completeDataAdapter;
	List<DataModel> completeList = new ArrayList<DataModel>();
	UnCompleteDataAdapter unCompleteDataAdapter;
	List<DataModel> unCompleteList = new ArrayList<DataModel>();
	
	ListView completeListView;
	ListView unCompleteListView;
	
	DatabaseHelper databaseHelper;
	private MessagerReceiver messagerReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fManager = getChildFragmentManager();
		MainActivity.fragmentStacks.add(new ActivityStack(fManager));
		
		databaseHelper = new DatabaseHelper(getActivity());
		this.registerMessagerReceiver();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layoutView = inflater.inflate(R.layout.record, null);
		
		this.initImageView();
		this.initTextView();
		this.initTitleRl();
		this.initViewPager();
		
		return layoutView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.initCompleteData();
		this.initUnCompleteData();
	}
	
	/**
	 * 初始化滑动页内容
	 */
	private void initViewPager() {
		viewPager = (ViewPager) layoutView.findViewById(R.id.vpage);
		views = new ArrayList<View>();
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.complete_record, null);
		view2 = inflater.inflate(R.layout.uncomplete_record, null);
		views.add(view);
		views.add(view2);
		viewPagerAdapter = new ViewPagerAdapter(views);
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		viewPager.setCurrentItem(0);
		
		this.initCompletePage();
		this.initUnCompletePage();
	}
	
	private void initCompletePage() {
		completeDataAdapter = new CompleteDataAdapter(getActivity(), completeList);
		completeListView = (ListView)view.findViewById(R.id.complete_dataList);
		completeListView.setAdapter(completeDataAdapter);
		completeListView.setOnItemClickListener(new completeItemClick());
		
	}
	
	private void initUnCompletePage() {
		unCompleteDataAdapter = new UnCompleteDataAdapter(getActivity(), unCompleteList);
		unCompleteListView = (ListView)view2.findViewById(R.id.uncomplete_dataList);
		unCompleteListView.setAdapter(unCompleteDataAdapter);
		unCompleteListView.setOnItemClickListener(new UnCompleteItemClick());
	}
	
	private void initCompleteData() {
		completeList.clear();
		
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select ID, date, startTime, startPoint, isUpload from measure_data where flag = 1", new String[]{});
		
		while(cursor.moveToNext()){
			DataModel dataModel = new DataModel();
			String ID = cursor.getString(cursor.getColumnIndex("ID"));
			String date = cursor.getString(cursor.getColumnIndex("date"));
			String starttime = cursor.getString(cursor.getColumnIndex("startTime"));
			String dateTime = date + starttime;
			String startPoint = cursor.getString(cursor.getColumnIndex("startPoint"));
			
			int isUpload = cursor.getInt(cursor.getColumnIndex("isUpload"));
			
			dataModel.setID(ID);
			dataModel.setStartTime(dateTime);
			dataModel.setStartPoint(startPoint);
			dataModel.setIsUpload(isUpload);
			
			completeList.add(dataModel);
		}
		cursor.close();
		completeDataAdapter.notifyDataSetChanged();
	}
	
	private void initUnCompleteData() {
		unCompleteList.clear();
		
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select ID, date, startTime, oneOrTwoMeasure, measureType from measure_data where flag = 0", new String[]{});
		
		while(cursor.moveToNext()){
			DataModel dataModel = new DataModel();
			String ID = cursor.getString(cursor.getColumnIndex("ID"));
			String date = cursor.getString(cursor.getColumnIndex("date"));
			String starttime = cursor.getString(cursor.getColumnIndex("startTime"));
			String dateTime = date + starttime;
			String oneOrTwoMeasure = cursor.getString(cursor.getColumnIndex("oneOrTwoMeasure"));
			
			int measureType = cursor.getInt(cursor.getColumnIndex("measureType"));
				
			dataModel.setID(ID);
			dataModel.setStartTime(dateTime);
			dataModel.setOneOrTwoMeasure(oneOrTwoMeasure);
			dataModel.setMeasureType(measureType);
			
			unCompleteList.add(dataModel);
		}
		cursor.close();
		unCompleteDataAdapter.notifyDataSetChanged();
	}


	class completeItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			String ID = completeList.get(position).getID();
			Intent intent = new Intent(getActivity(), CompleteDetailListAcitivity.class);
			intent.putExtra("ID", ID);
			startActivity(intent);
		}
	}
	
	class UnCompleteItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			String ID = unCompleteList.get(position).getID();
			Intent intent = new Intent(getActivity(), UnCompleteDetailListAcitivity.class);
			intent.putExtra("ID", ID);
			startActivity(intent);
		}
		
	}
	
	/**
	 * 初始化页卡指示图片
	 */
	private void initImageView() {
		imageView = (ImageView) layoutView.findViewById(R.id.cursor);
		bmpw = BitmapFactory.decodeResource(getResources(), R.drawable.a)
				.getWidth();// 获取图片宽度
		DisplayMetrics dMetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(dMetrics);
		int screenw = dMetrics.widthPixels;
		offset = (screenw / 2 - bmpw) / 2;
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		imageView.setImageMatrix(matrix);// 设置动画初始位置
		System.out.println("=======initImageView===========");
	}
	
	/**
	 * 初始化标题
	 */
	private void initTitleRl() {
		rltitle1 = (RelativeLayout) layoutView.findViewById(R.id.rltitle1);
		rltitle2 = (RelativeLayout) layoutView.findViewById(R.id.rltitle2);

		rltitle1.setOnClickListener(new MyOnClickListener(0));
		rltitle2.setOnClickListener(new MyOnClickListener(1));
	}
	
	/**
	 * 初始化标题
	 */
	private void initTextView() {
		textView = (TextView) layoutView.findViewById(R.id.txttitle1);
		textView2 = (TextView) layoutView.findViewById(R.id.txttitle2);

		textView.setOnClickListener(new MyOnClickListener(0));
		textView2.setOnClickListener(new MyOnClickListener(1));
		textView.setTextColor(Color.parseColor("#006cff"));
	}
	
	class MyOnClickListener implements OnClickListener {

		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			viewPager.setCurrentItem(index);
		}
	}
	
	class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpw;// 页卡1到页卡2便宜量

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = new TranslateAnimation(one * currIndex, one
					* arg0, 0, 0);
			currIndex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(300);
			imageView.startAnimation(animation);
			clearTextColor();
			if (currIndex == 0) {
				textView.setTextColor(Color.parseColor("#006cff"));
			} else if (currIndex == 1) {
				textView2.setTextColor(Color.parseColor("#006cff"));
			}
		}
	}
	
	/**
	 * 清空文本字体颜色
	 */
	private void clearTextColor() {
		textView.setTextColor(Color.parseColor("#686868"));
		textView2.setTextColor(Color.parseColor("#686868"));
	}
	
	public void registerMessagerReceiver(){
		messagerReceiver = new MessagerReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction("COMPLETE_DELETE");
		filter.addAction("COMPLETE_UPLOAD");
		filter.addAction("UNCOMPLETE_DELETE");
		getActivity().registerReceiver(messagerReceiver, filter);
	}
	
	public class MessagerReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if ("COMPLETE_DELETE".equals(intent.getAction())) {
				String ID = intent.getStringExtra("CompleteID");
				deleteData(ID);
			}else if("COMPLETE_UPLOAD".equals(intent.getAction())){
				String ID = intent.getStringExtra("CompleteID");
				uploadData(ID);
			}else if("UNCOMPLETE_DELETE".equals(intent.getAction())){
				String ID = intent.getStringExtra("CompleteID");
				deleteUnData(ID);
			}
		}
	}
	
	private void deleteData(String id){
		final String dataId = id;
		new AlertDialog.Builder(getActivity())
		.setTitle("删除数据")
		.setMessage("确认删除本条测量数据？删除后不可恢复，谨慎删除")
		.setCancelable(false)
		.setPositiveButton("确定", 
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteDataImpl(dataId);
					}
				})
		.setNegativeButton("取消", 
				new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).show();
	}
	
	private void deleteDataImpl(String id){
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		db.execSQL("delete from measure_data where ID =?",new Object[]{id});
		db.execSQL("delete from measure_data_detail where UID =?", new Object[]{id});
		for (DataModel dataModel : completeList) {
			if (dataModel.getID().equals(id)) {
				completeList.remove(dataModel);
				break;
			}
		}
		completeDataAdapter.notifyDataSetChanged();
		Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_LONG).show();
	}
	
	private void uploadData(String id){
		final String dataId = id;
		new AlertDialog.Builder(getActivity())
		.setTitle("上传数据")
		.setMessage("确认上传数据到服务器？")
		.setCancelable(false)
		.setPositiveButton("确定", 
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						uploadDataImpl(dataId);
					}
				})
		.setNegativeButton("取消", 
				new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).show();
	}
	
	private void uploadDataImpl(String id){
		String filePath[] = {};
		
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		Cursor cursora = db.rawQuery("select filePath from measure_data where ID=?", new String[]{id});
		
		while(cursora.moveToNext()){
			String filePathSave = cursora.getString(cursora.getColumnIndex("filePath"));
			filePath = filePathSave.split(",");
			System.out.println("filePath="+filePath[0]);
		}
		
		final String path[] = filePath;
		
		if (filePath.length == 1) {
			//单文件上传
			new Thread(new Runnable() {           
                @Override  
                public void run() {   
                    // 上传  
//                	String command = "chmod 777 " + path[0];//全部权限
//    	            Runtime runtime = Runtime.getRuntime();
//    	            try {
//						Process proc = runtime.exec(command);
//					} catch (IOException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
                    File file = new File(path[0]);
//                	File file = new File("/storage/sdcard0/mapEngineering/measureData/BM10-下午1059-measure_data.txt");
                    System.out.println("filePath=ddd");
                    try {  
                          
                        //单文件上传  
                        new FTP().uploadSingleFile(file, "/",new UploadProgressListener(){  
                            @Override  
                            public void onUploadProgress(String currentStep,long uploadSize,File file) {  
                                // TODO Auto-generated method stub  
                                Log.d(TAG, currentStep);                                          
                                if(currentStep.equals(RecordFragment.FTP_UPLOAD_SUCCESS)){  
                                    Log.d(TAG, "-----shanchuan--successful");  
                                } else if(currentStep.equals(RecordFragment.FTP_UPLOAD_LOADING)){  
                                    long fize = file.length();  
                                    float num = (float)uploadSize / (float)fize;  
                                    int result = (int)(num * 100);  
                                    Log.d(TAG, "-----shangchuan---"+result + "%");  
                                }  
                            }                             
                        });  
                    } catch (IOException e) {  
                        // TODO Auto-generated catch block  
                        e.printStackTrace();  
                    }  
                      
                }  
            }).start();  
		}else{
			//多文件上传
			//单文件上传
			new Thread(new Runnable() {           
                @Override  
                public void run() {   
                    // 上传  
                	LinkedList<File> fileList = new LinkedList<File>();
                      
                    for (int i = 0; i < path.length; i++) {
                    	File file = new File(path[i]);
                    	fileList.add(file);
					}
                    
                    try {  
                        
                        //单文件上传  
                        new FTP().uploadMultiFile(fileList, "/",new UploadProgressListener(){  
                            @Override  
                            public void onUploadProgress(String currentStep,long uploadSize,File file) {  
                                // TODO Auto-generated method stub  
                                Log.d(TAG, currentStep);                                          
                                if(currentStep.equals(RecordFragment.FTP_UPLOAD_SUCCESS)){  
                                    Log.d(TAG, "-----shanchuan--successful");  
                                } else if(currentStep.equals(RecordFragment.FTP_UPLOAD_LOADING)){  
                                    long fize = file.length();  
                                    float num = (float)uploadSize / (float)fize;  
                                    int result = (int)(num * 100);  
                                    Log.d(TAG, "-----shangchuan---"+result + "%");  
                                }  
                            }                             
                        });  
                    } catch (IOException e) {  
                        // TODO Auto-generated catch block  
                        e.printStackTrace();  
                    }  
                      
                }  
            }).start();
		}
		
//		db.execSQL("update measure_data set isUpload=1 where ID =?",new Object[]{id});
//		for (DataModel dataModel : completeList) {
//			if (dataModel.getID().equals(id)) {
//				dataModel.setIsUpload(1);
//				break;
//			}
//		}
		completeDataAdapter.notifyDataSetChanged();
		Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_LONG).show();
	}
	
	private void deleteUnData(String id){
		final String dataId = id;
		new AlertDialog.Builder(getActivity())
		.setTitle("删除数据")
		.setMessage("确认删除本条测量数据？删除后不可恢复，谨慎删除")
		.setCancelable(false)
		.setPositiveButton("确定", 
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteUnDataImpl(dataId);
					}
				})
		.setNegativeButton("取消", 
				new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).show();
	}
	
	private void deleteUnDataImpl(String id){
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		db.execSQL("delete from measure_data where ID =?",new Object[]{id});
		db.execSQL("delete from measure_data_detail where UID =?", new Object[]{id});
		for (DataModel dataModel : unCompleteList) {
			if (dataModel.getID().equals(id)) {
				unCompleteList.remove(dataModel);
				break;
			}
		}
		unCompleteDataAdapter.notifyDataSetChanged();
		Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_LONG).show();
	}
	
}
