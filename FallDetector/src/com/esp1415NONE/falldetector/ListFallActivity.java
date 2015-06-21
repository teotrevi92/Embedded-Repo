package com.esp1415NONE.falldetector;

import com.esp1415NONE.falldetector.classi.DbAdapter;
import com.esp1415NONE.falldetector.classi.ListFallSimpleCursorAdapter;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.app.Activity;

public class ListFallActivity extends Activity {

	private ListView listFallActivity;
	private Cursor c;
	private DbAdapter dbAdapter;
	private String ids;
	private Intent i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_fall);

		dbAdapter = new DbAdapter(getApplicationContext());
		i = getIntent();

		ids = i.getStringExtra("ids");

		listFallActivity = (ListView) findViewById(R.id.listFallActivity);
		//inizializzo cursore
		c = dbAdapter.getInfoTable2(ids);

		//richiamo il SimpleCursorAdampter
		listFallActivity.setAdapter(new ListFallSimpleCursorAdapter(getApplicationContext(), c));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_fall, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
