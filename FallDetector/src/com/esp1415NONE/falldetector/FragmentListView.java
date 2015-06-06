package com.esp1415NONE.falldetector;

import com.esp1415NONE.falldetector.classi.DbAdapter;
import com.esp1415NONE.falldetector.classi.SessionSimpleCursorAdapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentListView extends ListFragment {


	private DbAdapter dbHelper;
	private Activity activity;
	private ListView ls;
	private FragmentTransaction fragmentTransaction;
	private FragmentManager fragmentManager;
	private TextView textview;
	private TextView textview1;
//	private TextView textview2;
	private String ids;
	private int cad;
	
	SessionSimpleCursorAdapter ssca;


	@Override
	public void onAttach(Activity a) {
		super.onAttach(a);
		activity = a;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		dbHelper = new DbAdapter(getActivity());

		fragmentManager = getActivity().getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		ls = (ListView) getActivity().findViewById(android.R.id.list);
		Cursor c = dbHelper.getAllRowsTable1();
		getActivity().startManagingCursor(c);
		ssca = new SessionSimpleCursorAdapter(getActivity(), c);
		setListAdapter(ssca);
		registerForContextMenu(getListView());


		ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {


				default:
					textview = (TextView) view.findViewById(R.id.nome);
//					textview2 = (TextView) view.findViewById(R.id.nomeS);
					textview1 = (TextView) view.findViewById(R.id.sessione);

					ids = textview.getText().toString();
					cad = Integer.parseInt(textview1.getText().toString());
					if(cad > 0) {
						FragmentDetailSession frg = new FragmentDetailSession();
						Bundle args = new Bundle();
						args.putString("ids", ids);
						frg.setArguments(args);
						fragmentTransaction.replace(R.id.frag_show_activity, frg);
						fragmentManager.popBackStack(); //viene tolto dallo stack il fragment precedente
						fragmentTransaction.commit();
						fragmentTransaction = fragmentManager.beginTransaction();
					}
					else {
						Toast.makeText(activity, "Non ci sono cadute", Toast.LENGTH_SHORT).show();
					}
					break;
				}

			}

		});

				ls.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						switch (position) {
						
						default:
							textview = (TextView) view.findViewById(R.id.nome);
							ids = textview.getText().toString();
							
//							dbHelper.dropSession(ids);
//							Cursor c = dbHelper.getAllRowsTable1();
//							getActivity().startManagingCursor(c);
//							ssca = new SessionSimpleCursorAdapter(getActivity(), c);
//							setListAdapter(ssca);
							break;
						}
						
						return false;
					}
				});

	}

	//		@Override
	//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
	//				Bundle savedInstanceState) {
	//			// TODO Auto-generated method stub
	//			View view = inflater.inflate(R.layout.activity_riepilogo_sessioni, container, false);
	//			
	//			return view;
	//		}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = activity.getMenuInflater();
		inflater.inflate(R.menu.main_context_menu, menu);
		
//		View parent = ((ViewGroup)v).getChildAt(1);
//		View child = ((ViewGroup)parent).getChildAt(1);
//		View child2 = ((ViewGroup)child).getChildAt(0);
//		TextView child3 =(TextView)((ViewGroup)child2).getChildAt(1);
//		ids = child3.getText().toString();



		//		ids = textview.getText().toString();
	}


	@SuppressWarnings("deprecation")
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
	//	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	//	int pos = info.position;


		Cursor c;
		switch (item.getItemId()) {
		case R.id.delete_id:

			Toast.makeText(activity, ""+ids, Toast.LENGTH_SHORT).show();
			dbHelper.dropSession(ids);
			ssca.notifyDataSetChanged();
			//per vedere la modifica in tempo reale
			c = dbHelper.getAllRowsTable1();
			getActivity().startManagingCursor(c);
			ssca = new SessionSimpleCursorAdapter(getActivity(), c);
			setListAdapter(ssca);
			return true;
		case R.id.ren_id:
			Toast.makeText(activity, "Da implementare la modifica", Toast.LENGTH_SHORT).show();
//			RenameDialog rd = new RenameDialog(getActivity(), ids);
//            rd.show();
			Intent i = new Intent(getActivity(), RenameActivity.class);
			i.putExtra("ids", ids);
			i.putExtra("where", "rename");
//			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			ssca.notifyDataSetChanged();
			//per vedere la modifica in tempo reale
			c = dbHelper.getAllRowsTable1();
			getActivity().startManagingCursor(c);
			ssca = new SessionSimpleCursorAdapter(getActivity(), c);
			setListAdapter(ssca);
			// arrayList.set(info.position,setItem);
			// adapter.notifyDataSetChanged();
			return true;
		case R.id.new_session:
			FragmentHome ls_fragment = new FragmentHome();
			fragmentManager.popBackStack(); //viene tolto dallo stack il fragment precedente
			fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment);
			fragmentTransaction.commit();
			fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.addToBackStack(null);
			Toast.makeText(activity, "Da implementare altre opzioni", Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onContextItemSelected(item);
		}


	}





}