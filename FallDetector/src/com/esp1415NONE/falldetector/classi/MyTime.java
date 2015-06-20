package com.esp1415NONE.falldetector.classi;

import java.util.Calendar;

public class MyTime 
{

	public String myTime() 
	{
		String time = "";
		String hour = "";
		String min = "";
		String sec = "";
		String month = "";
		String day = "";

		Calendar c = Calendar.getInstance(); 

		int hours = c.get(Calendar.HOUR_OF_DAY);
		if(hours < 10)
			hour = "0"+hours;
		else hour = ""+hours;

		int minuts = c.get(Calendar.MINUTE);
		if(minuts < 10)
			min = "0"+minuts;
		else min = ""+minuts;

		int seconds = c.get(Calendar.SECOND);
		if(seconds < 10)
			sec = "0"+seconds;
		else sec = ""+seconds;

		int year = c.get(Calendar.YEAR);
		int months = c.get(Calendar.MONTH)+1;
		if(months < 10)
			month = "0"+months;
		else month = ""+months;

		int days = c.get(Calendar.DAY_OF_MONTH);
		if(days < 10)
			day = "0"+days;
		else day = ""+days;

		time = year+ "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec;

		return time;
	}

}
