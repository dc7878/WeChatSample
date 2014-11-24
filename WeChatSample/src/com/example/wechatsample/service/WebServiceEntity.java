package com.example.wechatsample.service;

public class WebServiceEntity {

	public static class MerchantEntity{
		public String name; //店铺名
		public String avatar; //头像
		public String address; //地址
		
	}

	public static class ProductEntity{
		public String   name; //商品名
		public Double   priceNow; //现价
		public Double   priceOld; //原价
		public String[] imgs; //商品图
	}
	
	public static class ADItem{
		public String type;
		public String ad_url;
		public String ad_id;
		public String status;
		public String image;
	}
}
