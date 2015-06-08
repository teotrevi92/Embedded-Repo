package com.esp1415NONE.falldetector;



import java.util.Calendar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TimePicker;

public class FragmentSettings extends Fragment {

	Spinner sp_acc;
	Spinner sp_duration;
	Spinner sp_sens;
	CheckBox cbt;
	TimePicker tmpicker;
	private PendingIntent pendIntent;
	private AlarmManager alarmManager;
	private Button button; //Tri
	private Button button_add; //Tri
	private FragmentTransaction fragmentTransaction;
	private FragmentManager fragmentManager;

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
		int int_spH = preferences.getInt("pickerValueH", 0);
		int int_spM = preferences.getInt("pickerValueM", 0);
		fragmentManager = getActivity().getSupportFragmentManager(); //Tri
		fragmentTransaction = fragmentManager.beginTransaction(); //Tri

		sp_acc = (Spinner) view.findViewById(R.id.spinnerAcc);
		sp_duration = (Spinner) view.findViewById(R.id.spinnerMaxSession);
		sp_sens = (Spinner) view.findViewById(R.id.spinnerSens);
		cbt = (CheckBox) view.findViewById(R.id.checkBoxTimer);
		tmpicker = (TimePicker) view.findViewById(R.id.timePicker);
		button = (Button) view.findViewById(R.id.buttonemail); //Tri 
		button_add = (Button) view.findViewById(R.id.addemail); //Tri 

		//Reimposto i valori salvati
		sp_acc.setSelection(int_sp_acc);
		sp_duration.setSelection(int_sp_duration);
		sp_sens.setSelection(int_sp_sens);
		cbt.setChecked(bln_cbt);
		tmpicker.setCurrentHour(int_spH);
		tmpicker.setCurrentMinute(int_spM);

		//Inizializzazione per il promemoria
		alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
		Intent myIntent = new Intent(getActivity(), AlarmReceiver.class);
		pendIntent = PendingIntent.getBroadcast(getActivity(), 0, myIntent,0);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentListContacts ls_fragment = new FragmentListContacts();
				fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment);
				fragmentTransaction.commit();
				/* controllo degli stack per rach*/
			}
		});


		button_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(),AddEmailActivity.class);
				startActivity(i);
				/* controllo degli stack per rach*/
			}
		});

		cbt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(cbt.isChecked())
				{//Attiva promemoria
					int hour_now = tmpicker.getCurrentHour();
					int min_now = tmpicker.getCurrentMinute();

					Calendar mycalendar = Calendar.getInstance();
					mycalendar.setTimeInMillis(System.currentTimeMillis());
					mycalendar.set(Calendar.HOUR_OF_DAY, hour_now);
					mycalendar.set(Calendar.MINUTE, min_now);
					//Imposto promemoria
					alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, mycalendar.getTimeInMillis(),
							AlarmManager.INTERVAL_DAY, pendIntent);
				}
				else
				{//Disattiva promemoria
					if (alarmManager!= null) {
						alarmManager.cancel(pendIntent);
					}	
				}

			}
		});


		tmpicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				//Reimposto il check e annullo il promemoria.
				cbt.setChecked(false);
				if (alarmManager!= null) {
					alarmManager.cancel(pendIntent);
				}
			}
		});

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
		int hour = tmpicker.getCurrentHour();
		int min = tmpicker.getCurrentMinute();



		//Salvataggio impostazioni
		editor.putInt("spinnerValueAcc", int_sp_acc);
		editor.putInt("spinnerValueDuration", int_sp_duration);
		editor.putInt("spinnerValueSens", int_sp_sens);
		editor.putBoolean("checkBoxTimerValue", bln_cbt);
		editor.putInt("pickerValueH", hour);
		editor.putInt("pickerValueM", min);
		//facciamo il commit
		editor.commit();

	}


}
