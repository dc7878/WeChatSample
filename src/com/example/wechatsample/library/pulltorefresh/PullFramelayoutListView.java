package com.example.wechatsample.library.pulltorefresh;

import com.example.wechatsample.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class PullFramelayoutListView extends FrameLayout{
	
	private View	  mCustomView = null;
	private LinearLayout ll_loading = null;
	private LinearLayout ll_loading_error = null;
	private PullToRefreshListView mPullListView = null;

	public PullFramelayoutListView(Context context) {
		super(context);
		
		initWithContext(context);
	}
	
	public PullFramelayoutListView(Context context, AttributeSet attrs){
		super(context, attrs);

		initWithContext(context);
	}
	
	public PullFramelayoutListView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);

		initWithContext(context);
	}
	
	private void initWithContext(Context context){
		mPullListView = new PullToRefreshListView(context);
		
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		addView(mPullListView, layoutParams);
		
		mCustomView = LayoutInflater.from(context).inflate(R.layout.view_loading, null);
		ll_loading = (LinearLayout) mCustomView.findViewById(R.id.pull_ll_loading);
		ll_loading_error = (LinearLayout) mCustomView.findViewById(R.id.pull_ll_loading_error);
		
		startLoading();
		addView(mCustomView, layoutParams);
	}
	
	public PullToRefreshListView getPullToRefreshListView(){
		return mPullListView;
	}
	
	//start loading
	public void startLoading(){
		mCustomView.setEnabled(false);
		mCustomView.setVisibility(View.VISIBLE);
		ll_loading.setVisibility(View.VISIBLE);
		ll_loading_error.setVisibility(View.GONE);
	}
	
	public void setLoadingErrorClickListener(OnClickListener onClickListener){
		mCustomView.setOnClickListener(onClickListener);
	}
	
	public void setErrorWhenLoading(){
		mCustomView.setEnabled(true);
		mCustomView.setVisibility(View.VISIBLE);
		ll_loading.setVisibility(View.GONE);
		ll_loading_error.setVisibility(View.VISIBLE);
	}

	//stop loading
	public void stopLoading() {
		mCustomView.setVisibility(View.GONE);
	}
}
