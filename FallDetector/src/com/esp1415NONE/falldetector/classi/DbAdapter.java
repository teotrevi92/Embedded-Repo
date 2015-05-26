package com.esp1415NONE.falldetector.classi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbAdapter  {
	SQLiteDatabase db;
	DbHelper helper;
	public DbAdapter(Context context){

		helper = new DbHelper(context);
	}
	private static String strSeparator = "_";
	MyTime myTime;

	public String getTable1()
		{
			SQLiteDatabase db = helper.getWritableDatabase();
			
			String[] columns = {StringName.UIDS, StringName.DATE, StringName.DURATION, StringName.SENS};
			Cursor cursor = db.query(StringName.TABLE_NAME1, columns, null, null, null, null, null);
			StringBuffer buffer = new StringBuffer();
			while(cursor.moveToNext())
			{
				int cid = cursor.getInt(0);
				String date = cursor.getString(1);
				String duration = cursor.getString(2);
				int sens = cursor.getInt(3);
				buffer.append(cid+ ""+ date + "" + duration + "" + sens + "\n");
			}
			
			return buffer.toString();
	
		}
	public String getTable2()
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		
		String[] columns = {StringName.UIDF, StringName.UIDSREF, StringName.LAT, StringName.LONG, StringName.DATEF, StringName.ARRAY};
		Cursor cursor = db.query(StringName.TABLE_NAME2, columns, null, null, null, null, null);
		StringBuffer buffer = new StringBuffer();
		while(cursor.moveToNext())
		{
			int cid = cursor.getInt(0);
			int ids = cursor.getInt(1);
			Long lat = cursor.getLong(2);
			Long longit = cursor.getLong(3);
			String datef = cursor.getString(4);
			String array = cursor.getString(5);
			buffer.append(cid+ ""+ ids + "" + lat + "" + longit + "" + datef+ "" + array + "\n");
		}
		
		return buffer.toString();

	}
	public String getTable3()
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		
		String[] columns = {StringName.UIDFREF, StringName.UIDSREF, StringName.MAILREF, StringName.SENT};
		Cursor cursor = db.query(StringName.TABLE_NAME3, columns, null, null, null, null, null);
		StringBuffer buffer = new StringBuffer();
		while(cursor.moveToNext())
		{
			int cid = cursor.getInt(0);
			int ids = cursor.getInt(1);
			String mail = cursor.getString(2);
			String sent = cursor.getString(3);
			buffer.append(cid+ ""+ ids + "" + mail + "" + sent + "\n");
		}
		
		return buffer.toString();

	}
	
	public String getTable4()
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		
		String[] columns = {StringName.MAIL, StringName.NAME, StringName.SURNAME};
		Cursor cursor = db.query(StringName.TABLE_NAME4, columns, null, null, null, null, null);
		StringBuffer buffer = new StringBuffer();
		while(cursor.moveToNext())
		{
			String mail = cursor.getString(0);
			String name = cursor.getString(1);
			String surname = cursor.getString(2);
			buffer.append(mail+ ""+ name + "" + surname + "\n");
		}
		
		return buffer.toString();

	}
	
	public DbAdapter open(){
		db = helper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		helper.close();
	}
	
	public Cursor gettAllRows(){
		String where = null;
		Cursor c = db.query(true, StringName.DATABASE_NAME, StringName.ALL_KEYS,where,null,null,null,null,null);
		if(c!=null){
			c.moveToFirst();
		}
		return c;
	}
	//registrazione contatto nelle impostazioni
	public long setContact(String mail, String name, String surname)
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(StringName.MAIL, mail);
		contentValues.put(StringName.NAME, name);
		contentValues.put(StringName.SURNAME, surname);
		long id = db.insert(StringName.TABLE_NAME4, null,contentValues); // ritorna -1 se qualcosa va storto
		return id;
	}
	
	//crea la sessione e restituisce l'id sessione
	public int createSession(int sens)
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		int ids = 0;
		myTime = new MyTime();
		contentValues.put(StringName.DATE, myTime.myTime());
		contentValues.put(StringName.DURATION, "");
		contentValues.put(StringName.SENS, sens);
		db.insert(StringName.TABLE_NAME1, null,contentValues); // ritorna -1 se qualcosa va storto
		
		SQLiteDatabase db1 = helper.getReadableDatabase();
		String query = "SELECT MAX(" + StringName.UIDS + ") FROM " + StringName.TABLE_NAME1 + " ;";
		Cursor cursor = db1.rawQuery(query, null);
		if(cursor != null) {
			cursor.moveToFirst();
			ids = Integer.parseInt(cursor.getString(0));
		}
		return ids;
	}
	
	//restituisce la sensibilita della sessione dandone l'ids
	public int getSens(int ids) {
		SQLiteDatabase db = helper.getReadableDatabase();
		int s = 0;
		String query = "SELECT " + StringName.SENS + " FROM " + StringName.TABLE_NAME1 + 
				" WHERE " + StringName.UIDS + " = ' " + ids + " ' ;";
		Cursor cursor = db.rawQuery(query, null);
		if(cursor != null) {
			cursor.moveToFirst();
			s = Integer.parseInt(cursor.getString(0));
		}
		return s;
	}
	
	//restituisce la data della sessione dandone l'ids
	public float[] getDate(int ids) {
		SQLiteDatabase db = helper.getReadableDatabase();
		float[] dat = new float[6];
		String result = "";
		String query = "SELECT " + StringName.DATE + " FROM " + StringName.TABLE_NAME1 + 
				" WHERE " + StringName.UIDS + " = ' " + ids + " ' ;";
		Cursor cursor = db.rawQuery(query, null);
		if(cursor != null) {
			cursor.moveToFirst();
			result = cursor.getString(0);
		}
		dat[0] = Float.parseFloat(result.substring(0, 4)); //anno
		dat[1] = Float.parseFloat(result.substring(5, 7)); //mese
		dat[2] = Float.parseFloat(result.substring(8, 10)); //giorno
		dat[3] = Float.parseFloat(result.substring(11, 13)); //ore
		dat[4] = Float.parseFloat(result.substring(14, 16)); //minuti
		dat[5] = Float.parseFloat(result.substring(17, 19)); //secondi
		return dat;
	}
	
	//si setta la durata della sessione una volta terminata
	public void setDuration(int ids,String duration)
	{
		SQLiteDatabase db = helper.getWritableDatabase();

		String QUERY = "UPDATE " + StringName.TABLE_NAME1 + " SET " + StringName.DURATION 
				+ " = ' " + duration + " ' WHERE " + StringName.UIDS + " = ' " + ids + " ' ;";
		db.execSQL(QUERY);
	}


	public void createFall(int idf, int ids, Long lat, Long longit, String datef, String array)
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(StringName.UIDF, idf);
		contentValues.put(StringName.UIDSREF, ids);
		contentValues.put(StringName.LAT, lat);
		contentValues.put(StringName.LONG, longit);
		contentValues.put(StringName.DATEF, datef);
		contentValues.put(StringName.ARRAY, array);
		db.insert(StringName.TABLE_NAME2, null,contentValues); // ritorna -1 se qualcosa va storto
		
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

	public float[] convertStringToArray(String str){
		String[] arr = str.split(strSeparator);
		float[] a = new float[arr.length];
		for(int i = 0; i < arr.length; i++)
			a[i] = Float.parseFloat(arr[i]);
		return a;
	}

	public void setSent(int idf, int ids, String mailr, boolean sent)
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();

		contentValues.put(StringName.UIDFREF, idf);
		contentValues.put(StringName.UIDSREF, ids);
		contentValues.put(StringName.MAILREF, mailr);
		contentValues.put(StringName.SENT, sent);
		db.insert(StringName.TABLE_NAME3, null,contentValues); // ritorna -1 se qualcosa va storto
	}
	



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
				+StringName.UIDS + " INTEGER PRIMARY KEY AUTOINCREMENT, " + StringName.DATE + " TIMESTAMP NOT NULL, " 
				+ StringName.DURATION + " TIME, " + StringName.SENS + " INTEGER NOT NULL " + ");";
		private static final String CREATE_TABLE2 = "CREATE TABLE "+ StringName.TABLE_NAME2 + " ( "
				+ StringName.UIDF + " INTEGER , " + StringName.UIDSREF + " TIMESTAMP NOT NULL, " 
				+ StringName.LAT + " DECIMAL(8,10) NOT NULL, " + StringName.LONG + " DECIMAL(8,10) NOT NULL, "
				+ StringName.DATEF + " TIMESTAMP NOT NULL, " + StringName.ARRAY + " VARCHAR(1000) NOT NULL, "
				+ "PRIMARY KEY(" + StringName.UIDF + "," + StringName.UIDSREF + "),"
				+ "FOREIGN KEY(" + StringName.UIDSREF + ") REFERENCES " + StringName.TABLE_NAME1 + "(" + StringName.UIDS + ")" + ");";
		private static final String CREATE_TABLE4 = "CREATE TABLE "+ StringName.TABLE_NAME4 + " ( "
				+ StringName.MAIL + " VARCHAR(100) PRIMARY KEY , " + StringName.NAME + " VARCHAR(70) NOT NULL, " 
				+ StringName.SURNAME + " VARCHAR(70) NOT NULL" + ");";
		private static final String CREATE_TABLE3 = "CREATE TABLE "+ StringName.TABLE_NAME3 + " ( "
				+ StringName.UIDFREF + " INTEGER NOT NULL, " + StringName.UIDSREF + " INTEGER NOT NULL, " 
				+ StringName.MAILREF + " VARCHAR(100) NOT NULL," + StringName.SENT + " BOOLEAN NOT NULL," 
				+ "PRIMARY KEY(" + StringName.UIDFREF + "," + StringName.UIDSREF + "," + StringName.MAILREF + "),"
				+ "FOREIGN KEY(" + StringName.UIDFREF + "," + StringName.UIDSREF + ") REFERENCES " 
				+ StringName.TABLE_NAME2 + "(" + StringName.UIDF + "," + StringName.UIDSREF + "),"
				+ "FOREIGN KEY(" + StringName.MAILREF + ") REFERENCES " + StringName.TABLE_NAME4 + "(" + StringName.MAIL + ")" + ");";

		private static final String DROP_TABLE1 = "DROP TABLE IF EXSISTS" + StringName.TABLE_NAME1;
		private static final String DROP_TABLE2 = "DROP TABLE IF EXSISTS" + StringName.TABLE_NAME2; 
		private static final String DROP_TABLE3 = "DROP TABLE IF EXSISTS" + StringName.TABLE_NAME3; 
		private static final String DROP_TABLE4 = "DROP TABLE IF EXSISTS" + StringName.TABLE_NAME4; 



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

	}
}

