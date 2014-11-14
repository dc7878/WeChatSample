package com.example.wechatsample.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.wechatsample.R;
import com.example.wechatsample.R.id;
import com.example.wechatsample.R.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter{
	
	private Context mContext = null;
	private List<String> strList = new ArrayList<String>();
	
	public ListAdapter(Context context, List<String> strList){
		this.mContext = context;
		this.strList = strList;
	}

	@Override
	public int getCount() {
		return strList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return strList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item, null);
			
			viewHolder = new ViewHolder();
			viewHolder.tv_content = (TextView) convertView.findViewById(R.id.name);
			
			convertView.setTag(viewHolder);
		} else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.tv_content.setText(strList.get(position));
		
		return convertView;
	}
	
	static class ViewHolder{
		TextView tv_content = null;
	}
	
}
