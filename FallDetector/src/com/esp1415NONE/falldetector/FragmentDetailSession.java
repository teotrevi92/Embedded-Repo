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
	private TextView cad,idess,dataS,nameS,durationS;
	private ImageView logo;
	private Cursor c;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.activity_fragment_detail_session_fixed, container, false);
		listfall = (ListView) view.findViewById(R.id.listfall);
		idess = (TextView) view.findViewById(R.id.idsess);
		nameS = (TextView) view.findViewById(R.id.nameS);
		dataS = (TextView) view.findViewById(R.id.data);
		durationS = (TextView) view.findViewById(R.id.durationS);
		logo = (ImageView) view.findViewById(R.id.logoS);
		dbAdapter = new DbAdapter(getActivity());
		return view;
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		ids = getArguments().getString("ids");


		c = dbAdapter.getInfoTable2(ids);
		listfall.setAdapter(new SessionSimpleCursorAdapterDetails(getActivity(), c));

		listfall.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {

				default:

					cad = (TextView) view.findViewById(R.id.idf);


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
					intent.putExtra("sent", result[7]);
					intent.putExtra("array", result[8]);
					startActivity(intent);
					break;
				}

			}
		});
		//		c = dbAdapter.getInfoTable2(ids);
		String[] arr = new String[3];
		//		String ids = c.getString(c.getColumnIndex("_id"));
		arr = dbAdapter.getInfoTable1(ids);
		idess.setText(ids);
		dataS.setText(arr[0]);
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

		String idsess_ = idess.getText().toString();
		if((idsess_.equals(idsC)) && ((isPlaying == 1) || (isPlaying == -1))) {
			//			Toast.makeText(context, "la sessione corrent e' " + idss, Toast.LENGTH_SHORT).show();
			//			v.setBackgroundColor(Color.RED);
			//			textview.setTextColor(Color.RED);
			//			textview.setTypeface(textview.getTypeface(), Typeface.BOLD);
			//			rec.setVisibility(View.VISIBLE);
			//			view.setBackgroundColor(Color.RED);
			idess.setTextColor(Color.RED);
			nameS.setTextColor(Color.RED);


		}

	}
}
