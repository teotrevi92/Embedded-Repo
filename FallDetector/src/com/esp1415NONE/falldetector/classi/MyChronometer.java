package com.esp1415NONE.falldetector.classi;

public class MyChronometer
{

	private long elapsedTime;
	private long startTime;
	private boolean isRunning;

	public MyChronometer() { reset(); }

	public void start(){
		if (isRunning) return;
		isRunning = true;
		startTime = System.currentTimeMillis();
	}
	public void stop(){
		isRunning = false;
		long endTime = System.currentTimeMillis();
		elapsedTime = elapsedTime + endTime - startTime;
		reset();
	}
	public void pause()
	{
		if (!isRunning) return;
		isRunning = false;
		long endTime = System.currentTimeMillis();
		elapsedTime = elapsedTime + endTime - startTime;
	}

	public String getElapsedTime(){
		long endTime=0;
		if (isRunning)
			endTime = elapsedTime + System.currentTimeMillis() - startTime;
		else
			endTime = elapsedTime;
		String h,m,s;
		int seconds = (int)(endTime / 1000 % 60);  
		int minutes = (int)(endTime / 60000 % 60);  
		int hours = (int)(endTime / 3600000);
		if(seconds<10)
			s = "0"+seconds;
		else
			s=""+seconds;
		if(hours<10)
			h = "0"+hours;
		else
			h=""+hours;
		if(minutes<10)
			m = "0"+minutes;
		else
			m=""+minutes;
		return h+":"+m+":"+s; 
	}

	public void reset(){
		elapsedTime = 0;
		isRunning = false; }
}