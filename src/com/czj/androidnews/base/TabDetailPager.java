package com.czj.androidnews.base;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.http.HttpConnection;

import com.czj.androidnews.NewsDetailsActivity;
import com.czj.androidnews.R;
import com.czj.androidnews.domain.NewsData.NewsTabData;
import com.czj.androidnews.domain.TabData;
import com.czj.androidnews.domain.TabData.TabNewsData;
import com.czj.androidnews.domain.TabData.TopNewsData;
import com.czj.androidnews.global.GlobalContants;
import com.czj.androidnews.utils.CacheUtils;
import com.czj.androidnews.utils.PrefUtils;
import com.czj.androidnews.view.TouchDownRefreshListview;
import com.czj.androidnews.view.TouchDownRefreshListview.RefreshListener;
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
import android.app.usage.UsageEvents.Event;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 页签详情页
 * 
 * @author czj
 * 
 */
public class TabDetailPager extends BaseMenuDetailPager
		implements OnPageChangeListener, OnItemClickListener, RefreshListener {
	private TabData tabData;
	private NewsTabData mTabData;
	private TextView tvText;
	@ViewInject(R.id.topnews_title)
	private TextView topnews_title;
	@ViewInject(R.id.vp_image)
	private ViewPager mViewPager;
	private ArrayList<TabNewsData> mTabNewsDatas;
	@ViewInject(R.id.lv_news)
	private TouchDownRefreshListview lv_news;
	@ViewInject(R.id.indicator)
	private CirclePageIndicator mcircle;
	private BitmapUtils bitmapUtils;
	private Handler handler;
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
		lv_news.setOnItemClickListener(this);
		lv_news.setOnRefreshListener(this);
		return view;

	}

	/**
	 * ListView的适配器，初始化listview的数据
	 * 
	 * @author czj
	 *
	 */
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

			return null;
		}

		@Override
		public long getItemId(int position) {

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
			holder.tv_title.setText(item.title);
			String ids = PrefUtils.getString(mActivity, "read_ids", "");
			if (ids.contains(item.id)) {
				holder.tv_title.setTextColor(Color.GRAY);
			} else {
				holder.tv_title.setTextColor(Color.BLACK);
			}
			holder.tv_date.setText(item.pubdate);
			// bitmapUtils.display(holder.iv_pic, item.listimage);

			setimage(item.listimage, holder.iv_pic);
			return convertView;
		}

	}

	private void setimage(final String imageurl, final ImageView iv) {

		new Thread() {

			public void run() {

				try {
					URL url = new URL(imageurl);

					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(5000);
					connection.setReadTimeout(5000);
					connection.connect();
					if (connection.getResponseCode() == 200) {
						InputStream iStream = connection.getInputStream();
						final Bitmap bitmap = BitmapFactory.decodeStream(iStream);
						mActivity.runOnUiThread(new Runnable() {
							public void run() {
								iv.setImageBitmap(bitmap);
								System.out.println("加载图片。。。");
							}
						});
					}
				} catch (Exception e) {

					e.printStackTrace();

				}

			};
		}.start();

	}

	static class ViewHolder {
		private ImageView iv_pic;
		private TextView tv_title;
		private TextView tv_date;
	}

	/**
	 * 新闻头条图片viewpager的数据源
	 * 
	 * @author czj
	 *
	 */
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
			image.setOnTouchListener(new topnewlistener());
			container.addView(image);
			return image;
		}
	}

	class topnewlistener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				System.out.println("按下");
				handler.removeCallbacksAndMessages(null);// 删除Handler中的所有消息

				break;
			case MotionEvent.ACTION_CANCEL:
				System.out.println("事件取消");
				handler.sendEmptyMessageDelayed(0, 3000);
				break;
			case MotionEvent.ACTION_UP:
				System.out.println("抬起");
				handler.sendEmptyMessageDelayed(0, 3000);
				break;
			default:
				break;
			}
			return true;
		}
	}

	/**
	 * 初始化数据
	 */
	@Override
	public void initData() {
		String url = GlobalContants.SERVER_URL + mTabData.url;
		String cache = CacheUtils.getcache(mActivity, url);
//		if (!TextUtils.isEmpty(cache)) {
//			System.out.println("从缓存中读取数据");
//			parser(cache);
//		}
		getdatafromserver(url);

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
		if (handler == null) {
			handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {

					int current = mViewPager.getCurrentItem();
					if (current < mTopNewsDatasList.size() - 1) {
						current++;
					} else {
						current = 0;
					}
					mViewPager.setCurrentItem(current);
					handler.sendEmptyMessageDelayed(0, 4000);
				}
			};
			handler.sendEmptyMessageDelayed(0, 4000);
		}
	}

	/**
	 * 格式化从服务器得到的字符串
	 * 
	 * @param result
	 */
	private void parser(String result) {
		Gson gson = new Gson();
		tabData = gson.fromJson(result, TabData.class);
		mTopNewsDatasList = tabData.data.topnews;
		mTabNewsDatas = tabData.data.news;

	}

	/**
	 * 使用xutils框架从服务器得到Json数据并格式化
	 */
	private void getdatafromserver(final String url) {

		HttpUtils hu = new HttpUtils();
		hu.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException exception, String msg) {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String data = responseInfo.result;

				CacheUtils.setcache(mActivity, url, data);
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
		int newposition = position - 2;
		String newsdetailsurl = mTabNewsDatas.get(newposition).url;// 拿到新闻详情页的url
		System.out.println("点击位置：" + newposition);

		String ids = PrefUtils.getString(mActivity, "read_ids", "");
		String readId = mTabNewsDatas.get(newposition).id; // 拿到当前点击新闻的id
		if (!ids.contains(readId)) {
			ids = ids + readId + ",";
			PrefUtils.setString(mActivity, "read_ids", ids);
		}
		changwatchstate(view);
		Intent intent = new Intent(mActivity, NewsDetailsActivity.class);
		intent.putExtra("url", newsdetailsurl);
		mActivity.startActivity(intent);

	}

	private void changwatchstate(View view) {
		TextView tv = (TextView) view.findViewById(R.id.tv_title);
		tv.setTextColor(Color.GRAY); // 如果读过则把标题颜色换成灰色
	}

	@Override
	public void refresh() {
		try {
			Thread.sleep(2000);
			Toast.makeText(mActivity, "刷新成功", Toast.LENGTH_SHORT).show();
			lv_news.onRefreshComplete();
		} catch (InterruptedException e) {

			e.printStackTrace();
			Toast.makeText(mActivity, "刷新失败", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void loadmore() {
		// TODO Auto-generated method stub

	}

}
