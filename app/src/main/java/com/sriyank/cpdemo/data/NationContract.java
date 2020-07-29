package com.sriyank.cpdemo.data;


import android.provider.BaseColumns;

/* A contract class contains constants that define names for URIs, tables, and columns.  */
public final class NationContract {
	//inner class for database table; basecolumns ensures that the NationEntry class has an id column
	public static final class NationEntry implements BaseColumns {

		// Table Name
		public static final String TABLE_NAME = "countries";

		// Columns
		public static final String _ID = BaseColumns._ID;
		public static final String COLUMN_COUNTRY = "country";
		public static final String COLUMN_CONTINENT = "continent";
	}
}
