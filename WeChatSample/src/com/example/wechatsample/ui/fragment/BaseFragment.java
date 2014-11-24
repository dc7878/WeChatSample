package com.example.wechatsample.ui.fragment;

import com.example.wechatsample.utils.LogUtils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment基类
 * 
 * @author dewyze
 * 
 */
public abstract class BaseFragment extends Fragment {

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		LogUtils.i(getFragmentName(), " onAttach()");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtils.i(getFragmentName(), " onCreate()");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtils.i(getFragmentName(), " onCreateView()");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		LogUtils.i(getFragmentName(), " onViewCreated()");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LogUtils.i(getFragmentName(), " onActivityCreated()");
	}

	@Override
	public void onStart() {
		super.onStart();
		LogUtils.i(getFragmentName(), " onStart()");
	}

	@Override
	public void onResume() {
		super.onResume();
		LogUtils.i(getFragmentName(), " onResume()");
	}

	@Override
	public void onPause() {
		super.onPause();
		LogUtils.i(getFragmentName(), " onPause()");
	}

	@Override
	public void onStop() {
		super.onStop();
		LogUtils.i(getFragmentName(), " onStop()");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		LogUtils.i(getFragmentName(), " onDestroyView()");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtils.i(getFragmentName(), " onDestroy()");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		LogUtils.i(getFragmentName(), " onDetach()");
	}

	/**
	 * fragment name
	 */
	public abstract String getFragmentName();
	
	
	
	protected boolean isVisible;  
    /** 
     * 在这里实现Fragment数据的缓加载. 
     * @param isVisibleToUser 
     */  
    @Override  
    public void setUserVisibleHint(boolean isVisibleToUser) {  
        super.setUserVisibleHint(isVisibleToUser);  
        if(getUserVisibleHint()) {  
            isVisible = true;  
            onVisible();  
        } else {  
            isVisible = false;  
            onInvisible();  
        }  
    }  
  
    protected void onVisible(){  
        lazyLoad();  
    }  
  
    protected abstract void lazyLoad();  
  
    protected void onInvisible(){}  

}
