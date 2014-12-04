package com.example.wechatsample.utils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

public class LocationUtil {

	/** 高德定位  */
	private static LocationManagerProxy mLocationManagerProxy = null;
	private static TextView setLocation = null;
	
	public static void getMyLocation(Activity activity, TextView tvLocation){
		setLocation = tvLocation;
		mLocationManagerProxy = LocationManagerProxy.getInstance(activity);
   	 
        //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
        //在定位结束后，在合适的生命周期调用destroy()方法     
        //其中如果间隔时间为-1，则定位只定一次
    	//provider：有三种定位Provider供用户选择，分别是:LocationManagerProxy.GPS_PROVIDER，代表使用手机GPS定位；
    	          //LocationManagerProxy.NETWORK_PROVIDER，代表使用手机网络定位；LocationProviderProxy.AMapNetwork，代表高德网络定位服务，混合定位。
    	//minTime：位置变化的通知时间，单位为毫秒。如果为-1，定位只定位一次。
    	//minDistance:位置变化通知距离，单位为米。
    	//listener:定位监听者。
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 50, aMapLocListener);
 
        //网络定位与混合定位方式的区别在于是否启用GPS定位，只需在发送定位请求前进行 
        //LocationManagerProxy.setGpsEnable(false) 的设置。发送定位请求设置相同。
        mLocationManagerProxy.setGpsEnable(false);
	}; 
	
	private static AMapLocationListener aMapLocListener = new AMapLocationListener() {
		
		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			
		}
		
		@Override
		public void onProviderEnabled(String arg0) {
			
		}
		
		@Override
		public void onProviderDisabled(String arg0) {
			
		}
		
		@Override
		public void onLocationChanged(Location arg0) {
			
		}
		
		@Override
		public void onLocationChanged(AMapLocation amapLocation) {
			 if(amapLocation != null && amapLocation.getAMapException().getErrorCode() == 0){
		          //获取位置信息
		          Double geoLat = amapLocation.getLatitude();
		          Double geoLng = amapLocation.getLongitude(); 
		          String detailAddress = amapLocation.getAddress();
		          String province = amapLocation.getProvince();
		          String city = amapLocation.getCity();
		          String district = amapLocation.getDistrict();
		          String street = amapLocation.getStreet();
		          String cityCode= amapLocation.getCityCode();
		          String floor = amapLocation.getFloor();
		          String poiId = amapLocation.getPoiId();
		          
		          String desc = "";
		          Bundle locBundle = amapLocation.getExtras();
		          if (locBundle != null) {
		              desc = locBundle.getString("desc");
		          }
		          
		          setLocation.setText("高德定位\n经度：" + geoLng +"\t\t纬度：" + geoLat +"\n"+ detailAddress+","+cityCode+","+floor+","+poiId);
		     }
		}
	};
	
	/** 停止定位，并销毁定位资源  */ 
	public static void stopLocation(){
		if (mLocationManagerProxy != null) {
    		mLocationManagerProxy.removeUpdates(aMapLocListener);
    		mLocationManagerProxy.destory();
        }
    	mLocationManagerProxy = null;
	}
}
