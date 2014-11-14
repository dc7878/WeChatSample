package com.example.wechatsample.library.cache;

import com.example.wechatsample.utils.CommonUtil;


public class FileManager
{

	public static String getSaveFilePath()
	{
		return CommonUtil.getRootFilePath()+ "WeChatSample/cache/";
	}
}
