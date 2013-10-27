package com.mysliborski.tools.view.list;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;

import com.mysliborski.tools.data.HasId;

public abstract class BaseListAdapter<T extends HasId> extends BaseAdapter {

	protected List<T> backingList = new ArrayList<T>(); 
	
	public void setData(List<T> data) {
		backingList = data;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return backingList.size();
	}

	@Override
	public T getItem(int position) {
		return backingList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

}
