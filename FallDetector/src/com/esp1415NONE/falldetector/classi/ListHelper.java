package com.esp1415NONE.falldetector.classi;

import java.util.ArrayList;

import android.app.Activity;

public class ListHelper {

	
	
	protected static ArrayList<MyObj> buildData() {
		ArrayList<MyObj> list = new ArrayList<MyObj>();
		for (int i = 1; i < 1000; i++) {
			MyObj obj = new MyObj("col1", "col2", "col3", "col4", "col5", "col6");
			list.add(obj);
			
		}
		return list;
	}
	
	public static ViewHolderAdapter buildViewHolderAdapter(Activity context,
			int textViewResourceId) {

		ArrayList<MyObj> list = buildData();
		ViewHolderAdapter viewHolder = new ViewHolderAdapter(context, textViewResourceId);
		viewHolder.addAll(list);
		return viewHolder;
	}


}