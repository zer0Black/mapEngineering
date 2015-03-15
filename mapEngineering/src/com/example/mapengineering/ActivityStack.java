package com.example.mapengineering;

import java.util.Stack;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class ActivityStack {
	private Stack<Fragment> mActivityStack;
	private FragmentManager fm;

	public ActivityStack(FragmentManager fm) {
		this.fm = fm;
		mActivityStack = new Stack<Fragment>();
	}

	public void popActivity(Fragment activity) {
		if (activity != null) {
			mActivityStack.remove(activity);
			fm.popBackStack();
			// mActivityStack.pop();
			activity = null;
		}
	}

	public Fragment currentActivity() {
		Fragment activity = mActivityStack.lastElement();
		// Activity activity = mActivityStack.pop();
		return activity;
	}

	public void pushActivity(Fragment activity) {
		mActivityStack.add(activity);
		// mActivityStack.push(activity);
	}

	public void popAllActivityExceptOne(Class<Fragment> cls) {
		while (true) {
			Fragment activity = currentActivity();
			if (activity == null) {
				break;
			}
			if (activity.getClass().equals(cls)) {
				break;
			}
			popActivity(activity);
		}
	}

	public int getFragmentSize() {
		return mActivityStack.size();
	}
}
