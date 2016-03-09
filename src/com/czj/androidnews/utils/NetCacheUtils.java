package com.czj.androidnews.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class NetCacheUtils {
	private MemoryCacheUtils memory;
	private LocalCacheUtils local;

	public NetCacheUtils(MemoryCacheUtils memoryCacheUtils, LocalCacheUtils localCacheUtils) {
		memory = memoryCacheUtils;
		local = localCacheUtils;
	}

	public void getbitmapfromserver(String url, ImageView ivPic) {
		new BitmapTask().execute(url, ivPic);
	}

	class BitmapTask extends AsyncTask<Object, Integer, Bitmap> {
		ImageView ivPic;
		String url;

		@Override
		protected Bitmap doInBackground(Object... params) {
			url = (String) params[0];
			ivPic = (ImageView) params[1];
			ivPic.setTag(url);
			return Downloader(url);
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (result != null) {
				String bind = (String) ivPic.getTag();
				if (url.equals(bind)) {
					ivPic.setImageBitmap(result);
					memory.setCacheM(url, result);
					local.setCacheL(url, result);
					System.out.println("从网络加载图片");
				}

			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			super.onProgressUpdate(values);
		}

	}

	private Bitmap Downloader(String biturl) {
		URL url;
		try {
			url = new URL(biturl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setReadTimeout(5000);
			connection.setReadTimeout(5000);
			connection.connect();
			if (connection.getResponseCode() == 200) {
				InputStream inputStream = connection.getInputStream();
				// 图片压缩处理
				BitmapFactory.Options option = new BitmapFactory.Options();
				option.inSampleSize = 2;// 宽高都压缩为原来的二分之一, 此参数需要根据图片要展示的大小来确定
				option.inPreferredConfig = Bitmap.Config.RGB_565;// 设置图片格式
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, option);
				return bitmap;
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}
}
