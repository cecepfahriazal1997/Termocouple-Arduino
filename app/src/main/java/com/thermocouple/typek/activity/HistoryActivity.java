package com.thermocouple.typek.activity;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;

import com.thermocouple.typek.R;
import com.thermocouple.typek.adapter.HistoryRecordAdapter;
import com.thermocouple.typek.model.RecordModel;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends MasterActivity {
    private Toolbar toolbar;
    private List<RecordModel> list = new ArrayList<RecordModel>();
    private HistoryRecordAdapter adapter;
    private ListView listView;
    private String date[] = {
            "28 Desember 2019 09:10:00",
            "28 Desember 2019 10:00:00",
            "28 Desember 2019 12:00:00",
            "28 Desember 2019 14:00:00",
            "28 Desember 2019 17:00:00"
    };

    private String celcius[] = {
            "35",
            "25",
            "90.8",
            "100.5",
            "44"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_record);

        toolbar             = findViewById(R.id.toolbar);
        listView            = findViewById(R.id.listview);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Data Record");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialData();
    }

    private void initialData() {
        adapter = new HistoryRecordAdapter(HistoryActivity.this, list);
        listView.setAdapter(adapter);
        list.clear();

        for (int i = 0; i < date.length; i++) {
            RecordModel model = new RecordModel();
            model.setId("" + i);
            model.setDate(date[i]);
            model.setTemperature(celcius[i]);
            list.add(model);
        }

        adapter.notifyDataSetChanged();
    }
}