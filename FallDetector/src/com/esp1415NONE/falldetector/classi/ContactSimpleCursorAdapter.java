package com.esp1415NONE.falldetector.classi;


import com.esp1415NONE.falldetector.R;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter;

public class ContactSimpleCursorAdapter extends SimpleCursorAdapter 
{	

	@SuppressWarnings("deprecation")
	public ContactSimpleCursorAdapter(Context context, Cursor c)
	{
		super(context, R.layout.contact, c, new String[]
				{ "_id" , StringName.NAME ,StringName.SURNAME }, new int[]
						{ R.id.mail,R.id.name,R.id.surname });
	}



	@Override
	public void bindView(View view, Context context, Cursor cursor)
	{
		super.bindView(view, context, cursor);


	}


}