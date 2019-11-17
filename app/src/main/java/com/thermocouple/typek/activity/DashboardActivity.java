package com.thermocouple.typek.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.thermocouple.typek.R;
import com.thermocouple.typek.adapter.RecordAdapter;
import com.thermocouple.typek.model.RecordModel;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends MasterActivity {
    private List<RecordModel> list = new ArrayList<RecordModel>();
    private RecordAdapter adapter;
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
        setContentView(R.layout.activity_dashboard);

        listView            = findViewById(R.id.listview);

        initialData();
    }

    private void initialData() {
        adapter = new RecordAdapter(DashboardActivity.this, list);
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