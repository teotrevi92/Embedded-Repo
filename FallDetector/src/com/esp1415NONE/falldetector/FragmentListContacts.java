package com.esp1415NONE.falldetector;

import com.esp1415NONE.falldetector.classi.ContactSimpleCursorAdapter;
import com.esp1415NONE.falldetector.classi.DbAdapter;
import com.esp1415NONE.falldetector.classi.SessionSimpleCursorAdapterDetails;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ListView;

public class FragmentListContacts extends ListFragment {

	private DbAdapter dbAdapter;
	private ListView ls;
	
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
		ls = (ListView) getActivity().findViewById(android.R.id.list);
		Cursor c = dbAdapter.getInfoTable4();
		getActivity().startManagingCursor(c);
		setListAdapter(new ContactSimpleCursorAdapter(getActivity(), c));
		
	}
	
	
	
}
