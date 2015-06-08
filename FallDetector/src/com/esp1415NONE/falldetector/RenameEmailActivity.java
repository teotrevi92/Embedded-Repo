package com.esp1415NONE.falldetector;

import com.esp1415NONE.falldetector.classi.DbAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RenameEmailActivity extends Activity {

	private TextView email, name, surname;
	private Button ok, no;
	private DbAdapter dbAdapter;
	private String mail, name_, surname_;
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(R.layout.activity_rename_email);
		intent = getIntent();
		mail = intent.getStringExtra("mail");
		email = (TextView) findViewById(R.id.mail);
		name = (EditText) findViewById(R.id.name);
		surname = (EditText) findViewById(R.id.surname);
		ok = (Button) findViewById(R.id.btn_yes);
		no = (Button) findViewById(R.id.btn_no);
		dbAdapter = new DbAdapter(this);
		name_ = dbAdapter.getName(mail);
		surname_ = dbAdapter.getSurname(mail);
		name.setText(name_);
		surname.setText(surname_);
		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				name_ = name.getText().toString();
				surname_ = surname.getText().toString(); 
				if(name_.equals(""))
					name_ = dbAdapter.getName(mail); 
				if(surname_.equals(""))
					surname_ = dbAdapter.getSurname(mail);
				dbAdapter.setNameContact(mail, name_, surname_);

				RenameEmailActivity.this.finish();

			}
		});
		
		no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RenameEmailActivity.this.finish();
			}
		});
	}

}
