package com.sriyank.cpdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sriyank.cpdemo.data.NationDbHelper;
import com.sriyank.cpdemo.data.NationContract.NationEntry;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etCountry, etContinent, etWhereToUpdate, etNewContinent, etWhereToDelete, etQueryRowById;
    private Button btnInsert, btnUpdate, btnDelete, btnQueryByRowId, btnDisplayAll;

    private static final String TAG = MainActivity.class.getSimpleName();

    private SQLiteDatabase database;
    private NationDbHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCountry = findViewById(R.id.etCountry);
        etContinent = findViewById(R.id.etContinent);
        etWhereToUpdate = findViewById(R.id.etWhereToUpdate);
        etNewContinent = findViewById(R.id.etUpdateContinent);
        etQueryRowById = findViewById(R.id.etQueryByRowId);
        etWhereToDelete = findViewById(R.id.etWhereToDelete);

        btnInsert = findViewById(R.id.btnInsert);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnQueryByRowId = findViewById(R.id.btnQueryByID);
        btnDisplayAll = findViewById(R.id.btnDisplayAll);

        btnInsert.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnQueryByRowId.setOnClickListener(this);
        btnDisplayAll.setOnClickListener(this);

        dataBaseHelper = new NationDbHelper(this);
        database = dataBaseHelper.getWritableDatabase(); //connect to SQLite db (perform read/write operation)

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())  {

            case R.id.btnInsert:
                insert();
                break;

            case R.id.btnUpdate:
                update();
                break;

            case R.id.btnDelete:
                delete();
                break;

            case R.id.btnDisplayAll:
                queryAndDisplayAll();
                break;

            case R.id.btnQueryByID:
                queryRowById();
                break;
        }

    }

    private void insert() {
        //get user input
        String countryName = etCountry.getText().toString();
        String continentName = etContinent.getText().toString();
        //insert the above info to database. ContentValue accept values in form of key(column name) value(column value) pair
        ContentValues contentValues = new ContentValues();
        contentValues.put(NationEntry.COLUMN_COUNTRY, countryName);
        contentValues.put(NationEntry.COLUMN_CONTINENT, continentName);

        //insert the above content values to the NationEntry table
        //rowId is the value of the _ID column
        long rowId = database.insert(NationEntry.TABLE_NAME, null, contentValues);
        //display mgs on logcat when a new row is inserted
        Log.i(TAG, "item inserted with row id" + rowId);


    }

    private void update() {

        String newContinent = etNewContinent.getText().toString();  //enables user to enter newContinent
        String whereCountry = etCountry.getText().toString();   //enables user to enter the country that its continent should be updated

        String selection = NationEntry.COLUMN_COUNTRY + " = ?";
        String[] selectionArgs = { whereCountry };

        ContentValues contentValues = new ContentValues();
        contentValues.put(NationEntry.COLUMN_CONTINENT, newContinent);  //update continent column based on the newContinent provided by user

        int rowsUpdated = database.update(NationEntry.TABLE_NAME, contentValues, selection, selectionArgs); //update db: returns integer value (rowsUpdate)
        Log.i(TAG, "Numbers of rows updated" + rowsUpdated);

    }

    private void delete() {

        String countryName = etWhereToDelete.getText().toString();

        String selection = NationEntry.COLUMN_COUNTRY + " = ? "; // the selection can be set based on any column name including the _id
        String[] selectionArgs = {countryName};

        int rowsDeleted = database.delete(NationEntry.TABLE_NAME, selection, selectionArgs);
        Log.i(TAG, "Number of rows deleted" + rowsDeleted);

    }
    private void queryRowById() {
        //since selectionArgs accepts string value there's need to convert rowId to string
        String rowId = etQueryRowById.getText().toString();
        //columns expected to be returned by the query
        String[] projection = {
                NationEntry._ID,
                NationEntry.COLUMN_COUNTRY,
                NationEntry.COLUMN_CONTINENT
        };
        String[] selectionArgs = {rowId};   //filter database based on rowId which will be entered by users
        // i.e. "_id = ?" of the selection parameter will be changed(?) to whatever id user insert

        //Cursor is an object that contains the reference of all the rows - the above columns will be present in these rows
        //it enables the output of database values
        Cursor cursor = database.query(NationEntry.TABLE_NAME, projection, NationEntry._ID + " = ?",
                selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToNext()) {   //if cursor is not null, move to next row and output the single row requested by the user

            String str = "";
                String[] columns = cursor.getColumnNames();    //retrieve column names
                for (String column : columns)  {   //iterate through all columns
                    str += "\t" + cursor.getString(cursor.getColumnIndex(column));
                }
                str += "\n";

            cursor.close();    //closing cursor prevent memory leakage and it improves the app performance
            Log.i(TAG, str);    //print the value of str on the logcat
        }

    }
    private void queryAndDisplayAll() {
        //columns expected to be returned by the query
        String[] projection = {
             NationEntry._ID,
             NationEntry.COLUMN_COUNTRY,
             NationEntry.COLUMN_CONTINENT
        };
        //Cursor is an object that contains the reference of all the rows - the above columns will be present in these rows
         //it enables the output of database values
        Cursor cursor = database.query(NationEntry.TABLE_NAME, projection, null,
                null, null, null, null);

         if (cursor != null) {          //check if cursor is not null . . .
             String str = "";
             while (cursor.moveToNext()) {      //. . . iterate through all the rows
                 String[] columns = cursor.getColumnNames();    //retrieve column names
                 for (String column : columns)  {   //iterate through all columns
                     str += "\t" + cursor.getString(cursor.getColumnIndex(column));
                 }
                 str += "\n";
             }
             cursor.close();    //closing cursor prevent memory leakage and it improves the app performance
            Log.i(TAG, str);    //print the value of str on the logcat
         }
    }

    @Override
    protected void onDestroy() {
        //close the connection between db and MainActivity
        database.close();
        super.onDestroy();
    }
}


/** db.query() returns a cursor object when executed which in turn contains the reference to all the rows.
 * Each row returned by the method can be accessed using a specific index starting from zero
 * but when the method returns a cursor object, this object will have a current position of -1 instead of 0
 * that's why moveToNext() is called to move the cursor to the starting point of the row index(0) as well other rows*/