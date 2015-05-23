package com.esp1415NONE.falldetector.classi;

public class StringName {

	
	static final String DATABASE_NAME="fallenDB";
	static final int DATABASE_VERSION = 1;
	//tabella 1
	static final String TABLE_NAME1 = "session";
	static final String UIDS = "_ids";
	static final String DATE = "dates";
	static final String DURATION = "duration";
	static final String SENS = "sens";
	//tabella 2
	static final String TABLE_NAME2 ="fall";
	static final String UIDF = "_idf";
	static final String UIDSREF = "ids"; // si riferisce a UIDS
	static final String LAT = "lat";
	static final String LONG = "longit";
	static final String DATEF = "datef";
	static final String ARRAY = "array";
	
	//tabella 3
	static final String TABLE_NAME3 ="state";
	static final String UIDFREF = "idf";
	//static final String UIDSREF = "ids";
	static final String MAILREF = "mailr";
	static final String SENT = "sent";
	
	//tabella 4
	static final String TABLE_NAME4 ="contact";
	static final String MAIL = "mail";
	static final String NAME = "name";
	static final String SURNAME = "surname";
}
