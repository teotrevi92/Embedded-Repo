package com.esp1415NONE.falldetector;

import java.util.regex.Pattern;

import com.esp1415NONE.falldetector.classi.ContactSimpleCursorAdapter;
import com.esp1415NONE.falldetector.classi.DbAdapter;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
	private int isOpenDialog = 0; //1 se sto aggiungendo, 2 se sto rinominando, 0 se non ci sono Dialog aperti
	private EditText name_,surname_,mail_;
	private Cursor c;
	private boolean IsconfigChange ;
	//	private boolean inDialog = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.add_button, container, false);
		dbAdapter = new DbAdapter(getActivity());
		IsconfigChange = true ;
//		c = dbAdapter.getInfoTable4();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		isOpenDialog = preferences.getInt("dialog", 0);

		if(isOpenDialog == 1) {
			dialogAddContact(preferences.getString("name", null), preferences.getString("surname",null), preferences.getString("mail",null));
		}
		else if(isOpenDialog == 2) {
			mail = preferences.getString("mail",null);
			dialogRenameContact(preferences.getString("name", null), preferences.getString("surname",null), mail);
		}

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
				if((ChronoService.isPlaying == 1) || (ChronoService.isPlaying == -1)) {
					Toast.makeText(getActivity(), "Impossibile aggiungere\ncontatti durante la sessione", Toast.LENGTH_SHORT).show();
				}
				else {
					dialogAddContact(null, null, null);
				}
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



		
		switch (item.getItemId()) {
		case R.id.delete_id_contact:
			if((ChronoService.isPlaying == 1) || (ChronoService.isPlaying == -1)) {
				Toast.makeText(getActivity(), "Impossibile cancellare\ni contatti durante la sessione", Toast.LENGTH_SHORT).show();
			}
			else {
				//DEVO ELIMINARE ANCHE LA TABELLA 3 PRIMA DI ELIMINARE QUESTA DOPO IMPLEMENTAZIONE
				Toast.makeText(getActivity(), "Eliminato:"+mail, Toast.LENGTH_SHORT).show();
				dbAdapter.dropContact(mail);
				csca.notifyDataSetChanged();
				//per vedere la modifica in tempo reale
				c = dbAdapter.getInfoTable4();
				//			getActivity().startManagingCursor(c);
				csca = new ContactSimpleCursorAdapter(getActivity(), c);
				listcontact.setAdapter(csca);
			}
			return true;
		case R.id.ren_id_contact:

			dialogRenameContact(null, null, null);
			return true;
		default:
			return super.onContextItemSelected(item);
		}



	}

	private void dialogRenameContact(String nameC, String surnameC, String mailC) {
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.activity_rename_email);
		dialog.setTitle("Rinomina Contatto");
		final String n = nameC;
		final String s = surnameC;
		final String m = mailC;
		//		inDialog = true;
		//personalizzo il Dialog
		name_ = (EditText) dialog.findViewById(R.id.name);
		surname_ = (EditText) dialog.findViewById(R.id.surname);
		email = (TextView) dialog.findViewById(R.id.mail);
		if(isOpenDialog == 2) {
			name_.setText(n);
			surname_.setText(s);
			email.setText(m);
		}
		else {
			email.setText(mail);
			name_.setText(dbAdapter.getName(mail));
			surname_.setText(dbAdapter.getSurname(mail));
		}
		isOpenDialog = 2;
		Button ok = (Button) dialog.findViewById(R.id.btn_yes);
		Button no = (Button) dialog.findViewById(R.id.btn_no);

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
					isOpenDialog = 0;
					csca.notifyDataSetChanged();
					//per vedere la modifica in tempo reale
					c = dbAdapter.getInfoTable4();
					csca = new ContactSimpleCursorAdapter(getActivity(), c);
					listcontact.setAdapter(csca);
					//					inDialog = false;
					dialog.dismiss();
				}
			}
		});
		// cosa faccio al click dell'annulla
		no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				isOpenDialog = 0;
				//				inDialog = false;
				dialog.dismiss();

			}
		});
		dialog.show();
	}

	private void dialogAddContact(String nameC, String surnameC, String mailC) {
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.activity_add_email);
		dialog.setTitle("Aggiungi Contatto");
		final String n = nameC;
		final String s = surnameC;
		final String m = mailC;

		//		inDialog = true;
		//personalizzo il Dialog
		name_ = (EditText) dialog.findViewById(R.id.name);
		surname_ = (EditText) dialog.findViewById(R.id.surname);
		mail_ = (EditText) dialog.findViewById(R.id.mail);
		if(isOpenDialog == 1) {
			name_.setText(n);
			surname_.setText(s);
			mail_.setText(m);
		}
		isOpenDialog = 1;
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
				else if(!checkEmail(mail)) {
					Toast.makeText(getActivity(), "Indirizzo e-mail non corretto", Toast.LENGTH_SHORT).show();
				}
				else {
					dbAdapter.createContact(mail, name, surname);
					isOpenDialog = 0;
					csca.notifyDataSetChanged();
					//per vedere la modifica in tempo reale
					c = dbAdapter.getInfoTable4();
					csca = new ContactSimpleCursorAdapter(getActivity(), c);
					listcontact.setAdapter(csca);
					//					inDialog = false;
					dialog.dismiss();
				}
			}
		});
		// cosa faccio al click dell'annulla
		no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				isOpenDialog = 0;
				//				inDialog = false;
				dialog.dismiss();

			}
		});
		dialog.show();
	}
	public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
			"[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
					"\\@" +
					"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
					"(" +
					"\\." +
					"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
					")+"
			);
	private boolean checkEmail(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}

	public void onPause()
	{
		super.onPause();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		SharedPreferences.Editor editor = preferences.edit();

		//		////		String id_s = dbAdapter.getCurrentSessionID();
		//		if(isOpenDialog == 1) {
		//			editor.putString("name", name_.getText().toString());
		//			editor.putString("surname", surname_.getText().toString());
		//			editor.putString("mail", mail_.getText().toString());
		//		}
		//		else if(isOpenDialog == 2)
		//		{
		//			editor.putString("name", name_.getText().toString());
		//			editor.putString("surname", surname_.getText().toString());
		//			editor.putString("mail", email.getText().toString());
		//		}
		//		//		//Salvataggio impostazioni
		//		//		if(inDialog == true)
		if(getActivity().isFinishing()) {
			editor.putInt("dialog", 0);
		}
		else {
			if(isOpenDialog == 1) {
				editor.putString("name", name_.getText().toString());
				editor.putString("surname", surname_.getText().toString());
				editor.putString("mail", mail_.getText().toString());
			}
			else if(isOpenDialog == 2)
			{
				editor.putString("name", name_.getText().toString());
				editor.putString("surname", surname_.getText().toString());
				editor.putString("mail", email.getText().toString());
			}
			editor.putInt("dialog", isOpenDialog);
		}
//		c.close();



		//facciamo il commit
		editor.commit();

	}


	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		SharedPreferences.Editor editor = preferences.edit();
		if(getActivity().isFinishing()) {
			editor.putInt("dialog", 0);
//			c.close();
		}
		//facciamo il commit
		editor.commit();
	}

	//	@Override
	//	public void onConfigurationChanged(Configuration config) 
	//	{
	//		super.onConfigurationChanged(config);
	//		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
	//		SharedPreferences.Editor editor = preferences.edit();
	//
	//		if(config.orientation == Configuration.ORIENTATION_LANDSCAPE)
	//		{
	//			if(isOpenDialog == 1) {
	//				editor.putString("name", name_.getText().toString());
	//				editor.putString("surname", surname_.getText().toString());
	//				editor.putString("mail", mail_.getText().toString());
	//			}
	//			else if(isOpenDialog == 2)
	//			{
	//				editor.putString("name", name_.getText().toString());
	//				editor.putString("surname", surname_.getText().toString());
	//				editor.putString("mail", email.getText().toString());
	//			}
	//			//		//Salvataggio impostazioni
	//			//		if(inDialog == true)
	//			editor.putInt("dialog", isOpenDialog);
	//
	//
	//		}
	//		//facciamo il commit
	//		editor.commit();
	//	}
	//	 @Override
	//	public void onStart() {
	//		// TODO Auto-generated method stub
	//		super.onStart();
	//		isOpenDialog = 0;
	//	}
	//	 


}
