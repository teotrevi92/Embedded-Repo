package com.esp1415NONE.falldetector.classi;

import java.util.Calendar;

public class MyTime {
	
	public String myTime() {
		String time = "";
		String or = "";
		String min = "";
		String sec = "";
		String mes = "";
		String gio = "";

		Calendar c = Calendar.getInstance(); 

		int ore = c.get(Calendar.HOUR_OF_DAY);
		if(ore < 10)
			or = "0"+ore;
		else or = ""+ore;

		int minuti = c.get(Calendar.MINUTE);
		if(minuti < 10)
			min = "0"+minuti;
		else min = ""+minuti;

		int secondi = c.get(Calendar.SECOND);
		if(secondi < 10)
			sec = "0"+secondi;
		else sec = ""+secondi;

		int anno = c.get(Calendar.YEAR);
		int mese = c.get(Calendar.MONTH)+1;
		if(mese < 10)
			mes = "0"+mese;
		else mes = ""+mese;

		int giorno = c.get(Calendar.DAY_OF_MONTH);
		if(giorno < 10)
			gio = "0"+giorno;
		else gio = ""+giorno;

		time = anno+ "-" + mes + "-" + gio + " " + or + ":" + min + ":" + sec;
		
		return time;
	}

}
