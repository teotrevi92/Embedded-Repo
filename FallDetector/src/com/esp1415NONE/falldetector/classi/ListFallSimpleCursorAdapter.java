package com.esp1415NONE.falldetector.classi;

import com.esp1415NONE.falldetector.R;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter;

public class ListFallSimpleCursorAdapter extends SimpleCursorAdapter
{

	@SuppressWarnings("deprecation")
	public ListFallSimpleCursorAdapter(Context context, Cursor c)
	{
		super(context, R.layout.detail_fall, c, new String[]
				{ StringName.UIDF, StringName.DATEF , StringName.SENT }, new int[]
						{ R.id.idf,R.id.dateF , R.id.sent});
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor)
	{
		super.bindView(view, context, cursor);

	}

}