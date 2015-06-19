package com.esp1415NONE.falldetector;


import java.io.IOException;
import java.util.List;

import com.esp1415NONE.falldetector.classi.DbAdapter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class LocationService extends Service implements LocationListener{

	private final IBinder mBinder = new LocalBinder();
	private LocationManager mgr;
	private Location mLastLocation;
	private Geocoder geocoder;
	private String where;
	private DbAdapter dbAdapter;
	private Vibrator vib;
	private Ringtone rng;
	private int n = 0;

	private Intent email;
	private String[] emailTo;
	private String emailText = "Aiuto!! Sono caduto. Localizzazione approssimativa:\n";
	private String subject = "AIUTO DI SOCCORSO";

	private static boolean check=false; //Viene usata per avere l'ok dell'invio	
	private static boolean ready=false; //Localizzazione salvata
	private static boolean closeS=false; //e' stato premuto annulla, per cui salva la localizzazione e non viene mandata la mail
	private String ids;
	private String idf;
	private String lat;
	private String longit;

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		setLocation();
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
		lat = "ND";
		longit="ND";
		where=lat+", "+longit;
		dbAdapter = new DbAdapter(this);
		mgr = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		setNotify();
		//avvio l'avviso con l'allarme predefinito e la vibrazione
		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		rng = RingtoneManager.getRingtone(getApplicationContext(), notification);
		rng.play();
		vib = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		vib.vibrate(10000);
		//inizializzo gli indirizzi email
		n = dbAdapter.getNumberContact();
		emailTo = new String[n];
		emailTo = dbAdapter.getListContact();

		//Cerco e salvo la localizzazione se possibile
		geocoder = new Geocoder(this);
		if(mgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
		{
			mgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
			mLastLocation = mgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);	
		}
		else if(mgr.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
			mLastLocation = mgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
		else
		{
			where = lat+", "+longit;
		}
		setLocation();
		return mBinder;
	}
	private void setNotify() {
		///Creo la notifica
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
		Notification notificationPlay = new NotificationCompat.Builder(getApplicationContext())
		.setContentTitle("FallDetector")
		.setContentText("Localizzazione in corso")
		.setSmallIcon(R.drawable.findloc)
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
			lat = Double.toString(latitude); 
			longit = Double.toString(longitude);
			where = lat+", "+longit;
			ready=true;
			if (closeS) //Se e' stato premuto annulla, salva i dati ed esci
				finish();
			setAdress();
		}
	}


	private void setAdress()
	{
		//Provo a cercare l'indirizzo tramite network
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
//			sent=true; //mail inviata
			email = new Intent(Intent.ACTION_SEND);
			email.putExtra(Intent.EXTRA_EMAIL,emailTo);
			email.putExtra(Intent.EXTRA_SUBJECT, subject);	
			email.putExtra(Intent.EXTRA_TEXT, emailText+where);
			email.setType("message/rfc822");
			Intent i = Intent.createChooser(email, "Scegli un email client:");
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			check=false;

			/* QUI BISOGNA SALVARE CHE LA MAIL E' STATA INVIATA ----------------------------------------------------------*/			
			dbAdapter.setSentTrue(idf, ids, emailTo);
			//notifico l'invio mail
			NotificationCompat.Builder mBuilder =
				    new NotificationCompat.Builder(this)
				    .setSmallIcon(R.drawable.sendmail)
				    .setContentTitle("FallDetector")
				    .setContentText("Email di soccorso inviata!");
			int mNotificationId = 75623;
			NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			mNotifyMgr.notify(mNotificationId, mBuilder.build());
			//Chiudo il service
			finish();
		}
	}
	public void check()
	{
		check=true; //conferma di invio mail
		if (ready)
			sendMail(); //invio mail se la localizzazione e' pronta, altrimenti aspetto e verra' chiamata quando e' pronta
	}
	public void closeService()
	{
		closeS = true;
		if (ready)
			finish();
	}
	
	public void stopAlarm()
	{
		rng.stop();
		vib.cancel();
	}
	public void setId(String id_s, String id_f) //Arrivano i valori per salvare i dati
	{
		ids=id_s;
		idf=id_f;
	}
	
	private void finish()
	{
		check=false;
		closeS=false;
		ready=false;
		dbAdapter.setLatLongGPS(ids, idf, lat, longit); //salvo i dati della localizzazione prima di chiudere
		stopForeground(true);
		stopSelf();
	}
}
