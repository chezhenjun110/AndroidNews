package com.czj.androidnews.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class LocalCacheUtils {
	private String CACHEPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/newscache";

	public void setCacheL(String url, Bitmap result) {
		try {
			String filename = MD5Utils.getMD5(url);
			File file = new File(CACHEPATH, filename);
			File parentFile = file.getParentFile();
			if (!parentFile.exists()) {// 如果文件夹不存在, 创建文件夹
				parentFile.mkdirs();
			}

			// 将图片保存在本地

			result.compress(CompressFormat.JPEG, 100, new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Bitmap getCacheL(String url) {
		try {
			String filename = MD5Utils.getMD5(url);
			File file = new File(CACHEPATH, filename);

			if (file.exists()) { // 判断文件是否存在

				Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
				return bitmap;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("文件不存在");
		}

		return null;
	}

}
