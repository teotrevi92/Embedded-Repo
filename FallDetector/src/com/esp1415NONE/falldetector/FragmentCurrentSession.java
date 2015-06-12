package com.esp1415NONE.falldetector;

import com.esp1415NONE.falldetector.ChronoService;
import com.esp1415NONE.falldetector.ChronoService.LocalBinder;
import com.esp1415NONE.falldetector.classi.DbAdapter;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
public class FragmentCurrentSession extends Fragment {	

	private ImageButton play;
	private ImageButton pause;
	private ImageButton stop;
	private ProgressBar pbx;
	private ProgressBar pby;
	private ProgressBar pbz;
	private ChronoService cronom;
	private boolean mBound = false;
	private TextView time;
	private TextView title;
	private TextView statusGps;
	private TextView statusNtw;
	private Timer myTimer;
	private TimerTask myTimerTask;
	//	private Handler handler = new Handler();
	private FragmentTransaction fragmentTransaction;
	private FragmentManager fragmentManager;
	private DbAdapter dbAdapter;
	private static int isOpenDialog = 0;
	private EditText nameS_;


	/** Defines callbacks for service binding, passed to bindService() */
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className,
				IBinder service) {
			// We've bound to LocalService, cast the IBinder and get LocalService instance
			LocalBinder binder = (LocalBinder) service;
			cronom = binder.getService();
			mBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};

	private void inPause()
	{
		stop.setVisibility(View.VISIBLE);
		play.setVisibility(View.VISIBLE);
		pause.setVisibility(View.INVISIBLE);
		title.setText(R.string.playtitleSession);
		title.setVisibility(View.VISIBLE);
		time.setVisibility(View.VISIBLE);
		pbx.setVisibility(View.VISIBLE);
		pby.setVisibility(View.VISIBLE);
		pbz.setVisibility(View.VISIBLE);
		pbx.setProgress(0);
		pby.setProgress(0);
		pbz.setProgress(0);
	}
	private void inPlay()
	{
		stop.setVisibility(View.INVISIBLE);
		play.setVisibility(View.INVISIBLE);
		pause.setVisibility(View.VISIBLE);
		title.setText(R.string.playtitleSession);
		title.setVisibility(View.VISIBLE);
		time.setVisibility(View.VISIBLE);
		pbx.setVisibility(View.VISIBLE);
		pby.setVisibility(View.VISIBLE);
		pbz.setVisibility(View.VISIBLE);

	}
	private void inStop()
	{
		stop.setVisibility(View.INVISIBLE);
		play.setVisibility(View.VISIBLE);
		pause.setVisibility(View.INVISIBLE);
		title.setText(R.string.titleSession);
		title.setVisibility(View.VISIBLE);
		time.setVisibility(View.VISIBLE);
		pbx.setVisibility(View.INVISIBLE);
		pby.setVisibility(View.INVISIBLE);
		pbz.setVisibility(View.INVISIBLE);
		pbx.setProgress(0);
		pby.setProgress(0);
		pbz.setProgress(0);
	}
	private void doStop()
	{
		FragmentResume ls_fragment = new FragmentResume();
		//		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment);
		fragmentTransaction.commit();
		//		time.setVisibility(View.INVISIBLE);
		//		title.setVisibility(View.INVISIBLE);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.activity_fragment_current_session, container, false);
		dbAdapter = new DbAdapter(getActivity());
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		isOpenDialog = preferences.getInt("dialog", 0);

		if(isOpenDialog == 1)
			dialogRenameSession(getActivity(), preferences.getString("ids", null),preferences.getString("nameS",null));

		play = (ImageButton) view.findViewById(R.id.playbutton);
		pause = (ImageButton) view.findViewById(R.id.pausebutton);
		stop = (ImageButton) view.findViewById(R.id.stopbutton);
		time = (TextView) view.findViewById(R.id.textCrono);
		title = (TextView) view.findViewById(R.id.titleSession);
		statusGps =(TextView) view.findViewById(R.id.textGps);
		statusNtw =(TextView) view.findViewById(R.id.textNetwork);
		pbx = (ProgressBar) view.findViewById(R.id.progressBarX);
		pby = (ProgressBar) view.findViewById(R.id.progressBarY);
		pbz = (ProgressBar) view.findViewById(R.id.progressBarZ);

		fragmentManager = getActivity().getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();

		Intent intent = new Intent(getActivity(), ChronoService.class);
		getActivity().startService(intent);
		getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

		play.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), ChronoService.class);
				getActivity().startService(intent);
				getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

				boolean control=true;
				if(!controlInternet()){
					Toast.makeText(getActivity(), R.string.toastNet , Toast.LENGTH_LONG).show();
					control=false;
				}
				if(!controlLocGps() && !controlLocNet()){
					Toast.makeText(getActivity(),  R.string.toastLoc , Toast.LENGTH_LONG).show();
					control=false;
				}
				if(dbAdapter.getNumberContact()==0){
					Toast.makeText(getActivity(),  R.string.toastCont , Toast.LENGTH_LONG).show();
					control=false;
					FragmentSettings ls_fragment = new FragmentSettings();
					fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment);
					fragmentTransaction.commit();
				}


				if(control)
				{
					if (mBound)
					{
						cronom.play();
						inPlay();
						Toast.makeText(getActivity(), R.string.toastPlay , Toast.LENGTH_LONG).show();
					}
				}

			}
		});

		stop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mBound) {
					cronom.stop();
					String tm = cronom.getString();
					time.setText(tm);
					getActivity().unbindService(mConnection);
					mBound = false;
					inStop();
					Toast.makeText(getActivity(), R.string.toastStop , Toast.LENGTH_LONG).show();


					//implemento rinomina
					String ids = dbAdapter.getCurrentSessionID();
					dialogRenameSession(getActivity(),ids, null);
					//					Intent i = new Intent(getActivity(), RenameActivity.class);
					//					i.putExtra("ids", ids);
					//					i.putExtra("where", "stop");
					//					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					//					startActivity(i);
//					doStop();
				}
			}
		});
		/*
		 Quando premo Stop, si aprira' una nuava Activity. In questo caso quando 
		 si vorra' premere su Play, mBound sara' gia' impostata su true.
		 */

		pause.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mBound) 
				{
					//inPause();
					cronom.pause();
					String tm = cronom.getString();
					time.setText(tm);
					inPause();
					Toast.makeText(getActivity(), R.string.toastPause , Toast.LENGTH_LONG).show();
				}
			}
		});





		//ogni mezzo secondo mi aggiorna il cronometro e controlla lo stato
		myTimer = new Timer();   
		myTimerTask = new TimerTask() {  
			@Override  
			public void run() { getActivity().runOnUiThread(new Runnable() {  
				@Override  
				public void run() {  
					if(controlLocGps() || controlLocNet())
						statusGps.setText(R.string.enableLoc);
					else
						statusGps.setText(R.string.NoenableLoc);
					if(controlInternet())
						statusNtw.setText(R.string.enableNet);
					else
						statusNtw.setText(R.string.NoenableNet);

					if (mBound) {
						if (cronom.getPlaying() > 0)
							inPlay();
						else if (cronom.getPlaying() < 0)
							inPause();
						else
							inStop();
						if(cronom.getPlaying()!=0){
							time.setText(cronom.getString());
							pbx.setProgress(cronom.getX());
							pby.setProgress(cronom.getY());
							pbz.setProgress(cronom.getZ());
						}

					}
				}
			});
			}; 
		};
		myTimer.scheduleAtFixedRate(myTimerTask, 0, 200);		
		return view;
	}



	private boolean controlInternet() {
		getActivity();
		NetworkInfo actNetworkInfo = null;
		//controllo CONNESSIONE INTERNET
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
			actNetworkInfo = connectivityManager.getActiveNetworkInfo();
		} catch (NullPointerException e) {}
		return actNetworkInfo!=null;
	}
	private boolean controlLocGps(){
		//Controllo ATTIVAZIONE GPS
		boolean control=false;

		try {
			LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
			control=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (NullPointerException e) {}


		return control;
	}
	private boolean controlLocNet(){
		boolean control=false;
		try {
			LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
			control=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (NullPointerException e) {}
		return control;
	}

	public void onDestroyView() {
		//per non avere piu' thread quando passo da un fragment all'altro chiudo il thread
		myTimer.cancel();
		myTimerTask.cancel();
		super.onDestroy();
	}

	private void dialogRenameSession(Activity activity,String ids, String nameSe) {
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.activity_rename);
		dialog.setTitle("Rinomina Sessione");
		final String id_s = ids;
		final Activity a = activity;
		//personalizzo il Dialog
		nameS_ = (EditText) dialog.findViewById(R.id.nameS);if(isOpenDialog == 1)
			nameS_.setText(nameSe);
		else
			nameS_.setText("Sessione");
		isOpenDialog = 1;
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
					isOpenDialog = 0;
					dialog.dismiss();
					doStop();
				}
			}
		});
		// cosa faccio al click dell'annulla
		no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dbAdapter.setNameSession(id_s, "Sessione");
				isOpenDialog = 0;
				dialog.dismiss();
				doStop();

			}
		});
		dialog.show();
	}

	public void onPause()
	{
		super.onPause();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		SharedPreferences.Editor editor = preferences.edit();

		String id_s = dbAdapter.getCurrentSessionID();
		if(isOpenDialog == 1)
			editor.putString("nameS", nameS_.getText().toString());
		//Salvataggio impostazioni
		editor.putInt("dialog", isOpenDialog);

		editor.putString("ids", id_s);
		//facciamo il commit
		editor.commit();

	}

}
