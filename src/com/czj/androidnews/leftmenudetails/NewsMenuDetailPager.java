package com.czj.androidnews.leftmenudetails;

import java.util.ArrayList;

import com.czj.androidnews.R;
import com.czj.androidnews.base.BaseMenuDetailPager;
import com.czj.androidnews.base.TabDetailPager;
import com.czj.androidnews.domain.NewsData.NewsTabData;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.app.Activity;
import android.provider.MediaStore.Video;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * 菜单详情页-新闻
 * 
 * @author czj
 * 
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager {
	private ArrayList<TabDetailPager> mTabPageList;
	private ArrayList<NewsTabData> mNewsTabData;
	private TabPageIndicator indicator;
	private ViewPager mViewPager;

	public NewsMenuDetailPager(Activity activity, ArrayList<NewsTabData> children) {
		super(activity);
		mNewsTabData = children;

	}

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, com.czj.androidnews.R.layout.news_menu_detail, null);
		indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
		mViewPager = (ViewPager) view.findViewById(R.id.vp_menu_detail);
		ViewUtils.inject(this, view);
		return view;
	}

	@OnClick(R.id.btn_next)
	public void next(View view) {
		int item = mViewPager.getCurrentItem();
		if (item < mTabPageList.size()) {
			indicator.setCurrentItem(++item);
		} else {
			return;
		}

	}

	@Override
	public void initData() {

		mTabPageList = new ArrayList<TabDetailPager>();

		// 初始化页签数据
		for (int i = 0; i < mNewsTabData.size(); i++) {
			TabDetailPager pager = new TabDetailPager(mActivity, mNewsTabData.get(i));
			mTabPageList.add(pager);
		}
		mViewPager.setAdapter(new NewsTabAdapter());
		indicator.setViewPager(mViewPager);
	}

	class NewsTabAdapter extends PagerAdapter {

		@Override
		public CharSequence getPageTitle(int position) {// 设置viewpagerindicator的标题
			// TODO Auto-generated method stub
			return mNewsTabData.get(position).title;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mTabPageList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			TabDetailPager tabDetailPager = mTabPageList.get(position);
			container.addView(tabDetailPager.mRootView);
			tabDetailPager.initData();
			return tabDetailPager.mRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

}
