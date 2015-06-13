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
		super(context, R.layout.activity_session_riepilog, c, new String[]
				{ "_id" , StringName.NAMES ,StringName.DATE , StringName.DURATION, "countFall" }, new int[]
						{ R.id.nome,R.id.nomeS,R.id.data, R.id.durata, R.id.sessione });
		this.idsC = Integer.toString(idsC);
		this.isPlaying = isPlaying;
	}


	@Override
	public void bindView(View view, Context context, Cursor cursor)
	{
		super.bindView(view, context, cursor);

		ImageView logo = (ImageView) view.findViewById(R.id.logo);
		TextView sess = (TextView) view.findViewById(R.id.sessione);
		String date = cursor.getString(cursor.getColumnIndex(StringName.DATE));
		String countFall = cursor.getString(cursor.getColumnIndex("countFall"));
		if(countFall == null)
			sess.setText("0");
		int[] dateA = new int[6];
		DbAdapter dbAdapter = new DbAdapter(context);
		dateA = dbAdapter.getDate(date);
		int size = 30;
		//String nomeImmagine = date.toLowerCase().replace(' ', '_').replace('\'', '_') + ".png";
		MyGraph rndBitmap = new MyGraph(size,size);
		rndBitmap.doRandomImg(dateA[0], dateA[1], dateA[2], dateA[3], dateA[4], dateA[5], size);
		logo.setImageBitmap(rndBitmap.getRandomImg());

		
		//		for (int i = 0; i < getListView().getAdapter().getCount(); i++) {

		//COME IDENTIFICARE LA SESSIONE CORRENTE REGISTRATA E EVIDENZIARE LA RIGA NELLA LISTVIEW
		TextView ids = (TextView) view.findViewById(R.id.nome);
		TextView nomeS = (TextView) view.findViewById(R.id.nomeS);
		String idss = ids.getText().toString();
		if((idss.equals(idsC)) && ((isPlaying == 1) || (isPlaying == -1))) {
			//			Toast.makeText(context, "la sessione corrent e' " + idss, Toast.LENGTH_SHORT).show();
			//			v.setBackgroundColor(Color.RED);
			//			textview.setTextColor(Color.RED);
			//			textview.setTypeface(textview.getTypeface(), Typeface.BOLD);
			//			rec.setVisibility(View.VISIBLE);
			//			view.setBackgroundColor(Color.RED);
			ids.setTextColor(Color.RED);
			nomeS.setTextColor(Color.RED);
		}
		else {
			ids.setTextColor(Color.GRAY);
			nomeS.setTextColor(Color.GRAY);
		}



	}









	/*private Bitmap readBitmap(String nomeImmagine)
	{
		InputStream is = null;
		try
		{
			is = context.getAssets().open(nomeImmagine);
			return BitmapFactory.decodeStream(is);
		}
		catch (IOException e)
		{
			return null;
		}
		finally
		{
			if (is != null)
			{
				try
				{
					is.close();
				}
				catch (IOException ignored)
				{
				}
			}
		}
	}*/
}