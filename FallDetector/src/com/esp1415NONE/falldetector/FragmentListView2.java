package com.esp1415NONE.falldetector;


import com.esp1415NONE.falldetector.classi.ListHelper;
import com.esp1415NONE.falldetector.classi.MyObj;
import com.esp1415NONE.falldetector.classi.ViewHolderAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class FragmentListView2 extends ListFragment {
	
	private ArrayAdapter<MyObj> adapter;
	MyObj obj;
	private Activity activity;
	
	
	@Override
	public void onAttach(Activity a){
		super.onAttach(a);
		activity = a;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.activity_riepilogo_sessioni, container, false);
		return view;
	
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setListAdapter(ListHelper.buildViewHolderAdapter(activity,
				R.layout.list_item));
		adapter = (ViewHolderAdapter) getListView().getAdapter();
		registerForContextMenu(getListView());
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = activity.getMenuInflater();
		inflater.inflate(R.menu.main_context_menu,menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.delete_id:
		//	Toast.makeText(this, "Da implementare la rimozione", Toast.LENGTH_SHORT).show();
			MyObj o = adapter.getItem(info.position);
			adapter.remove(o);
			adapter.notifyDataSetChanged();
			return true;
		case R.id.mod_id:
			Toast.makeText(activity, "Da implementare la modifica", Toast.LENGTH_SHORT).show();
//			arrayList.set(info.position,setItem);
//			adapter.notifyDataSetChanged();
			return true;
		case R.id.other_id:
			Toast.makeText(activity, "Da implementare altre opzioni", Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
		//return super.onContextItemSelected(item);
	}
	
	

}
