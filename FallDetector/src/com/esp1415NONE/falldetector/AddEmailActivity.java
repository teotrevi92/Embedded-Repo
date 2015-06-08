package com.esp1415NONE.falldetector;

import com.esp1415NONE.falldetector.classi.DbAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddEmailActivity extends Activity {

	public DbAdapter dbAdapter;
	public EditText name, surname, email;
	//	private FragmentTransaction fragmentTransaction;


	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(R.layout.activity_add_email);

		email = (EditText) findViewById(R.id.mail);
		name = (EditText) findViewById(R.id.name);
		surname = (EditText) findViewById(R.id.surname);
		Button ok = (Button) findViewById(R.id.btn_yes);
		Button no = (Button) findViewById(R.id.btn_no);
		ok.setText(R.string.toast_rename_button);
		no.setText("Annulla");
		dbAdapter = new DbAdapter(this);
		//		TextView txt = new TextView(this);
		//		txt.setText(R.string.toast_rename_messagge);


		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String mail = email.getText().toString();
				String name_ = name.getText().toString();
				String surname_ = surname.getText().toString(); 
				dbAdapter.createContact(mail, name_, surname_);

				AddEmailActivity.this.finish();

			}
		});

		no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AddEmailActivity.this.finish();
			}
		});
	}

}