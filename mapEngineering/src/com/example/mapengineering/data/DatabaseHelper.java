package com.example.mapengineering.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

	private final static String NAME = "measure.db"; //数据库名
	private final static int version = 1; //数据库版本
	
	public DatabaseHelper(Context context) {
		super(context, NAME, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE measure_data (ID varcha(30) primary key, " +
				"date varchar(15), startTime varchar(15), endTime varchar(15), " +
				"mancodeOne varchar(15), mancodeTwo varchar(15), mancodeThree varchar(15)," +
				" startPoint varchar(30), endPoint varchar(30), measureType int," +
				"againMeasure int, flag int)");
		
		db.execSQL("CREATE TABLE measure_data_detail (ID INTEGER primary key autoincrement, " +
				"zhuanghao varchar(20), qianshi varchar(20)," +
				"zhongshi varchar(20), houshi varchar(20), UID varchar(30))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
