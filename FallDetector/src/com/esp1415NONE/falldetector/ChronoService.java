package com.esp1415NONE.falldetector;

import com.esp1415NONE.falldetector.classi.*;

import java.util.Calendar;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class ChronoService extends Service implements SensorEventListener {
		private final IBinder mBinder = new LocalBinder();
		private MyChronometer crn;
		private static int isPlaying = 0; //0 se e' in stop, 1 in play, -1 in pausa
		SensorManager sm = null;
		int sensorAccurancy;
		final String tag = "AccLogger";
		//variabili per i calcoli
		float totAcc;
		Queue que;
		int sizeQueue;
		int sensibilityMin;
		int sensibilityMax;
		//variabili per il grafico e la data
		Calendar todayTime;
		Grafico graph;
		String maxTimeSession;
		
		
		public void onSensorChanged(SensorEvent event)
	{
	// Java's synchronized keyword is used to ensure mutually exclusive
	// access to the sensor. See also
	// http://download.oracle.com/javase/tutorial/essential/concurrency/locksync.html
		synchronized(this)
		{
			// The SensorEvent object holds informations such as
			// the sensor's type, the time-stamp, accuracy and of course
			// the sensor's data.
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			{
					// IMPORTANT NOTE: The axes are swapped when the device's
					// screen orientation changes. To access the unswapped values,
					// use indices 3, 4 and 5 in values[]
					float x = event.values[0];
					float y = event.values[1];
					float z = event.values[2];
					totAcc = (float) Math.sqrt((x*x)+(y*y)+(z*z));
					que.enqueue(totAcc);
					
					//le sessione viene termina
					//quando arriva alla massima durata, impostata dall'utente
					if (getString().equals(maxTimeSession))
						stop();
					
					//Rilevazione caduta
					if (que.isFall())
					{	
						
						/*QUI SI SALVARE TUTTI I DATI DELLA CADUTA,
					 	DATA E ORA E ARRAY DEI DATI
						e mandare id_f nell'intent
					*/
						
						//allerta
						Intent i = new Intent(this, ToastAllertActivity.class);
						i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(i);	
					}
						
					
			}
		}
	}
	
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		Log.d(tag,"onAccuracyChanged: " + sensor + ", accuracy: " + accuracy);
	}
	
	//classe che estende Binder per permette di usare i metodi si questo Service
	public class LocalBinder extends Binder 
	{
		ChronoService getService() 
        {
            return ChronoService.this;
        }
    }
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	};
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub		
		sm = (SensorManager)getSystemService(SENSOR_SERVICE);
//		graph = new Grafico(300,300);
//		graph.doBase();
		return mBinder;
		
	}
	
	public void play()
	{	
		if (isPlaying==0) //nuova sessione
		{
			crn = new MyChronometer();
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			int accurancy = preferences.getInt("spinnerValueAcc", 1);
			int int_maxTimeSession = preferences.getInt("spinnerValueDuration", 1)+1;
			int int_sens = preferences.getInt("spinnerValueSens", 0);
			//imposto l'accuratezza dell'accellerometro
			if (accurancy==0)	//se l'accuratezza e' normale allora ricevo 1 dato ogni 60 millisecondi
			{	
				sizeQueue = 5; //ogni 200 millesimi di secondi riceve un dato
				sensorAccurancy = SensorManager.SENSOR_DELAY_NORMAL;
			}
			else if(accurancy==1) //se l'accuratezza e' normale allora ricevo 1 dato ogni 20 millisecondi
			{
				sizeQueue = 17; //ogni 60 millesimi di secondi riceve un dato
				sensorAccurancy = SensorManager.SENSOR_DELAY_UI;
			}
			else if(accurancy==2) //se l'accuratezza e' normale allora ricevo 1 dato ogni 20 millisecondi
			{
				sizeQueue = 125; //ogni 8 millesimi di secondi riceve un dato
				sensorAccurancy = SensorManager.SENSOR_DELAY_GAME;
			}
			
			//imposto la sensibilita'
			if(int_sens==0)
			{
				sensibilityMin = 5;
				sensibilityMax = 22;
			}
			else if(int_sens==1)
			{
				sensibilityMin = 6;
				sensibilityMax = 18;
			}
			else if(int_sens==2)
			{
				sensibilityMin = 6;
				sensibilityMax = 10;
			}
				
			//creo l'array per il salvataggio dei dati dell'accellerometro
			que = new Queue(sizeQueue,sensibilityMin,sensibilityMax);
			//creo la stringa che mi indica la massima durata della sessione
			maxTimeSession = "0"+int_maxTimeSession+":00:00";
			
			
			/*QUI SI SALVA LA DATA DI INIZIO DELLA SESSIONE
				salvare anche sizeQueue che serve poi per capire da quanti dati e' composto l'array
			*/
		}
		isPlaying = 1;
		crn.start();
		Sensor Accel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		// register this class as a listener for the accelerometer sensor
		sm.registerListener((SensorEventListener) this, Accel, sensorAccurancy);
		
		//Creo la notifica
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
		Notification notificationPlay = new NotificationCompat.Builder(getApplicationContext())
		.setContentTitle("FallDetector")
		.setContentText("In play")
		.setSmallIcon(R.drawable.play)
		.setContentIntent(pi) 
		.build();
		final int notificationID = 5786000;
		startForeground(notificationID, notificationPlay);
	}		
	
	public void pause()
	{
		crn.pause();
		sm.unregisterListener(this);
		isPlaying = -1;
	}
	public void stop()
	{
		/*QUI SI SALVA LA DURATA DELLA SESSIONE
		 String lifeSession = getString();
		*/
		crn.stop();
		sm.unregisterListener(this);
		isPlaying = 0;
		stopForeground(true); //qui termino la richiesta di non chiudere il service
	}
	public int getPlaying()
	{
		return isPlaying;
	}
	public String getString()
	{
		return crn.getElapsedTime();
	}
	
	@Override
	public void onDestroy() {
	// TODO Auto-generated method stub
		stop();
	}
	
}
