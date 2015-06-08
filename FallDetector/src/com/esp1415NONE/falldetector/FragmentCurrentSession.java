package com.esp1415NONE.falldetector;

import com.esp1415NONE.falldetector.ChronoService;
import com.esp1415NONE.falldetector.ChronoService.LocalBinder;
import com.esp1415NONE.falldetector.classi.DbAdapter;

import java.util.Timer;
import java.util.TimerTask;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
public class FragmentCurrentSession extends Fragment {	

	ImageButton play;
	ImageButton pause;
	ImageButton stop;
	ChronoService cronom;
	boolean mBound = false;
	TextView time;
	TextView title;
	TextView statusGps;
	TextView statusNtw;
	Timer myTimer;
	TimerTask myTimerTask;
	Handler hander = new Handler();
	private FragmentTransaction fragmentTransaction;
	private FragmentManager fragmentManager;
	private DbAdapter dbAdapter;


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
	}
	private void inPlay()
	{
		stop.setVisibility(View.INVISIBLE);
		play.setVisibility(View.INVISIBLE);
		pause.setVisibility(View.VISIBLE);
		title.setText(R.string.playtitleSession);
		title.setVisibility(View.VISIBLE);
		time.setVisibility(View.VISIBLE);

	}
	private void inStop()
	{
		stop.setVisibility(View.INVISIBLE);
		play.setVisibility(View.VISIBLE);
		pause.setVisibility(View.INVISIBLE);
		title.setText(R.string.titleSession);
		title.setVisibility(View.VISIBLE);
		time.setVisibility(View.VISIBLE);
	}
	private void doStop()
	{
		FragmentResume ls_fragment = new FragmentResume();
		//		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment);
		fragmentTransaction.commit();
		time.setVisibility(View.INVISIBLE);
		title.setVisibility(View.INVISIBLE);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.activity_fragment_current_session, container, false);
		dbAdapter = new DbAdapter(getActivity());

		play = (ImageButton) view.findViewById(R.id.playbutton);
		pause = (ImageButton) view.findViewById(R.id.pausebutton);
		stop = (ImageButton) view.findViewById(R.id.stopbutton);
		time = (TextView) view.findViewById(R.id.textCrono);
		title = (TextView) view.findViewById(R.id.titleSession);
		statusGps =(TextView) view.findViewById(R.id.textGps);
		statusNtw =(TextView) view.findViewById(R.id.textNetwork);

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
				if(controlInternet())
				{
					if (mBound)
					{
						cronom.play();
						inPlay();
						Toast.makeText(getActivity(), "Play" , Toast.LENGTH_LONG).show();
					}
				}
				else
					Toast.makeText(getActivity(), "ATTIVARE CONNESSIONE DATI" , Toast.LENGTH_LONG).show();

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
					Toast.makeText(getActivity(), "Stop" , Toast.LENGTH_LONG).show();


					//implemento rinomina
					String ids = dbAdapter.getCurrentSessionID();
					Intent i = new Intent(getActivity(), RenameActivity.class);
					i.putExtra("ids", ids);
					i.putExtra("where", "stop");
					//i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(i);
					doStop();
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
					Toast.makeText(getActivity(), "Pause" , Toast.LENGTH_LONG).show();
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
					if(controlGps())
						statusGps.setText(R.string.enableGps);
					else
						statusGps.setText(R.string.NoenableGps);
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
						if(cronom.getPlaying()!=0)
							time.setText(cronom.getString());
					}
				}
			});
			}; 
		};
		myTimer.scheduleAtFixedRate(myTimerTask, 0, 500);		
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
	private boolean controlGps(){
		//Controllo ATTIVAZIONE GPS
		boolean control=false;

		try {
			LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
			control=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (NullPointerException e) {}
		return control;
	}

	public void onDestroyView() {
		//per non avere piu' thread quando passo da un fragment all'altro chiudo il thread
		myTimer.cancel();
		myTimerTask.cancel();
		super.onDestroy();
	}

}
