package com.esp1415NONE.falldetector.classi;

import com.esp1415NONE.falldetector.R;
import com.esp1415NONE.falldetector.classi.DbAdapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SessionSimpleCursorAdapterDetails extends SimpleCursorAdapter
{
	TextView idsess;
	TextView dataS;
	TextView nameS;
	TextView durationS;
	ImageView logo;


	@SuppressWarnings("deprecation")
	public SessionSimpleCursorAdapterDetails(Context context, Cursor c)
	{
		super(context, R.layout.activity_fragment_detail_session, c, new String[]
				{ StringName.UIDF, StringName.DATEF , StringName.SENT }, new int[]
						{ R.id.idf,R.id.dataF , R.id.sent});
		
	}


	@Override
	public void bindView(View view, Context context, Cursor cursor)
	{
		super.bindView(view, context, cursor);



		idsess = (TextView) view.findViewById(R.id.idsess);
		nameS = (TextView) view.findViewById(R.id.nameS);
		dataS = (TextView) view.findViewById(R.id.data);
		durationS = (TextView) view.findViewById(R.id.durationS);
		logo = (ImageView) view.findViewById(R.id.logoS);

		DbAdapter dbAdapter = new DbAdapter(context);
		String[] arr = new String[3];
		String ids = cursor.getString(cursor.getColumnIndex("_id"));
		arr = dbAdapter.getInfoTable1(ids);
		idsess.setText(ids);
		dataS.setText(arr[0]);
		nameS.setText(arr[1]);
		durationS.setText(arr[2]);

		int[] dateA = new int[6];
		dateA = dbAdapter.getDate(arr[0]);
		int size = 30;
		MyGraph rndBitmap = new MyGraph(size,size);
		rndBitmap.doRandomImg(dateA[0], dateA[1], dateA[2], dateA[3], dateA[4], dateA[5], size);
		logo.setImageBitmap(rndBitmap.getRandomImg());

		//		registerForContextMenu(getListView());



		//		ImageView logo = (ImageView) view.findViewById(R.id.logo);
		//		TextView sess = (TextView) view.findViewById(R.id.idsess);
		//		String date = cursor.getString(cursor.getColumnIndex(StringName.DATE));
		//		TextView dur = (TextView) view.findViewById(R.id.durationS);
		//		String countFall = cursor.getString(cursor.getColumnIndex("countFall"));
		//		if(countFall == null)
		//			sess.setText("0");
		//		int[] dateA = new int[6];
		//		DbAdapter dbAdapter = new DbAdapter(context);
		//		dateA = dbAdapter.getDate(date);
		//		int size = 60;
		//		//String nomeImmagine = date.toLowerCase().replace(' ', '_').replace('\'', '_') + ".png";
		//		Grafico rndBitmap = new Grafico(size,size);
		//		rndBitmap.doRandomImg(dateA[0], dateA[1], dateA[2], dateA[3], dateA[4], dateA[5], size);
		//		logo.setImageBitmap(rndBitmap.getRandomImg());






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