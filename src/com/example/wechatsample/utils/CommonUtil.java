package com.example.wechatsample.utils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import com.example.wechatsample.MyApplication;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

public class CommonUtil
{

	// private static final CommonLog log = LogFactory.createLog();

	/**
	 * 检测手机是否有SD卡
	 * 
	 * @return
	 */
	public static boolean hasSDCard()
	{
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED))
		{
			return false;
		}
		return true;
	}

	/**
	 * 根据是否有SD卡来判断存储路径
	 * 
	 * @return 存储路径
	 */
	public static String getRootFilePath()
	{
		if (hasSDCard())
		{
			return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";//  /storage/sdcard0/
		}
		else
		{
			return Environment.getDataDirectory() + "/data/"+MyApplication.getInstance().getPackageName()+"/"; // /data/data/com.runyetech.find2/
//			return Environment.getDownloadCacheDirectory().getAbsolutePath() + "/"; //  /cache/
//			return Environment.getRootDirectory() + "/"; //  /system/
		}
	}
	
//	public static File updateDir = null;
//	public static File updateFile = null;
//
//	/***
//	 * 创建文件
//	 */
//	public static void createFile(String versionName) {
//		Log.d("getRootFilePath", getRootFilePath());
//			updateDir = new File(getRootFilePath()+"Find/app/");
//			updateFile = new File(getRootFilePath()+"Find/app/"+"find-" + versionName + ".apk");
//
//			if (!updateDir.exists()) {
//				updateDir.mkdirs();
//			}
//			if (!updateFile.exists()) {
//				try {
//					updateFile.createNewFile();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//
//	}

	/**
	 * 检测网络状态
	 * 
	 * @param context
	 *            上下文对象
	 * @return 是否有网，true：有网，false：无网
	 */
	public static boolean checkNetState(Context context)
	{
		boolean netstate = false;
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null)
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
			{
				for (int i = 0; i < info.length; i++)
				{
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						netstate = true;
						break;
					}
				}
			}
		}
		return netstate;
	}
	
//	/**
//	 * 网络连接错误
//	 * @param context
//	 */
//	public static void showNetworkError(Context context){
//		Toast.makeText(context,context.getResources().getString(R.string.activity_login_toast_network_error) , Toast.LENGTH_SHORT).show();
//	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @param context
	 *            上下文对象
	 * @return 屏幕的宽度
	 */
	public static int getScreenWidth(Activity context)
	{
		DisplayMetrics dm = new DisplayMetrics();
		//获取屏幕信息
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		return dm.widthPixels;
	}

	/**
	 * 获取屏幕的高度
	 * 
	 * @param context
	 *            上下文对象
	 * @return 屏幕的高度
	 */
	public static int getScreenHeight(Activity context)
	{
		DisplayMetrics dm = new DisplayMetrics();
		//获取屏幕信息
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		return dm.heightPixels;
	}
	
	/**
	 * dip to px 转化
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue){ 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int)(dipValue * scale + 0.5f); 
	} 
	public static int px2dip(Context context, float pxValue){ 
        final float scale = context.getResources().getDisplayMetrics().density; 
	    return (int)(pxValue / scale + 0.5f); 
	}
	
	static DecimalFormat dfPrice=new DecimalFormat("￥0.00");
	static public String formatPrice(Double price){
		return dfPrice.format(price);
	}
	
	public static String subZeroAndDot(String s){  
        if(s.indexOf(".") > 0){  
            s = s.replaceAll("0+?$", "");//去掉多余的0  
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉  
        }  
        return s;  
    }

}
