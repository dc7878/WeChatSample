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
	
	/** 店铺merchants属性  */
	static final String MERCHANT_HEAD = "/merchants";
	static final String MERCHANT_NAME = "name";
	static final String MERCHANT_AVATAR = "avatar"; 
	static final String MERCHANT_ISMULTIPLESHOP = "isMultipleShop"; //是否为总店
	static final String MERCHANT_ADDRESS = "address"; 
	static final String MERCHANT_DISTRICT = "district"; 
	static final String MERCHANT_STREET_DETAIL = "street2"; 
	
	/** 商品products属性  */
	static final String PRODUCT_HEAD = "/products";
	static final String PRODUCT_NAME = "name";
	static final String PRODUCT_PRICE_NOW = "price";
	static final String PRODUCT_PRICE_OLD = "market_price";
	static final String PRODUCT_IMAGES = "images";
	static final String PRODUCT_LISTED = "listed"; //是否上架
	
	
	/************ 下面是一些公共属性 **************/
	static final String RECORDS = "records";
	static final String VERIFY_STATUS = "status"; //审核状态
	static final String LIMIT = "limit"; //每次加载条数
	static final String SKIP = "skip"; //当前已加载条数
	static final String FEATURED = "featured"; //是不是最旺
	
	/** 审核状态  */
	final String VERIFY_SUCC = "succeed_verify"; //审核通过
	
	/***************************************/
	
	private static class FindWebServiceHolder {
		private static final WebService mInstance = new WebService();
	}

	public static WebService getInstance() {
		return FindWebServiceHolder.mInstance;
	}

	/**
	 * 获取商户列表
	 * @param limit 每次获取多少条
	 * @param skip 已经获取的条数
	 * @param handler
	 */
	public void requestMerchants(int limit, int skip, JsonHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.put(VERIFY_STATUS, VERIFY_SUCC); //审核通过的
		params.put(MERCHANT_ISMULTIPLESHOP, "true");
		params.put(LIMIT, limit+"");
		params.put(SKIP, skip+"");
//		params.put("featured", "true");
		
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.get(mAPIUrl + MERCHANT_HEAD, params, responseHandler);
	}
	
	
	public void requestProducts(int limit, int skip, JsonHttpResponseHandler responseHandler){
		RequestParams params = new RequestParams();
		params.put(VERIFY_STATUS, VERIFY_SUCC);
		params.put(PRODUCT_LISTED, "true");
		params.put(FEATURED, "true");
		params.put(LIMIT, limit+"");
		params.put(SKIP, skip+"");
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(mAPIUrl + PRODUCT_HEAD, params, responseHandler);
	}
}
