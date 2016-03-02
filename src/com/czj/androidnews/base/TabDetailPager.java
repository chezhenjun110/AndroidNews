package com.czj.androidnews.base;

import java.util.ArrayList;

import com.czj.androidnews.R;
import com.czj.androidnews.domain.NewsData.NewsTabData;
import com.czj.androidnews.domain.TabData;
import com.czj.androidnews.domain.TabData.TabNewsData;
import com.czj.androidnews.domain.TabData.TopNewsData;
import com.czj.androidnews.global.GlobalContants;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 页签详情页
 * 
 * @author czj
 * 
 */
public class TabDetailPager extends BaseMenuDetailPager implements OnPageChangeListener {
	private TabData tabData;
	private NewsTabData mTabData;
	private TextView tvText;
	@ViewInject(R.id.topnews_title)
	private TextView topnews_title;
	@ViewInject(R.id.vp_image)
	private ViewPager mViewPager;
	private ArrayList<TabNewsData> mTabNewsDatas;
	@ViewInject(R.id.lv_news)
	private ListView lv_news;
	@ViewInject(R.id.indicator)
	private CirclePageIndicator mcircle;
	private BitmapUtils bitmapUtils;

	private ArrayList<TopNewsData> mTopNewsDatasList;

	public TabDetailPager(Activity activity, NewsTabData newsTabData) {
		super(activity);
		mTabData = newsTabData;
		tabData = new TabData();

	}

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.tab_detailpage, null);
		View headerview = View.inflate(mActivity, R.layout.list_headerview, null);
		ViewUtils.inject(this, view);
		ViewUtils.inject(this, headerview);

		lv_news.addHeaderView(headerview);

		return view;

	}

	class ListViewAdapter extends BaseAdapter {

		public ListViewAdapter() {
			bitmapUtils = new BitmapUtils(mActivity);
		bitmapUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mTabNewsDatas.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = new ViewHolder();

			if (convertView != null) {
				System.out.println("复用view对象:" + position);
				holder = (ViewHolder) convertView.getTag();
			} else {
				System.out.println("创建新的view对象:" + position);
				holder = new ViewHolder();
				convertView = View.inflate(mActivity, R.layout.list_news_item, null);
				holder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
				holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
				holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
				convertView.setTag(holder);
			}

			TabNewsData item = mTabNewsDatas.get(position);

			holder.tv_title.setText(item.title);
			holder.tv_date.setText(item.pubdate);
			bitmapUtils.display(holder.iv_pic, item.listimage);
			return convertView;
		}
	}

	static class ViewHolder {
		private ImageView iv_pic;
		private TextView tv_title;
		private TextView tv_date;
	}

	class ViewPagernewsAdapter extends PagerAdapter {
		public ViewPagernewsAdapter() {
			bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);
		}

		@Override
		public int getCount() {

			return mTopNewsDatasList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView image = new ImageView(mActivity);
			image.setScaleType(ScaleType.FIT_XY);

			TopNewsData topNewsData = mTopNewsDatasList.get(position);
			String imageurl = topNewsData.topimage;

			bitmapUtils.display(image, imageurl);
			container.addView(image);
			return image;
		}
	}

	@Override
	public void initData() {
		getdatafromserver();
		if (mTopNewsDatasList != null) {

			mViewPager.setAdapter(new ViewPagernewsAdapter());
			mcircle.setViewPager(mViewPager);
			mcircle.setSnap(true);
			mcircle.setOnPageChangeListener(this);

			mcircle.onPageSelected(0);// 让指示器重新定位到第一个点

			topnews_title.setText(mTopNewsDatasList.get(0).title);
		}
		if (mTabNewsDatas != null) {
			lv_news.setAdapter(new ListViewAdapter());
		}
	}

	private void parser(String result) {
		Gson gson = new Gson();
		tabData = gson.fromJson(result, TabData.class);
		mTopNewsDatasList = tabData.data.topnews;
		mTabNewsDatas = tabData.data.news;
		System.out.println("新闻页解析得到数据：" + tabData.toString());

	}

	private void getdatafromserver() {
		String url = GlobalContants.SERVER_URL + mTabData.url;
		HttpUtils hu = new HttpUtils();
		hu.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException exception, String msg) {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String data = responseInfo.result;

				parser(data);

			}
		});

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		topnews_title.setText(mTopNewsDatasList.get(arg0).title);

	}

}
