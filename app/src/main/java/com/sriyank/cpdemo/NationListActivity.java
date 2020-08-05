package com.sriyank.cpdemo;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sriyank.cpdemo.data.NationContract;
import com.sriyank.cpdemo.data.NationContract.NationEntry;

public class NationListActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter simpleCursorAdapter;
    private Cursor cursor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nations);

        //loader initialization
        getSupportLoaderManager().initLoader(10, null, this );

        String[] from = { NationEntry.COLUMN_COUNTRY, NationEntry.COLUMN_CONTINENT };
        int[] to = { R.id.txvCountryName, R.id.txvContinentName };

        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.nation_list_item, null, from, to, 0 );

        ListView listView = findViewById(R.id.listView);
        FloatingActionButton fab = findViewById(R.id.fabInsertData);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String[] projection = {
                NationEntry.COLUMN_COUNTRY,
                NationEntry.COLUMN_CONTINENT
        };

        //returns a CursorLoader object that carries a cursor object
        //The cursor object contains all rows queried from the db using content provider
        return new CursorLoader(this, NationEntry.CONTENT_URI, projection,
                null, null, null );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        SimpleCursorAdapter.swapCursor(cursor);


    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
