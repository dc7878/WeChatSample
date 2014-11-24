package com.example.wechatsample.utils.widget;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.wechatsample.R;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MyProductDetailViewPage extends ViewPager{
	
	private Activity mActivity = null;
	private List<View> mListViews = null;
	
	private ImageView[] imageViews = null; //小圆点
	
	public MyProductDetailViewPage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/** 
	 * 广告图片轮播
	 * @param activity
	 * @param imgList 广告图片
	 * @param ll_dotLayout 显示小圆点
	 */
	public void setProductDetailViewPager(Activity activity, List<View> imgList, LinearLayout ll_dotLayout) {
		this.mActivity = activity;
		this.mListViews = imgList;
		
		setDotImg(ll_dotLayout);
		
		this.setAdapter(new AdvAdapter(mListViews));
		this.setOnPageChangeListener(new GuidePageChangeListener());
		
	}
	
	/** 小圆点  */
	private void setDotImg(LinearLayout ll_dotLayout) {
		imageViews = new ImageView[mListViews.size()];
		for(int i=0;i<imageViews.length;i++){
			ImageView imgDot = new ImageView(mActivity);
			imgDot.setLayoutParams(new android.view.ViewGroup.LayoutParams(25,25));
			imageViews[i] = imgDot;
			if(i == 0){
				imageViews[i].setBackgroundResource(R.drawable.banner_dian_focus);
			}else{
				imageViews[i].setBackgroundResource(R.drawable.banner_dian_blur);
			}
			ll_dotLayout.addView(imageViews[i]);
		}
	}
	
	private final class AdvAdapter extends PagerAdapter {
		private List<View> views = null;

		public AdvAdapter(List<View> views) {
			this.views = views;
		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public Object instantiateItem(View arg0, int position) {
			((ViewPager) arg0).addView(views.get(position), 0);
			return views.get(position);
		}
		
		@Override
		public void destroyItem(View arg0, int position, Object arg2) {
			((ViewPager) arg0).removeView(views.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}
		
		@Override
		public void finishUpdate(View arg0) {

		}

	}
	
	private final class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			for (int i = 0; i < imageViews.length; i++) {
				if (position == i) {
					imageViews[position].setBackgroundResource(R.drawable.banner_dian_focus);
				}else{
					imageViews[i].setBackgroundResource(R.drawable.banner_dian_blur);
				}
			}

		}

	}
	
}
