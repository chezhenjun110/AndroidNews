package com.czj.androidnews.utils;

import android.content.Context;

public class CacheUtils {
	/**
	 * 设置缓存
	 * 
	 * @param context
	 * @param key
	 *            即为向服务器请求的url
	 * @param value
	 *            服务器返回的json字符串
	 */
	public static void setcache(Context context, String key, String value) {
		PrefUtils.setString(context, key, value);
	}

	/**
	 * 从sharedpreference中得到缓存的json数据
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getcache(Context context, String key) {
		String cache = PrefUtils.getString(context, key, null);
		return cache;
	}

}
