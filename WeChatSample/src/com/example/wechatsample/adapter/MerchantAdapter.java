package com.example.wechatsample.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.wechatsample.MyApplication;
import com.example.wechatsample.R;
import com.example.wechatsample.service.WebServiceEntity.MerchantEntity;
import com.example.wechatsample.utils.BitmapUitl;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MerchantAdapter extends BaseAdapter{
	
	private Context mContext = null;
	private List<MerchantEntity> merList = new ArrayList<MerchantEntity>();
	
	private Bitmap bmpMerchant = null;
	
	public MerchantAdapter(Context context, List<MerchantEntity> merList){
		this.mContext = context;
		this.merList = merList;
		bmpMerchant=BitmapUitl.changeResToBitmap(mContext, R.drawable.shop_img_default);
	}

	@Override
	public int getCount() {
		return merList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return merList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_merchant, null);
			
			viewHolder = new ViewHolder();
			viewHolder.iv_merchant = (ImageView) convertView.findViewById(R.id.merchant_avatar);
			viewHolder.tv_merchantName = (TextView) convertView.findViewById(R.id.merchant_name);
			viewHolder.tv_merchantAddress = (TextView) convertView.findViewById(R.id.merchant_address);
			
			convertView.setTag(viewHolder);
		} else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		MerchantEntity merchantEntity = merList.get(position);
		viewHolder.tv_merchantName.setText(merchantEntity.name);
		viewHolder.tv_merchantAddress.setText(merchantEntity.address);

		viewHolder.iv_merchant.setImageBitmap(bmpMerchant);
		if(!merchantEntity.avatar.isEmpty()){
			MyApplication.getImgLoader().displayImage(merchantEntity.avatar, viewHolder.iv_merchant, false);
		}
		
		return convertView;
	}
	
	static class ViewHolder{
		ImageView iv_merchant = null; //店铺图片
		TextView tv_merchantName = null; //店铺名
		TextView tv_merchantAddress = null; //店铺地址
	}
	
}
