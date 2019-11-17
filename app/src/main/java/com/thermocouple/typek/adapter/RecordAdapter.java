package com.thermocouple.typek.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.mikepenz.iconics.view.IconicsImageView;
import com.thermocouple.typek.R;
import com.thermocouple.typek.model.RecordModel;

import java.util.List;
/**
 * Created by Cecep Rokani on 3/18/2019.
 */

public class RecordAdapter extends BaseAdapter {

    private AppCompatActivity activity;
    private LayoutInflater inflater;
    private List<RecordModel> items;
    private RecordModel item;

    ViewHolder holder;

    public RecordAdapter(AppCompatActivity activity, List<RecordModel> items) {
        this.activity = activity;
        this.items = items;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null) {
            convertView         = inflater.inflate(R.layout.list_record, null);
            holder              = new ViewHolder();
            holder.Time         = convertView.findViewById(R.id.time);
            holder.Number       = convertView.findViewById(R.id.number);
            holder.Symbol       = convertView.findViewById(R.id.symbol);
            holder.Celcius      = convertView.findViewById(R.id.celcius);
            holder.Alarm        = convertView.findViewById(R.id.alarm);
            holder.Arrow        = convertView.findViewById(R.id.arrow);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RecordModel item = items.get(position);

        holder.Number.setText(item.getTemperature());
        holder.Time.setText(item.getDate());

        holder.Number.setTextColor(ContextCompat.getColor(activity, R.color.white));
        holder.Time.setTextColor(ContextCompat.getColor(activity, R.color.white));
        holder.Symbol.setTextColor(ContextCompat.getColor(activity, R.color.white));
        holder.Celcius.setTextColor(ContextCompat.getColor(activity, R.color.white));
        holder.Alarm.setColorFilter(R.color.white, PorterDuff.Mode.SRC_IN);
        holder.Arrow.setColorFilter(R.color.white, PorterDuff.Mode.SRC_IN);

        return convertView;
    }

    static class ViewHolder {
        TextView Time, Number, Symbol, Celcius;
        IconicsImageView Alarm, Arrow;
    }

    public List<RecordModel> getData() {
        return items;
    }
}