package com.esp1415NONE.falldetector;



import java.util.Calendar;

import com.esp1415NONE.falldetector.classi.DbAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class FragmentSettings extends Fragment {

	private Spinner sp_acc;
	private Spinner sp_duration;
	private Spinner sp_sens;
	private CheckBox cbt;
	private TimePicker tmpicker;
	private PendingIntent pendIntent;
	private AlarmManager alarmManager;
	private Button button, btSession, btData; //Tri
	private FragmentTransaction fragmentTransaction;
	private FragmentManager fragmentManager;
	private DbAdapter dbAdapter;
	private int isOpenDialog = 0; //1 se cancella sessioni, 2 se cancella tutto, 0 se non ci sono Dialog aperti
	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.activity_fragment_settings, container, false);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		isOpenDialog = preferences.getInt("dialog", 0);
		if(isOpenDialog == 1) {
			dialogDropSession();
		}
		else if(isOpenDialog == 2) {
			dialogDropData();
		}
		int int_sp_acc = preferences.getInt("spinnerValueAcc", 1);
		int int_sp_duration = preferences.getInt("spinnerValueDuration", 1);
		int int_sp_sens = preferences.getInt("spinnerValueSens", 1);
		boolean bln_cbt = preferences.getBoolean("checkBoxTimerValue", false);
		int int_spH = preferences.getInt("pickerValueH", 0);
		int int_spM = preferences.getInt("pickerValueM", 0);
		fragmentManager = getActivity().getSupportFragmentManager(); //Tri
		fragmentTransaction = fragmentManager.beginTransaction(); //Tri
		dbAdapter = new DbAdapter(getActivity()); //Tri


		sp_acc = (Spinner) view.findViewById(R.id.spinnerAcc);
		sp_duration = (Spinner) view.findViewById(R.id.spinnerMaxSession);
		sp_sens = (Spinner) view.findViewById(R.id.spinnerSens);
		cbt = (CheckBox) view.findViewById(R.id.checkBoxTimer);
		tmpicker = (TimePicker) view.findViewById(R.id.timePicker);
		button = (Button) view.findViewById(R.id.buttonemail); //Tri 
		btSession = (Button) view.findViewById(R.id.deletesession); //Tri
		btData = (Button) view.findViewById(R.id.deletedata); //Tri

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
				//				if(dbAdapter.getNumberContact() == 0) {
				//					Toast.makeText(getActivity(), "Non ci sono contatti salvati", Toast.LENGTH_SHORT).show();
				//				}
				//				else {
				FragmentListContacts ls_fragment = new FragmentListContacts();
				fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment);
				fragmentTransaction.commit();
				/*CONTROLLO DEGLI STACK PER RACH*/
				//				}
			}
		});

		btSession.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if((ChronoService.isPlaying == 1) || (ChronoService.isPlaying == -1)) {
					Toast.makeText(getActivity(),"Impossibile durante la sessione in corso", Toast.LENGTH_SHORT).show();

				}
				else {

					//					dbAdapter.dropAllSession();
					//					Toast.makeText(getActivity(),"Cancellate tutte le sessioni", Toast.LENGTH_SHORT).show();
					dialogDropSession();
				}
			}
		});

		btData.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				if((ChronoService.isPlaying == 1) || (ChronoService.isPlaying == -1)) {
					Toast.makeText(getActivity(),"Impossibile durante la sessione in corso", Toast.LENGTH_SHORT).show();

				}
				else {
					dialogDropData();
//					dbAdapter.dropAllData();
//					Toast.makeText(getActivity(),"Cancellati tutti i dati", Toast.LENGTH_SHORT).show();
				}
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
		if(getActivity().isFinishing()) {
			editor.putInt("dialog", 0);
		}
		else {
			editor.putInt("dialog", isOpenDialog);
		}


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

	private void dialogDropSession() {
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.confirm);
		dialog.setTitle("Gestione Dati Database");
		TextView text;
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
		{@Override
			public void onCancel(DialogInterface dialog)
		{
			isOpenDialog = 0;
			dialog.dismiss();
		}
		});

		//personalizzo il Dialog
		text = (TextView) dialog.findViewById(R.id.text);
		text.setText("Vuoi cancellare tutte le sessioni con relative cadute dal Database?");
		isOpenDialog = 1;
		Button ok = (Button) dialog.findViewById(R.id.btn_yes);
		Button no = (Button) dialog.findViewById(R.id.btn_no);

		// cosa faccio al click del conferma
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dbAdapter.dropAllSession();
				isOpenDialog = 0;
				Toast.makeText(getActivity(),"Cancellate tutte le sessioni", Toast.LENGTH_SHORT).show();
				dialog.dismiss();

			}
		});
		// cosa faccio al click dell'annulla
		no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				isOpenDialog = 0;
				//				inDialog = false;
				dialog.dismiss();

			}
		});
		dialog.show();
	}
	
	private void dialogDropData() {
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.confirm);
		dialog.setTitle("Gestione Dati Database");
		TextView text;
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
		{@Override
			public void onCancel(DialogInterface dialog)
		{
			isOpenDialog = 0;
			dialog.dismiss();
		}
		});

		//personalizzo il Dialog
		text = (TextView) dialog.findViewById(R.id.text);
		text.setText("Vuoi cancellare tutti i dati del Database?");
		isOpenDialog = 2;
		Button ok = (Button) dialog.findViewById(R.id.btn_yes);
		Button no = (Button) dialog.findViewById(R.id.btn_no);

		// cosa faccio al click del conferma
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dbAdapter.dropAllData();
				isOpenDialog = 0;
				Toast.makeText(getActivity(),"Cancellate tutti i dati", Toast.LENGTH_SHORT).show();
				dialog.dismiss();

			}
		});
		// cosa faccio al click dell'annulla
		no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				isOpenDialog = 0;
				//				inDialog = false;
				dialog.dismiss();

			}
		});
		dialog.show();
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		SharedPreferences.Editor editor = preferences.edit();
		if(getActivity().isFinishing())
			editor.putInt("dialog", 0);
		//			c.close();
		//		}
		//facciamo il commit
		editor.commit();
	}


}
