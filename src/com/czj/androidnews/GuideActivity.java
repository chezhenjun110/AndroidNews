package com.czj.androidnews;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.czj.androidnews.utils.PrefUtils;;

/**
 * 鏂版墜寮曞
 * 
 * @author Kevin
 * 
 */
public class GuideActivity extends Activity {

	private static final int[] mImageIds = new int[] { R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3 };

	private ViewPager vpGuide;
	private ArrayList<ImageView> mImageViewList;

	private LinearLayout llPointGroup;// 寮曞鍦嗙偣鐨勭埗鎺т欢

	private int mPointWidth;// 鍦嗙偣闂寸殑璺濈

	private View viewRedPoint;// 灏忕孩鐐�

	private Button btnStart;// 寮�濮嬩綋楠�

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);// 鍘绘帀鏍囬
		setContentView(R.layout.activity_guide);
		vpGuide = (ViewPager) findViewById(R.id.vp_guide);
		llPointGroup = (LinearLayout) findViewById(R.id.ll_point_group);
		viewRedPoint = findViewById(R.id.view_red_point);
		btnStart = (Button) findViewById(R.id.btn_start);

		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 鏇存柊sp, 琛ㄧず宸茬粡灞曠ず浜嗘柊鎵嬪紩瀵�
				PrefUtils.setBoolean(GuideActivity.this, "is_user_guide_showed", true);

				// 璺宠浆涓婚〉闈�
				startActivity(new Intent(GuideActivity.this, MainActivity.class));
				finish();
			}
		});

		initViews();
		vpGuide.setAdapter(new GuideAdapter());

		vpGuide.setOnPageChangeListener(new GuidePageListener());
	}

	/**
	 * 鍒濆鍖栫晫闈�
	 */
	private void initViews() {
		mImageViewList = new ArrayList<ImageView>();

		// 鍒濆鍖栧紩瀵奸〉鐨�3涓〉闈�
		for (int i = 0; i < mImageIds.length; i++) {
			ImageView image = new ImageView(this);
			image.setBackgroundResource(mImageIds[i]);// 璁剧疆寮曞椤佃儗鏅�
			mImageViewList.add(image);
		}

		// 鍒濆鍖栧紩瀵奸〉鐨勫皬鍦嗙偣
		for (int i = 0; i < mImageIds.length; i++) {
			View point = new View(this);
			point.setBackgroundResource(R.drawable.shape_point_gray);// 璁剧疆寮曞椤甸粯璁ゅ渾鐐�

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
			if (i > 0) {
				params.leftMargin = 10;// 璁剧疆鍦嗙偣闂撮殧
			}

			point.setLayoutParams(params);// 璁剧疆鍦嗙偣鐨勫ぇ灏�

			llPointGroup.addView(point);// 灏嗗渾鐐规坊鍔犵粰绾挎�у竷灞�
		}

		// 鑾峰彇瑙嗗浘鏍�, 瀵筶ayout缁撴潫浜嬩欢杩涜鐩戝惉
		llPointGroup.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			// 褰搇ayout鎵ц缁撴潫鍚庡洖璋冩鏂规硶
			@Override
			public void onGlobalLayout() {
				System.out.println("layout 缁撴潫");
				llPointGroup.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				mPointWidth = llPointGroup.getChildAt(1).getLeft() - llPointGroup.getChildAt(0).getLeft();
				System.out.println("鍦嗙偣璺濈:" + mPointWidth);
			}
		});
	}

	/**
	 * ViewPager鏁版嵁閫傞厤鍣�
	 * 
	 * @author Kevin
	 * 
	 */
	class GuideAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mImageIds.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mImageViewList.get(position));
			return mImageViewList.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	/**
	 * viewpager鐨勬粦鍔ㄧ洃鍚�
	 * 
	 * @author Kevin
	 * 
	 */
	class GuidePageListener implements OnPageChangeListener {

		// 婊戝姩浜嬩欢
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			// System.out.println("褰撳墠浣嶇疆:" + position + ";鐧惧垎姣�:" +
			// positionOffset
			// + ";绉诲姩璺濈:" + positionOffsetPixels);
			int len = (int) (mPointWidth * positionOffset) + position * mPointWidth;
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewRedPoint.getLayoutParams();// 鑾峰彇褰撳墠绾㈢偣鐨勫竷灞�鍙傛暟
			params.leftMargin = len;// 璁剧疆宸﹁竟璺�

			viewRedPoint.setLayoutParams(params);// 閲嶆柊缁欏皬绾㈢偣璁剧疆甯冨眬鍙傛暟
		}

		// 鏌愪釜椤甸潰琚�変腑
		@Override
		public void onPageSelected(int position) {
			if (position == mImageIds.length - 1) {// 鏈�鍚庝竴涓〉闈�
				btnStart.setVisibility(View.VISIBLE);// 鏄剧ず寮�濮嬩綋楠岀殑鎸夐挳
			} else {
				btnStart.setVisibility(View.INVISIBLE);
			}
		}

		// 婊戝姩鐘舵�佸彂鐢熷彉鍖�
		@Override
		public void onPageScrollStateChanged(int state) {

		}

	}

}
