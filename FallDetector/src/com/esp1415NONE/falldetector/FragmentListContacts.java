package com.esp1415NONE.falldetector;

import com.esp1415NONE.falldetector.classi.ContactSimpleCursorAdapter;
import com.esp1415NONE.falldetector.classi.DbAdapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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

public class FragmentListContacts extends ListFragment {

	private DbAdapter dbAdapter;
	private Activity activity;
	private ListView ls;
	private TextView email;
	private String mail;
	ContactSimpleCursorAdapter csca;

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

		dbAdapter = new DbAdapter(getActivity());
		ls = (ListView) getActivity().findViewById(android.R.id.list);
		Cursor c = dbAdapter.getInfoTable4();
		getActivity().startManagingCursor(c);
		csca = new ContactSimpleCursorAdapter(getActivity(), c);
		setListAdapter(csca);
		registerForContextMenu(getListView());
		
		ls.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {

				default:
					email = (TextView) view.findViewById(R.id.mail);
					mail = email.getText().toString();
					break;
				}

				return false;
			}
		});
	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = activity.getMenuInflater();
		inflater.inflate(R.menu.main_context_menu_contact, menu);

		
	}


//	@SuppressWarnings("deprecation")
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		Cursor c;
		switch (item.getItemId()) {
		case R.id.delete_id_contact:
			//DEVO ELIMINARE ANCHE LA TABELLA 3 PRIMA DI ELIMINARE QUESTA DOPO IMPLEMENTAZIONE
			Toast.makeText(activity, "Eliminato:"+mail, Toast.LENGTH_SHORT).show();
			dbAdapter.dropContact(mail);
			csca.notifyDataSetChanged();
			//per vedere la modifica in tempo reale
			c = dbAdapter.getInfoTable4();
//			getActivity().startManagingCursor(c);
			csca = new ContactSimpleCursorAdapter(getActivity(), c);
			setListAdapter(csca);
			return true;
		case R.id.ren_id_contact:
			//			Toast.makeText(activity, "Rinominato", Toast.LENGTH_SHORT).show();
			//			RenameDialog rd = new RenameDialog(getActivity(), ids);
			//            rd.show();
			Intent i = new Intent(getActivity(), RenameEmailActivity.class);
			i.putExtra("mail", mail);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			return true;
		default:
			return super.onContextItemSelected(item);
		}


	}

}
