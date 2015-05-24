package com.esp1415NONE.falldetector;



import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Spinner;

public class FragmentSettings extends Fragment {

	Spinner sp_acc;
	Spinner sp_duration;
	Spinner sp_sens;
	CheckBox cbt;
	
	View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.activity_fragment_settings, container, false);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		int int_sp_acc = preferences.getInt("spinnerValueAcc", 1);
		int int_sp_duration = preferences.getInt("spinnerValueDuration", 1);
		int int_sp_sens = preferences.getInt("spinnerValueSens", 1);
		boolean bln_cbt = preferences.getBoolean("checkBoxTimerValue", false);
		
		sp_acc = (Spinner) view.findViewById(R.id.spinnerAcc);
		sp_duration = (Spinner) view.findViewById(R.id.spinnerMaxSession);
		sp_sens = (Spinner) view.findViewById(R.id.spinnerSens);
		cbt = (CheckBox) view.findViewById(R.id.checkBoxTimer);
		
		sp_acc.setSelection(int_sp_acc);
		sp_duration.setSelection(int_sp_duration);
		sp_sens.setSelection(int_sp_sens);
		cbt.setChecked(bln_cbt);
		
		return view;
	}
	
	public void onPause()
	{
		super.onPause();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		SharedPreferences.Editor editor = preferences.edit();
		int int_sp_acc = sp_acc.getSelectedItemPosition();
		int int_sp_duration = sp_duration.getSelectedItemPosition();
		int int_sp_sens = sp_sens.getSelectedItemPosition();
		boolean bln_cbt = cbt.isChecked();
		editor.putInt("spinnerValueAcc", int_sp_acc);
		editor.putInt("spinnerValueDuration", int_sp_duration);
		editor.putInt("spinnerValueSens", int_sp_sens);
		editor.putBoolean("checkBoxTimerValue", bln_cbt);
		//facciamo il commit
		editor.commit();
		
	}
	
	
}
