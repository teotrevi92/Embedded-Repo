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

	//restituisce le informazioni della tabella Session in un array
	public String[] getInfoTable1(String ids)
	{
		SQLiteDatabase db = helper.getReadableDatabase();

		String[] arr = new String[3];
		String[] columns = {StringName.DATE,StringName.NAMES,StringName.DURATION};
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

	//restituisce il cursore con le informazioni della tabella Fall
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

		return cursor;

	}

	//restituisce il cursore con le informazioni della tabella Contact
	public Cursor getInfoTable4()
	{
		SQLiteDatabase db = helper.getReadableDatabase();

		String query = "SELECT " + StringName.MAIL + " as _id ," + StringName.NAME + "," 
				+ StringName.SURNAME + " FROM " + StringName.TABLE_NAME4 + " ;";
		Cursor cursor = db.rawQuery(query, null);
		return cursor;

	}

	//restituisce le informazioni della tabella Fall più dettagliate in un array
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

	/*eliminazione di tutte le sessioni, devo cancellare le tabelle 
	  per azzerare ids che e' un autoincrement*/
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

	/*eliminazione di tutti i dati, devo cancellare le tabelle 
	  per azzerare ids che e' un autoincrement, non serve cancellare la tabella Contact*/
	public void dropAllData() 
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete(StringName.TABLE_NAME3, null, null);
		db.delete(StringName.TABLE_NAME2, null, null);
		db.delete(StringName.TABLE_NAME1, null, null);
		db.delete(StringName.TABLE_NAME4, null, null);
		db.execSQL(DbHelper.DROP_TABLE3);
		db.execSQL(DbHelper.DROP_TABLE2);
		db.execSQL(DbHelper.DROP_TABLE1);
		db.execSQL(DbHelper.CREATE_TABLE1);
		db.execSQL(DbHelper.CREATE_TABLE2);
		db.execSQL(DbHelper.CREATE_TABLE3);
		db.close();
	}

	//restituisce un cursore con tutte le informazioni della tabella Session
	public Cursor getAllRowsTable1() 
	{
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
		return cursor;
	}

	//restituisce un cursore con le informazioni per il riepilogo sessioni in listview
	public Cursor getInfoTableRiepilog(String ids) 
	{
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
		return cursor;
	}

	//elimina una sessione in particolare 
	public void dropSession(String ids) 
	{
		SQLiteDatabase db = helper.getWritableDatabase();

		db.delete(StringName.TABLE_NAME3, StringName.UIDSREF + " = '" + ids + "' ", null);
		db.delete(StringName.TABLE_NAME2, StringName.UIDSREF + " = '" + ids + "' ", null);
		db.delete(StringName.TABLE_NAME1, StringName.UIDS + " = '" + ids + "' ", null);
		db.close();
	}

	//setta a No la caduta di default
	public void setInfoSent(String ids, String idf, String[] listContact)
	{
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

	//elimina un contatto in particolare
	public void dropContact(String mail) 
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		String table = StringName.TABLE_NAME4;
		String where =StringName.MAIL + " = '" + mail + "' ";
		db.delete(table, where, null);

		db.close();
	}

	//inserisce un nuovo contatto
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
		ids = Integer.parseInt(getCurrentSessionID());
		db.close();

		return ids;
	}

	//restituisce l'id della sessione ultima, quindi se si e' in play, e' quella corrente
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

	//restituisce il nome della sessione in base all'ids
	public String getNameSession(String ids)
	{	
		String name = "";
		SQLiteDatabase db = helper.getReadableDatabase();
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
	public int getSens(String ids) 
	{
		SQLiteDatabase db = helper.getReadableDatabase();
		int s = 0;
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
	public int[] getDate(String d) 
	{
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
		ContentValues contentValues = new ContentValues();
		contentValues.put(StringName.DURATION, duration);
		String table = StringName.TABLE_NAME1;
		String where = StringName.UIDS +" ='" + ids + "'";
		db.update(table, contentValues, where, null);
		db.close();
	}

	//setta il nome di un contatto quando si rinomina
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

	//restituisce il numero di contatti salvati, 0 se nessuno
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

	//restituisce il numero di sessioni salvati, 0 se nessuno
	public int getNumberSession()
	{ 
		int n = 0;
		SQLiteDatabase db = helper.getReadableDatabase();
		String QUERY = "SELECT COUNT(*) FROM " + StringName.TABLE_NAME1 + ";";
		Cursor cursor = db.rawQuery(QUERY, null);
		if(cursor != null) {
			cursor.moveToNext();
			n = cursor.getInt(0);
		}
		else n = 0;

		cursor.close();
		db.close();
		return n;
	}

	//restituisce la lista di contatti salvati in un array
	public String[] getListContact() 
	{
		int n = getNumberContact();
		String[] listcontact = new String[n];
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] columns = {StringName.MAIL};
		String table = StringName.TABLE_NAME4;
		Cursor cursor = db.query(table, columns , null, null, null, null, null);
		if(cursor != null) {
			cursor.moveToFirst();
			for(int i = 0; i < n; i++) {
				listcontact[i] = cursor.getString(0);
				cursor.moveToNext();
			}
		}
		cursor.close();
		db.close();
		return listcontact;
	}

	//si setta latitudine e longitudine dei dati del GPS
	public void setLatLongGPS(String ids, String idf, String lat, String longit)
	{ 
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(StringName.LAT, lat);
		contentValues.put(StringName.LONG, longit);
		String table = StringName.TABLE_NAME2;
		String where = StringName.UIDSREF + " = '" + ids + "' AND "
				+ StringName.UIDF + " = '" + idf +"'";
		db.update(table, contentValues, where, null);
		db.close();
	}

	//setta il nome della sessione una volta che si rinomina una sessione
	public void setNameSession(String ids,String name)
	{ 
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(StringName.NAMES, name);
		String table = StringName.TABLE_NAME1;
		String where = StringName.UIDS +" = '" + ids + "'";
		db.update(table, contentValues, where, null);
		db.close();
	}

	//restituisce il nome del contatto dandone mail
	public String getName(String mail) 
	{
		String s = "";
		SQLiteDatabase db = helper.getReadableDatabase();
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

	//restituisce il cognome del contatto dandone mail
	public String getSurname(String mail) 
	{
		String s = "";
		SQLiteDatabase db = helper.getReadableDatabase();
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

	//registra una caduta
	public void createFall(int idf, int ids, String datef, String array, String[] listContact)
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

	//converte in String[] un array di tipo float[]
	public String convertArrayToString(float[] array)
	{
		String str = "";
		for (int i = 0;i < array.length; i++) {
			str = str+array[i];
			if(i<array.length-1){
				str = str+strSeparator;
			}
		}
		return str;
	}

	//converte in float[] un array di tipo String[]
	public float[] convertStringToArray(String str)
	{
		String[] arr = str.split(strSeparator);
		float[] a = new float[arr.length];
		for(int i = 0; i < arr.length; i++)
			a[i] = Float.parseFloat(arr[i]);
		return a;
	}

	//setta una caduta a Si, quindi mail inviata
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

	static class DbHelper extends SQLiteOpenHelper
	{

		//create table 
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


	}
}

