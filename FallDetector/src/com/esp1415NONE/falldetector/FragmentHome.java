package com.esp1415NONE.falldetector;

import java.util.Timer;
import java.util.TimerTask;

import com.esp1415NONE.falldetector.ChronoService.LocalBinder;
import com.esp1415NONE.falldetector.classi.DbAdapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

public class FragmentHome extends Fragment{

	private ChronoService cronom;
	private boolean mBound = false;
	private ImageButton play;
	private TextView titlehome;
	private TextView statusGps;
	private TextView statusNtw;
	private FragmentTransaction fragmentTransaction;
	private FragmentManager fragmentManager;
	private Timer myTimer;
	private TimerTask myTimerTask;
	private DbAdapter dbAdapter;
	//	private Handler handler = new Handler();

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

	private void inPlay()
	{
		FragmentCurrentSession ls_fragment2 = new FragmentCurrentSession();
		fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment2);
		fragmentTransaction.commit();
		fragmentTransaction = fragmentManager.beginTransaction();
	}
	private void inStop()
	{
		play.setVisibility(View.VISIBLE);
		titlehome.setVisibility(View.VISIBLE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.activity_fragment_home, container, false);
		dbAdapter = new DbAdapter(getActivity());
		play = (ImageButton) view.findViewById(R.id.startSession);
		titlehome = (TextView) view.findViewById(R.id.titleHome);
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
				boolean control=true;
				if(!controlInternet()){
					Toast.makeText(getActivity(), R.string.toastNet , Toast.LENGTH_LONG).show();
					control=false;
				}
				if(!controlLocGps() && !controlLocNet()){
					Toast.makeText(getActivity(), R.string.toastLoc , Toast.LENGTH_LONG).show();
					control=false;
				}
				if(dbAdapter.getNumberContact()==0){
					Toast.makeText(getActivity(),  R.string.toastCont , Toast.LENGTH_LONG).show();
					control=false;
				}
					
				if(control){
					if (mBound)
					{
						cronom.play();
						inPlay();
						Toast.makeText(getActivity(), R.string.toastPlay, Toast.LENGTH_LONG).show();
					}	
				}
			}
		});

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
						if (cronom.getPlaying() !=0)
							inPlay();
						else
							inStop();
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
}