package com.labs.jangkriek.carilahan.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.labs.jangkriek.carilahan.POJO.Lokasi;
import com.labs.jangkriek.carilahan.POJO.RankingLokasi;
import com.labs.jangkriek.carilahan.POJO.SavePencarian;

import java.util.ArrayList;
import java.util.List;

public class DbSavePencarian extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;

    private static final String DATABASE_NAME = "save_hasil_db";
    private static final String TABLE_NAME = "save";
    private static final String SAVE_ID = "id";
    private static final String SAVE_ID_GROUP = "id_group";

    private static final String SAVE_NAMA = "nama";
    private static final String SAVE_HARGA_LAHAN = "harga_lahan";
    private static final String SAVE_LUAS_LAHAN = "luas_lahan";

    private static final String SAVE_LONGITUDE = "longitude";
    private static final String SAVE_LATITUDE= "latitude";

    private static final String SAVE_DAYA_DUKUNG_TANAH= "dd_tanah";
    private static final String SAVE_K_AIR = "k_air";
    private static final String SAVE_K_LERENG = "k_lereng";
    private static final String SAVE_AKSEBILITAS = "aksebilitas";
    private static final String SAVE_K_BENCANA = "k_bencana";
    private static final String SAVE_JARAK_BANDARA = "jarak_bandara";

    private static final String SAVE_ID_USER = "id_user";
    private static final String SAVE_GAMBAR = "gambar";
    private static final String SAVE_WAKTU= "waktu";
    private static final String SAVE_METODE= "metode";
    private static final String SAVE_KRITERIA= "kriteria";

    public DbSavePencarian(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + "("
                + SAVE_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SAVE_ID_GROUP +" TEXT,"
                + SAVE_NAMA +" TEXT,"
                + SAVE_HARGA_LAHAN +" DOUBLE,"
                + SAVE_LUAS_LAHAN +" DOUBLE,"
                + SAVE_LATITUDE +" DOUBLE,"
                + SAVE_LONGITUDE +" DOUBLE,"

                + SAVE_DAYA_DUKUNG_TANAH +" TEXT,"
                + SAVE_K_AIR +" TEXT,"
                + SAVE_K_LERENG +" TEXT,"
                + SAVE_AKSEBILITAS +" DOUBLE,"
                + SAVE_K_BENCANA +" TEXT,"
                + SAVE_JARAK_BANDARA +" DOUBLE,"

                + SAVE_ID_USER +" INTEGER,"
                + SAVE_GAMBAR +" TEXT,"
                + SAVE_WAKTU +" DATETIME,"
                + SAVE_METODE +" TEXT,"
                + SAVE_KRITERIA +" TEXT"
                + ")";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertSaveLokasi(String id_group, String namaLokasi, double harga, double luas, double lat, double longi,
                                 String ddTanah, String kAir, String kLereng, double aksebilitas, String kBencana, double jBandara,
                                  int idUser, String gambar, String waktu, String metode, String kriteria) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(SAVE_ID_GROUP, id_group);
        values.put(SAVE_NAMA, namaLokasi);
        values.put(SAVE_LATITUDE, lat);
        values.put(SAVE_LONGITUDE, longi);
        values.put(SAVE_HARGA_LAHAN, harga);
        values.put(SAVE_LUAS_LAHAN, luas);

        values.put(SAVE_DAYA_DUKUNG_TANAH, ddTanah);
        values.put(SAVE_K_AIR, kAir);
        values.put(SAVE_K_LERENG, kLereng);
        values.put(SAVE_AKSEBILITAS, aksebilitas);
        values.put(SAVE_K_BENCANA, kBencana);
        values.put(SAVE_JARAK_BANDARA, jBandara);

        values.put(SAVE_ID_USER, idUser);
        values.put(SAVE_GAMBAR, gambar);
        values.put(SAVE_WAKTU, waktu);
        values.put(SAVE_METODE, metode);
        values.put(SAVE_KRITERIA, kriteria);

        long id = db.insert(TABLE_NAME, null, values);

        db.close();

        return id;
    }

    public List<Lokasi> getDetailSaveLokasi(String idGroup, String waktu) {
        List<Lokasi> saved = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME +
                " WHERE " +
                SAVE_ID_GROUP + "='" + idGroup + "'" +
                " AND " +
                SAVE_WAKTU + "='" + waktu + "'" +
                " ORDER BY " +
                SAVE_WAKTU + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Lokasi save = new Lokasi();

                save.setId(cursor.getInt(cursor.getColumnIndex(SAVE_ID)));
                save.setIdgroup(cursor.getString(cursor.getColumnIndex(SAVE_ID_GROUP)));
                save.setNama(cursor.getString(cursor.getColumnIndex(SAVE_NAMA)));
                save.setHargaLahan(cursor.getDouble(cursor.getColumnIndex(SAVE_HARGA_LAHAN)));
                save.setLuasLahan(cursor.getDouble(cursor.getColumnIndex(SAVE_LUAS_LAHAN)));
                save.setLatitude(cursor.getDouble(cursor.getColumnIndex(SAVE_LATITUDE)));
                save.setLongitude(cursor.getDouble(cursor.getColumnIndex(SAVE_LONGITUDE)));

                save.setDayaDukungTanah(cursor.getString((cursor.getColumnIndex(SAVE_DAYA_DUKUNG_TANAH))));
                save.setKetersediaanAir(cursor.getString((cursor.getColumnIndex(SAVE_K_AIR))));
                save.setKemiringanLereng(cursor.getString((cursor.getColumnIndex(SAVE_K_LERENG))));
                save.setAksebilitas(cursor.getDouble(cursor.getColumnIndex(SAVE_AKSEBILITAS)));
                save.setKerawananBencana(cursor.getString((cursor.getColumnIndex(SAVE_K_BENCANA))));
                save.setJarakKeBandara(cursor.getDouble((cursor.getColumnIndex(SAVE_JARAK_BANDARA))));

                save.setId_user(cursor.getInt(cursor.getColumnIndex(SAVE_ID_USER)));
                save.setGambar(cursor.getString((cursor.getColumnIndex(SAVE_GAMBAR))));
                save.setWaktu(cursor.getString((cursor.getColumnIndex(SAVE_WAKTU))));
                save.setMetode(cursor.getString((cursor.getColumnIndex(SAVE_METODE))));
                save.setKriteria(cursor.getString((cursor.getColumnIndex(SAVE_KRITERIA))));

                saved.add(save);
            } while (cursor.moveToNext());
        }

        db.close();

        return saved;
    }

    public List<Lokasi> getAllSaveLokasi() {
        List<Lokasi> saved = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " +
                SAVE_ID_GROUP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Lokasi save = new Lokasi();

                save.setId(cursor.getInt(cursor.getColumnIndex(SAVE_ID)));
                save.setWaktu(cursor.getString(cursor.getColumnIndex(SAVE_WAKTU)));
                save.setIdgroup(cursor.getString(cursor.getColumnIndex(SAVE_ID_GROUP)));
                save.setMetode(cursor.getString((cursor.getColumnIndex(SAVE_METODE))));
                save.setKriteria(cursor.getString((cursor.getColumnIndex(SAVE_KRITERIA))));

                saved.add(save);
            } while (cursor.moveToNext());
        }

        db.close();

        return saved;
    }

    public List<Lokasi> getDataByIdGroup(String idGroup) {
        List<Lokasi> saved = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + SAVE_ID_GROUP + "='" + idGroup + "'" + " ORDER BY " +
                SAVE_ID_GROUP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Lokasi save = new Lokasi();

                save.setId(cursor.getInt(cursor.getColumnIndex(SAVE_ID)));
                save.setWaktu(cursor.getString(cursor.getColumnIndex(SAVE_WAKTU)));
                save.setIdgroup(cursor.getString(cursor.getColumnIndex(SAVE_ID_GROUP)));

                saved.add(save);
            } while (cursor.moveToNext());
        }

        db.close();

        return saved;
    }

    //

    public int getRankLokasiCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count/3;
    }

    public void deleteLokasi(String idGroup) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,SAVE_ID_GROUP + " = ?",
                new String[]{String.valueOf(idGroup)});
        db.close();
    }

}
