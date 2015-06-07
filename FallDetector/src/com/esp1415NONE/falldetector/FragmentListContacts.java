package com.esp1415NONE.falldetector;

import com.esp1415NONE.falldetector.classi.DbAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

public class FragmentListContacts extends ListFragment {

	private DbAdapter dbAdapter;
	
	@Override
	public void onAttach(Activity a) {
		super.onAttach(a);
//		activity = a;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		dbAdapter = new DbAdapter(getActivity());
		
		
	}
	
	
	
}
