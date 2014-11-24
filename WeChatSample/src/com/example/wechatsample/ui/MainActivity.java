package com.example.wechatsample.ui;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.Toast;

import com.example.wechatsample.R;
import com.example.wechatsample.R.id;
import com.example.wechatsample.R.layout;
import com.example.wechatsample.R.menu;
import com.example.wechatsample.R.string;
import com.example.wechatsample.ui.demo.PullRefreshDemoActivity;
import com.example.wechatsample.ui.fragment.MainFragment;
import com.example.wechatsample.ui.fragment.MerchantFragment;
import com.example.wechatsample.ui.fragment.MoreOneFragment;
import com.example.wechatsample.ui.fragment.MoreTwoFragment;
import com.example.wechatsample.ui.fragment.ProductFragment;
import com.example.wechatsample.ui.fragment.SettingFragment;
import com.example.wechatsample.ui.fragment.WebFragment;
import com.example.wechatsample.utils.widget.PagerSlidingTabStrip;

/**
 * 高仿微信的主界面
 * 
 * http://blog.csdn.net/guolin_blog/article/details/26365683
 * 
 * @author guolin
 */
public class MainActivity extends FragmentActivity {
	
	private String[] titles = null;

	/**
	 * 店铺界面的Fragment
	 */
	private MerchantFragment merchantFragment;

	/**
	 * 商品界面的Fragment
	 */
	private ProductFragment productFragment;

	/**
	 * 主界面的Fragment
	 */
	private MainFragment mainFragment;
	
	/**
	 * 网页界面的Fragment
	 */
	private WebFragment webFragment;
	
	/**
	 * 我的资料界面的Fragment
	 */
	private SettingFragment setFragment;
	
	/**
	 * 我的资料界面的Fragment
	 */
	private MoreOneFragment moreOneFragment;
	
	/**
	 * 我的资料界面的Fragment
	 */
	private MoreTwoFragment moreTwoFragment;
	
	private List<Fragment> listFragments = new ArrayList<Fragment>();
	
	private FragmentManager fragmentManager;

	/**
	 * PagerSlidingTabStrip的实例
	 */
	private PagerSlidingTabStrip tabs;

	/**
	 * 获取当前屏幕的密度
	 */
	private DisplayMetrics dm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		titles = getResources().getStringArray(R.array.title_array);
		
		setOverflowShowingAlways();
		dm = getResources().getDisplayMetrics();
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		
		merchantFragment = MerchantFragment.newInstance();
		productFragment = ProductFragment.newInstance();
		mainFragment = MainFragment.newInstance();
		webFragment = WebFragment.newInstance();
		listFragments.add(mainFragment);
		listFragments.add(merchantFragment);
		listFragments.add(productFragment);
		listFragments.add(webFragment);
		
		pager.setOffscreenPageLimit(listFragments.size()-1);
		
		fragmentManager = getSupportFragmentManager();
		pager.setAdapter(new MyPagerAdapter(fragmentManager));
		pager.setCurrentItem(0);//设置当前显示标签页为第一页  
		tabs.setViewPager(pager);
		setTabsValue();
		
	}

	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值。
	 */
	private void setTabsValue() {
		// 设置Tab是自动填充满屏幕的，若为false则充满屏幕
		tabs.setShouldExpand(false);
		// 设置Tab的分割线是透明的
		tabs.setDividerColor(Color.TRANSPARENT);
		// 设置Tab底部线的高度
		tabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 1, dm));
		// 设置Tab Indicator的高度
		tabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, dm));
		// 设置Tab标题文字的大小
		tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, dm));
		// 设置Tab Indicator的颜色
//		tabs.setIndicatorColor(Color.parseColor("#45c01a"));
		tabs.setIndicatorColor(getResources().getColor(R.color.indicator_color));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		tabs.setSelectedTextColor(getResources().getColor(R.color.indicator_color));
		
		// 取消点击Tab时的背景色
		tabs.setTabBackground(0);
	}
	
	public class MyPagerAdapter extends FragmentStatePagerAdapter{

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public Fragment getItem(int arg0) {
			return listFragments.get(arg0);
		}

		@Override
		public int getCount() {
			return listFragments.size();
		}
		
	}

//	public class MyPagerAdapter extends FragmentStatePagerAdapter {
//		
////		private FragmentTransaction fragmentTransaction;
//
//		public MyPagerAdapter(FragmentManager fm) {
//			super(fm);
////			fragmentTransaction = fm.beginTransaction();
////			hideFragments(fragmentTransaction);
//		}
//
//		@Override
//		public CharSequence getPageTitle(int position) {
//			return titles[position];
//		}
//
//		@Override
//		public int getCount() {
//			return titles.length;
//		}
//
//		@Override
//		public Fragment getItem(int position) {
//			return listFragments.get(position);
////			switch (position) {
////			case 0:
////				if (chatFragment == null) {
////					chatFragment = new ChatFragment();
//////					fragmentTransaction.add
////				}
////				return chatFragment;
////			case 1:
////				if (foundFragment == null) {
////					foundFragment = new FoundFragment();
////				}
////				return foundFragment;
////			case 2:
////				if (contactsFragment == null) {
////					contactsFragment = new ContactsFragment();
////				}
////				return contactsFragment;
////			
////			default:
////				return null;
////			}
//		}
//
//	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_pullrefreshdemo: //PullRefreshDemo
			Intent intent = new Intent(this, PullRefreshDemoActivity.class);
			startActivity(intent);
			break;
		case R.id.action_collection: //我的收藏
			Toast.makeText(this, getString(R.string.action_collection), Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_card: //我的银行卡
			Toast.makeText(this, getString(R.string.action_card), Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_settings: //设置
			Toast.makeText(this, getString(R.string.action_settings), Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_feed: //意见反馈
			Toast.makeText(this, getString(R.string.action_feed), Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}

	private void setOverflowShowingAlways() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			menuKeyField.setAccessible(true);
			menuKeyField.setBoolean(config, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}