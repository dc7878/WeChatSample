package com.example.wechatsample.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.wechatsample.MyApplication;
import com.example.wechatsample.R;
import com.example.wechatsample.service.WebServiceEntity.MerchantEntity;
import com.example.wechatsample.service.WebServiceEntity.ProductEntity;
import com.example.wechatsample.utils.BitmapUitl;
import com.example.wechatsample.utils.CommonUtil;
import com.example.wechatsample.utils.ToastUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProductAdapter extends BaseAdapter{
	
	private Context mContext = null;
	private List<ProductEntity> listProduct = new ArrayList<ProductEntity>();
	
	private Bitmap bmpProduct = null;
	
	private int imgWidth = 0;
	
	public ProductAdapter(Context context, List<ProductEntity> listProduct, int gridItemWidth){
		this.mContext = context;
		this.listProduct = listProduct;
		imgWidth = gridItemWidth;
		bmpProduct=BitmapUitl.changeResToBitmap(mContext, R.drawable.goods_img_default);
	}

	@Override
	public int getCount() {
		return listProduct.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listProduct.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_product, null);
			
			viewHolder = new ViewHolder();
			viewHolder.tv_productName = (TextView) convertView.findViewById(R.id.product_name);
			viewHolder.tv_priceNow = (TextView) convertView.findViewById(R.id.product_price_now);
			viewHolder.tv_priceOld = (TextView) convertView.findViewById(R.id.product_price_old);
			viewHolder.iv_product = (ImageView) convertView.findViewById(R.id.product_img);
			viewHolder.iv_product.setLayoutParams(new LinearLayout.LayoutParams(imgWidth, imgWidth-5));
			
			convertView.setTag(viewHolder);
		} else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		ProductEntity productEntity = listProduct.get(position);
		viewHolder.tv_productName.setText(productEntity.name);
		viewHolder.tv_priceNow.setText(CommonUtil.subZeroAndDot(CommonUtil.formatPrice(productEntity.priceNow)));
		viewHolder.tv_priceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //原价画个横线
		viewHolder.tv_priceOld.setText(CommonUtil.subZeroAndDot(CommonUtil.formatPrice(productEntity.priceOld)));

		viewHolder.iv_product.setImageBitmap(bmpProduct);
		if(productEntity.imgs.length>0){
			MyApplication.getImgLoader().displayImage(productEntity.imgs[0], viewHolder.iv_product, false);
		}
		
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ToastUtil.showNormalToast(mContext, listProduct.get(position).name);
			}
		});
		return convertView;
	}
	
	static class ViewHolder{
		ImageView iv_product = null; //商品图片
		TextView tv_productName = null; //商品名
		TextView tv_priceNow; //现价
		TextView tv_priceOld; //原价
	}
	
}
