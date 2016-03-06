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
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewsDetailsActivity extends Activity {
	@ViewInject(R.id.wv_news)
	private WebView wv_news;
	private SharedPreferences sp;
	private WebSettings webSettings;
	private int currentchosed; // 记录当前选中的字体大小

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
		currentchosed = sp.getInt("textsize", 2);
		System.out.println("当前选中的为：" + currentchosed);
		setcurrentsize(currentchosed);
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

	/**
	 * 使用第三方分享平台sharesdk
	 * 
	 * @param view
	 */
	@OnClick(R.id.ib_share)
	private void share(View view) {

		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("我是分享文本");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		oks.show(this);
	}

	private int chosedchoice;

	@OnClick(R.id.ib_textsize)
	private void settextsize(View view) {
		System.out.println("");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("设置字体大小");
		String[] items = new String[] { "超大", "大", "标准", "小", "极小", };
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
				setcurrentsize(chosedchoice);
			}
		});
		builder.setPositiveButton("取消", null);
		builder.show();
	}

	private void setcurrentsize(int chosednumber) {
		switch (chosednumber) {
		case 0:
			webSettings.setTextSize(TextSize.LARGEST);
			System.out.println(TextSize.LARGEST);
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

}
