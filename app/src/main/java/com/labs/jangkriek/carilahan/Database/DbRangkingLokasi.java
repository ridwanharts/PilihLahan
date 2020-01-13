package com.labs.jangkriek.carilahan.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.labs.jangkriek.carilahan.POJO.RankingLokasi;

import java.util.ArrayList;
import java.util.List;

public class DbRangkingLokasi extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;

    private static final String DATABASE_NAME = "rangking_lokasi_db";
    private static final String TABLE_NAME = "rangking";
    private static final String RANK_ID = "id";
    private static final String RANK_ID_GROUP = "id_group";
    private static final String RANK_NAMA = "nama";
    private static final String RANK_LONGITUDE = "longitude";
    private static final String RANK_LATITUDE= "latitude";
    private static final String RANK_JUMLAH= "jumlah";
    private static final String RANK_WAKTU= "waktu";

    public DbRangkingLokasi(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + "("
                + RANK_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RANK_ID_GROUP +" INTEGER,"
                + RANK_NAMA +" TEXT,"
                + RANK_LATITUDE +" DOUBLE,"
                + RANK_LONGITUDE +" DOUBLE,"
                + RANK_JUMLAH +" DOUBLE,"
                + RANK_WAKTU +" DATETIME"
                + ")";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertRankLokasi(int id_group, String namaLokasi, double lat, double longi, double jumlah, String waktu) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(RANK_ID_GROUP, id_group);
        values.put(RANK_NAMA, namaLokasi);
        values.put(RANK_LATITUDE, lat);
        values.put(RANK_LONGITUDE, longi);
        values.put(RANK_JUMLAH, jumlah);
        values.put(RANK_WAKTU, waktu);

        long id = db.insert(TABLE_NAME, null, values);

        db.close();

        return id;
    }

    public List<RankingLokasi> getAllRankLokasi(int idgroup, String waktu) {
        List<RankingLokasi> rank = new ArrayList<>();
/*
        + RANK_ID_GROUP + "='" + idgroup + "' AND "
        + RANK_WAKTU + "='" + waktu + "'"
*/
        String selectQuery = ("SELECT * FROM " + TABLE_NAME
                + " WHERE " + RANK_ID_GROUP + "='" + idgroup + "'" + " AND "
                + RANK_WAKTU + "='" + waktu + "'"
                + " ORDER BY " + RANK_JUMLAH + " DESC"
        );

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                RankingLokasi note = new RankingLokasi();
                note.setId(cursor.getInt(cursor.getColumnIndex(RANK_ID)));
                note.setIdGroup(cursor.getInt(cursor.getColumnIndex(RANK_ID_GROUP)));
                note.setNama(cursor.getString(cursor.getColumnIndex(RANK_NAMA)));
                note.setLatitude(cursor.getDouble(cursor.getColumnIndex(RANK_LATITUDE)));
                note.setLongitude(cursor.getDouble(cursor.getColumnIndex(RANK_LONGITUDE)));
                note.setJumlah(cursor.getDouble(cursor.getColumnIndex(RANK_JUMLAH)));
                note.setWaktu(cursor.getString(cursor.getColumnIndex(RANK_WAKTU)));

                rank.add(note);
            } while (cursor.moveToNext());
        }

        db.close();

        return rank;
    }

    public List<RankingLokasi> getLatLongRankLokasi(int idgroup) {
        List<RankingLokasi> loc = new ArrayList<>();

        String selectQueryLat = ("SELECT * FROM " + TABLE_NAME + " WHERE " + RANK_ID_GROUP + "='" + idgroup + "'");
        //db.rawQuery("select * from " + DATABASE_TABLE_EHS + " where " + TASK_ID + "='" + taskid + "'" , null);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQueryLat, null);

        if (cursor.moveToFirst()) {
            do {
                RankingLokasi temp = new RankingLokasi();
                temp.setLatitude(cursor.getDouble(cursor.getColumnIndex(RANK_LATITUDE)));
                temp.setLongitude(cursor.getDouble(cursor.getColumnIndex(RANK_LONGITUDE)));
                loc.add(temp);
            } while (cursor.moveToNext());
        }
        db.close();

        return loc;
    }

    public int getRankLokasiCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }
}
