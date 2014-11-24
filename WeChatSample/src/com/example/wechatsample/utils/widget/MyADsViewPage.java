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

public class MyADsViewPage extends ViewPager{
	
	private Context mContext = null;
	private List<View> mListViews = null;
	private Long mSleepTime = 0l;
	
	private LinearLayout ll_dotLayout = null;
	private ImageView[] imageViews = null; //小圆点
	
	private AtomicInteger what = new AtomicInteger(0);
	private boolean isContinue = true;

	public MyADsViewPage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/** 
	 * 广告图片轮播
	 * @param activity
	 * @param imgList 广告图片
	 * @param ll_dotLayout 显示小圆点
	 * @param sleepTime 轮播睡眠时间
	 */
	public void setADsViewPager(Context context, List<View> imgList, LinearLayout ll_dotLayout, long sleepTime) {
		this.mContext = context;
		this.mListViews = imgList;
		this.mSleepTime = sleepTime;
		
		setDotImg(ll_dotLayout);
		
		this.setAdapter(new AdvAdapter(mListViews));
		this.setOnPageChangeListener(new GuidePageChangeListener());
		this.setOnTouchListener(new MyOnTouchListener());
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (isContinue) {
						try {
							viewHandler.sendEmptyMessage(what.get());
							whatOption();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}

		}).start();
	}
	
	/** 小圆点  */
	private void setDotImg(LinearLayout ll_dotLayout) {
		imageViews = new ImageView[mListViews.size()];
		for(int i=0;i<imageViews.length;i++){
			ImageView imgDot = new ImageView(mContext);
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
			return Integer.MAX_VALUE;
		}

		@Override
		public Object instantiateItem(View arg0, int position) {
			int index = position % views.size();// ���㵱ǰ��ʾ��itemview
			((ViewPager) arg0).addView(views.get(index), 0);
			return views.get(index);
		}
		
		@Override
		public void destroyItem(View arg0, int position, Object arg2) {
			((ViewPager) arg0).removeView(views.get(position % views.size()));
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
			what.getAndSet(position);
			int index = position % imageViews.length;// ���㵱ǰ��ʾ��itemview
			for (int i = 0; i < imageViews.length; i++) {
				if (index == i) {
					imageViews[index].setBackgroundResource(R.drawable.banner_dian_focus);
				}else{
					imageViews[i].setBackgroundResource(R.drawable.banner_dian_blur);
				}
			}

		}

	}
	
	private final class MyOnTouchListener implements OnTouchListener{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: //按下
			case MotionEvent.ACTION_MOVE: //滑动  手指未离开viewpager
				isContinue = false;
				break;
			case MotionEvent.ACTION_UP:
				isContinue = true;
				break;
			default:
				isContinue = true;
				break;
			}
			return false;
		}
		
	}
	
	private final Handler viewHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			setCurrentItem(msg.what);
			super.handleMessage(msg);
		}
	};
	
	private void whatOption() {
		what.incrementAndGet();
//		if (what.get() > imageViews.length - 1) {
//			what.getAndAdd(-4);
//		}
		try {
			Thread.sleep(mSleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void setContinue(boolean isContinue){
		this.isContinue = isContinue;
	}

}
