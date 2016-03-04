package com.czj.androidnews;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class NewsDetailsActivity extends Activity {
	@ViewInject(R.id.wv_news)
	private WebView wv_news;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newsdetails);
		ViewUtils.inject(this);
		Intent intent = getIntent();
		String url = intent.getStringExtra("url");
		System.out.println("拿到url：" + url);
		initwebview(url);
		WebSettings webSettings = wv_news.getSettings();
		webSettings.setJavaScriptEnabled(true);
	}

	private void initwebview(String url) {
		if (url != null) {
			wv_news.loadUrl(url);
		} else {
			wv_news.loadUrl("www.baidu.com");
		}

	}
}
