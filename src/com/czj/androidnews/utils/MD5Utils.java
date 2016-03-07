package com.czj.androidnews.utils;

import java.security.MessageDigest;

public class MD5Utils {
	public static String getMD5(String name) {

		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {

			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] byteinput = name.getBytes(); // 将字符串转为字节数组

			messageDigest.update(byteinput);
			// 获得密文
			byte[] md = messageDigest.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
