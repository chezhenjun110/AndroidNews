package com.czj.androidnews.utils;

import javax.security.auth.PrivateCredentialPermission;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class MyBitmapUtils {
	private NetCacheUtils net;
	private LocalCacheUtils local;
	private MemoryCacheUtils memory;

	public MyBitmapUtils() {
		local = new LocalCacheUtils();
		memory = new MemoryCacheUtils();
		net = new NetCacheUtils(memory, local);

	}

	public void display(String url, ImageView iv) {
		Bitmap bitmap;
		bitmap = memory.getCacheM(url);
		if (bitmap != null) {
			iv.setImageBitmap(bitmap);
			System.out.println("从内存加载图片");
			return;
		}
		bitmap = local.getCacheL(url);
		if (bitmap != null) {
			iv.setImageBitmap(bitmap);
			memory.setCacheM(url, bitmap);
			System.out.println("从本地加载图片");

			return;
		}
		net.getbitmapfromserver(url, iv);

	}

}
