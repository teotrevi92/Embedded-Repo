package com.esp1415NONE.falldetector.classi;


import com.esp1415NONE.falldetector.R;
import com.esp1415NONE.falldetector.classi.DbAdapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SessionSimpleCursorAdapter extends SimpleCursorAdapter 
{	
	String idsC;
	int isPlaying;

	@SuppressWarnings("deprecation")
	public SessionSimpleCursorAdapter(Context context, Cursor c, int isPlaying, int idsC)
	{
		super(context, R.layout.fragment_listview, c, new String[]
				{ "_id" , StringName.NAMES ,StringName.DATE , StringName.DURATION, "countFall" }, new int[]
						{ R.id.id_s,R.id.nameS,R.id.date, R.id.duration, R.id.nfall });
		this.idsC = Integer.toString(idsC);
		this.isPlaying = isPlaying;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor)
	{
		super.bindView(view, context, cursor);

		ImageView logo = (ImageView) view.findViewById(R.id.logo);
		TextView sess = (TextView) view.findViewById(R.id.nfall);
		String date = cursor.getString(cursor.getColumnIndex(StringName.DATE));
		String countFall = cursor.getString(cursor.getColumnIndex("countFall"));
		if(countFall == null)
			sess.setText("0");
		int[] dateA = new int[6];

		DbAdapter dbAdapter = new DbAdapter(context);
		dateA = dbAdapter.getDate(date);
		int size = 30;
		MyGraph rndBitmap = new MyGraph(size,size);
		rndBitmap.doRandomImg(dateA[0], dateA[1], dateA[2], dateA[3], dateA[4], dateA[5], size);
		logo.setImageBitmap(rndBitmap.getRandomImg());

		//COME IDENTIFICARE LA SESSIONE CORRENTE REGISTRATA E EVIDENZIARE LA RIGA NELLA LISTVIEW
		TextView ids = (TextView) view.findViewById(R.id.id_s);
		TextView nomeS = (TextView) view.findViewById(R.id.nameS);
		String idss = ids.getText().toString();
		if((idss.equals(idsC)) && ((isPlaying == 1) || (isPlaying == -1))) {

			//evidenzio la sessione corrente
			ids.setTextColor(Color.RED);
			nomeS.setTextColor(Color.RED);
		}
		else {
			ids.setTextColor(Color.WHITE);
			nomeS.setTextColor(Color.WHITE);
		}

	}
}