package com.example.wechatsample.library.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.wechatsample.library.http.BinaryHttpResponseHandler;
import com.example.wechatsample.library.http.SyncHttpClient;
import com.example.wechatsample.utils.BitmapUitl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.widget.ImageView;


public class ImageLoader {

	private final MemoryCache memoryCache = new MemoryCache();
	private final AbstractFileCache fileCache;
	private final Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	// 线程池
	private final ExecutorService executorService;

	public ImageLoader(Context context)
	{
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
	}

	/**
	 * 异步加载图片
	 * 
	 * @param url
	 *            图片地址
	 * @param imageView
	 *            图片要放入的控件
	 * @param isLoadOnlyFromCache
	 *            是否只从缓存中加载
	 */
	public void displayImage(String url, ImageView imageView, boolean isLoadOnlyFromCache) {
		imageViews.put(imageView, url);

		Bitmap bitmap = memoryCache.get(url);// 先从内存缓存中查找
		if (bitmap != null){
			bitmap=scaleImg(bitmap,200,200);
			imageView.setImageBitmap(bitmap);
//			imageView.setBackgroundResource(R.drawable.shop_img_default);
		}else if (!isLoadOnlyFromCache) {
//			Log.d("true", "ImageLoader-->手机内存中没有图片：" + url);
			queuePhoto(url, imageView);
		}
	}
	
	// 设置图片指定大小
	public Bitmap scaleImg(Bitmap bm, int newWidth, int newHeight) {
		// 图片源
		// Bitmap bm = BitmapFactory.decodeStream(getResources()
		// .openRawResource(id));
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 设置想要的大小
		int newWidth1 = newWidth;
		int newHeight1 = newHeight;
		// 计算缩放比例
		float scaleWidth = ((float) newWidth1) / width;
		float scaleHeight = ((float) newHeight1) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);

		return newbm;
	}
	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	private Bitmap getBitmap(String url) {
		final File f = fileCache.getFile(url);
		Bitmap bmp_diskCache = null;// 先从文件缓存中查找是否有
		if (f != null && f.exists()) {
			bmp_diskCache = decodeFile(f);
		}
		if (bmp_diskCache != null) {
			Log.d("true", "ImageLoader-->磁盘缓存中找到图片：" + url);
			return bmp_diskCache;
		}

		Log.d("true", "ImageLoader-->磁盘缓存中没有图片：" + url);
		Log.d("true", "ImageLoader-->联网去加载这个图片：" + url);

		try {// 最后从指定的url中下载图片
			Bitmap bmp_IntentLoader = null;
			if (url.startsWith("http://")) {// HTTP DOWNLOAD AND CACHE IMG
				Log.d("true", "ImageLoader-->联网载图片使用协议：" + "HTTP");
				URL imageUrl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
				conn.setConnectTimeout(30000);
				conn.setReadTimeout(30000);
				conn.setInstanceFollowRedirects(true);
				InputStream is = conn.getInputStream();
				OutputStream os = new FileOutputStream(f);
				CopyStream(is, os);
				os.close();
				bmp_IntentLoader = decodeFile(f);
			} else {// HTTPS Download And Cache IMG--I SEE
				Log.d("true", "ImageLoader-->联网载图片使用协议：" + "HTTPS");
				new SyncHttpClient() {
					@Override
					public String onRequestFailed(Throwable error, String content) {
						Log.d("true", "ImageLoader-->SyncHttpClient同步方法获取图片失败后返回来的内容：" + content);
						return content;
					}
				}.get(url, new BinaryHttpResponseHandler(new String[] { "image/png", "image/png; charset=UTF-8", "image/*", "application/octet-stream" }) {
					@Override
					public void onSuccess(byte[] binaryData) {
						try {
							Log.d("true", "ImageLoader-->图片的大小：" + binaryData.length);
							InputStream is = BitmapUitl.getInputStreamFromBytes(binaryData);
							Log.d("true", "ImageLoader-->获取回来的图片输入流的编码：" + new InputStreamReader(is).getEncoding());
							OutputStream os = new FileOutputStream(f);
							CopyStream(is, os);
							os.close();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						super.onSuccess(binaryData);
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						Log.d("true", "ImageLoader-->异步加载图片出错");
					}
				});
				bmp_IntentLoader = decodeFile(f);
			}
			return bmp_IntentLoader;
		} catch (Exception ex) {
			Log.e("", "getBitmap catch Exception...\nmessage = " + ex.getMessage());
			return null;
		}
	}

	// decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 100;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i)
		{
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad)
		{
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = getBitmap(photoToLoad.url);
			bmp=scaleImg(bmp, 200,200);
			memoryCache.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			// 更新的操作放在UI线程中
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	/**
	 * 防止图片错位
	 * 
	 * @param photoToLoad
	 * @return
	 */
	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// 用于在UI线程中更新界面
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p)
		{
			bitmap = b;
			photoToLoad = p;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null)
			photoToLoad.imageView.setImageBitmap(bitmap);
//			photoToLoad.imageView.setBackgroundResource(R.drawable.shop_img_default);

		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
			Log.e("", "CopyStream catch Exception...");
		}
	}

}