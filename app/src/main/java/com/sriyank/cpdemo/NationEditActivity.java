package com.sriyank.cpdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NationEditActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = NationEditActivity.class.getSimpleName();

    private EditText etCountry, etContinent;
    private Button btnUpdate, btnDelete, btnInsert;

    int _id;
    String country, continent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nations_edit);

        etCountry = findViewById(R.id.etCountry);
        etContinent = findViewById(R.id.etContinent);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnInsert = findViewById(R.id.btnInsert);

        Intent intent = getIntent();
        _id = intent.getIntExtra("_id", 0);
        country = intent.getStringExtra("country");
        continent = intent.getStringExtra("continent");

        if (_id != 0) {		// We want to delete or update data
            etCountry.setText(country);
            etContinent.setText(continent);
            btnInsert.setVisibility(View.GONE);
        } else {			// We want to insert data
            btnUpdate.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }

        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnInsert.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnUpdate:
                update();
                break;

            case R.id.btnDelete:
                delete();
                break;

            case R.id.btnInsert:
                insert();
                break;
        }

    }

    private void update() {

    }

    private void delete() {

    }

    private void insert() {

    }
}
