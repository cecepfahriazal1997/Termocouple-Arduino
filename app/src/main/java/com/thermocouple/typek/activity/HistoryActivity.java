package com.thermocouple.typek.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_record);

        toolbar             = findViewById(R.id.toolbar);
        listView            = findViewById(R.id.listview);
        swipeRefreshLayout  = findViewById(R.id.swipe_refresh_layout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Data Record");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialData();
    }

    private void initialData() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        });
    }

    private void loadData() {
        functionHelper.showLoadingSwipeRefreshLayout(swipeRefreshLayout, true);
        functionHelper.setEnabledSwipeRefreshListView(swipeRefreshLayout, listView, "normal");
        adapter = new HistoryRecordAdapter(HistoryActivity.this, list);
        listView.setAdapter(adapter);
        list.clear();
        list.addAll(databaseHelper.getAllRecord());
        adapter.notifyDataSetChanged();
        functionHelper.showLoadingSwipeRefreshLayout(swipeRefreshLayout, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}