package com.esp1415NONE.falldetector;

import com.esp1415NONE.falldetector.classi.DbAdapter;
import com.esp1415NONE.falldetector.classi.MyGraph;
import com.esp1415NONE.falldetector.classi.SessionSimpleCursorAdapterDetails;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentDetailSession extends Fragment {

	private String ids;
	private String idf;
	private DbAdapter dbAdapter;
	private Intent intent;
	private ListView listfall;
	private TextView fall,idsess,dateS,nameS,durationS;
	private ImageView logo;
	private Cursor c;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_detail_session_fixed, container, false);

		listfall = (ListView) view.findViewById(R.id.listfall);
		idsess = (TextView) view.findViewById(R.id.idsess);
		nameS = (TextView) view.findViewById(R.id.nameS);
		dateS = (TextView) view.findViewById(R.id.date);
		durationS = (TextView) view.findViewById(R.id.durationS);
		logo = (ImageView) view.findViewById(R.id.logoS);

		//inizializzazione database
		dbAdapter = new DbAdapter(getActivity());
		return view;
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		ids = getArguments().getString("ids");

		//inizializzo cursore
		c = dbAdapter.getInfoTable2(ids);

		//richiamo il SimpleCursorAdampter
		listfall.setAdapter(new SessionSimpleCursorAdapterDetails(getActivity(), c));

		listfall.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {

				default:

					fall = (TextView) view.findViewById(R.id.idf);

					idf = fall.getText().toString();

					String[] result = new String[8];
					result = dbAdapter.getMoreInfoTable2(ids, idf);

					intent = new Intent(getActivity(),DetailFallActivity.class);
					intent.putExtra("ids", result[0]);
					intent.putExtra("name_s", result[1]);
					intent.putExtra("date_s", result[2]);
					intent.putExtra("lat", result[3]);
					intent.putExtra("long", result[4]);
					intent.putExtra("idf", result[5]);
					intent.putExtra("date_f", result[6]);
					intent.putExtra("sent", result[7]);
					intent.putExtra("array", result[8]);
					startActivity(intent);

					break;
				}

			}
		});

		String[] arr = new String[3];

		arr = dbAdapter.getInfoTable1(ids);
		idsess.setText(ids);
		dateS.setText(arr[0]);
		nameS.setText(arr[1]);
		durationS.setText(arr[2]);

		int[] dateA = new int[6];
		dateA = dbAdapter.getDate(arr[0]);

		int size = 30;
		MyGraph rndBitmap = new MyGraph(size,size);
		rndBitmap.doRandomImg(dateA[0], dateA[1], dateA[2], dateA[3], dateA[4], dateA[5], size);
		logo.setImageBitmap(rndBitmap.getRandomImg());

		String idsC = Integer.toString(ChronoService.id_s);
		int isPlaying = ChronoService.isPlaying;

		String idsess_ = idsess.getText().toString();
		if((idsess_.equals(idsC)) && ((isPlaying == 1) || (isPlaying == -1))) {

			//evidenzio sessione corrente
			idsess.setTextColor(Color.RED);
			nameS.setTextColor(Color.RED);

		}

	}
}
