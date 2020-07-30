package com.sriyank.cpdemo.data;


import android.net.Uri;
import android.provider.BaseColumns;

/* A contract class contains constants that define names for URIs, tables, and columns.  */
public final class NationContract {

	//define authority string (packageName.ContentProvider class name) P.S.: .data is added because the NationProvider class is inside data package
	public static final String CONTENT_AUTHORITY = "com.sriyank.cpdemo.data.NationProvider";	//packageName(app id) can be found in build.gradle file

	//declare content URI: content://packageName.data.CP classname
	public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

	//declare database table path
	public  static final String PATH_COUNTRIES = "countries";


	//inner class for database table; BaseColumns ensures that the NationEntry class has an id column
	public static final class NationEntry implements BaseColumns {

		//final declaration of content URI: content://com.sriyank.cpdemo.data.NationProvider/countries
		public  static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_COUNTRIES);

		// Table Name
		public static final String TABLE_NAME = "countries";

		// Columns
		public static final String _ID = BaseColumns._ID;
		public static final String COLUMN_COUNTRY = "country";
		public static final String COLUMN_CONTINENT = "continent";
	}
}


/**All constants related to the content provider and the SQLite database must be stored in the contract class*/