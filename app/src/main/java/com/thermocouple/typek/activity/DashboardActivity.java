package com.thermocouple.typek.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.AsyncHttpRequest;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.thermocouple.typek.R;
import com.thermocouple.typek.adapter.RecordAdapter;
import com.thermocouple.typek.model.RecordModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import mehdi.sakout.fancybuttons.FancyButton;

public class DashboardActivity extends MasterActivity {
    private FancyButton seeMore;
    private List<RecordModel> list = new ArrayList<RecordModel>();
    private RecordAdapter adapter;
    private ListView listView;
    private TextView date, time, temperature;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
    private SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
    private ScheduledExecutorService scheduleTaskExecutor;
    private CheckBox status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        seeMore             = findViewById(R.id.btnSeeMore);
        listView            = findViewById(R.id.listview);
        date                = findViewById(R.id.date);
        time                = findViewById(R.id.time);
        temperature         = findViewById(R.id.temperature);
        status              = findViewById(R.id.status);

        initialData();
    }

    private void initialData() {
        scheduleTaskExecutor = Executors.newScheduledThreadPool(1);
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setDate();
                        fetchData();
                    }
                });
            }
        }, 0, 1, TimeUnit.SECONDS);

        seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                functionHelper.startIntent(HistoryActivity.class, false, false, null);
            }
        });
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                functionHelper.startIntent(SettingRecordActivity.class, false, false, null);
            }
        });
    }

    private void setDate() {
        long millis             = System.currentTimeMillis();
        java.sql.Date tempDate  = new java.sql.Date(millis);
        date.setText(formatter.format(tempDate));
        time.setText(formatter2.format(tempDate));
    }

    private void setData() {
        adapter = new RecordAdapter(DashboardActivity.this, list);
        listView.setAdapter(adapter);
        list.clear();
        list.addAll(databaseHelper.getAllRecord());
        adapter.notifyDataSetChanged();
        if (list != null && list.size() > 5)
            seeMore.setVisibility(View.VISIBLE);
    }

    public void fetchData() {
        try {
            if (functionHelper.isNetworkAvailable()) {
                Ion.with(this).load(urlRecord)
                    .setTimeout(AsyncHttpRequest.DEFAULT_TIMEOUT)
                    .setLogging("Data Record", Log.DEBUG)
                    .noCache()
                    .asString()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<String>>() {
                        @Override
                        public void onCompleted(Exception e, Response<String> result) {
                            try {
                                if (e == null) {
                                    if (result.getHeaders().code() == 200) {
                                        JSONObject json = new JSONObject(result.getResult());
                                        JSONArray data  = json.getJSONArray("feeds");
                                        JSONObject detail = data.getJSONObject(0);
//                                        databaseHelper.deleteRecordAll();
                                        databaseHelper.insertRecord(String.valueOf(
                                                Double.parseDouble(detail.getString("field1"))),
                                                detail.getString("created_at").replace("T", " ")
                                                        .substring(0, detail.getString("created_at").length() - 1));
                                        setData();
                                        temperature.setText(Double.parseDouble(detail.getString("field1")) + "");
                                    } else {
                                        Log.e("Opps", "Data tidak ditemukan pengambilan data gagal");
                                    }
                                } else {
                                    Log.e("Opps", "Data tidak ditemukan");
                                }
                            } catch (Exception ex) {
                                Log.e("Exception ", "exception", ex);
                            }
                        }
                    });
            } else {
                Log.e("Opps", "Data tidak ditemukan, tidak ada koneksi");
            }
        } catch (Exception e) {
            Log.e("Exception ", "Exception", e);
        }
    }
}