package com.labs.jangkriek.carilahan.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.labs.jangkriek.carilahan.POJO.RankingLokasi;
import com.labs.jangkriek.carilahan.POJO.SavePencarian;

import java.util.ArrayList;
import java.util.List;

public class DbSavePencarian extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "save_hasil_db";
    private static final String TABLE_NAME = "save";
    private static final String SAVE_ID = "id";
    private static final String SAVE_ID_GROUP = "id_group";
    private static final String SAVE_NAMA = "nama";
    private static final String SAVE_LONGITUDE = "longitude";
    private static final String SAVE_LATITUDE= "latitude";
    private static final String SAVE_WAKTU= "waktu";

    public DbSavePencarian(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + "("
                + SAVE_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SAVE_ID_GROUP +" INTEGER,"
                + SAVE_NAMA +" TEXT,"
                + SAVE_LATITUDE +" DOUBLE,"
                + SAVE_LONGITUDE +" DOUBLE,"
                + SAVE_WAKTU +" DATETIME"
                + ")";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertSaveLokasi(int id_group, String namaLokasi, double lat, double longi, String waktu) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(SAVE_ID_GROUP, id_group);
        values.put(SAVE_NAMA, namaLokasi);
        values.put(SAVE_LATITUDE, lat);
        values.put(SAVE_LONGITUDE, longi);
        values.put(SAVE_WAKTU, waktu);

        long id = db.insert(TABLE_NAME, null, values);

        db.close();

        return id;
    }

    public List<SavePencarian> getAllSaveLokasi() {
        List<SavePencarian> saved = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " +
                SAVE_ID_GROUP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SavePencarian save = new SavePencarian();
                save.setId(cursor.getInt(cursor.getColumnIndex(SAVE_ID)));
                save.setIdGroup(cursor.getInt(cursor.getColumnIndex(SAVE_ID_GROUP)));
                save.setNama(cursor.getString(cursor.getColumnIndex(SAVE_NAMA)));
                save.setLatitude(cursor.getDouble(cursor.getColumnIndex(SAVE_LATITUDE)));
                save.setLongitude(cursor.getDouble(cursor.getColumnIndex(SAVE_LONGITUDE)));
                save.setWaktu(cursor.getString((cursor.getColumnIndex(SAVE_WAKTU))));

                saved.add(save);
            } while (cursor.moveToNext());
        }

        db.close();

        return saved;
    }

    public int getRankLokasiCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public List<RankingLokasi> getLatLongSaveLokasi() {
        List<RankingLokasi> loc = new ArrayList<>();

        String selectQueryLat = ("SELECT * FROM " + TABLE_NAME);
        //db.rawQuery("select * from " + DATABASE_TABLE_EHS + " where " + TASK_ID + "='" + taskid + "'" , null);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQueryLat, null);

        if (cursor.moveToFirst()) {
            do {
                RankingLokasi temp = new RankingLokasi();
                temp.setLatitude(cursor.getDouble(cursor.getColumnIndex(SAVE_LATITUDE)));
                temp.setLongitude(cursor.getDouble(cursor.getColumnIndex(SAVE_LONGITUDE)));
                loc.add(temp);
            } while (cursor.moveToNext());
        }
        db.close();

        return loc;
    }

}
