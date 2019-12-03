package com.thermocouple.typek.other;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.thermocouple.typek.model.RecordModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "thermocouple";
    private String table = "record";
    private String columnOne = "temperature";
    private String columnTwo = "date";
    private String createTable =
            "CREATE TABLE " + table + "("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + columnOne + " TEXT,"
                    + columnTwo + " TEXT"
                    + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table);
        onCreate(db);
    }

    public RecordModel getRecord(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(table,
                new String[]{"id", columnOne, columnTwo},
                "id=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        RecordModel recordModel = new RecordModel();
        recordModel.setId(String.valueOf(cursor.getInt(cursor.getColumnIndex("id"))));
        recordModel.setTemperature(cursor.getString(cursor.getColumnIndex(columnOne)));
        recordModel.setDate(cursor.getString(cursor.getColumnIndex(columnTwo)));

        cursor.close();
        return recordModel;
    }

    public List<RecordModel> getAllRecord() {
        List<RecordModel> notes = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + table + " ORDER BY " +
                columnTwo + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                RecordModel recordModel = new RecordModel();
                recordModel.setId(String.valueOf(cursor.getInt(cursor.getColumnIndex("id"))));
                recordModel.setTemperature(cursor.getString(cursor.getColumnIndex(columnOne)));
                recordModel.setDate(cursor.getString(cursor.getColumnIndex(columnTwo)));

                notes.add(recordModel);
            } while (cursor.moveToNext());
        }

        db.close();
        return notes;
    }

    public int getRecordCount() {
        String countQuery = "SELECT  * FROM " + table;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public long insertRecord(String temperature, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(columnOne, temperature);
        values.put(columnTwo, date);
        long id = db.insert(table, null, values);
        db.close();
        return id;
    }

    public int updateRecord(RecordModel recordModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(columnOne, recordModel.getTemperature());
        values.put(columnTwo, recordModel.getDate());

        return db.update(table, values, "id = ?",
                new String[]{String.valueOf(recordModel.getId())});
    }

    public void deleteRecord(RecordModel recordModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, "id = ?",
                new String[]{String.valueOf(recordModel.getId())});
        db.close();
    }

    public void deleteRecordAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ table);
        db.close();
    }
}