package com.example.wechatsample.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
/**
 * 对话框提示
 */
public class DialogUtil extends Activity{
	
	/**
	* 正在加载中提示对话框
	* @param @param context
	* @param @param title 对话框标题（可有可无）
	* @param @param message 对话框提示内容
	*/
	public static Dialog showLoadingDialog(Context context,CharSequence title,CharSequence message,boolean cancelAble){
		Dialog mProgressDialog=ProgressDialog.show(context, title, message);
//		mProgressDialog.set
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setCancelable(cancelAble);
		return mProgressDialog;
	}
	
}
