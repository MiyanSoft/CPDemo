package com.sriyank.cpdemo.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sriyank.cpdemo.data.NationContract.NationEntry;

import static com.sriyank.cpdemo.data.NationContract.CONTENT_AUTHORITY;
import static com.sriyank.cpdemo.data.NationContract.NationEntry.TABLE_NAME;
import static com.sriyank.cpdemo.data.NationContract.PATH_COUNTRIES;

public class NationProvider extends ContentProvider {

    private static final String TAG = NationProvider.class.getSimpleName();

    //declare reference of the NationDbHelper class
    private NationDbHelper databaseHelper;

    //define constants for the operations.
    private static final int COUNTRIES = 1;             //For whole table
    private static final int COUNTRIES_COUNTRY_NAME = 2;//For a specific row in a table identified by COUNTRY_NAME
    private static final int COUNTRIES_ID = 3;          //For a specific row in a table identified by _ID


    //define content URI pattern
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        //declare all Uri patterns. (check footnotes)

        //table patterns
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_COUNTRIES, COUNTRIES);                           //com.sriyank.cpdemo.data.NationProvider/countries
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_COUNTRIES + "/#", COUNTRIES_ID);           //com.sriyank.cpdemo.data.NationProvider/countries/#
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_COUNTRIES + "/*", COUNTRIES_COUNTRY_NAME); //com.sriyank.cpdemo.data.NationProvider/countries/*
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new NationDbHelper(getContext());
        return true;    //this implies that the initialization process for the NationProvider is has been completed
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        //connect to database
        SQLiteDatabase database = databaseHelper.getReadableDatabase(); //read from database
        Cursor cursor;                                                  //define cursor reference
        //use switch case to match incoming uri
        switch (uriMatcher.match(uri))  {
            case COUNTRIES:
                 cursor = database.query(TABLE_NAME, projection, null,
                        null, null, null, null);
                break;
            case COUNTRIES_ID:
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };  //check footnote
                cursor = database.query(TABLE_NAME, projection, NationEntry._ID + " = ?",
                        selectionArgs, null, null, null);
                break;
            default:
                throw new IllegalArgumentException(TAG + " Insert unknown URI: " + uri);
        }
        return cursor;  //cursor will be received in MainActivity class
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        //A particular Uri is expected to be passed from the main activity which will appear as one of the parameters in this method
        //that Uri will match the table patterns defined above with the help of uriMatcher class
        switch (uriMatcher.match(uri))  {   //check pattern of incoming uri(the one passed as the 1st parameter in the inert method of MA class)

            //if the uri is for COUNTRIES table, the insertRecord method below will be executed
            case COUNTRIES:
                return insertRecord(uri, values, TABLE_NAME);

            default:
                throw new IllegalArgumentException(TAG + " Insert unknown URI: " + uri);
        }

    }

    private Uri insertRecord(Uri uri, ContentValues values, String tableName) {

        //get instance of SQLite db
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        //rowId is the value of the _ID column
        long rowId = database.insert(tableName, null, values);

        //if the insert operation isn't successful i.e rowId is -1 return null. This will prevent having "invalid argument -1" error at runtime
        if (rowId == -1)    {
            Log.e(TAG, "Insert error for URI " + uri);
            return null;
        }

        //return uri of the new inserted row. This will be received in the MainActivity Class (check insert method)
        return ContentUris.withAppendedId(uri, rowId);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        switch (uriMatcher.match(uri))  {
            case COUNTRIES:                 //delete whole table
                return deleteRecord(null, null, TABLE_NAME);
            case COUNTRIES_ID:              //delete a row by id
                selection = NationEntry._ID + " = ?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return deleteRecord(selection, selectionArgs, TABLE_NAME);
            case COUNTRIES_COUNTRY_NAME:    //delete a row by country name
                selection = NationEntry.COLUMN_COUNTRY + " = ?";
                selectionArgs = new String[] { uri.getLastPathSegment() };  //get back to this
                return deleteRecord(selection, selectionArgs, TABLE_NAME);
            default:
                throw new IllegalArgumentException(TAG + " Insert unknown URI: " + uri);
        }

    }

    private int deleteRecord(String selection, String[] selectionArgs, String tableName) {

        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        int rowsDeleted = database.delete(tableName, selection, selectionArgs);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        switch (uriMatcher.match(uri))  {

            case COUNTRIES:
                return updatedRecord(values, selection, selectionArgs, TABLE_NAME);
            default:
                throw new IllegalArgumentException(TAG + " Insert unknown URI: " + uri);
        }

    }

    private int updatedRecord(ContentValues values, String selection, String[] selectionArgs, String tableName) {

        SQLiteDatabase database= databaseHelper.getWritableDatabase();
        int rowsUpdated = database.update(tableName, values, selection, selectionArgs);
            return rowsUpdated;
    }
}




/**Instead of the main activity to have direct interaction with the database,
 * this class is created to serve as an intermediate between our app and the db.
 * It extends from the content provider class  */

/**The uri patterns are prioritized based on sequence
 * i.e the 1st one will have more priority over the 2nd and so on (check exercise file)*/

/**selectionArgs code line: It makes the selectionArgs to be similar to the one defined in MA class.
 * It's defined again to prevent cursor from returning unwanted result
 * parseId(uri) will get the rowId appended in MA class and since the method returns long value
 * there's need to convert it to string using String.valueOf() */

/**This class must be declared in the manifest file so that the app will not crash at runtime*/