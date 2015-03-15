package com.example.mapengineering;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

public class ExpressApplication extends Application {
    private List<Activity> activityList=new ArrayList<Activity>();
    private static ExpressApplication instance=null;
    
    @Override
    public void onCreate() {    	     
         super.onCreate();
         instance=this;
    }
    
    public synchronized static ExpressApplication getInstance() {
		return instance;
	}
    
    /**
     * 添加Activity
     * @param activity
     */
    public void addActivity(Activity activity) {
		activityList.add(activity);
	}
    
    /**
     * 退出app
     */
    public void exit() {
		try {
			for (Activity activity : activityList) {
				if (activity!=null) {
					activity.finish();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//System.exit(0);
		}
	}
    
    @Override
    public void onLowMemory() {
    	// TODO Auto-generated method stub
    	super.onLowMemory();
    	System.gc();
    }
}
