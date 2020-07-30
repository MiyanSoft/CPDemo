package com.sriyank.cpdemo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sriyank.cpdemo.data.NationContract.NationEntry;

public class NationDbHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "nations.db";
	private static final int DATABASE_VERSION = 1;

	//database schema - rather than using raw string,
	// the table name as well as its columns are named based on how they've been defined(constant) in the contract class
	//using these constant values will minimize the chances of getting errors as the app is further built
	private final String SQL_CREATE_COUNTRY_TABLE
			= "CREATE TABLE " + NationEntry.TABLE_NAME
			+ " (" + NationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ NationEntry.COLUMN_COUNTRY + " TEXT NOT NULL, "
			+ NationEntry.COLUMN_CONTINENT + " TEXT"
			+ ")";
	//this constructor contains many parameters but only the context is needed
	//responsible for SQLite database - creating, upgrading and downgrading
	public NationDbHelper(Context context) {

		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//enables creation of the table as seen in the comment below
		db.execSQL(SQL_CREATE_COUNTRY_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Ideally we wouldn't want to delete all of our entries!
		//delete existing table
		db.execSQL("DROP TABLE IF EXISTS " + NationEntry.TABLE_NAME);
		onCreate(db);	// Call to create a new db with upgraded schema and version
	}
}

/*
		TABLE NAME: countries	Database Name: nations.db

		 _id	country		continent
 		  1		 India		 Asia
 		  2		 Japan		 Asia
 		  3		 Nigeria	 Africa
* */
