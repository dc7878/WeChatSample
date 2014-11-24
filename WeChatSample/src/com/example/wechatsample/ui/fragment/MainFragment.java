package com.example.wechatsample.ui.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.wechatsample.R;
import com.example.wechatsample.adapter.MerchantAdapter;
import com.example.wechatsample.library.cache.ImageADsLoader;
import com.example.wechatsample.library.http.AsyncHttpClient;
import com.example.wechatsample.library.http.JsonHttpResponseHandler;
import com.example.wechatsample.library.http.RequestParams;
import com.example.wechatsample.service.WebService;
import com.example.wechatsample.service.WebServiceEntity.ADItem;
import com.example.wechatsample.service.WebServiceHelper;
import com.example.wechatsample.ui.pulltorefresh.PullFramelayoutScrollView;
import com.example.wechatsample.utils.widget.MyADsViewPage;
import com.fackquan.mypulltorefreshlibrary.PullToRefreshBase;
import com.fackquan.mypulltorefreshlibrary.PullToRefreshBase.OnRefreshListener;
import com.fackquan.mypulltorefreshlibrary.PullToRefreshScrollView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

/**
 * 主界面
 * http://blog.csdn.net/guolin_blog/article/details/26365683
 */
public class MainFragment extends BaseFragment {
	
	private final String TAG = "MainFragment";
	public static SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
	private final int LOAD_DATA_SUCC = 0; 
	
	private ScrollView scrollView = null;
	private PullFramelayoutScrollView mPullFramelayoutScrollView = null;
	private PullToRefreshScrollView mPullScrollView = null;
	
	private MyADsViewPage mViewPager = null; //广告图片
	private LinearLayout dotLayout = null; //小圆点
	
	private int mListLimit = 10; //每次加载条数
	private int lastListSize = 0;
	private boolean isFirstLoad = false; //是否第一次加载
	private List<ADItem> listADs = new ArrayList<ADItem>();
	
	private ImageADsLoader imgLoader = null;
	
	public static MainFragment newInstance(){
		return new MainFragment();
	} 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);
		
		initView(view);
		initListeners();
		
		//界面加载完加载数据
		isFirstLoad = true;
		lazyLoad();
		
		return view;
	}

	private void initView(View view) {
		mPullFramelayoutScrollView = (PullFramelayoutScrollView) view.findViewById(R.id.pull_scrollview_framelayout);
		mPullScrollView = mPullFramelayoutScrollView.getPullToRefreshScrollView();
//		mPullScrollView.setPullLoadEnabled(false); //滑动到底部上拉加载
//		mPullScrollView.setScrollLoadEnabled(true); //滑动到底部自动加载
		mPullScrollView.setPullRefreshEnabled(true); //下拉刷新
		
		scrollView = mPullScrollView.getRefreshableView();
		scrollView.setVerticalScrollBarEnabled(false);
		
		imgLoader = new ImageADsLoader(getActivity());
	}
	
	private void initListeners() {
		// 加载失败
		mPullFramelayoutScrollView.setLoadingErrorClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
			}
		});
		
		mPullScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
//				downLoadData();
				for(int i=0;i<i+1;i++){
					if(i>2000){
						mPullScrollView.onPullDownRefreshComplete();
						i=0;
						break;
					}
				}
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				
			}
		});
	}
	
	@Override
	protected void lazyLoad() {
		if(isVisible && isFirstLoad){
			downLoadData();
		}
	}
	
	/** 下载广告数据  */
	private void downLoadData() {
		WebService.getInstance().requestADs(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				mPullFramelayoutScrollView.stopLoading();
				
				List<ADItem> succAds = WebServiceHelper.adsToList(response);
				listADs.addAll(succAds);
				
				if(isFirstLoad){
					setFirstLoadDoneState();
				}else{
					setToNormalState();
				}
			}
			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				Log.d("downFail", errorResponse.toString());
			}
		});
	}
	
	private void setData() {
		View scroll = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_main_scroll, null);
		scrollView.addView(scroll);
		mViewPager = (MyADsViewPage) scroll.findViewById(R.id.adv_pager);
		dotLayout = (LinearLayout) scroll.findViewById(R.id.viewGroup);
		
		List<View> advPics = new ArrayList<View>();
		for(int j=0;j<listADs.size();j++){
			ImageView img = new ImageView(getActivity());
			img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			img.setScaleType(ScaleType.FIT_XY);
//			img.setBackgroundResource(R.drawable.advertising_default);
			imgLoader.displayADsImage(listADs.get(j).image, img, false);
			
			final int index = j;
			img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Toast.makeText(getActivity(), index+"->"+listADs.get(index).image, Toast.LENGTH_SHORT).show();
				}
			});
			advPics.add(img);
		}
		
		mViewPager.setADsViewPager(getActivity(), advPics, dotLayout, 1800);
		
	};
	
	private void setFirstLoadDoneState() {
		setData();
		setLastUpdateTime();
        
		isFirstLoad = false; //首次加载置为false
	}
	
	private void setToNormalState() {
        setLastUpdateTime();
	}
	
	private void setLastUpdateTime() {
        String text = formatDateTime(System.currentTimeMillis());
        mPullScrollView.setLastUpdatedLabel(text);
    }

    private String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }
        return mDateFormat.format(new Date(time));
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	if(null != mViewPager){
    		mViewPager.setContinue(false);
    	}
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	if(null != mViewPager){
    		mViewPager.setContinue(true);
    	}
    }
	
	@Override
	public String getFragmentName() {
		return TAG;
	}

}
