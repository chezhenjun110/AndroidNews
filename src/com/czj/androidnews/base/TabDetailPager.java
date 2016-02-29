package com.czj.androidnews.base;

import com.czj.androidnews.R;
import com.czj.androidnews.domain.NewsData.NewsTabData;
import com.czj.androidnews.domain.TabData;
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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;

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
	@ViewInject(R.id.lv_news)
	private ListView lv_news;
	private BitmapUtils bitmapUtils;

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
		mViewPager.setAdapter(new MynewsAdapter());
		return view;

	}

	class MynewsAdapter extends PagerAdapter {

		public MynewsAdapter() {
			bitmapUtils = new BitmapUtils(mActivity);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return tabData.data.topnews.size();
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
			ImageView imageView = new ImageView(mActivity);
			imageView.setScaleType(ScaleType.FIT_XY);

			String imageurl = tabData.data.topnews.get(position).topimage;

			bitmapUtils.display(imageView, imageurl);
			container.addView(imageView);
			return imageView;
		}
	}

	@Override
	public void initData() {
		String url = GlobalContants.SERVER_URL + mTabData.url;
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String msg) {
				System.out.println("解析新闻页出现错误");
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				parser(result);

			}

			private void parser(String result) {
				Gson gson = new Gson();
				tabData = gson.fromJson(result, TabData.class);
				System.out.println("新闻页解析得到数据：" + tabData);

			}
		});
	}

}
