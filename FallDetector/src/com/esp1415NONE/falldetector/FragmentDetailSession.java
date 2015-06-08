package com.esp1415NONE.falldetector;

import com.esp1415NONE.falldetector.classi.DbAdapter;
import com.esp1415NONE.falldetector.classi.SessionSimpleCursorAdapterDetails;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentDetailSession extends ListFragment {

	//	private Activity activity;
	private String ids;
	private String idf;
	private DbAdapter dbAdapter;
	private ListView ls;
	private TextView idsess;
	private TextView cad;
	private Intent intent;



	@Override
	public void onAttach(Activity a) {
		super.onAttach(a);
		//		activity = a;
	}

	//	@Override
	//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	//			Bundle savedInstanceState) {
	//		// TODO Auto-generated method stub
	//		View view = inflater.inflate(R.layout.activity_fragment_detail_session, container, false);
	//		return view;
	//	}
	//	

	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		ids = getArguments().getString("ids");
		dbAdapter = new DbAdapter(getActivity());
		ls = (ListView) getActivity().findViewById(android.R.id.list);
		Cursor c = dbAdapter.getInfoTable2(ids);
		getActivity().startManagingCursor(c);
		setListAdapter(new SessionSimpleCursorAdapterDetails(getActivity(), c));

		ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {

				default:
					idsess = (TextView) view.findViewById(R.id.idsess);
					cad = (TextView) view.findViewById(R.id.idf);

					ids = idsess.getText().toString();
					idf = cad.getText().toString();
					String[] result = new String[8];
					result = dbAdapter.getMoreInfoTable2(ids, idf);
					intent = new Intent(getActivity(),DetailFallActivity.class);
					intent.putExtra("ids", result[0]);
					intent.putExtra("nomeS", result[1]);
					intent.putExtra("dataS", result[2]);
					intent.putExtra("lat", result[3]);
					intent.putExtra("long", result[4]);
					intent.putExtra("idf", result[5]);
					intent.putExtra("dataf", result[6]);
					intent.putExtra("array", result[7]);
					startActivity(intent);
					break;
				}

			}
		});


		//		registerForContextMenu(getListView());



	}




	//	@Override
	//	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	//		// TODO Auto-generated method stub
	//		super.onCreateContextMenu(menu, v, menuInfo);
	//		MenuInflater inflater = activity.getMenuInflater();
	//		inflater.inflate(R.menu.main_context_menu, menu);
	//
	//	}
	//
	//
	//	@Override
	//	public boolean onContextItemSelected(MenuItem item) {
	//		// TODO Auto-generated method stub
	//
	//
	//		switch (item.getItemId()) {
	//		case R.id.delete_id:
	//			Toast.makeText(activity, "Da implementare la rimozione", Toast.LENGTH_SHORT).show();
	//			
	//			return true;
	//		case R.id.ren_id:
	//			Toast.makeText(activity, "Da implementare la modifica", Toast.LENGTH_SHORT).show();
	//			// arrayList.set(info.position,setItem);
	//			// adapter.notifyDataSetChanged();
	//			return true;
	//		case R.id.new_session:
	//			Toast.makeText(activity, "Da implementare altre opzioni", Toast.LENGTH_SHORT).show();
	//			return true;
	//		default:
	//			return super.onContextItemSelected(item);
	//		}
	//
	//
	//
	//	}



}
