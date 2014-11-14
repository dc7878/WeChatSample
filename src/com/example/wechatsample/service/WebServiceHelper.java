package com.example.wechatsample.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.wechatsample.service.WebServiceEntity.MerchantEntity;

public class WebServiceHelper {

	public static List<MerchantEntity> jsonToMerchantList(JSONObject response){
		List<MerchantEntity> listMerchant = new ArrayList<MerchantEntity>();
		
		try {
			JSONArray ja = response.optJSONArray(WebService.MERCHANT_RECORDS);
			for(int i=0; i<ja.length(); i++){
				listMerchant.add(jsonToMerchant(ja.optJSONObject(i)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listMerchant;
	}
	
	public static MerchantEntity jsonToMerchant(JSONObject response){
		MerchantEntity merchantEntity = new MerchantEntity();
		
		try {
			merchantEntity.name = response.optString(WebService.MERCHANT_NAME);
			merchantEntity.avatar = WebService.mImgUrl + response.optString(WebService.MERCHANT_AVATAR);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return merchantEntity;
	}

}
