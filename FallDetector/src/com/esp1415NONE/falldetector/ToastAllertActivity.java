package com.esp1415NONE.falldetector;

import com.esp1415NONE.falldetector.classi.SoundManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;




public class ToastAllertActivity extends Activity {
	
	Intent email;
	String[] emailTo = {"azi92@hotmail.it", "azi92rach@gmail.com"};
	String gps = "Via Gradenigo";
	String emailText = "Sono caduto. Mi trovo qui: " + gps;
	String subject = "AIUTO DI SOCCORSO";
	Handler handler;
	boolean check=false;;
	boolean sent = false;
	
	@Override
	public void onCreate(Bundle state) {
		// auto-kill activity after X seconds <-------------------------
		super.onCreate(state);
		
		SoundManager.init(this);
		SoundManager.play();
		Button ok = new Button(this);
//		ok.setWidth(200);
//		ok.setHeight(200);
		ok.setText(R.string.toast_act_button);
		TextView txt = new TextView(this);
		txt.setText(R.string.toast_act_messagge);
		ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SoundManager.stop();
				ToastAllertActivity.this.finish();
				check = true;
				
			}
		});
		
		// Create the layout
		LinearLayout mylayout = new LinearLayout(this);
		// Add the UI elements to the layout
		mylayout.addView(ok);
		mylayout.addView(txt);
		
		mylayout.setGravity(Gravity.CENTER);
		// Display the layout
		setContentView(mylayout);
		
		//se per 10 secondi non viene premuto il tasto ok, viene chiusa
		handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				SoundManager.stop();
				ToastAllertActivity.this.finish(); // kill after X seconds
				if(!check)
				{
					sent=true;
					email = new Intent(Intent.ACTION_SEND);
					email.putExtra(Intent.EXTRA_EMAIL,emailTo);
					email.putExtra(Intent.EXTRA_SUBJECT, subject);	
					email.putExtra(Intent.EXTRA_TEXT, emailText);
					email.setType("message/rfc822");
					startActivity(Intent.createChooser(email, "chose en email client:"));
					
				}

					/*
					 SALVARE CHE LA MAIL E' STATA INVIATA
					 setSent
					 */		
				
			}
		}, 10000);
		

	}
}