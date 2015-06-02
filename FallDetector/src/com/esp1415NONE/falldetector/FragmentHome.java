package com.esp1415NONE.falldetector;

import java.util.Timer;
import java.util.TimerTask;

import com.esp1415NONE.falldetector.ChronoService.LocalBinder;

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

public class FragmentHome extends Fragment{
	
	ChronoService cronom;
	boolean mBound = false;
	ImageButton play;
	TextView titlehome;
	TextView statusGps;
	TextView statusNtw;
	private FragmentTransaction fragmentTransaction;
	private FragmentManager fragmentManager;
	Timer myTimer;
	TimerTask myTimerTask;
	Handler hander = new Handler();
	
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
				if(controlGps())
				{
					if (mBound)
					{
						cronom.play();
						inPlay();
						Toast.makeText(getActivity(), "Play" , Toast.LENGTH_LONG).show();
					}	
				}
				else
					Toast.makeText(getActivity(), "ATTIVARE GPS" , Toast.LENGTH_LONG).show();
			}
			});
	    
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
		//controllo CONNESSIONE INTERNET
		ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo actNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return actNetworkInfo!=null;
	}
	private boolean controlGps(){
		//Controllo ATTIVAZIONE GPS
		LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	
	
	public void onDestroyView() {
		//per non avere piu' thread quando passo da un fragment all'altro chiudo il thread
	    myTimer.cancel();
	    myTimerTask.cancel();
	    super.onDestroy();
	}
}