package com.esp1415NONE.falldetector.classi;

import java.io.IOException;
import java.io.InputStream;

import com.esp1415NONE.falldetector.R;
import com.esp1415NONE.falldetector.classi.DbAdapter;
import com.esp1415NONE.falldetector.classi.DbAdapter.DbHelper;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SessionSimpleCursorAdapter extends SimpleCursorAdapter 
{
	private static Context cont;	
		
	public SessionSimpleCursorAdapter(Context context, Cursor c)
	{
		super(context, R.layout.activity_session_riepilog, c, new String[]
				{ "_id" , StringName.NAMES ,StringName.DATE , StringName.DURATION, "countFall" }, new int[]
						{ R.id.nome,R.id.nomeS,R.id.data, R.id.durata, R.id.sessione });
		this.cont = context;
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
		DbAdapter dbHelper = new DbAdapter(context);
		dateA = dbHelper.getDate(date);
		int size = 30;
		//String nomeImmagine = date.toLowerCase().replace(' ', '_').replace('\'', '_') + ".png";
		MyGraph rndBitmap = new MyGraph(size,size);
		rndBitmap.doRandomImg(dateA[0], dateA[1], dateA[2], dateA[3], dateA[4], dateA[5], size);
		logo.setImageBitmap(rndBitmap.getRandomImg());
		
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