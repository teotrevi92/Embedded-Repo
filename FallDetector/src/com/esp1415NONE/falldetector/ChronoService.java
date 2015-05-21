package com.esp1415NONE.falldetector;

import com.esp1415NONE.falldetector.classi.*;

import java.util.Calendar;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class ChronoService extends Service implements SensorEventListener {
		//private long tmp = 0;
		private final IBinder mBinder = new LocalBinder();
		private MyChronometer crn = new MyChronometer();
		private static int isPlaying = 0; //0 se e' in stop, 1 in play, -1 in pausa
		SensorManager sm = null;
		final String tag = "AccLogger";
		//variabili per i calcoli
		float totAcc;
		Queue que;
		static final int SIZE = 100;
		//variabili per il grafico
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
		//creo l'immagine random per la sessione
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
			Calendar todayTime = Calendar.getInstance();
			int year = todayTime.get(Calendar.YEAR);
			int day = todayTime.get(Calendar.DAY_OF_MONTH);
			int month = todayTime.get(Calendar.MONTH)+1;
			int hours= todayTime.get(Calendar.HOUR_OF_DAY);
			int minutes = todayTime.get(Calendar.MINUTE);
			int seconds = todayTime.get(Calendar.SECOND);
			int milliseconds= todayTime.get(Calendar.MILLISECOND);
			graph.doRandomImg(year,month,day,hours,minutes,seconds,milliseconds,100);
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
		
	}
	public int getPlaying()
	{
		return isPlaying;
	}
	public String getString()
	{
		return crn.getElapsedTime();
	}
	public Bitmap getBitmap()
	{
		return que.getGraphQueue();
	}
	public Bitmap getRandomImg()
	{
		return graph.getRandomImg();
	}
	
}