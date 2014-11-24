package com.example.wechatsample.utils;

import com.example.wechatsample.R;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {
	private static Toast toast=null; 

	public static void showToastInCenter(Context context,String content){
		toast=Toast.makeText(context,content , Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, -100);
		toast.show();
	}
	
	public static void showToastInTop(Context context,String content){
		toast=Toast.makeText(context,content , Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP, 0, 70);
		toast.show();
	}
	
	public static void showNormalToast(Context context,String content){
		toast=Toast.makeText(context, content,  Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM, 0, 200);
		toast.show();
	}
	
	public static void showNetworkError(Context context){
		showNormalToast(context,"请检查网络连接");
	}
	
	public static void showNetworkErrorInCenter(Context context){
		showToastInCenter(context,"请检查网络连接");
	}
	
	public static void showNetworkErrorInTop(Context context){
		showToastInTop(context,"请检查网络连接");
	}
}
