package com.esp1415NONE.falldetector;


import java.io.IOException;
import java.util.List;

import com.esp1415NONE.falldetector.classi.DbAdapter;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class LocationService extends Service implements LocationListener{

	private final IBinder mBinder = new LocalBinder();
	private String where;
    LocationManager mgr;
    private Location mLastLocation;
    private Geocoder geocoder;
    DbAdapter dbAdapter;
    
    Intent email;

	String[] emailTo = {"azi92@hotmail.it", "azi92rach@gmail.com"};
	String emailText = "Aiuto!! Sono caduto. Mi trovo qui:\n";
	String subject = "AIUTO DI SOCCORSO";
	
	boolean check; //Viene usata per avere l'ok dell'invio	
	boolean sent; //Segnala il corretto invio della mail, da salvare del database
	boolean ready; //Localizzazione salvata
    
    
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		setLocation();
		ready=true;
		sendMail();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
	//---------------------------SERVICE------------------------
	public class LocalBinder extends Binder 
	{
		LocationService getService() 
        {
            return LocationService.this;
        }
    }
	
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	};

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		dbAdapter = new DbAdapter(this);
		check=false;
		sent=false;
		ready=false;
		mgr = (LocationManager) getSystemService(LOCATION_SERVICE);
		setNotify();
		//Cerco e salvo la localizzazione
        geocoder = new Geocoder(this);
		if(mgr.isProviderEnabled(LocationManager.GPS_PROVIDER))
    	{
			mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 10, this);
			mLastLocation = mgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    		
    	}
		else if(mgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
		{
			mgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 10, this);
        	mLastLocation = mgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);	
		}
		else
		{
			mgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 10, this);
        	mLastLocation = mgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);	
		}
		
		return mBinder;
	}
	private void setNotify() {
		///Creo la notifica
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
		Notification notificationPlay = new NotificationCompat.Builder(getApplicationContext())
		.setContentTitle("FallDetector")
		.setContentText("Mail: invio in corso")
		.setSmallIcon(R.drawable.sendmail)
		.setContentIntent(pi)
		.build();
		final int notificationID = 5786050;
		startForeground(notificationID, notificationPlay);
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mgr.removeUpdates(this);
		
	}
	//--------------------------METODI----------------

	//Salvo la localizzazione
    private void setLocation() {
        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            //salvo i dati del gps
//          dbAdapter.setLatLongGPS(ids, idf, latitude, longitude); //da capire come passare idf e ids
            where = latitude + ", " + longitude;
            setAdress();
        }
        else {
        	where="(Non e' stato possibile rocevere i dati del gps!!)";
        }
        
        /*SALVARE LA  where COME LOCALIZZAZIONE, siccome puo' essere aggiornata, verra' salvata ogni volta cosi' da tenere quella ultima
         FATTO SOPRA
         ----------------------------------------------------*/
        
    }
	
	
	private void setAdress()
    {
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
    }
	
	private void sendMail()
	{	//Se confermato l'invio mail viene inviata appena vengono presi i dati del gps
		if(check)
		{
			sent=true; //mail inviata
			email = new Intent(Intent.ACTION_SEND);
			email.putExtra(Intent.EXTRA_EMAIL,emailTo);
			email.putExtra(Intent.EXTRA_SUBJECT, subject);	
			email.putExtra(Intent.EXTRA_TEXT, emailText+where);
			email.setType("message/rfc822");
			Intent i = Intent.createChooser(email, "chose en email client:");
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			check=false;
			
			/* QUI BISOGNA SALVARE CHE LA MAIL E' STATA INVIATA ----------------------------------------------------------*/			
			
			finish();
		}
	}
	public void check()
	{
		check=true; //conferma di invio mail
		if (ready)
			sendMail(); //invio mail se la localizzazione e' pronta, altrimenti aspetto e verra' chiamata quando e' pronta
	}
	public void finish()
	{
		stopForeground(true);
		stopSelf();
	}
}
