package com.thermocouple.typek.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.thermocouple.typek.other.FunctionHelper;

public class MasterActivity extends AppCompatActivity {
    public FunctionHelper functionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        functionHelper      = new FunctionHelper(this);
    }
}
