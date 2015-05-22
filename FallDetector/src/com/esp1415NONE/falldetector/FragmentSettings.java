package com.esp1415NONE.falldetector;



import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

public class FragmentSettings extends Fragment {

	View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.activity_fragment_settings, container, false);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		int int_sp = preferences.getInt("spinnerValue", 1);
		
		Spinner sp = (Spinner) view.findViewById(R.id.spinner1);
		sp.setSelection(int_sp);
		
		return view;
	}
	
	public void onPause()
	{
		super.onPause();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		SharedPreferences.Editor editor = preferences.edit();
		Spinner sp = (Spinner) view.findViewById(R.id.spinner1);
		int int_sp = sp.getSelectedItemPosition();
		editor.putInt("spinnerValue", int_sp);
		//facciamo il commit
		editor.commit();
		
	}
	
	
}
