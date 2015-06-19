package com.esp1415NONE.falldetector.classi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbAdapter  {

	private DbHelper helper;
	public DbAdapter(Context context){

		helper = new DbHelper(context);
	}
	private static String strSeparator = "_";
	private MyTime myTime;

	public String getTable1()
	{
		SQLiteDatabase db = helper.getReadableDatabase();

		String[] columns = {StringName.UIDS, StringName.DATE, StringName.DURATION, StringName.SENS};
		String table = StringName.TABLE_NAME1;
		Cursor cursor = db.query(table, columns, null, null, null, null, null);
		StringBuffer buffer = new StringBuffer();
		while(cursor.moveToNext())
		{
			int cid = cursor.getInt(0);
			String date = cursor.getString(1);
			String duration = cursor.getString(2);
			int sens = cursor.getInt(3);
			buffer.append(cid+ ""+ date + "" + duration + "" + sens + "\n");
		}
		cursor.close();
		db.close();
		return buffer.toString();

	}
	public String getTable2(String id_s)
	{
		SQLiteDatabase db = helper.getReadableDatabase();

		String[] columns = {StringName.UIDF, StringName.LAT, StringName.LONG, StringName.DATEF, StringName.ARRAY};
		String table = StringName.TABLE_NAME2;
		Cursor cursor = db.query(table, columns, null, null, null, null, null);
		StringBuffer buffer = new StringBuffer();
		while(cursor.moveToNext())
		{
			int cid = cursor.getInt(0);
			int ids = cursor.getInt(1);
			String lat = cursor.getString(2);
			String longit = cursor.getString(3);
			String datef = cursor.getString(4);
			String array = cursor.getString(5);
			buffer.append(cid+ ""+ ids + "" + lat + "" + longit + "" + datef+ "" + array + "\n");
		}

		cursor.close();
		db.close();
		return buffer.toString();

	}

	public String getTable2plus()
	{
		SQLiteDatabase db = helper.getReadableDatabase();

		String[] columns = {StringName.UIDSREF,StringName.UIDF, StringName.LAT, StringName.LONG, StringName.DATEF};
		String table = StringName.TABLE_NAME2;
		Cursor cursor = db.query(table, columns, null, null, null, null, null);
		StringBuffer buffer = new StringBuffer();
		while(cursor.moveToNext())
		{
			int ids = cursor.getInt(0);
			String idf = cursor.getString(1);
			String lat = cursor.getString(2);
			String longi = cursor.getString(3);
			String date = cursor.getString(4);
			buffer.append(ids+" "+idf+ " "+ lat + " " + longi + " " + date + "\n");
		}

		cursor.close();
		db.close();
		return buffer.toString();

	}


	public String getTable3()
	{
		SQLiteDatabase db = helper.getReadableDatabase();

		String[] columns = {StringName.UIDSREF, StringName.UIDFREF, StringName.MAILREF, StringName.SENT};
		String table = StringName.TABLE_NAME3;
		Cursor cursor = db.query(table, columns, null, null, null, null, null);
		StringBuffer buffer = new StringBuffer();
		while(cursor.moveToNext())
		{
			String ids = cursor.getString(0);
			String idf = cursor.getString(1);
			String mail = cursor.getString(2);
			String sent = cursor.getString(3);
			buffer.append(ids+ " "+ idf + " " + mail + " " + sent + "\n");
		}
		//cursor.close();

		cursor.close();
		db.close();
		return buffer.toString();

	}

	public String getTable4()
	{
		SQLiteDatabase db = helper.getReadableDatabase();

		String[] columns = {StringName.MAIL, StringName.NAME, StringName.SURNAME};
		String table = StringName.TABLE_NAME4;
		Cursor cursor = db.query(table, columns, null, null, null, null, null);
		StringBuffer buffer = new StringBuffer();
		while(cursor.moveToNext())
		{
			String mail = cursor.getString(0);
			String name = cursor.getString(1);
			String surname = cursor.getString(2);
			buffer.append(mail+ ""+ name + "" + surname + "\n");
		}

		cursor.close();
		db.close();
		return buffer.toString();

	}

	//	public String[] getInfoTable1(String ids)
	//	{
	//		SQLiteDatabase db = helper.getReadableDatabase();
	//		String[] columns = {StringName.DATE, StringName.DURATION};
	//		Cursor cursor = db.query(StringName.TABLE_NAME1, columns, ids, null, null, null, null);
	//		String[] arr = new String[2];
	//		if(cursor != null) {
	//			cursor.moveToFirst();
	//			arr[0] = cursor.getString(0);
	//			arr[1] = cursor.getString(1);
	//			
	//		}
	//		return arr;
	//	}

	public String[] getInfoTable1(String ids)
	{
		SQLiteDatabase db = helper.getReadableDatabase();

		String[] arr = new String[3];
		//		String query = "SELECT " + StringName.DATE + "," + StringName.NAMES + "," + StringName.DURATION 
		//				+ " FROM " + StringName.TABLE_NAME1 + 
		//				" WHERE " + StringName.UIDS + " = ' " + ids + " ' ;";
		String[] columns = {StringName.DATE,StringName.NAMES,StringName.DURATION};
		//		Cursor cursor = db.rawQuery(query, null);
		String table = StringName.TABLE_NAME1;
		String where = StringName.UIDS + "='" + ids +"'";
		Cursor cursor = db.query(table, columns, where, null, null, null, null);
		if(cursor != null) {
			cursor.moveToFirst();
			arr[0] = cursor.getString(0);
			arr[1] = cursor.getString(1);
			arr[2] = cursor.getString(2);
		}
		cursor.close();
		db.close();
		return arr;
	}

	//	public Cursor getInfoTable2(String id_s)
	//	{
	//		SQLiteDatabase db = helper.getReadableDatabase();
	//
	//		String query = "SELECT DISTINCT " + StringName.UIDS + " as _id ," + StringName.NAMES + "," 
	//				+ StringName.UIDF + "," + StringName.DATEF  + "," + StringName.SENT 
	//				+ " FROM (" + StringName.TABLE_NAME2 + " JOIN " + StringName.TABLE_NAME1 + 
	//				" ON " + StringName.UIDS + " = " + StringName.UIDSREF + ") AS J JOIN " 
	//				+ StringName.TABLE_NAME3 + " ON " + StringName.UIDS + " = " + StringName.TABLE_NAME3
	//				+ "." + StringName.UIDSREF + " AND "+ StringName.UIDF + " = " + StringName.TABLE_NAME3
	//				+ "." + StringName.UIDFREF +
	//				" WHERE " + StringName.UIDS + " = '" + id_s + "' ;";
	//		Cursor cursor = db.rawQuery(query, null);
	//
	//		return cursor;
	//
	//	}
	public Cursor getInfoTable2(String id_s)
	{
		SQLiteDatabase db = helper.getReadableDatabase();

		String query = "SELECT DISTINCT " + StringName.UIDS + " as _id ," + StringName.NAMES + "," 
				+ StringName.UIDF + "," + StringName.DATEF  + "," + StringName.SENT 
				+ " FROM (" + StringName.TABLE_NAME2 + " JOIN " + StringName.TABLE_NAME1 + 
				" ON " + StringName.UIDS + " = " + StringName.UIDSREF + ") AS J JOIN " 
				+ StringName.TABLE_NAME3 + " ON " + StringName.UIDS + " = " + StringName.TABLE_NAME3
				+ "." + StringName.UIDSREF + " AND "+ StringName.UIDF + " = " + StringName.TABLE_NAME3
				+ "." + StringName.UIDFREF +
				" WHERE " + StringName.UIDS + " = '" + id_s + "' ;";
		Cursor cursor = db.rawQuery(query, null);


		//		db.close();
		return cursor;

	}

	//	String SUBQUERY = "(SELECT DISTINCT " + StringName.SENT  + ",fall." + StringName.UIDSREF + " AS ids3,"
	//			+ StringName.UIDF + " AS idf3," + StringName.DATEF + " FROM " +
	//			StringName.TABLE_NAME3 + " JOIN " + StringName.TABLE_NAME2 + 
	//			" ON fall." + StringName.UIDSREF + " = state."+ StringName.UIDSREF +")";
	//
	//	String query = "SELECT DISTINCT ids3 as _id, idf3," + StringName.NAMES + ","
	//			+ StringName.SENT +  "," + StringName.DATEF 
	//			+ " FROM (" + SUBQUERY + " JOIN " + StringName.TABLE_NAME1
	//			+ " ON ids3 = " + StringName.UIDS + ") AS J "
	//			+ " WHERE ids3 = '" + id_s + "' ;";

	//	String query = "SELECT DISTINCT " + StringName.UIDS + " as _id ," + StringName.NAMES + "," 
	//			+ StringName.UIDF + "," + StringName.DATEF  + "," + StringName.SENT 
	//			+ " FROM (" + StringName.TABLE_NAME1 + " JOIN " + StringName.TABLE_NAME2 + 
	//			" ON " + StringName.UIDS + " = " + StringName.UIDSREF + ") AS J JOIN " 
	//			+ StringName.TABLE_NAME3 + " ON " + StringName.UIDS + " = " + StringName.TABLE_NAME3
	//			+ "." + StringName.UIDSREF +
	//			" WHERE " + StringName.UIDS + " = '" + id_s + "' ;";

	//	String query = "SELECT DISTINCT " + StringName.UIDS + " as _id ," + StringName.NAMES + "," 
	//			+ StringName.UIDF + "," + StringName.DATEF  + "," + StringName.SENT 
	//			+ " FROM (" + StringName.TABLE_NAME2 + " JOIN " + StringName.TABLE_NAME1 + 
	//			" ON " + StringName.UIDS + " = " + StringName.UIDSREF + ") AS J JOIN " 
	//			+ StringName.TABLE_NAME3 + " ON " + StringName.UIDS + " = " + StringName.TABLE_NAME3
	//			+ "." + StringName.UIDSREF +
	//			" WHERE " + StringName.UIDS + " = '" + id_s + "' ;";

	public Cursor getInfoTable4()
	{
		SQLiteDatabase db = helper.getReadableDatabase();

		String query = "SELECT " + StringName.MAIL + " as _id ," + StringName.NAME + "," 
				+ StringName.SURNAME + " FROM " + StringName.TABLE_NAME4 + " ;";
		Cursor cursor = db.rawQuery(query, null);
		//		String[] columns = {StringName.MAIL,StringName.NAME,StringName.SURNAME};
		//		String[] columns1 = {"id_",StringName.NAME,StringName.SURNAME};
		//		Cursor cursor = db.query(StringName.TABLE_NAME4, columns, null, columns1, null, null, null);

		//		db.close();
		return cursor;

	}

	public String[] getMoreInfoTable2(String id_s, String id_f)
	{
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] arr = new String[9];
		String query = "SELECT DISTINCT " + StringName.UIDS + " as _id ," + StringName.NAMES + ","  
				+ StringName.DATE + "," + StringName.LAT + "," +  StringName.LONG + ","
				+ StringName.UIDF + "," + StringName.DATEF  + "," + StringName.SENT  + "," 
				+ StringName.ARRAY + " FROM (" + StringName.TABLE_NAME1 + " JOIN " + StringName.TABLE_NAME2 + 
				" ON " + StringName.UIDS + " = " + StringName.UIDSREF + ") AS J JOIN "+ StringName.TABLE_NAME3 + " ON " + StringName.UIDS + " = " + StringName.TABLE_NAME3
				+ "." + StringName.UIDSREF + " AND "+ StringName.UIDF + " = " + StringName.TABLE_NAME3
				+ "." + StringName.UIDFREF +
				" WHERE " + StringName.UIDS + " = '" + id_s + "' AND " + StringName.UIDF + 
				" = '" + id_f + "';";
		Cursor cursor = db.rawQuery(query, null);
		if(cursor != null) {
			cursor.moveToFirst();
			arr[0] = cursor.getString(0); //ids
			arr[1] = cursor.getString(1); //nome sessione
			arr[2] = cursor.getString(2); //data sessione
			arr[3] = cursor.getString(3); //lat
			arr[4] = cursor.getString(4); //long
			arr[5] = cursor.getString(5); //idf
			arr[6] = cursor.getString(6); //data caduta
			arr[7] = cursor.getString(7); //sent
			arr[8] = cursor.getString(8); //array dati accelerometro
		}
		cursor.close();
		db.close();
		return arr;

	}

	public void dropAllSession() {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete(StringName.TABLE_NAME3, null, null);
		db.delete(StringName.TABLE_NAME2, null, null);
		db.delete(StringName.TABLE_NAME1, null, null);
		db.execSQL(DbHelper.DROP_TABLE3);
		db.execSQL(DbHelper.DROP_TABLE2);
		db.execSQL(DbHelper.DROP_TABLE1);
		db.execSQL(DbHelper.CREATE_TABLE1);
		db.execSQL(DbHelper.CREATE_TABLE2);
		db.execSQL(DbHelper.CREATE_TABLE3);
		db.close();
	}

	public void dropAllData() {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete(StringName.TABLE_NAME3, null, null);
		db.delete(StringName.TABLE_NAME2, null, null);
		db.delete(StringName.TABLE_NAME1, null, null);
		db.delete(StringName.TABLE_NAME4, null, null);
//		String DROP_TABLE3 = "DROP TABLE IF EXSISTS" + StringName.TABLE_NAME3;
//		String DROP_TABLE2 = "DROP TABLE IF EXSISTS" + StringName.TABLE_NAME2;
//		String DROP_TABLE1 = "DROP TABLE IF EXSISTS" + StringName.TABLE_NAME1;
		db.execSQL(DbHelper.DROP_TABLE3);
		db.execSQL(DbHelper.DROP_TABLE2);
		db.execSQL(DbHelper.DROP_TABLE1);
		db.execSQL(DbHelper.CREATE_TABLE1);
		db.execSQL(DbHelper.CREATE_TABLE2);
		db.execSQL(DbHelper.CREATE_TABLE3);
		db.close();
	}

	public Cursor getAllRowsTable1() {
		SQLiteDatabase db = helper.getReadableDatabase();

		String query = "SELECT DISTINCT " + StringName.UIDS + " as _id, "  
				+ StringName.NAMES + " , " + StringName.DATE + " , " + StringName.DURATION + " , countFall" 
				+ " FROM (" + StringName.TABLE_NAME1 + " LEFT OUTER JOIN (SELECT " 
				+ StringName.UIDSREF  + ", COUNT("+ StringName.UIDF +") AS countFall"
				+ " FROM " + StringName.TABLE_NAME2
				+ " GROUP BY " + StringName.UIDSREF + ") AS J ON " 
				+ StringName.UIDS + " = " + StringName.UIDSREF +" ) ORDER BY " + 
				StringName.UIDS + " DESC;" ;

		Cursor cursor = db.rawQuery(query, null);
		//		db.close();
		return cursor;
	}
	
	public Cursor getInfoTableRiepilog(String ids) {
		SQLiteDatabase db = helper.getReadableDatabase();

		String query = "SELECT DISTINCT " + StringName.UIDS + " as _id, "  
				+ StringName.NAMES + " , " + StringName.DATE + " , " + StringName.DURATION + " , countFall" 
				+ " FROM (" + StringName.TABLE_NAME1 + " LEFT OUTER JOIN (SELECT " 
				+ StringName.UIDSREF  + ", COUNT("+ StringName.UIDF +") AS countFall"
				+ " FROM " + StringName.TABLE_NAME2 + " WHERE " + StringName.UIDSREF + "='" + ids + "'"
				+ " GROUP BY " + StringName.UIDSREF + ") AS J ON " 
				+ StringName.UIDS + " = " + StringName.UIDSREF +" ) ORDER BY " + 
				StringName.UIDS + " DESC;" ;

		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToNext();
		//		db.close();
		return cursor;
	}

	public String[] getInfoUltimateSession(String ids) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] s = new String[4];
		String[] columns = {StringName.UIDS,StringName.NAMES,StringName.DATE,StringName.DURATION};
		String table = StringName.TABLE_NAME1;
		String where = StringName.UIDS + "='" + ids + "'";
		Cursor cursor = db.query(table, columns, where, null, null, null, null);
		if(cursor != null) {
			cursor.moveToFirst();
			s[0] = Integer.toString(cursor.getInt(0));
			s[1] = cursor.getString(1);
			s[2] = cursor.getString(2);
			s[3] = cursor.getString(3);
		}
		cursor.close();
		db.close();
		return s;
	}
	//	public Cursor getNumberFall(String ids) {
	//		SQLiteDatabase db = helper.getReadableDatabase();
	//		String table = StringName.TABLE_NAME2;
	//		String where = StringName.UIDSREF + " = '" + ids + "'";
	//		String query = "SELECT DISTINCT " + StringName.UIDSREF + " AS _id, COUNT(*) AS countFall FROM " + table
	//				+ " WHERE " + where + " GROUP BY " + StringName.UIDSREF + ";";
	//		Cursor cursor = db.rawQuery(query, null);
	////		if(cursor != null) {
	////			cursor.moveToNext();
	////			s = cursor.getString(cursor.getColumnIndex("countFall"));
	////		}
	////		cursor.close();
	////		db.close();
	//
	//		return cursor;

	//	}

	public int getNumberFall(String ids) {
		SQLiteDatabase db = helper.getReadableDatabase();
		int n = 0;
		String table = StringName.TABLE_NAME2;
		String where = StringName.UIDSREF + " = '" + ids + "'";
		String query = "SELECT DISTINCT " + StringName.UIDSREF + " AS _id," + StringName.UIDF
				+ " FROM " + table + " WHERE " + where + ";";
		Cursor cursor = db.rawQuery(query, null);
		n = cursor.getCount();
			
		cursor.close();
		db.close();

		return n;
	}
	public String[] getInfoCurrentSession(String ids) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] s = new String[3];
		String[] columns = {StringName.UIDS,StringName.NAMES,StringName.DATE};
		String table = StringName.TABLE_NAME1;
		String where = StringName.UIDS + "='" + ids + "'";
		Cursor cursor = db.query(table, columns, where, null, null, null, null);
		if(cursor != null) {
			cursor.moveToFirst();
			s[0] = Integer.toString(cursor.getInt(0));
			s[1] = cursor.getString(1);
			s[2] = cursor.getString(2);
		}
		cursor.close();
		db.close();
		return s;
	}

	//	String query = "SELECT DISTINCT " + StringName.UIDS + " as _id, "  
	//			+ StringName.NAMES + " , " + StringName.DATE + " , " + StringName.DURATION + " , countFall" 
	//			+ " FROM (" + StringName.TABLE_NAME1 + " LEFT OUTER JOIN (SELECT " 
	//			+ StringName.UIDSREF  + ", COUNT(*) AS countFall"
	//			+ " FROM " + StringName.TABLE_NAME2
	//			+ " GROUP BY " + StringName.UIDSREF + ") AS J ON " 
	//			+ StringName.UIDS + " ='" + StringName.UIDSREF +"' ) AS K"  
	//			+ " ; ";

	public Cursor getAllRowsTable2() {
		SQLiteDatabase db = helper.getReadableDatabase();

		String query = "SELECT " + StringName.UIDF + " as _id,"  
				+ StringName.LAT + "," + StringName.LONG + "," + StringName.UIDSREF + ","
				+ StringName.DATEF + " FROM " + StringName.TABLE_NAME2 +";";
		Cursor cursor = db.rawQuery(query, null);

		//		db.close();
		return cursor;
	}

	public void dropSession(String ids) {
		SQLiteDatabase db = helper.getWritableDatabase();
		//		db.delete(StringName.TABLE_NAME3, ids, null);
		db.delete(StringName.TABLE_NAME3, StringName.UIDSREF + " = '" + ids + "' ", null);
		db.delete(StringName.TABLE_NAME2, StringName.UIDSREF + " = '" + ids + "' ", null);
		db.delete(StringName.TABLE_NAME1, StringName.UIDS + " = '" + ids + "' ", null);
		//		String QUERY = "DELETE FROM " + StringName.TABLE_NAME2 + " WHERE "
		//				+ StringName.UIDSREF + " = ' " + ids + " ' ;";
		//		db.execSQL(QUERY);
		//		QUERY = "DELETE FROM " + StringName.TABLE_NAME1 + " WHERE "
		//				+ StringName.UIDS + " = ' " + ids + " ' ;";
		//		db.execSQL(QUERY);

		db.close();
	}

	public void setInfoSent(String ids, String idf, String[] listContact) {
		String sent = "No";
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		for(int i = 0; i < listContact.length; i++) {
			contentValues.put(StringName.UIDFREF, idf);
			contentValues.put(StringName.UIDSREF, ids);
			contentValues.put(StringName.MAILREF, listContact[i]);
			contentValues.put(StringName.SENT, sent);
			String table = StringName.TABLE_NAME3;
			db.insert(table, null,contentValues);
			contentValues.clear();
		}
		db.close();
	}

	public void dropContact(String mail) {
		SQLiteDatabase db = helper.getWritableDatabase();
		//		db.delete(StringName.TABLE_NAME3, StringName.MAILREF + " = '" + mail + "' ", null);
		String table = StringName.TABLE_NAME4;
		String where =StringName.MAIL + " = '" + mail + "' ";
		db.delete(table, where, null);

		db.close();
	}

	//registrazione contatto nelle impostazioni
	public void createContact(String mail, String name, String surname)
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(StringName.MAIL, mail);
		contentValues.put(StringName.NAME, name);
		contentValues.put(StringName.SURNAME, surname);
		String table = StringName.TABLE_NAME4;
		db.insert(table, null,contentValues);
		db.close();
	}

	//crea la sessione e restituisce l'id sessione
	public int createSession(int sens, String name)
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		int ids = 0;
		myTime = new MyTime();
		contentValues.put(StringName.DATE, myTime.myTime());
		contentValues.put(StringName.NAMES, name);
		contentValues.put(StringName.DURATION, "");
		contentValues.put(StringName.SENS, sens);
		String table = StringName.TABLE_NAME1;
		db.insert(table, null,contentValues); // ritorna -1 se qualcosa va storto

		//		SQLiteDatabase db1 = helper.getReadableDatabase();
		//		String query = "SELECT MAX(" + StringName.UIDS + ") FROM " + StringName.TABLE_NAME1 + " ;";
		//		Cursor cursor = db1.rawQuery(query, null);
		//		if(cursor != null) {
		//			cursor.moveToFirst();
		//			ids = Integer.parseInt(cursor.getString(0));
		ids = Integer.parseInt(getCurrentSessionID());
		//		}
		//		cursor.close();
		db.close();

		return ids;
	}


	public String getCurrentSessionID()
	{	
		String ids = "";
		SQLiteDatabase db = helper.getReadableDatabase();
		String columns = StringName.UIDS;
		String table = StringName.TABLE_NAME1;
		String query = "SELECT MAX(" + columns + ") FROM " + table + " ;";
		Cursor cursor = db.rawQuery(query, null);
		if(cursor != null) {
			cursor.moveToNext();
			ids = cursor.getString(0);
		}
		cursor.close();
		db.close();

		return ids;
	}
	public String getNameSession(String ids)
	{	
		String name = "";
		SQLiteDatabase db = helper.getReadableDatabase();
		//		String query = "SELECT " + StringName.NAMES + " FROM " + StringName.TABLE_NAME1 + 
		//				" WHERE " + StringName.UIDS + " = ' " + ids + " ';";
		//		Cursor cursor = db.rawQuery(query, null);
		String[] columns = {StringName.NAMES};
		String table = StringName.TABLE_NAME1;
		String where = StringName.UIDS + "='" + ids + "'";
		Cursor cursor = db.query(table, columns, where, null, null, null, null);
		if(cursor != null) {
			cursor.moveToFirst();
			name = cursor.getString(0);
		}
		cursor.close();
		db.close();

		return name;
	}

	//restituisce la sensibilita della sessione dandone l'ids
	public int getSens(String ids) {
		SQLiteDatabase db = helper.getReadableDatabase();
		int s = 0;
		//		String query = "SELECT " + StringName.SENS + " FROM " + StringName.TABLE_NAME1 + 
		//				" WHERE " + StringName.UIDS + " = ' " + ids + " ' ;";
		//		Cursor cursor = db.rawQuery(query, null);
		String[] columns = {StringName.SENS};
		String table = StringName.TABLE_NAME1;
		String where = StringName.UIDS + "='" + ids + "'";
		Cursor cursor = db.query(table, columns, where, null, null, null, null);
		if(cursor != null) {
			cursor.moveToFirst();
			s = Integer.parseInt(cursor.getString(0));
		}
		cursor.close();
		db.close();
		return s;
	}

	//restituisce la data della sessione dandone l'ids
	public int[] getDate(int ids) {
		SQLiteDatabase db = helper.getReadableDatabase();
		int[] dat = new int[6];
		String result = "";
		//		String query = "SELECT " + StringName.DATE + " FROM " + StringName.TABLE_NAME1 + 
		//				" WHERE " + StringName.UIDS + " = ' " + ids + " ' ;";
		//		Cursor cursor = db.rawQuery(query, null);
		String[] columns = {StringName.DATE};
		String table = StringName.TABLE_NAME1;
		String where = StringName.UIDS + "='" + ids + "'";
		Cursor cursor = db.query(table, columns, where, null, null, null, null);
		if(cursor != null) {
			cursor.moveToFirst();
			result = cursor.getString(0);
		}
		dat[0] = Integer.parseInt(result.substring(0, 4)); //anno
		dat[1] = Integer.parseInt(result.substring(5, 7)); //mese
		dat[2] = Integer.parseInt(result.substring(8, 10)); //giorno
		dat[3] = Integer.parseInt(result.substring(11, 13)); //ore
		dat[4] = Integer.parseInt(result.substring(14, 16)); //minuti
		dat[5] = Integer.parseInt(result.substring(17, 19)); //secondi

		cursor.close();
		db.close();
		return dat;
	}

	public int[] getDate(String d) {
		int[] dat = new int[6];
		dat[0] = Integer.parseInt(d.substring(0, 4)); //anno
		dat[1] = Integer.parseInt(d.substring(5, 7)); //mese
		dat[2] = Integer.parseInt(d.substring(8, 10)); //giorno
		dat[3] = Integer.parseInt(d.substring(11, 13)); //ore
		dat[4] = Integer.parseInt(d.substring(14, 16)); //minuti
		dat[5] = Integer.parseInt(d.substring(17, 19)); //secondi
		return dat;
	}

	//si setta la durata della sessione una volta terminata
	public void setDuration(int ids,String duration)
	{ 
		SQLiteDatabase db = helper.getWritableDatabase();

		//		String QUERY = "UPDATE " + StringName.TABLE_NAME1 + " SET " + StringName.DURATION 
		//				+ " = ' " + duration + " ' WHERE " + StringName.UIDS + " = ' " + ids + " ' ;";
		//		db.execSQL(QUERY);

		ContentValues contentValues = new ContentValues();
		contentValues.put(StringName.DURATION, duration);
		String table = StringName.TABLE_NAME1;
		String where = StringName.UIDS +" ='" + ids + "'";
		db.update(table, contentValues, where, null);
		db.close();
	}

	public void setNameContact(String mail, String name, String surname)
	{ 
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(StringName.NAME, name);
		contentValues.put(StringName.SURNAME, surname);
		String table = StringName.TABLE_NAME4;
		String where = StringName.MAIL +" = '" + mail + "'";
		db.update(table, contentValues, where, null);
		db.close();
	}

	public int getNumberContact()
	{ 
		int n = 0;
		SQLiteDatabase db = helper.getReadableDatabase();
		String QUERY = "SELECT COUNT(*) FROM " + StringName.TABLE_NAME4 + ";";
		Cursor cursor = db.rawQuery(QUERY, null);
		if(cursor != null) {
			cursor.moveToFirst();
			n = cursor.getInt(0);
		}
		else n = 0;

		cursor.close();
		db.close();
		return n;
	}

	public int getNumberSession()
	{ 
		int n = 0;
		SQLiteDatabase db = helper.getReadableDatabase();
		String QUERY = "SELECT COUNT(*) FROM " + StringName.TABLE_NAME1 + ";";
		Cursor cursor = db.rawQuery(QUERY, null);
		if(cursor != null) {
			cursor.moveToNext();
			n = cursor.getInt(0);
			//			if(cursor.getCount() == 0)
			//				n = 0;
		}
		else n = 0;

		cursor.close();
		db.close();
		return n;
	}

	public String[] getListContact() {
		int n = getNumberContact();
		String[] listcontact = new String[n];
		SQLiteDatabase db = helper.getReadableDatabase();
		//		String QUERY = "SELECT " + StringName.MAIL + " FROM " + StringName.TABLE_NAME4 + ";";
		String[] columns = {StringName.MAIL};
		String table = StringName.TABLE_NAME4;
		Cursor cursor = db.query(table, columns , null, null, null, null, null);
		//		Cursor cursor = db.rawQuery(QUERY, null);
		if(cursor != null) {
			cursor.moveToFirst();
			for(int i = 0; i < n; i++) {
				listcontact[i] = cursor.getString(0);
				cursor.moveToNext();
			}
			//			listcontact[0] = cursor.getString(0);
			//			listcontact[1] = cursor.getString(1);
		}
		cursor.close();
		db.close();
		return listcontact;
	}





	//si setta la durata della sessione una volta terminata
	public void setLatLongGPS(String ids, String idf, String lat, String longit)
	{ 
		SQLiteDatabase db = helper.getWritableDatabase();

		//		String QUERY = "UPDATE " + StringName.TABLE_NAME1 + " SET " + StringName.DURATION 
		//				+ " = ' " + duration + " ' WHERE " + StringName.UIDS + " = ' " + ids + " ' ;";
		//		db.execSQL(QUERY);

		ContentValues contentValues = new ContentValues();
		contentValues.put(StringName.LAT, lat);
		contentValues.put(StringName.LONG, longit);
		String table = StringName.TABLE_NAME2;
		String where = StringName.UIDSREF + " = '" + ids + "' AND "
				+ StringName.UIDF + " = '" + idf +"'";
		db.update(table, contentValues, where, null);
		db.close();
	}



	//si setta il nome della sessione
	public void setNameSession(String ids,String name)
	{ 
		SQLiteDatabase db = helper.getWritableDatabase();

		//		String QUERY = "UPDATE " + StringName.TABLE_NAME1 + " SET " + StringName.DURATION 
		//				+ " = ' " + duration + " ' WHERE " + StringName.UIDS + " = ' " + ids + " ' ;";
		//		db.execSQL(QUERY);

		ContentValues contentValues = new ContentValues();
		contentValues.put(StringName.NAMES, name);
		String table = StringName.TABLE_NAME1;
		String where = StringName.UIDS +" = '" + ids + "'";
		db.update(table, contentValues, where, null);
		db.close();
	}

	public String getName(String mail) {
		String s = "";
		SQLiteDatabase db = helper.getReadableDatabase();
		//		String query = "SELECT " + StringName.NAME + " FROM " + StringName.TABLE_NAME4 + 
		//				" WHERE " + StringName.MAIL + " = '" + mail + "' ;";
		//		Cursor cursor = db.rawQuery(query, null);
		String[] columns = {StringName.NAME};
		String where = StringName.MAIL + "='" + mail + "'";
		String table = StringName.TABLE_NAME4;
		Cursor cursor = db.query(table, columns, where, null, null, null, null);
		if(cursor != null) {
			cursor.moveToFirst();
			s = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return s;
	}

	public String getSurname(String mail) {
		String s = "";
		SQLiteDatabase db = helper.getReadableDatabase();
		//		String query = "SELECT " + StringName.SURNAME + " FROM " + StringName.TABLE_NAME4 + 
		//				" WHERE " + StringName.MAIL + " = '" + mail + "' ;";
		//		Cursor cursor = db.rawQuery(query, null);
		String[] columns = {StringName.SURNAME};
		String where = StringName.MAIL + "='" + mail + "'";
		String table = StringName.TABLE_NAME4;
		Cursor cursor = db.query(table, columns, where, null, null, null, null);
		if(cursor != null) {
			cursor.moveToFirst();
			s = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return s;
	}

	public void createFall(int idf, int ids, /*String lat, String longit,*/ String datef, String array, String[] listContact)
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(StringName.UIDF, idf);
		contentValues.put(StringName.UIDSREF, ids);
		contentValues.put(StringName.LAT, "");
		contentValues.put(StringName.LONG, "");
		contentValues.put(StringName.DATEF, datef);
		contentValues.put(StringName.ARRAY, array);
		String table = StringName.TABLE_NAME2;
		db.insert(table, null,contentValues);
		String idsString = ids+"";
		String idfString = idf+"";
		setInfoSent(idsString, idfString, listContact);

		db.close();
	}



	public String convertArrayToString(float[] array){
		String str = "";
		for (int i = 0;i < array.length; i++) {
			str = str+array[i];
			// Do not append comma at the end of last element
			if(i<array.length-1){
				str = str+strSeparator;
			}
		}
		return str;
	}

	public String getArrayString(int ids, int idf) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String result = "";
		//		String query = "SELECT " + StringName.ARRAY + " FROM " + StringName.TABLE_NAME2 + 
		//				" WHERE " + StringName.UIDSREF + " = ' " + ids + " ' AND " + StringName.UIDF + " = ' " + idf + " ' ;";
		//		Cursor cursor = db.rawQuery(query, null);
		String[] columns = {StringName.ARRAY};
		String table = StringName.TABLE_NAME2;
		String where = StringName.UIDSREF + " = '" + ids + "' AND " + StringName.UIDF + " = '" + idf + "'";
		Cursor cursor = db.query(table, columns, where, null, null, null, null);
		if(cursor != null) {
			cursor.moveToFirst();
			result = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return result;
	}

	public float[] convertStringToArray(String str){
		String[] arr = str.split(strSeparator);
		float[] a = new float[arr.length];
		for(int i = 0; i < arr.length; i++)
			a[i] = Float.parseFloat(arr[i]);
		return a;
	}
	public int convertStringToArrayInt(String str){
		String[] arr = str.split(strSeparator);
		float[] a = new float[arr.length];
		for(int i = 0; i < arr.length; i++)
			a[i] = Float.parseFloat(arr[i]);
		return a.length;
	}

	public void setSentTrue(String idf, String ids, String[] listContact)
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		String sent = "Si";
		ContentValues contentValues = new ContentValues();
		contentValues.put(StringName.SENT, sent);
		String table = StringName.TABLE_NAME3;
		for(int i = 0; i < listContact.length; i++) {
			db.update(table, contentValues, StringName.UIDFREF + " = '" + idf + "' AND "
					+ StringName.UIDSREF + " = '" + ids + "' AND "
					+ StringName.MAILREF + " = '" + listContact[i] + "'", null);
		}
		db.close();
	}

	//	String QUERY = "UPDATE " + StringName.TABLE_NAME3 + " SET " + StringName.SENT 
	//			+ " = '" + sent + "' WHERE " + StringName.UIDSREF + " = '" + ids + "' AND "
	//			+ StringName.UIDFREF + " = '" + idf + "' AND " 
	//			+ StringName.MAILREF + " = '" + listContact[i] + "';";
	//	db.execSQL(QUERY);

	//	ContentValues contentValues = new ContentValues();
	//	contentValues.put(StringName.SENT, sent);
	//	for(int i = 0; i < listContact.length; i++) {
	//		db.update(StringName.TABLE_NAME3, contentValues, StringName.UIDFREF + " = '" + idf + "' AND "
	//				+ StringName.UIDSREF + " = '" + ids + "' AND "
	//				+ StringName.MAILREF + " = '" + listContact[i] + "'", null);
	//	}




	//	public String getAllData()
	//	{
	//		SQLiteDatabase db = helper.getWritableDatabase();
	//		// select _id, Name, Password from TABELLATEO
	//		String[] columns = {StringName.UID, StringName.NOME, StringName.PASSWORD};
	//		Cursor cursor = db.query(StringName.TABLE_NAME, columns, null, null, null, null, null);
	//		StringBuffer buffer = new StringBuffer();
	//		while(cursor.moveToNext())
	//		{
	//			int cid = cursor.getInt(0);
	//			String name = cursor.getString(1);
	//			String password = cursor.getString(2);
	//			buffer.append(cid+ ""+ name + "" + password + "\n");
	//		}
	//		
	//		return buffer.toString();
	//
	//	}
	static class DbHelper extends SQLiteOpenHelper
	{


		
		private static final String CREATE_TABLE1 = "CREATE TABLE "+ StringName.TABLE_NAME1 + " ( "
				+ StringName.UIDS + " INTEGER PRIMARY KEY AUTOINCREMENT, " + StringName.NAMES + " VARCHAR(20) NOT NULL, "
				+ StringName.DATE + " TIMESTAMP NOT NULL, " + StringName.DURATION + " CHAR(8), " 
				+ StringName.SENS + " INTEGER NOT NULL " + ");";
		private static final String CREATE_TABLE2 = "CREATE TABLE "+ StringName.TABLE_NAME2 + " ( "
				+ StringName.UIDF + " INTEGER NOT NULL, " + StringName.UIDSREF + " INTEGER NOT NULL, " 
				+ StringName.LAT + " VARCHAR(20), " + StringName.LONG + " VARCHAR(20), "
				+ StringName.DATEF + " TIMESTAMP NOT NULL, " + StringName.ARRAY + " VARCHAR(1000) NOT NULL, "
				+ "PRIMARY KEY(" + StringName.UIDF + "," + StringName.UIDSREF + "),"
				+ "FOREIGN KEY(" + StringName.UIDSREF + ") REFERENCES " + StringName.TABLE_NAME1 + "(" + StringName.UIDS 
				+ ") ON UPDATE CASCADE ON DELETE CASCADE" +");";
		private static final String CREATE_TABLE4 = "CREATE TABLE "+ StringName.TABLE_NAME4 + " ( "
				+ StringName.MAIL + " VARCHAR(100) PRIMARY KEY , " + StringName.NAME + " VARCHAR(70) NOT NULL, " 
				+ StringName.SURNAME + " VARCHAR(70) NOT NULL"  + ");";
		private static final String CREATE_TABLE3 = "CREATE TABLE "+ StringName.TABLE_NAME3 + " ( "
				+ StringName.UIDFREF + " INTEGER NOT NULL, " + StringName.UIDSREF + " INTEGER NOT NULL, " 
				+ StringName.MAILREF + " VARCHAR(100) NOT NULL," + StringName.SENT + " CHAR(2)," 
				+ "PRIMARY KEY(" + StringName.UIDFREF + "," + StringName.UIDSREF + "," + StringName.MAILREF + "),"
				+ "FOREIGN KEY(" + StringName.UIDFREF + "," + StringName.UIDSREF + ") REFERENCES " 
				+ StringName.TABLE_NAME2 + "(" + StringName.UIDF + "," + StringName.UIDSREF
				+ ") ON UPDATE CASCADE ON DELETE CASCADE," + "FOREIGN KEY(" + StringName.MAILREF 
				+ ") REFERENCES " + StringName.TABLE_NAME4 + "(" + StringName.MAIL 
				+ ") ON UPDATE CASCADE ON DELETE SET NULL" +");";

		private static final String DROP_TABLE1 = "DROP TABLE IF EXISTS " + StringName.TABLE_NAME1;
		private static final String DROP_TABLE2 = "DROP TABLE IF EXISTS " + StringName.TABLE_NAME2; 
		private static final String DROP_TABLE3 = "DROP TABLE IF EXISTS " + StringName.TABLE_NAME3; 
		private static final String DROP_TABLE4 = "DROP TABLE IF EXISTS " + StringName.TABLE_NAME4; 



		public DbHelper(Context context) {
			super(context, StringName.DATABASE_NAME , null, StringName.DATABASE_VERSION); //contesto, nomeDB, cursore (null in questo caso) e versione del DB
		}


		// questo metodo viene chiamato la prima volta in cui il DB viene chiamato
		@Override
		public void onCreate(SQLiteDatabase db) {
			// creazione effettiva.. comando SQL: CREATE TABLE TABELLATEO (_id INTEGER PRIMARY KEY AUTOINCREMENT , Name VARCHAR(255));
			try {
				db.execSQL(CREATE_TABLE1);
				db.execSQL(CREATE_TABLE2);
				db.execSQL(CREATE_TABLE4);
				db.execSQL(CREATE_TABLE3);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// viene chiamato in caso di cancellazione o aggiornamento/modifica (ad es l'aggiunta di una nuova colonna) del DB
			// N.B: ogni volta che si modifica qualcosa bisogna ricordarsi di aggiornare la versione del DB

			try {
				db.execSQL(DROP_TABLE3); // cancello db
				db.execSQL(DROP_TABLE4); // cancello db
				db.execSQL(DROP_TABLE2); // cancello db
				db.execSQL(DROP_TABLE1); // cancello db
				onCreate(db); // ricreo db
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			}
		}


		//		private void ondestroy() {
		//			// TODO Auto-generated method stub
		//
		//		}

	}
}

