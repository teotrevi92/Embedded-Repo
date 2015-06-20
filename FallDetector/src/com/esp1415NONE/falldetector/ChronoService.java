package com.esp1415NONE.falldetector;

import com.esp1415NONE.falldetector.classi.*;

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
	//Viene impostata statica per usarla in altre classi
	static int isPlaying = 0; //0 se e' in stop, 1 in play, -1 in pausa 
	private SensorManager sm = null;
	private int sensorAccurancy;
	private final String tag = "AccLogger";
	//variabili per i calcoli
	private float totAcc;
	private Queue que;
	private int sizeQueue;
	private int sensibilityMin;
	private int sensibilityMax;
	//variabile per sapere il massimo della durata della sessione salvata nelle
	//impostazioni dal utente
	private String maxTimeSession;
	//Variabili per il database
	private DbAdapter dbAdapter;
	static int id_s = 0; //id sessione corrente messa statica
	private int id_f = 0; //id caduta per ogni sessione
	private MyTime myTime; //per calcolare la data della sessione e/o caduta

	private float x;
	private float y;
	private float z;

	public void onSensorChanged(SensorEvent event)
	{
		synchronized(this)
		{
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			{

				x = event.values[0];
				y = event.values[1];
				z = event.values[2];
				totAcc = (float) Math.sqrt((x*x)+(y*y)+(z*z));
				que.enqueue(totAcc);

				//le sessione viene termina
				//quando arriva alla massima durata, impostata dall'utente
				if (getString().equals(maxTimeSession))
					stop();

				//Rilevazione caduta
				if (que.isFall())
				{	
					//si incrementa l'id della caduta
					id_f++;

					//si acquisisce le info sulla data e ora attuali
					myTime = new MyTime();
					String date = dbAdapter.convertArrayToString(que.getBox());

					//si inserisce nel database la caduta
					dbAdapter.createFall(id_f, id_s, myTime.myTime(), date, dbAdapter.getListContact());

					//Allerta caduta
					Intent i = new Intent(this, ToastAllertActivity.class);
					i.putExtra("ids", id_s+"");
					i.putExtra("idf", id_f+""); 
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(i);
				}

			}
		}
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		//controllo nei Log
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

		//Inizializzazione database
		dbAdapter = new DbAdapter(this); 

		return mBinder;

	}

	public void play()
	{	

		if (isPlaying==0) //Nuova sessione
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
				sensibilityMax = 15;
			}

			//creo l'array per il salvataggio dei dati dell'accellerometro
			que = new Queue(sizeQueue,sensibilityMin,sensibilityMax);

			//creo la stringa che mi indica la massima durata della sessione
			maxTimeSession = "0"+int_maxTimeSession+":00:00";

			//aggiungo la sessione a database e registro in ids l'id della sessione
			id_s = dbAdapter.createSession(sizeQueue, "Sessione");
		}
		isPlaying = 1; //in play
		crn.start(); //avvio cronometro
		Sensor Accel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sm.registerListener((SensorEventListener) this, Accel, sensorAccurancy);

		//Creo la notifica e chiedo che il service non venga chiuso
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
		Notification notificationPlay = new NotificationCompat.Builder(getApplicationContext())
		.setContentTitle("FallDetector")
		.setContentText("In play")
		.setSmallIcon(R.drawable.icplay)
		.setContentIntent(pi) 
		.build();
		final int notificationID = 5786000;
		startForeground(notificationID, notificationPlay);
	}		

	public void pause()
	{
		crn.pause(); //cronometro in pausa
		sm.unregisterListener(this); 
		isPlaying = -1; //in pausa
	}
	public void stop()
	{	
		//setto a database la durata della sessione
		String lifeSession = getString(); 
		dbAdapter.setDuration(id_s, lifeSession);

		//azzero l'id caduta una volta terminata la sessione
		id_f = 0;
		crn.stop();
		sm.unregisterListener(this);
		isPlaying = 0; //in stop
		stopForeground(true); //termino la richiesta di non chiudere il service
	}
	public int getPlaying()
	{
		return isPlaying;
	}
	public String getString()
	{
		return crn.getElapsedTime();
	}
	public int getX(){
		return (int)x;
	}
	public int getY(){
		return (int)y;
	}
	public int getZ(){
		return (int)z;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		stop();
	}

}
