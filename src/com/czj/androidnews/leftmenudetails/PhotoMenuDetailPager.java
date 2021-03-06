package com.czj.androidnews.leftmenudetails;

import java.util.ArrayList;

import com.czj.androidnews.R;
import com.czj.androidnews.base.BaseMenuDetailPager;
import com.czj.androidnews.domain.PhotoData;
import com.czj.androidnews.domain.PhotoData.PhotoInfo;
import com.czj.androidnews.global.GlobalContants;
import com.czj.androidnews.utils.CacheUtils;
import com.czj.androidnews.utils.MyBitmapUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 菜单详情页-组图
 * 
 * @author czj
 * 
 */
public class PhotoMenuDetailPager extends BaseMenuDetailPager {

	private ListView lvPhoto;
	private GridView gvPhoto;
	private ArrayList<PhotoInfo> mPhotoList;
	private PhotoAdapter mAdapter;
	private ImageButton btnPhoto;

	public PhotoMenuDetailPager(Activity activity, ImageButton btnPhoto) {
		super(activity);

		this.btnPhoto = btnPhoto;

		btnPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeDisplay();
			}
		});
	}

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.menu_photo_pager, null);

		lvPhoto = (ListView) view.findViewById(R.id.lv_photo);
		gvPhoto = (GridView) view.findViewById(R.id.gv_photo);

		return view;
	}

	@Override
	public void initData() {

		String cache = CacheUtils.getcache(mActivity, GlobalContants.PHOTOS_URL);

		if (!TextUtils.isEmpty(cache)) {

		}

		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, GlobalContants.PHOTOS_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = (String) responseInfo.result;
				parseData(result);
				// 设置缓存
				CacheUtils.setcache(mActivity, result, GlobalContants.PHOTOS_URL);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				error.printStackTrace();
			}
		});
	}

	protected void parseData(String result) {
		Gson gson = new Gson();
		PhotoData data = gson.fromJson(result, PhotoData.class);

		mPhotoList = data.data.news;// 获取组图列表集合

		if (mPhotoList != null) {
			mAdapter = new PhotoAdapter();
			lvPhoto.setAdapter(mAdapter);
			gvPhoto.setAdapter(mAdapter);
		}
	}

	class PhotoAdapter extends BaseAdapter {

		// private BitmapUtils utils;

		private MyBitmapUtils mbUtils;

		public PhotoAdapter() {
			// utils = new BitmapUtils(mActivity);
			// utils.configDefaultLoadingImage(R.drawable.news_pic_default);
			mbUtils = new MyBitmapUtils();
		}

		@Override
		public int getCount() {
			return mPhotoList.size();
		}

		@Override
		public PhotoInfo getItem(int position) {
			return mPhotoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.list_photo_item, null);

				holder = new ViewHolder();
				holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
				holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			PhotoInfo item = getItem(position);

			holder.tvTitle.setText(item.title);

			mbUtils.display(item.listimage, holder.ivPic);

			return convertView;
		}

	}

	static class ViewHolder {
		public TextView tvTitle;
		public ImageView ivPic;
	}

	private boolean isListDisplay = true;// 是否是列表展示

	/**
	 * 切换展现方式
	 */
	private void changeDisplay() {
		if (isListDisplay) {
			isListDisplay = false;
			lvPhoto.setVisibility(View.GONE);
			gvPhoto.setVisibility(View.VISIBLE);

			btnPhoto.setImageResource(R.drawable.icon_pic_list_type);

		} else {
			isListDisplay = true;
			lvPhoto.setVisibility(View.VISIBLE);
			gvPhoto.setVisibility(View.GONE);

			btnPhoto.setImageResource(R.drawable.icon_pic_grid_type);
		}
	}
}
