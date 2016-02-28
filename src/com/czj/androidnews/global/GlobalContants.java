package com.czj.androidnews.global;

/**
 * 定义全局参数
 * 
 * @author czj
 * 
 * 
 */
public class GlobalContants {
	// 10.0.2.2 为默认的本地主机名
	public static final String SERVER_URL = "http://10.0.2.2:8080/zhbj";// 服务器本地文件的路径

	public static final String CATEGORIES_URL = SERVER_URL + "/categories.json";// 获取分类信息的接口
	public static final String PHOTOS_URL = SERVER_URL + "/photos/photos_1.json";// 获取组图信息的接口

}
