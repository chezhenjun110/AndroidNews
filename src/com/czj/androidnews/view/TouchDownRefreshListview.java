package com.czj.androidnews.view;

import com.czj.androidnews.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

public class TouchDownRefreshListview extends ListView {

	public TouchDownRefreshListview(Context context) {
		super(context);
		initheader();
	}

	public TouchDownRefreshListview(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initheader();
	}

	public TouchDownRefreshListview(Context context, AttributeSet attrs) {
		super(context, attrs);
		initheader();
	}

	/*
	 * 初始化listview的下拉刷新显示条
	 */
	private void initheader() {
		View header = View.inflate(getContext(), R.layout.refresh_header, null);
		header.measure(0, 0);
		int height = header.getMeasuredHeight();
		header.setPadding(0, -height, 0, 0);
		this.addView(header);
	}

}
