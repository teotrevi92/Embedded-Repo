package com.esp1415NONE.falldetector.classi;

public class StringName {

	public static final String DATABASE_NAME="fallenDB";
	public static final int DATABASE_VERSION = 1;
	//tabella 1
	public static final String TABLE_NAME1 = "session";
	public static final String UIDS = "_ids";
	public static final String NAMES = "names";
	public static final String DATE = "dates";
	public static final String DURATION = "duration";
	public static final String SENS = "sens";
	//tabella 2
	public static final String TABLE_NAME2 ="fall";
	public static final String UIDF = "_idf";
	public static final String UIDSREF = "ids"; // si riferisce a UIDS
	public static final String LAT = "lat";
	public static final String LONG = "longit";
	public static final String DATEF = "datef";
	public static final String ARRAY = "array";

	//tabella 3
	public static final String TABLE_NAME3 ="state";
	public static final String UIDFREF = "idf";
	//static final String UIDSREF = "ids";
	public static final String MAILREF = "mailr";
	public static final String SENT = "sent";

	//tabella 4
	public static final String TABLE_NAME4 ="contact";
	public static final String MAIL = "mail";
	public static final String NAME = "name";
	public static final String SURNAME = "surname";
}
