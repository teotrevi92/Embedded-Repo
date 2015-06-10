package com.esp1415NONE.falldetector;

import com.esp1415NONE.falldetector.classi.DbAdapter;
import com.esp1415NONE.falldetector.classi.SessionSimpleCursorAdapter;

import android.app.Activity;
import android.app.Dialog;
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
	//	private TextView textview2;
	private String ids;
	private int cad;

	private SessionSimpleCursorAdapter ssca;

	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		dbAdapter = new DbAdapter(getActivity());
		fragmentManager = getActivity().getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		Cursor c = dbAdapter.getAllRowsTable1();
		getActivity().startManagingCursor(c);
		ssca = new SessionSimpleCursorAdapter(getActivity(), c);
		setListAdapter(ssca);

		registerForContextMenu(getListView());


		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
						//						fragmentManager.popBackStack(); //viene tolto dallo stack il fragment precedente
						fragmentTransaction.commit();
						//						fragmentTransaction = fragmentManager.beginTransaction();
					}
					else {
						Toast.makeText(getActivity(), "Non ci sono cadute", Toast.LENGTH_SHORT).show();
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
					textview = (TextView) view.findViewById(R.id.nome);
					ids = textview.getText().toString();

					//							dbAdapter.dropSession(ids);
					//							Cursor c = dbAdapter.getAllRowsTable1();
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
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.main_context_menu, menu);

		//		View parent = ((ViewGroup)v).getChildAt(1);
		//		View child = ((ViewGroup)parent).getChildAt(1);
		//		View child2 = ((ViewGroup)child).getChildAt(0);
		//		TextView child3 =(TextView)((ViewGroup)child2).getChildAt(1);
		//		ids = child3.getText().toString();



		//		ids = textview.getText().toString();
	}


	//	@SuppressWarnings("deprecation")
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		//	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		//	int pos = info.position;


		Cursor c;
		switch (item.getItemId()) {
		case R.id.delete_id:

			Toast.makeText(getActivity(), "Eliminato:"+ids, Toast.LENGTH_SHORT).show();
			dbAdapter.dropSession(ids);
			ssca.notifyDataSetChanged();
			//per vedere la modifica in tempo reale
			c = dbAdapter.getAllRowsTable1();
			//			getActivity().startManagingCursor(c);
			ssca = new SessionSimpleCursorAdapter(getActivity(), c);
			setListAdapter(ssca);
			return true;
		case R.id.ren_id:
			//			Toast.makeText(activity, "Rinominato", Toast.LENGTH_SHORT).show();
			//			RenameDialog rd = new RenameDialog(getActivity(), ids);
			//            rd.show();
			//			Intent i = new Intent(getActivity(), RenameActivity.class);
			//			i.putExtra("ids", ids);
			//			i.putExtra("where", "rename");
			//			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//			startActivity(i);
			dialogRenameSession(getActivity(), ids);
			ssca.notifyDataSetChanged();
			//per vedere la modifica in tempo reale
			//SEMBRA NON FUNZIONARE QUESTO METODO
			c = dbAdapter.getAllRowsTable1();
			//			getActivity().startManagingCursor(c);
			ssca = new SessionSimpleCursorAdapter(getActivity(), c);
			setListAdapter(ssca);
			// arrayList.set(info.position,setItem);
			// adapter.notifyDataSetChanged();
			return true;
		case R.id.new_session:
			FragmentCurrentSession ls_fragment = new FragmentCurrentSession();
			//			fragmentManager.popBackStack(); //viene tolto dallo stack il fragment precedente
			fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment);
			fragmentTransaction.commit();
			//			fragmentTransaction = fragmentManager.beginTransaction();
			//			fragmentTransaction.addToBackStack(null);
			//			Toast.makeText(activity, "Da implementare altre opzioni", Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onContextItemSelected(item);
		}


	}
	private void dialogRenameSession(Activity activity,String ids) {
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.activity_rename);
		dialog.setTitle("Rinomina Sessione");
		final String id_s = ids;
		final Activity a = activity;
		//personalizzo il Dialog
		final EditText nameS_ = (EditText) dialog.findViewById(R.id.nameS);
		String nameDB = dbAdapter.getNameSession(ids);
		nameS_.setText(nameDB);
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
					ssca.notifyDataSetChanged();
					//per vedere la modifica in tempo reale
					Cursor c = dbAdapter.getAllRowsTable1();
					ssca = new SessionSimpleCursorAdapter(getActivity(), c);
					setListAdapter(ssca);
					dialog.dismiss();
				}
			}
		});
		// cosa faccio al click dell'annulla
		no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dbAdapter.setNameSession(id_s, "Sessione");
				dialog.dismiss();

			}
		});
		dialog.show();
	}


}