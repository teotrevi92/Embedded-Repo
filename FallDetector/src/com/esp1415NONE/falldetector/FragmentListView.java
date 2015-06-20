package com.esp1415NONE.falldetector;

import com.esp1415NONE.falldetector.classi.DbAdapter;
import com.esp1415NONE.falldetector.classi.SessionSimpleCursorAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentListView extends ListFragment {

	private DbAdapter dbAdapter;
	private FragmentTransaction fragmentTransaction;
	private FragmentManager fragmentManager;
	private TextView textview;
	private TextView textview1;
	private String ids;
	private int n_fall;
	private int isOpenDialog = 0; //1 se Dialog aperto, 0 se chiuso
	private EditText nameS_;
	private Cursor c;
	private SessionSimpleCursorAdapter ssca;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		dbAdapter = new DbAdapter(getActivity());

		fragmentManager = getActivity().getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();

		//inizializzo cursore
		c = dbAdapter.getAllRowsTable1();

		registerForContextMenu(getListView());

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		isOpenDialog = preferences.getInt("dialog", 0);

		if(isOpenDialog == 1) {
			ids = preferences.getString("ids1", null);
			dialogRenameSession(getActivity(), ids, preferences.getString("nameS",null));
		}

		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {


				default:

					textview = (TextView) view.findViewById(R.id.id_s);
					textview1 = (TextView) view.findViewById(R.id.nfall);

					ids = textview.getText().toString();
					n_fall = Integer.parseInt(textview1.getText().toString());
					String idsC = Integer.toString(ChronoService.id_s);

					if((ids.equals(idsC)) &&  ((ChronoService.isPlaying == 1) || (ChronoService.isPlaying == -1))) {
						FragmentCurrentSession frg = new FragmentCurrentSession();
						fragmentTransaction.replace(R.id.frag_show_activity, frg);
						fragmentTransaction.commit();
					}
					else {
						if(n_fall > 0) {
							FragmentDetailSession frg = new FragmentDetailSession();
							Bundle args = new Bundle();
							args.putString("ids", ids);
							frg.setArguments(args);
							fragmentTransaction.addToBackStack(null);
							fragmentTransaction.replace(R.id.frag_show_activity, frg);
							fragmentTransaction.commit();
						}
						else {
							Toast.makeText(getActivity(), "Non ci sono cadute", Toast.LENGTH_SHORT).show();
						}
					}
					break;
				}

			}

		});

		getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {

				default:
					textview = (TextView) view.findViewById(R.id.id_s);
					ids = textview.getText().toString();

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

		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.main_context_menu, menu);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		String idsC = Integer.toString(ChronoService.id_s);

		switch (item.getItemId()) {
		case R.id.delete_id:

			if((ids.equals(idsC)) && ((ChronoService.isPlaying == 1) || (ChronoService.isPlaying == -1))) {
				Toast.makeText(getActivity(), "Impossibile cancellare\nla sessione in corso", Toast.LENGTH_SHORT).show();
			}
			else {

				Toast.makeText(getActivity(), "Eliminato:"+ids, Toast.LENGTH_SHORT).show();
				dbAdapter.dropSession(ids);
				ssca.notifyDataSetChanged();

				//per vedere la modifica in tempo reale
				c = dbAdapter.getAllRowsTable1();
				ssca = new SessionSimpleCursorAdapter(getActivity(), c, ChronoService.isPlaying, ChronoService.id_s);
				setListAdapter(ssca);
			}
			return true;
		case R.id.ren_id:

			if((ids.equals(idsC)) && ((ChronoService.isPlaying == 1) || (ChronoService.isPlaying == -1))) {
				Toast.makeText(getActivity(), "Impossibile rinominare\nla sessione in corso", Toast.LENGTH_SHORT).show();
			}
			else {
				dialogRenameSession(getActivity(), ids, null);			
			}
			return true;
		case R.id.new_session:

			if(ChronoService.isPlaying == 1 || ChronoService.isPlaying == -1) {
				FragmentCurrentSession ls_fragment = new FragmentCurrentSession();		
				fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment);
				fragmentTransaction.commit();
			}
			else if(ChronoService.isPlaying == 0) {
				FragmentHome ls_fragment = new FragmentHome();		
				fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment);
				fragmentTransaction.commit();
			}

			return true;
		default:
			return super.onContextItemSelected(item);
		}

	}
	private void dialogRenameSession(Activity activity,String ids, String nameSe) {
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.rename_list);

		dialog.setTitle("Rinomina Sessione");
		final String id_s = ids;
		final Activity a = activity;

		dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface dialog)
			{
				isOpenDialog = 0;
				dialog.dismiss();
			}
		});

		String nameDB = dbAdapter.getNameSession(ids);
		nameS_ = (EditText) dialog.findViewById(R.id.nameS);
		if(isOpenDialog == 1)
			nameS_.setText(nameSe);
		else
			nameS_.setText(nameDB);
		isOpenDialog = 1;

		Button ok = (Button) dialog.findViewById(R.id.btn_yes);
		Button no = (Button) dialog.findViewById(R.id.btn_no);

		// cosa faccio al click del conferma
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String nameS = nameS_.getText().toString(); 

				if(nameS.equals(""))
					Toast.makeText(a, "Inserisci un nome", Toast.LENGTH_SHORT).show();
				else {
					dbAdapter.setNameSession(id_s, nameS);

					isOpenDialog = 0;
					ssca.notifyDataSetChanged();

					//per vedere la modifica in tempo reale
					c = dbAdapter.getAllRowsTable1();
					ssca = new SessionSimpleCursorAdapter(getActivity(), c, ChronoService.isPlaying, ChronoService.id_s);
					setListAdapter(ssca);

					dialog.dismiss();
				}
			}
		});

		// cosa faccio al click dell'annulla
		no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				isOpenDialog = 0;
				dialog.dismiss();

			}
		});
		dialog.show();
	}

	public void onPause()
	{
		super.onPause();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		SharedPreferences.Editor editor = preferences.edit();

		if(getActivity().isFinishing()) {
			editor.putInt("dialog", 0);
		}
		else {
			if(isOpenDialog == 1)
				editor.putString("nameS", nameS_.getText().toString());

			//Salvataggio impostazioni
			editor.putInt("dialog", isOpenDialog);
			//
			editor.putString("ids1", ids);
		}

		//facciamo il commit
		editor.commit();

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		SharedPreferences.Editor editor = preferences.edit();
		if(getActivity().isFinishing())
			editor.putInt("dialog", 0);

		//facciamo il commit
		editor.commit();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		c = dbAdapter.getAllRowsTable1();
		ssca = new SessionSimpleCursorAdapter(getActivity(), c, ChronoService.isPlaying, ChronoService.id_s);
		setListAdapter(ssca);
	}

}