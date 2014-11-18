package com.example.wechatsample.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.wechatsample.R;
import com.example.wechatsample.adapter.ListAdapter;
import com.example.wechatsample.library.http.JsonHttpResponseHandler;
import com.example.wechatsample.service.WebService;
import com.example.wechatsample.utils.DialogUtil;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

/**
 * 通讯录Fragment的界面
 * 
 * http://blog.csdn.net/guolin_blog/article/details/26365683
 * 
 * @author guolin
 */
public class MainFragment extends BaseFragment {
	
	private final String TAG = "ContactsFragment";
	
	private LinearLayout ll_loading = null;
	private ListView listView = null;
	
	private List<String> merNameList = new ArrayList<String>();
	private int mListLimit = 10; //每次加载条数
	private int lastListSize = 0;
	private ListAdapter listAdapter = null;
	
	public static MainFragment newInstance(){
		return new MainFragment();
	} 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_contacts, container, false);
		
		initView(view);
		
		return view;
	}

	private void initView(View view) {
		ll_loading = (LinearLayout) view.findViewById(R.id.contacts_ll_loading);
		listView = (ListView) view.findViewById(R.id.contacts_listview);
		
		listAdapter = new ListAdapter(getActivity(), merNameList);
		listView.setAdapter(listAdapter);
	}
	
	@Override
	protected void lazyLoad() {
		
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser){ //fragment可见时加载数据
			if(merNameList.size()<=0){
				loadData();
			}else{
				setLoadVisible(false);
			}
		}
	}
	
	private void loadData() {
		WebService.getInstance().requestMerchants(mListLimit, merNameList.size(), new  JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, JSONObject response) {
//				Log.d("contactssucc", response.toString());
				JSONArray ja= response.optJSONArray("records");
				for(int i=0;i<ja.length();i++){
					JSONObject jb = ja.optJSONObject(i);
					merNameList.add(jb.optString("phone_num"));
				}
				listAdapter.notifyDataSetChanged();
				setLoadVisible(false);
			}
			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				Log.d("contactsfail", errorResponse.toString());
			}
		});
	}
	
	private void setLoadVisible(boolean isLoading) {
		if(isLoading){
			ll_loading.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		}else{
			ll_loading.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public String getFragmentName() {
		return TAG;
	}

}
