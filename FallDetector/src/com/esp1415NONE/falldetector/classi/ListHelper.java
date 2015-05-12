package com.esp1415NONE.falldetector.classi;

import java.util.ArrayList;

import android.app.Activity;

public class ListHelper {

	protected static final String longText="xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
	
	protected static ArrayList<MyObj> buildData() {
		ArrayList<MyObj> list = new ArrayList<MyObj>();
		for (int i = 1; i < 1000; i++) {
			MyObj obj = new MyObj("text" + i, "text text text 2  ",longText);
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