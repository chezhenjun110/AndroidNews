package com.czj.androidnews.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class MemoryCacheUtils {
	private LruCache<String, Bitmap> mLruCache;

	public MemoryCacheUtils() {
		long maxmemory = Runtime.getRuntime().maxMemory();
		int maxSize = (int) (maxmemory / 8);

		System.out.println("虚拟机总内存为" + maxmemory + ";分配到的内存为" + maxSize);
		mLruCache = new LruCache<String, Bitmap>(maxSize) {

			@Override
			protected int sizeOf(String key, Bitmap value) {
				int bytecount = value.getHeight() * value.getRowBytes();
				return bytecount / 1024;
			}

		};

	}

	public void setCacheM(String url, Bitmap result) {
		
		if (result!= null) {
			mLruCache.put(url, result);
		}
		
		

	}

	public Bitmap getCacheM(String url) {
		return mLruCache.get(url);
	}
}
