package com.esp1415NONE.falldetector;




import com.esp1415NONE.falldetector.LocationService.LocalBinder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
//import android.widget.Toast;



public class ToastAllertActivity extends Activity{
	
	
	Handler handler;
    boolean check;
    boolean mBound = false;
    LocationService loc;
    

    private Intent intent; //Tri
    private String ids; //Tri
    private String idf; //Tri
//    private DbAdapter dbAdapter;
 
    
    
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            loc = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
    

	@Override
	public void onCreate(Bundle state) {
		/* Verra' chiusa dopo 10 secondi se non viene premuto il tasto
		 annulla e verra' inviata la mail*/
		super.onCreate(state);
		intent = getIntent(); //Tri
		ids = intent.getStringExtra("ids"); //Tri
		idf = intent.getStringExtra("idf"); //Tri
//		dbAdapter = new DbAdapter(this);
		
		Button ok = new Button(this);
		ok.setText(R.string.toast_act_button);
		TextView txt = new TextView(this);
		txt.setText(R.string.toast_act_messagge);
		check=false;
		
		Intent intent = new Intent(this, LocationService.class);
		startService(intent);
	    bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		
		ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				check = true;
				if(mBound){
					loc.finish();
					loc.stopAlarm();
				}
//				unbindService(mConnection);
				
				/* QUI BISOGNA SALVARE CHE NON E' STATA INVIATA LA MAIL*/
				
				ToastAllertActivity.this.finish();
				
			}
		});
		
		//Creo il layout
		LinearLayout mylayout = new LinearLayout(this);
		//Aggiungo gli elementi al layout
		mylayout.addView(ok);
		mylayout.addView(txt);
		
		mylayout.setGravity(Gravity.CENTER);
		//Visualizzo il layout
		setContentView(mylayout);
		playCountDown();
	}


	private void playCountDown() {
		//se per 10 secondi non viene premuto il tasto ok, viene chiusa
				handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						if(mBound) 
							loc.setId(ids, idf); 
						
						if(!check)//se non e' stato premuto Annulla chiudi dopo 10 secondi e invia mail
						{
							if(mBound){
								loc.check();
								loc.stopAlarm();
							}
							ToastAllertActivity.this.finish(); 
						}
					}
				}, 10000);
		
	}
	


	
	
}