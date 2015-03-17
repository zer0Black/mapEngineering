package com.example.mapengineering.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

	private final static String NAME = "measure.db"; //数据库名
	private final static int version = 1; //数据库版本
	
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE measure_data (ID integer primary key autoincrement, " +
				"data varchar(30), mancodeOne varchar(15)," +
				"mancodeTwo varchar(15), mancodeThree varchar(15), flag int)");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
