package com.esp1415NONE.falldetector;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class AlarmService extends Service 
{

	private NotificationManager mManager;

	@Override
	public IBinder onBind(Intent arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() 
	{
		// TODO Auto-generated method stub  
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		this.getApplicationContext();
		// TODO Auto-generated method stub
		Intent intent1 = new Intent(this.getApplicationContext(),MainActivity.class);
		PendingIntent pendingNotificationIntent = PendingIntent.getActivity( this.getApplicationContext(),0, intent1,PendingIntent.FLAG_UPDATE_CURRENT);
		intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

		Notification notification = new NotificationCompat.Builder(getApplicationContext())
		.setContentTitle("FallDetector")
		.setContentText("Ricordati di iniziare la sessione")
		.setSmallIcon(R.drawable.ic_launcher)
		.setAutoCancel(true)
		.setContentIntent(pendingNotificationIntent)
		.build();

		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_ALL;
		final int notificationID = 5786001;
		mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mManager.notify(notificationID, notification);


		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void onDestroy() 
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
