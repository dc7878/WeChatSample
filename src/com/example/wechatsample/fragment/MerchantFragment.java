package com.example.wechatsample.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.wechatsample.R;
import com.example.wechatsample.adapter.ListAdapter;
import com.example.wechatsample.adapter.MerchantAdapter;
import com.example.wechatsample.library.http.JsonHttpResponseHandler;
import com.example.wechatsample.library.pulltorefresh.PullFramelayoutListView;
import com.example.wechatsample.library.pulltorefresh.PullToRefreshBase;
import com.example.wechatsample.library.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.example.wechatsample.library.pulltorefresh.PullToRefreshListView;
import com.example.wechatsample.service.WebService;
import com.example.wechatsample.service.WebServiceEntity;
import com.example.wechatsample.service.WebServiceEntity.MerchantEntity;
import com.example.wechatsample.service.WebServiceHelper;
import com.example.wechatsample.utils.CommonUtil;
import com.example.wechatsample.utils.ToastUtil;

import android.R.integer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 聊天Fragment的界面
 * 
 * http://blog.csdn.net/guolin_blog/article/details/26365683
 * 
 * @author guolin
 */
public class MerchantFragment extends BaseFragment {
	
	private final String TAG = "MerchantFragment";
	
	public static SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
	
	private ListView listView = null;
	private PullFramelayoutListView mPullFramelayoutListView = null;
	private PullToRefreshListView mPullListView = null;
	
	private List<MerchantEntity> merList = new ArrayList<MerchantEntity>();
	private int mListLimit = 10; //每次加载条数
	private int lastListSize = 0;
	private MerchantAdapter merAdapter = null;
	private boolean isFirstLoad = false; //是否第一次加载
	
	public static MerchantFragment newInstance(){
		return new MerchantFragment();
	} 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_merchants, container, false);
		
		initUI(view);
		
		initListeners();
		
		//界面加载完再加载数据
		isFirstLoad = true;
		lazyLoad();
		
		return view;
	}

	private void initUI(View view) {
		mPullFramelayoutListView = (PullFramelayoutListView) view.findViewById(R.id.pull_listview_framelayout);
		mPullListView = mPullFramelayoutListView.getPullToRefreshListView();
		mPullListView.setPullLoadEnabled(false); //滑动到底部上拉加载
		mPullListView.setScrollLoadEnabled(true); //滑动到底部自动加载
		mPullListView.setPullRefreshEnabled(true); //下拉刷新
		
		listView = mPullListView.getRefreshableView();
		
	}
	
	private void initListeners() {
		// 加载失败
		mPullFramelayoutListView.setLoadingErrorClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mPullFramelayoutListView.startLoading();
				loadData();
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				Toast.makeText(getActivity(), merList.get(position).name, Toast.LENGTH_SHORT).show();
			}
		});
		
		mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				refreshData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadData();
			}
		});
	}
	
	@Override
	protected void lazyLoad() {
		if(isVisible && isFirstLoad){
			//刚开始就刷新
//			mPullListView.doPullRefreshing(true, 300);
			loadData();
		}
	}
	
	private void loadData() {
		if(CommonUtil.checkNetState(getActivity())){
			WebService.getInstance().requestMerchants(mListLimit, merList.size(), new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(int statusCode, JSONObject response) {
//					Log.d("chatsucc", response.toString());
					mPullFramelayoutListView.stopLoading();
					
					lastListSize = merList.size(); //上一次加载完后的总条数
					
					List<MerchantEntity> listLimit = WebServiceHelper.jsonToMerchantList(response);
					merList.addAll(listLimit);

					//如果是第一次加载
					if(isFirstLoad){
						setFirstLoadDoneState();
					}else{
						setToNormalState(); //恢复到正常状态
					}
				}
				
				@Override
				public void onFailure(Throwable e, JSONObject errorResponse) {
					Log.d("chatfail", errorResponse.toString());
					dealLoadDataError();
				}
			});
		}else{
			dealLoadDataError();
		}
		
	}
	
	private void setFirstLoadDoneState() {
		merAdapter = new MerchantAdapter(getActivity(), merList);
		listView.setAdapter(merAdapter);
		
		setLastUpdateTime();
		//如果不够10个（limit）或全部加载完了(某一次加载完后减去加载前<10说明完了)，显示没有更多了
        if(merList.size()<mListLimit || (merList.size() - lastListSize)< mListLimit){
        	mPullListView.setHasMoreData(false);
        }
        
		isFirstLoad = false; //首次加载置为false
	}
	
	private void setToNormalState() {
		merAdapter.notifyDataSetChanged();
		mPullListView.onPullDownRefreshComplete();
        mPullListView.onPullUpRefreshComplete();
        
        setLastUpdateTime();
        //如果不够limit个（limit）或全部加载完了(某一次加载完后减去加载前<limit说明完了)，显示没有更多了
        if(merList.size()<mListLimit || (merList.size() - lastListSize)< mListLimit){
        	mPullListView.setHasMoreData(false);
        }
        
	}
	
	private void setLastUpdateTime() {
        String text = formatDateTime(System.currentTimeMillis());
        mPullListView.setLastUpdatedLabel(text);
    }

    private String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }
        return mDateFormat.format(new Date(time));
    }
    
    /** 下拉刷新  */
    private void refreshData() {
    	if(CommonUtil.checkNetState(getActivity())){
			WebService.getInstance().requestMerchants(mListLimit, 0, new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(int statusCode, JSONObject response) {
//					Log.d("chatsucc", response.toString());
					mPullFramelayoutListView.stopLoading();
					
					List<MerchantEntity> listLimit = WebServiceHelper.jsonToMerchantList(response);
					
					//保留数据源前十个，删除后面所有的
					lastListSize = merList.size();
					for(int j=mListLimit;j<lastListSize;j++){
						merList.remove(mListLimit); //每次删除第11个
					}
					lastListSize = 0; //上一次加载完后的总条数
					
					//用刚刚获取的新数据替换保留的前十个数据，实现刷新
					for(int i=0;i<mListLimit;i++){
						merList.set(i, listLimit.get(i));
					}
					
					setToNormalState(); //恢复到正常状态
				}
				
				@Override
				public void onFailure(Throwable e, JSONObject errorResponse) {
					Log.d("chatfail", errorResponse.toString());
					dealLoadDataError();
				}
			});
		}else{
			dealLoadDataError();
		}
	}
    
    // 处理数据加载失败
    private void dealLoadDataError() {
    	if(isFirstLoad){
    		mPullFramelayoutListView.setErrorWhenLoading();
    	}else{
    		mPullListView.onPullDownRefreshComplete();
        	mPullListView.onPullUpRefreshComplete();
        	ToastUtil.showNetworkError(getActivity());
    	}
    	
	}
	
	@Override
	public String getFragmentName() {
		return TAG;
	}

}
