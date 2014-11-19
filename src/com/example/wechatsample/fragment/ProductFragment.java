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
import com.example.wechatsample.adapter.ProductAdapter;
import com.example.wechatsample.library.http.JsonHttpResponseHandler;
import com.example.wechatsample.library.pulltorefresh.PullFramelayoutGridView;
import com.example.wechatsample.library.pulltorefresh.PullToRefreshBase;
import com.example.wechatsample.library.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.example.wechatsample.library.pulltorefresh.PullToRefreshGridView;
import com.example.wechatsample.service.WebService;
import com.example.wechatsample.service.WebServiceEntity.MerchantEntity;
import com.example.wechatsample.service.WebServiceEntity.ProductEntity;
import com.example.wechatsample.service.WebServiceHelper;
import com.example.wechatsample.utils.CommonUtil;
import com.example.wechatsample.utils.DialogUtil;
import com.example.wechatsample.utils.LogUtils;
import com.example.wechatsample.utils.ToastUtil;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

/**
 * 发现Fragment的界面
 * 
 * http://blog.csdn.net/guolin_blog/article/details/26365683
 * 
 * @author guolin
 */
public class ProductFragment extends BaseFragment {

	private final String TAG = "ProductFragment";
	public static SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
	
	private PullFramelayoutGridView mPullFramelayoutGridView = null;
	private PullToRefreshGridView mPullToRefreshGridView = null;
	private GridView mGridView = null;
	
	private boolean isFirstLoad = false; //是否第一次加载
	private int mListLimit = 10; //每次加载条数
	private int lastListSize = 0;
	
	private List<ProductEntity> listProduct = new ArrayList<ProductEntity>();
	private ProductAdapter mProductAdapter = null;
	private int gridItemWidth = 0;
	
	public static ProductFragment newInstance(){
		return new ProductFragment();
	} 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_products, container, false);
		
		initView(view);
		
		initListeners();
		
		//加载数据
		isFirstLoad = true;
		lazyLoad();
		
		return view;
	}
	
	private void initView(View view) {
		mPullFramelayoutGridView = (PullFramelayoutGridView) view.findViewById(R.id.pull_gridview_framelayout);
		mPullToRefreshGridView = mPullFramelayoutGridView.getPullToRefreshGridView();
		
		mPullToRefreshGridView.setPullRefreshEnabled(true); //下拉刷新
		mPullToRefreshGridView.setScrollLoadEnabled(true); //滚动到底部自动加载
		mPullToRefreshGridView.setPullLoadEnabled(false); //上拉加载
		
		mGridView = mPullToRefreshGridView.getRefreshableView();
		int padding = CommonUtil.dip2px(getActivity(), 10);
		int horSpacePadding = padding;
		mGridView.setPadding(padding, 0, padding, 0);
		mGridView.setNumColumns(2); //列数
		mGridView.setHorizontalSpacing(horSpacePadding);
//		mGridView.setVerticalSpacing(horSpacePadding);
		mGridView.setVerticalScrollBarEnabled(false);
		
		//每一项的宽度
		gridItemWidth = (CommonUtil.getScreenWidth(getActivity()) - 2*padding - horSpacePadding)/2; 
	}
	
	private void initListeners() {
		//加载失败
		mPullFramelayoutGridView.setLoadingErrorClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mPullFramelayoutGridView.startLoading();
				loadData();
			}
		});
		
		mPullToRefreshGridView.setOnRefreshListener(new OnRefreshListener<GridView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
				refreshData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
				loadData();
			}
		});
	}
	
	@Override
	protected void lazyLoad() {
		if(isVisible && isFirstLoad){
			//刚开始就刷新
//			mPullToRefreshGridView.doPullRefreshing(true, 200);
			loadData();
		}
	}
	
	private void loadData() {
		if(CommonUtil.checkNetState(getActivity())){
			WebService.getInstance().requestProducts(mListLimit, listProduct.size(), new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(int statusCode, JSONObject response) {
					mPullFramelayoutGridView.stopLoading();
					
					lastListSize = listProduct.size();
					
					List<ProductEntity> listLimit = WebServiceHelper.jsonToProductList(response);
					listProduct.addAll(listLimit);
					
					//如果是第一次加载
					if(isFirstLoad){
						setFirstLoadDoneState();
					}else{
						setToNormalState();
					}
					
				}
				@Override
				public void onFailure(Throwable e, JSONObject errorResponse) {
					Log.d("productFail", errorResponse.toString());
					dealLoadDataError();
				}
			});
		}else{
			dealLoadDataError();
		}
	}

	private void setFirstLoadDoneState() {
		mProductAdapter = new ProductAdapter(getActivity(), listProduct, gridItemWidth);
		mGridView.setAdapter(mProductAdapter);
		
		setLastUpdateTime();
		//如果不够10个（limit）或全部加载完了(某一次加载完后减去加载前<10说明完了)，显示没有更多了
        if(listProduct.size()<mListLimit || (listProduct.size() - lastListSize)< mListLimit){
        	mPullToRefreshGridView.setHasMoreData(false);
        }
        
		isFirstLoad = false; //首次加载置为false
	}
	
	private void setToNormalState() {
		mProductAdapter.notifyDataSetChanged();
		mPullToRefreshGridView.onPullDownRefreshComplete();
		mPullToRefreshGridView.onPullUpRefreshComplete();
        
        setLastUpdateTime();
        //如果不够limit个（limit）或全部加载完了(某一次加载完后减去加载前<limit说明完了)，显示没有更多了
        if(listProduct.size()<mListLimit || (listProduct.size() - lastListSize)< mListLimit){
        	mPullToRefreshGridView.setHasMoreData(false);
        }
        
	}
	
	private void setLastUpdateTime() {
        String text = formatDateTime(System.currentTimeMillis());
        mPullToRefreshGridView.setLastUpdatedLabel(text);
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
			WebService.getInstance().requestProducts(mListLimit, 0, new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(int statusCode, JSONObject response) {
//					Log.d("chatsucc", response.toString());
					mPullFramelayoutGridView.stopLoading();
					
					List<ProductEntity> listLimit = WebServiceHelper.jsonToProductList(response);
					
					//保留数据源前十个，删除后面所有的
					lastListSize = listProduct.size();
					for(int j=mListLimit;j<lastListSize;j++){
						listProduct.remove(mListLimit); //每次删除第11个
					}
					lastListSize = 0; //上一次加载完后的总条数
					
					//用刚刚获取的新数据替换保留的前十个数据，实现刷新
					for(int i=0;i<mListLimit;i++){
						listProduct.set(i, listLimit.get(i));
					}
					
					setToNormalState(); //恢复到正常状态
				}
				
				@Override
				public void onFailure(Throwable e, JSONObject errorResponse) {
					Log.d("productFail", errorResponse.toString());
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
    		mPullFramelayoutGridView.setErrorWhenLoading();
    	}else{
    		mPullToRefreshGridView.onPullDownRefreshComplete();
    		mPullToRefreshGridView.onPullUpRefreshComplete();
        	ToastUtil.showNetworkError(getActivity());
    	}
    	
	}
	
	@Override
	public String getFragmentName() {
		return TAG;
	}

}
