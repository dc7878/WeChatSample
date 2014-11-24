package com.example.wechatsample;

import com.example.wechatsample.library.cache.ImageLoader;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class MyApplication extends Application{
	
	private static Context appContext = null; 

	private static MyApplication mInstance = null;
	
	private static ImageLoader imgLoader = null; 
	
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		
		appContext = getApplicationContext();
		
		
	}
	
	public static MyApplication getInstance(){
		return mInstance;
	}
	
	public static ImageLoader getImgLoader(){
		if(imgLoader == null){
			imgLoader = new ImageLoader(appContext);
		}
		return imgLoader;
	}
	
	/**
	 * 获取App安装包信息
	 * 
	 * @return
	 */
	public PackageInfo getPackageInfo()
	{
		PackageInfo info = null;
		try
		{
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}	
}
