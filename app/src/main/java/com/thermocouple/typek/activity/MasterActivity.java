package com.thermocouple.typek.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.thermocouple.typek.other.DatabaseHelper;
import com.thermocouple.typek.other.FunctionHelper;

public class MasterActivity extends AppCompatActivity {
    public FunctionHelper functionHelper;
    public DatabaseHelper databaseHelper;
    public String urlRecord = "https://api.thingspeak.com/channels/924206/feeds.json?results=2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        functionHelper      = new FunctionHelper(this);
        databaseHelper      = new DatabaseHelper(this);
    }
}
