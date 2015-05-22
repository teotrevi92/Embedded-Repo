package com.esp1415NONE.falldetector;

import com.esp1415NONE.falldetector.classi.*;

import java.util.Calendar;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class ChronoService extends Service implements SensorEventListener {
		private final IBinder mBinder = new LocalBinder();
		private MyChronometer crn = new MyChronometer();
		private static int isPlaying = 0; //0 se e' in stop, 1 in play, -1 in pausa
		SensorManager sm = null;
		final String tag = "AccLogger";
		//variabili per i calcoli
		float totAcc;
		Queue que;
		static final int SIZE = 100;
		//variabili per il grafico e la data
		Calendar todayTime;
		Grafico graph;
		
	
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
					
					//ALGORITMO DI CADUTA
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
		SoundManager.init(ChronoService.this);
		graph = new Grafico(300,300);
		graph.doBase();
		que = new Queue(SIZE);
		return mBinder;
		
	}
	
	public void play()
	{	
		crn.start();
		Sensor Accel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		// register this class as a listener for the accelerometer sensor
		sm.registerListener((SensorEventListener) this, Accel, SensorManager.SENSOR_DELAY_UI);
		if (isPlaying==0) //carico la data della sessione
		{
			/*QUI SI SALVA LA DATA DI INIZIO DELLA SESSIONE
			
			*/
		}
		isPlaying = 1;
		startForeground(0, null); //cerco di non far chiudere il service
	}		
	
	public void pause()
	{
		crn.pause();
		sm.unregisterListener(this);
		isPlaying = -1;
	}
	public void stop()
	{
		crn.stop();
		sm.unregisterListener(this);
		isPlaying = 0;
		stopForeground(true); //qui termino la richiesta di non chiudere il service
		
		/*QUI SI SALVA LA DURATA DELLA SESSIONE
		 String lifeSession = getString();
		*/
	}
	public int getPlaying()
	{
		return isPlaying;
	}
	public String getString()
	{
		return crn.getElapsedTime();
	}
	
}
