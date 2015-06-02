package com.esp1415NONE.falldetector;


import java.io.IOException;
import java.util.List;

import com.esp1415NONE.falldetector.classi.SoundManager;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



public class ToastAllertActivity extends Activity implements ConnectionCallbacks,
OnConnectionFailedListener {
	
	Intent email;

	String[] emailTo = {"azi92@hotmail.it", "azi92rach@gmail.com"};
	String emailText = "Aiuto!! Sono caduto. Mi trovo qui:\n";
	String subject = "AIUTO DI SOCCORSO";
	Handler handler;
	boolean check=false;;
	boolean sent = false; //se la mail non viene inviata il valore resta false

	//Variabili per la localizzazione
    private static final String TAG = MainActivity.class.getSimpleName();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private Geocoder geocoder;
    private String where;
	
	
	
	@Override
	public void onCreate(Bundle state) {
		// auto-kill activity after X seconds <-------------------------
		super.onCreate(state);
		
		SoundManager.init(this);
		SoundManager.play();
		Button ok = new Button(this);
		ok.setText(R.string.toast_act_button);
		TextView txt = new TextView(this);
		txt.setText(R.string.toast_act_messagge);
		
		 //Controllo che il play service sia accessibile
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
        }
        //Cerco e salvo la localizzazione
        geocoder = new Geocoder(this);
        setLocation();
		
		
		ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SoundManager.stop();
				ToastAllertActivity.this.finish();
				check = true;
				
			}
		});
		
		// Create the layout
		LinearLayout mylayout = new LinearLayout(this);
		// Add the UI elements to the layout
		mylayout.addView(ok);
		mylayout.addView(txt);
		
		mylayout.setGravity(Gravity.CENTER);
		// Display the layout
		setContentView(mylayout);
		
		//se per 10 secondi non viene premuto il tasto ok, viene chiusa
		handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				SoundManager.stop();
				ToastAllertActivity.this.finish(); // kill after X seconds
				if(!check)
				{
					sent=true; //mail inviata
					email = new Intent(Intent.ACTION_SEND);
					email.putExtra(Intent.EXTRA_EMAIL,emailTo);
					email.putExtra(Intent.EXTRA_SUBJECT, subject);	
					email.putExtra(Intent.EXTRA_TEXT, emailText+where);
					email.setType("message/rfc822");
					startActivity(Intent.createChooser(email, "chose en email client:"));
					
				}

					/*
					 SALVARE CHE LA MAIL E' STATA INVIATA O NO
					 usare la variabile sent che 
					 setSent
					 */		
				
			}
		}, 10000);
		

	}
	
	//Salvo la localizzazione
    private void setLocation() {
 
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);
 
        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            
            where = latitude + ", " + longitude;

            //Provo a cercare l'indirizzo
            try {
    			//Creo una lista con i dettagli degli indirizzi
    			List<Address> addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 10);
    			if (addresses != null && addresses.size() > 0)
    			{
    				for (Address address : addresses) {
    					where = where + "\n" + address.getAddressLine(0);
    				}
    			}
    			

    		} catch (IOException e) {
    			Log.e("LocateMe", "Could not get Geocoder data", e);
    		}
 
        } else {
 
        	where="(Couldn't get the location. Make sure location is enabled on the device)";
        }
    }
  //Creo un oggetto di tipo Google api client
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }
 
    //Verifica del Play service
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }
 
    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }
 
    @Override
    protected void onResume() {
        super.onResume();
 
        checkPlayServices();
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }
    @Override
    public void onConnected(Bundle arg0) {
        //Appena di connette registro i dati
        setLocation();
    }
 
    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }
	
	
}