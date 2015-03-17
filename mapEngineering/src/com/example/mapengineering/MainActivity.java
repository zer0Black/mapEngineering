package com.example.mapengineering;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.tech.MifareClassic;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabWidget;

public class MainActivity extends FragmentActivity {

	private FragmentTabHost  tabHost;
	public static ArrayList<ActivityStack> fragmentStacks;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		this.initTabhost();
		
//		ExpressApplication.getInstance().addActivity(this);
	}
	
	private void initTabhost() {
		
		fragmentStacks=new ArrayList<ActivityStack>();
		
		tabHost=(FragmentTabHost)findViewById(android.R.id.tabhost);
		tabHost.setup(MainActivity.this, getSupportFragmentManager(), R.id.realtabcontent);
		
//		TabWidget mTabWidget=(TabWidget)findViewById(android.R.id.tabs);
//		mTabWidget.setBackgroundColor(Color.WHITE);
//		 if(Build.VERSION.SDK_INT >= 11){
//			 mTabWidget.setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
//        }
//		mTabWidget.setGravity(Gravity.CENTER_VERTICAL);
		
		View view_measure=LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_measure, null);
		View view_record=LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_record, null);
		
		tabHost.addTab(tabHost.newTabSpec("measure").setIndicator(view_measure), MeasureFragment.class,null);
		tabHost.addTab(tabHost.newTabSpec("record").setIndicator(view_record), RecordFragment.class,null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
