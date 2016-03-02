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

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceActivity.Header;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
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
public class TabDetailPager extends BaseMenuDetailPager {
	private TabData tabData;
	private NewsTabData mTabData;
	private TextView tvText;
	private ViewPager mViewPager;
	private ArrayList<TabNewsData> mTabNewsDatas;
	private ListView lv_news;
	private BitmapUtils bitmapUtils;
	private ImageView iv_pic;
	private ArrayList<TopNewsData> mTopNewsDatasList;

	public TabDetailPager(Activity activity, NewsTabData newsTabData) {
		super(activity);
		mTabData = newsTabData;
		tabData = new TabData();

	}

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.tab_detailpage, null);
		ViewUtils.inject(this, view);
		mViewPager = (ViewPager) view.findViewById(R.id.vp_image);
		lv_news = (ListView) view.findViewById(R.id.lv_news);
		LayoutInflater li = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View headerview = li.inflate(R.layout.list_headerview, lv_news, false);
		iv_pic = (ImageView) headerview.findViewById(R.id.iv_pic);

		return view;

	}

	class ListViewAdapter extends BaseAdapter {

		public ListViewAdapter() {
			bitmapUtils = new BitmapUtils(mActivity);
			// TODO Auto-generated constructor stub
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
				holder = (ViewHolder) convertView.getTag();
			} else {
				holder = new ViewHolder();
				convertView = View.inflate(mActivity, R.layout.list_news_item, null);
				holder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
				holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
				holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
				convertView.setTag(holder);
			}
			
			TabNewsData item = mTabNewsDatas.get(position);
			bitmapUtils.display(holder.iv_pic, item.listimage);
			holder.tv_title.setText(item.title);
			holder.tv_date.setText(item.pubdate);
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
			bitmapUtils.configDefaultLoadFailedImage(R.drawable.topnews_item_default);
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

			iv_pic.setScaleType(ScaleType.FIT_XY);

			TopNewsData topNewsData = mTopNewsDatasList.get(position);
			String imageurl = topNewsData.topimage;

			bitmapUtils.display(iv_pic, imageurl);
			container.addView(iv_pic);
			return iv_pic;
		}
	}

	@Override
	public void initData() {
		getdatafromserver();
		if (mTopNewsDatasList != null) {
			mViewPager.setAdapter(new ViewPagernewsAdapter());
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

}
