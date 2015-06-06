package com.esp1415NONE.falldetector;

import com.esp1415NONE.falldetector.classi.DbAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RenameActivity extends Activity{

	public DbAdapter dbHelper;
	public EditText nameS;
	public String nameSess;
	public String ids;
	String where;
	private Intent i;
//	private FragmentTransaction fragmentTransaction;
	
	
	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
		
		i = getIntent();
		ids = i.getStringExtra("ids");
		where = i.getStringExtra("where");
		setContentView(R.layout.activity_rename);

		nameS = (EditText) findViewById(R.id.nameS);
		Button ok = (Button) findViewById(R.id.btn_yes);
		Button no = (Button) findViewById(R.id.btn_no);
		ok.setText(R.string.toast_rename_button);
		no.setText("Annulla");
		dbHelper = new DbAdapter(this);
		//		TextView txt = new TextView(this);
		//		txt.setText(R.string.toast_rename_messagge);


		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nameSess = nameS.getText().toString();
				if(nameSess.equals(""))
					nameSess = "Sessione";
				dbHelper.setNameSession(ids, nameSess);

				//Toast.makeText(RenameActivity.this, "bau", Toast.LENGTH_SHORT).show();
//				if(where.equals("rename"))
//				{
//					RenameActivity ls_fragment = new RenameActivity();
//					fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment);
//					fragmentTransaction.commit();
//				}
//				
				
				RenameActivity.this.finish();

			}
		});

		no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(where.equals("stop")) {
					nameSess = "Sessione";
					dbHelper.setNameSession(ids, nameSess);
				}
				else if(where.equals("rename")) 
					nameSess = dbHelper.getNameSession(ids);
				RenameActivity.this.finish();
			}
		});

		//Creo il layout
		//		LinearLayout mylayout = new LinearLayout(this);
		//Aggiungo gli elementi al layout
		//		mylayout.addView(ok);
		//		mylayout.addView(no);
		//		mylayout.addView(txt);

		//		mylayout.setGravity(Gravity.CENTER);
		//		//Visualizzo il layout
		//		setContentView(mylayout);

	}






}