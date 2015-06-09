package com.esp1415NONE.falldetector;

import com.esp1415NONE.falldetector.classi.ContactSimpleCursorAdapter;
import com.esp1415NONE.falldetector.classi.DbAdapter;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentListContacts extends Fragment {

	private DbAdapter dbAdapter;
	private TextView email;
	private String mail;
	private ContactSimpleCursorAdapter csca;
	private ListView listcontact;
	private ImageButton add;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.add_button, container, false);
		dbAdapter = new DbAdapter(getActivity());
		listcontact = (ListView) view.findViewById(R.id.listcontact);
		add = (ImageButton) view.findViewById(R.id.addbutton);
		return view;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);


		Cursor c = dbAdapter.getInfoTable4();
		getActivity().startManagingCursor(c);
		csca = new ContactSimpleCursorAdapter(getActivity(), c);
		listcontact.setAdapter(csca);

		registerForContextMenu(listcontact);

		listcontact.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

		add.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogAddContact();
			}
		});
	}



	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getActivity().getMenuInflater();
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
			Toast.makeText(getActivity(), "Eliminato:"+mail, Toast.LENGTH_SHORT).show();
			dbAdapter.dropContact(mail);
			csca.notifyDataSetChanged();
			//per vedere la modifica in tempo reale
			c = dbAdapter.getInfoTable4();
			//			getActivity().startManagingCursor(c);
			csca = new ContactSimpleCursorAdapter(getActivity(), c);
			listcontact.setAdapter(csca);
			return true;
		case R.id.ren_id_contact:
			//			Toast.makeText(activity, "Rinominato", Toast.LENGTH_SHORT).show();
			//			RenameDialog rd = new RenameDialog(getActivity(), ids);
			//            rd.show();
			//			Intent i = new Intent(getActivity(), RenameEmailActivity.class);
			//			i.putExtra("mail", mail);
			//			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//			startActivity(i);
			// custom dialog
			dialogRenameContact();

			return true;
		default:
			return super.onContextItemSelected(item);
		}



	}

	private void dialogRenameContact() {
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.activity_rename_email);
		dialog.setTitle("Rinomina Contatto");

		//personalizzo il Dialog
		final EditText name_ = (EditText) dialog.findViewById(R.id.name);
		final EditText surname_ = (EditText) dialog.findViewById(R.id.surname);
		final TextView mail_ = (TextView) dialog.findViewById(R.id.mail);
		Button ok = (Button) dialog.findViewById(R.id.btn_yes);
		Button no = (Button) dialog.findViewById(R.id.btn_no);
		mail_.setText(mail);
		name_.setText(dbAdapter.getName(mail));
		surname_.setText(dbAdapter.getSurname(mail));
		// cosa faccio al click del conferma
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = name_.getText().toString();
				String surname = surname_.getText().toString(); 
				if(name.equals("") || surname.equals("")) {
					Toast.makeText(getActivity(), "Controllare i campi obbligatori", Toast.LENGTH_SHORT).show();
				}
				else {
					dbAdapter.setNameContact(mail, name, surname);
					csca.notifyDataSetChanged();
					//per vedere la modifica in tempo reale
					Cursor c = dbAdapter.getInfoTable4();
					csca = new ContactSimpleCursorAdapter(getActivity(), c);
					listcontact.setAdapter(csca);
					dialog.dismiss();
				}
			}
		});
		// cosa faccio al click dell'annulla
		no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		dialog.show();
	}
	
	private void dialogAddContact() {
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.activity_add_email);
		dialog.setTitle("Aggiungi Contatto");

		//personalizzo il Dialog
		final EditText name_ = (EditText) dialog.findViewById(R.id.name);
		final EditText surname_ = (EditText) dialog.findViewById(R.id.surname);
		final EditText mail_ = (EditText) dialog.findViewById(R.id.mail);
		Button ok = (Button) dialog.findViewById(R.id.btn_yes);
		Button no = (Button) dialog.findViewById(R.id.btn_no);
		// cosa faccio al click del conferma
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = name_.getText().toString();
				String surname = surname_.getText().toString(); 
				String mail = mail_.getText().toString(); 
				if(name.equals("") || surname.equals("") || mail_.equals("")) {
					Toast.makeText(getActivity(), "Controllare i campi obbligatori", Toast.LENGTH_SHORT).show();
				}
				else {
					dbAdapter.createContact(mail, name, surname);
					csca.notifyDataSetChanged();
					//per vedere la modifica in tempo reale
					Cursor c = dbAdapter.getInfoTable4();
					csca = new ContactSimpleCursorAdapter(getActivity(), c);
					listcontact.setAdapter(csca);
					dialog.dismiss();
				}
			}
		});
		// cosa faccio al click dell'annulla
		no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		dialog.show();
	}

}

//package com.esp1415NONE.falldetector;
//
//import com.esp1415NONE.falldetector.classi.ContactSimpleCursorAdapter;
//import com.esp1415NONE.falldetector.classi.DbAdapter;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.support.v4.app.ListFragment;
//import android.view.ContextMenu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ContextMenu.ContextMenuInfo;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class FragmentListContacts extends ListFragment {
//
//	private DbAdapter dbAdapter;
//	private Activity activity;
//	private ListView ls;
//	private TextView email;
//	private String mail;
//	ContactSimpleCursorAdapter csca;
//
//	@Override
//	public void onAttach(Activity a) {
//		super.onAttach(a);
//		activity = a;
//	}
//
//	@SuppressWarnings("deprecation")
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onActivityCreated(savedInstanceState);
//
//		dbAdapter = new DbAdapter(getActivity());
//		ls = (ListView) getActivity().findViewById(android.R.id.list);
//		Cursor c = dbAdapter.getInfoTable4();
//		getActivity().startManagingCursor(c);
//		csca = new ContactSimpleCursorAdapter(getActivity(), c);
//		setListAdapter(csca);
//		registerForContextMenu(getListView());
//		
//		ls.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				switch (position) {
//
//				default:
//					email = (TextView) view.findViewById(R.id.mail);
//					mail = email.getText().toString();
//					break;
//				}
//
//				return false;
//			}
//		});
//	}
//
//
//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
//		// TODO Auto-generated method stub
//		super.onCreateContextMenu(menu, v, menuInfo);
//		MenuInflater inflater = activity.getMenuInflater();
//		inflater.inflate(R.menu.main_context_menu_contact, menu);
//
//		
//	}
//
//
////	@SuppressWarnings("deprecation")
//	@Override
//	public boolean onContextItemSelected(MenuItem item) {
//		// TODO Auto-generated method stub
//
//		Cursor c;
//		switch (item.getItemId()) {
//		case R.id.delete_id_contact:
//			//DEVO ELIMINARE ANCHE LA TABELLA 3 PRIMA DI ELIMINARE QUESTA DOPO IMPLEMENTAZIONE
//			Toast.makeText(activity, "Eliminato:"+mail, Toast.LENGTH_SHORT).show();
//			dbAdapter.dropContact(mail);
//			csca.notifyDataSetChanged();
//			//per vedere la modifica in tempo reale
//			c = dbAdapter.getInfoTable4();
////			getActivity().startManagingCursor(c);
//			csca = new ContactSimpleCursorAdapter(getActivity(), c);
//			setListAdapter(csca);
//			return true;
//		case R.id.ren_id_contact:
//			//			Toast.makeText(activity, "Rinominato", Toast.LENGTH_SHORT).show();
//			//			RenameDialog rd = new RenameDialog(getActivity(), ids);
//			//            rd.show();
//			Intent i = new Intent(getActivity(), RenameEmailActivity.class);
//			i.putExtra("mail", mail);
//			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(i);
//			return true;
//		default:
//			return super.onContextItemSelected(item);
//		}
//
//
//	}
//
//}
//
