package com.czj.androidnews;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NewsDetailsActivity extends Activity {
	@ViewInject(R.id.wv_news)
	private WebView wv_news;
	private SharedPreferences sp;
	private WebSettings webSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_newsdetails);

		sp = getSharedPreferences("config", MODE_PRIVATE);
		ViewUtils.inject(this);
		Intent intent = getIntent();
		String url = intent.getStringExtra("url");

		initwebview(url);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("新闻详情页面被销毁了。");
	}

	private void initwebview(String url) {
		webSettings = wv_news.getSettings();
		webSettings.setJavaScriptEnabled(true);

		wv_news.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		if (url != null) {
			wv_news.loadUrl(url);
		} else {
			wv_news.loadUrl("www.baidu.com");
		}

	}

	@OnClick(R.id.ib_back)
	private void back(View view) {
		this.finish();
	}

	@OnClick(R.id.ib_share)
	private void share(View view) {

	}

	private int chosedchoice;

	@OnClick(R.id.ib_textsize)
	private void settextsize(View view) {
		int currentchosed = sp.getInt("textsize", 2);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("设置网页字体大小");
		String[] items = new String[] { "超大字体", "大字体", "标准字体", "小字体", "极小字体", };
		builder.setSingleChoiceItems(items, currentchosed, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				chosedchoice = which;

			}
		});
		builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Editor editor = sp.edit();
				editor.putInt("textsize", chosedchoice);
				editor.commit();
				switch (chosedchoice) {
				case 0:
					webSettings.setTextSize(TextSize.LARGEST);
					break;

				case 1:
					webSettings.setTextSize(TextSize.LARGER);
					break;
				case 2:
					webSettings.setTextSize(TextSize.NORMAL);
					break;
				case 3:
					webSettings.setTextSize(TextSize.SMALLEST);
					break;
				case 4:
					webSettings.setTextSize(TextSize.SMALLER);
					break;
				}

			}
		});
		builder.setPositiveButton("取消", null);
		builder.show();
	}

}
