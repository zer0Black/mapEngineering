package com.example.mapengineering;

import java.util.ArrayList;
import java.util.List;

import com.example.mapengineering.view.ViewPagerAdapter;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RecordFragment extends Fragment {
	
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
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fManager = getChildFragmentManager();
		MainActivity.fragmentStacks.add(new ActivityStack(fManager));
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
	
}
