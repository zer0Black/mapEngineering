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
		
		ExpressApplication.getInstance().addActivity(this);
	}
	
	/**
	 * 初始化选项卡
	 */
	private void initTabhost() {
		
		fragmentStacks=new ArrayList<ActivityStack>();
		
		tabHost=(FragmentTabHost)findViewById(android.R.id.tabhost);
		tabHost.setup(MainActivity.this, getSupportFragmentManager());
		
		TabWidget mTabWidget=(TabWidget)findViewById(android.R.id.tabs);
		mTabWidget.setBackgroundColor(Color.WHITE);
//		 if(Build.VERSION.SDK_INT >= 11){
//			 mTabWidget.setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
//        }
		mTabWidget.setGravity(Gravity.CENTER_VERTICAL);
			
		View view_take=LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_record, null);
		View view_search=LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_measure, null);
		
		tabHost.addTab(tabHost.newTabSpec("take").setIndicator(view_take), MeasureFragment.class,null);
		tabHost.addTab(tabHost.newTabSpec("search").setIndicator(view_search), RecordFragment.class,null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
