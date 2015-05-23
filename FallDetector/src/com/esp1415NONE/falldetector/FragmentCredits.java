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

	Button b1;
	Button b2;
	Button b3;
	Button b4;
	DbAdapter dbHelper;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.activity_fragment_credits, container, false);
		
		b1 = (Button) view.findViewById(R.id.button1);
		b2 = (Button) view.findViewById(R.id.button2);
		b3 = (Button) view.findViewById(R.id.button3);
		b4 = (Button) view.findViewById(R.id.button4);
		dbHelper = new DbAdapter(getActivity());

		b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), dbHelper.getTable1(), Toast.LENGTH_LONG).show();
			}
		});
		b2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), dbHelper.getTable2(), Toast.LENGTH_LONG).show();
			}
		});
		b3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), dbHelper.getTable3(), Toast.LENGTH_LONG).show();
			}
		});
		b4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), dbHelper.getTable4(), Toast.LENGTH_LONG).show();
			}
		});
		
		
		return view;
	}


}
