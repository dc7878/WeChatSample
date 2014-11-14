package com.example.wechatsample.service;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.wechatsample.library.http.AsyncHttpClient;
import com.example.wechatsample.library.http.JsonHttpResponseHandler;
import com.example.wechatsample.library.http.RequestParams;

import android.R.integer;
import android.content.Context;
import android.util.Log;

public class WebService {
	
	static final String mAPIUrl = "http://api.tyfind.com:8888";
	
	static final String mImgUrl = mAPIUrl + "/uploads/"; 
	
	static final String MERCHANT_HEAD = "/merchants";
	static final String MERCHANT_RECORDS = "records";
	static final String MERCHANT_NAME = "name";
	static final String MERCHANT_AVATAR = "avatar"; 
	static final String MERCHANT_STATUS = "status"; //审核状态
	static final String MERCHANT_ISMULTIPLESHOP = "isMultipleShop"; //是否为总店
	static final String MERCHANT_LIMIT = "limit"; //每次加载条数
	static final String MERCHANT_SKIP = "skip"; //当前已加载条数
	
	private static class FindWebServiceHolder {
		private static final WebService mInstance = new WebService();
	}

	public static WebService getInstance() {
		return FindWebServiceHolder.mInstance;
	}

	/**
	 * 获取商户列表
	 * 
	 * @param name
	 *            商户名称（查询时用）
	 * @param featured
	 *            是不是最旺店铺
	 * @param sortby
	 * @param limit
	 *            每次获取多少条
	 * @param skip
	 *            已经获取的条数
	 * @param handler
	 */

	public void requestMerchants(int limit, int skip, JsonHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put(MERCHANT_STATUS, "succeed_verify"); //审核通过的
		params.put(MERCHANT_ISMULTIPLESHOP, "true");
		params.put(MERCHANT_LIMIT, limit+"");
		params.put(MERCHANT_SKIP, skip+"");
//		params.put("featured", "true");
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.get(mAPIUrl + MERCHANT_HEAD, params, handler);
	}
	
	

}
