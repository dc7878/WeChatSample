package com.example.wechatsample.library.pulltorefresh;

import com.example.wechatsample.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class PullFramelayoutGridView extends FrameLayout{
	
	private View	  mCustomView = null;
	private LinearLayout ll_loading = null;
	private LinearLayout ll_loading_error = null;
	private PullToRefreshGridView mPullGridView = null;

	public PullFramelayoutGridView(Context context) {
		super(context);
		
		initWithContext(context);
	}
	
	public PullFramelayoutGridView(Context context, AttributeSet attrs){
		super(context, attrs);

		initWithContext(context);
	}
	
	public PullFramelayoutGridView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);

		initWithContext(context);
	}
	
	private void initWithContext(Context context){
		mPullGridView = new PullToRefreshGridView(context);
		
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		addView(mPullGridView, layoutParams);
		
		mCustomView = LayoutInflater.from(context).inflate(R.layout.view_loading, null);
		ll_loading = (LinearLayout) mCustomView.findViewById(R.id.pull_ll_loading);
		ll_loading_error = (LinearLayout) mCustomView.findViewById(R.id.pull_ll_loading_error);
		
		startLoading();
		addView(mCustomView, layoutParams);
	}
	
	public PullToRefreshGridView getPullToRefreshGridView(){
		return mPullGridView;
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
