package com.example.wechatsample.ui;

import com.example.wechatsample.R;
import com.example.wechatsample.R.drawable;
import com.example.wechatsample.R.string;

import android.content.Context;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;
import android.widget.Toast;

public class PlusActionProvider extends ActionProvider {

	private Context context;

	public PlusActionProvider(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public View onCreateActionView() {
		return null;
	}

	@Override
	public void onPrepareSubMenu(SubMenu subMenu) {
		subMenu.clear();
		subMenu.add(context.getString(R.string.plus_group_chat)) //发起群聊
				.setIcon(R.drawable.ofm_group_chat_icon)
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						Toast.makeText(context, context.getString(R.string.plus_group_chat), Toast.LENGTH_SHORT).show();
						return true;
					}
				});
		subMenu.add(context.getString(R.string.plus_add_friend))//添加朋友
				.setIcon(R.drawable.ofm_add_icon)
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						Toast.makeText(context, context.getString(R.string.plus_add_friend), Toast.LENGTH_SHORT).show();
						return false;
					}
				});
		subMenu.add(context.getString(R.string.plus_video_chat))//视频聊天
				.setIcon(R.drawable.ofm_video_icon)
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						Toast.makeText(context, context.getString(R.string.plus_video_chat), Toast.LENGTH_SHORT).show();
						return false;
					}
				});
		subMenu.add(context.getString(R.string.plus_scan))//扫一扫
				.setIcon(R.drawable.ofm_qrcode_icon)
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						Toast.makeText(context, context.getString(R.string.plus_scan), Toast.LENGTH_SHORT).show();
						return false;
					}
				});
		subMenu.add(context.getString(R.string.plus_take_photo))//照片分享
				.setIcon(R.drawable.ofm_camera_icon)
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						Toast.makeText(context, context.getString(R.string.plus_take_photo), Toast.LENGTH_SHORT).show();
						return false;
					}
				});
	}

	@Override
	public boolean hasSubMenu() {
		return true;
	}

}