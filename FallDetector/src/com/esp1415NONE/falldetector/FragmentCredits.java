package com.esp1415NONE.falldetector;

import com.esp1415NONE.falldetector.classi.DbAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class FragmentCredits extends Fragment {
	private Button bt1,bt2;
	private DbAdapter dbAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.activity_fragment_credits, container, false);
		bt1 = (Button) view.findViewById(R.id.button1);
		bt2 = (Button) view.findViewById(R.id.button2);
		dbAdapter = new DbAdapter(getActivity());
		bt1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), ""+dbAdapter.getTable3(), Toast.LENGTH_LONG).show();
			}
		});
		bt2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), ""+dbAdapter.getTable2plus(), Toast.LENGTH_LONG).show();
			}
		});

		return view;
	}


}
