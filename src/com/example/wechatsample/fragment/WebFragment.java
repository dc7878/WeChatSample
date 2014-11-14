package com.example.wechatsample.fragment;

import com.example.wechatsample.R;
import com.example.wechatsample.utils.widget.ProgressWebView;

import android.R.integer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

/**
 * 通讯录Fragment的界面
 * 
 * http://blog.csdn.net/guolin_blog/article/details/26365683
 * 
 * @author guolin
 */
public class WebFragment extends BaseFragment {
	
	private final String TAG = "WebFragment";
	
	private ProgressWebView pWebView = null;
	private WebSettings mWebSettings;
	
	private boolean isFirstLoad = false; //是否第一次加载
	private int index = 0;
	
	public static WebFragment newInstance(){
		return new WebFragment();
	} 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_web, container, false);
		
		initUI(view);
		
		//界面加载完再加载数据
		isFirstLoad = true;
		lazyLoad();
		
		return view;
	}

	private void initUI(View view) {
		pWebView = (ProgressWebView) view.findViewById(R.id.web);
		mWebSettings = pWebView.getSettings();
		mWebSettings.setJavaScriptEnabled(true); //允许加载JavaScript
		mWebSettings.setSupportZoom(true); //允许缩放
		mWebSettings.setBuiltInZoomControls(true); //原网页基础上缩放
		mWebSettings.setUseWideViewPort(true); //任意缩放
		
		/**在点击请求的是链接时才会调用，重写此方法返回true表明点击网页里
                                面的链接还是在当前的WebView里跳转，不会跳到浏览器上运行。 */
		pWebView.setWebViewClient(new WebViewClient(){
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				view.loadUrl(loadUrl());
				return super.shouldOverrideUrlLoading(view, url);
			}
		});
		
//		pWebView.setDownloadListener(new DownloadListener() {
//			
//			@Override
//			public void onDownloadStart(String arg0, String arg1, String arg2,
//					String arg3, long arg4) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
	}
	
	@Override
	protected void lazyLoad() {
		if(isVisible && isFirstLoad){
			pWebView.loadUrl(loadUrl());
			index++;
			if(index == 6){
				index = 0;
			}
		}
	}
	
	private String loadUrl(){
		String[] urls = new String[]{"http://m.taobao.com/?sid=1c8f7f6796b9a3428f8ebc7d0c63ab93&an=1",
									 "http://i.ifeng.com/?ch=ucweb2009",
									 "http://m.mahua.com",
									 "http://m.2280.com",
									 "http://m.1518.com",
									 "http://m.oicq88.com"};
		return urls[index];
	}

	@Override
	public String getFragmentName() {
		return TAG;
	}

}
