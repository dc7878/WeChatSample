package com.example.wechatsample.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.wechatsample.service.WebServiceEntity.MerchantEntity;
import com.example.wechatsample.service.WebServiceEntity.ProductEntity;
import com.example.wechatsample.utils.LogUtils;

public class WebServiceHelper {

	/** 店铺列表  */
	public static List<MerchantEntity> jsonToMerchantList(JSONObject response){
		List<MerchantEntity> listMerchant = new ArrayList<MerchantEntity>();
		
		try {
			JSONArray ja = response.optJSONArray(WebService.RECORDS);
			for(int i=0; i<ja.length(); i++){
				listMerchant.add(jsonToMerchant(ja.optJSONObject(i)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listMerchant;
	}
	
	/** 单个店铺信息  */
	public static MerchantEntity jsonToMerchant(JSONObject response){
		MerchantEntity merchantEntity = new MerchantEntity();
		
		try {
			merchantEntity.name = response.optString(WebService.MERCHANT_NAME);
			merchantEntity.avatar = WebService.mImgUrl + response.optString(WebService.MERCHANT_AVATAR);
			if(response.has(WebService.MERCHANT_ADDRESS)){
				String district = "", streetDetail = ""; 
				JSONObject jbAddress = response.optJSONObject(WebService.MERCHANT_ADDRESS);
				if(jbAddress.has(WebService.MERCHANT_DISTRICT)){
					district = jbAddress.optString(WebService.MERCHANT_DISTRICT);
				}
				if(jbAddress.has(WebService.MERCHANT_STREET_DETAIL)){
					streetDetail = jbAddress.optString(WebService.MERCHANT_STREET_DETAIL);
				}
				merchantEntity.address = district + streetDetail;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return merchantEntity;
	}
	
	/** 商品列表  */
	public static List<ProductEntity> jsonToProductList(JSONObject response){
		List<ProductEntity> listProduct = new ArrayList<ProductEntity>();
		
		try {
			JSONArray ja = response.optJSONArray(WebService.RECORDS);
			for(int i=0;i<ja.length();i++){
				listProduct.add(jsonToProduct(ja.optJSONObject(i)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listProduct;
	}
	
	/** 单个商品信息 */
	public static ProductEntity jsonToProduct(JSONObject response){
		ProductEntity productEntity = new ProductEntity();
		
		try {
			productEntity.name = response.optString(WebService.PRODUCT_NAME);
			productEntity.priceNow = response.optDouble(WebService.PRODUCT_PRICE_NOW);
			productEntity.priceOld = response.optDouble(WebService.PRODUCT_PRICE_OLD);
			
			JSONArray jaImg = response.optJSONArray(WebService.PRODUCT_IMAGES);
			String[] imgs = new String[jaImg.length()];
			for(int j=0;j<jaImg.length();j++){
				String img = jaImg.optString(j);
				imgs[j] = WebService.mImgUrl + "/" +img;
			}
			productEntity.imgs = imgs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return productEntity;
	}

}
