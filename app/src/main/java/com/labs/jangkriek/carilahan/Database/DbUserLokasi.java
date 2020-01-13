package com.labs.jangkriek.carilahan.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.labs.jangkriek.carilahan.POJO.Lokasi;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DbUserLokasi extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;

    private static final String DATABASE_NAME = "lokasi_user_db";
    private static final String TABLE_NAME = "latlong";
    private static final String KOLOM_ID = "id";
    private static final String KOLOM_NAMA = "nama";
    private static final String KOLOM_LONGITUDE = "longitude";
    private static final String KOLOM_LATITUDE= "latitude";
    private static final String KOLOM_STATUS= "status";

    private static final String KOLOM_DAYA_DUKUNG_TANAH = "daya_dukung_tanah";
    private static final String KOLOM_KETERSEDIAAN_AIR = "ketersediaan_lahan";
    private static final String KEMIRINGAN_LERENG = "kemiringan_lahan";
    private static final String AKSEBILITAS = "aksebilitas";
    private static final String PERUBAHAN_LAHAN = "perubahan_lahan";
    private static final String KERAWANAN_BENCANA = "kerawanan_bencana";
    private static final String JARAK_KE_BANDARA = "jarak_bandara";
    private static final String GAMBAR = "gambar";

    public DbUserLokasi(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + "("
                + KOLOM_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KOLOM_NAMA +" TEXT,"
                + KOLOM_LATITUDE +" DOUBLE,"
                + KOLOM_LONGITUDE +" DOUBLE,"
                + KOLOM_DAYA_DUKUNG_TANAH +" DOUBLE,"
                + KOLOM_KETERSEDIAAN_AIR +" DOUBLE,"
                + KEMIRINGAN_LERENG +" DOUBLE,"
                + AKSEBILITAS +" DOUBLE,"
                + PERUBAHAN_LAHAN +" DOUBLE,"
                + KERAWANAN_BENCANA +" DOUBLE,"
                + JARAK_KE_BANDARA +" DOUBLE,"
                + KOLOM_STATUS +" TINYINT, "
                + GAMBAR +" BLOB"
                + ")";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertLokasi(String namaLokasi, double lat, double longi,
                             double ddTanah, double kAir, double kLereng, double aksebilitas,
                             double pLahan, double kBencana, double jBandara, int a, Bitmap bitmap) {
        // get writable database as we want to write data

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them

        values.put(KOLOM_NAMA, namaLokasi);
        values.put(KOLOM_LATITUDE, lat);
        values.put(KOLOM_LONGITUDE, longi);
        values.put(KOLOM_DAYA_DUKUNG_TANAH, ddTanah);
        values.put(KOLOM_KETERSEDIAAN_AIR, kAir);
        values.put(KEMIRINGAN_LERENG, kLereng);
        values.put(AKSEBILITAS, aksebilitas);
        values.put(PERUBAHAN_LAHAN, pLahan);
        values.put(KERAWANAN_BENCANA, kBencana);
        values.put(JARAK_KE_BANDARA, jBandara);
        values.put(KOLOM_STATUS, a);
        values.put(GAMBAR, getPictureByteOfArray(bitmap));

        // insert row
        long id = db.insert(TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    /*public Lokasi getLokasi(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{KOLOM_ID, KOLOM_NAMA, KOLOM_LATITUDE, KOLOM_LONGITUDE,
                        KOLOM_DAYA_DUKUNG_TANAH, KOLOM_KETERSEDIAAN_AIR, KEMIRINGAN_LERENG, AKSEBILITAS,
                        PERUBAHAN_LAHAN, KERAWANAN_BENCANA, JARAK_KE_BANDARA, KOLOM_STATUS, GAMBAR},
                KOLOM_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        byte[] image = cursor.getBlob(cursor.getColumnIndex(GAMBAR));
        Lokasi note = new Lokasi(
                cursor.getInt(cursor.getColumnIndex(KOLOM_ID)),
                cursor.getString(cursor.getColumnIndex(KOLOM_NAMA)),
                cursor.getDouble(cursor.getColumnIndex(KOLOM_LATITUDE)),
                cursor.getDouble(cursor.getColumnIndex(KOLOM_LONGITUDE)),
                cursor.getDouble(cursor.getColumnIndex(KOLOM_DAYA_DUKUNG_TANAH)),
                cursor.getDouble(cursor.getColumnIndex(KOLOM_KETERSEDIAAN_AIR)),
                cursor.getDouble(cursor.getColumnIndex(KEMIRINGAN_LERENG)),
                cursor.getDouble(cursor.getColumnIndex(AKSEBILITAS)),
                cursor.getDouble(cursor.getColumnIndex(PERUBAHAN_LAHAN)),
                cursor.getDouble(cursor.getColumnIndex(KERAWANAN_BENCANA)),
                cursor.getDouble(cursor.getColumnIndex(JARAK_KE_BANDARA)),
                cursor.getInt(cursor.getColumnIndex(KOLOM_STATUS)),
                getImage(image)
                );

        // close the db connection
        cursor.close();

        return note;
    }*/

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    /*public List<Lokasi> getAllLokasi() {
        List<Lokasi> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " +
                KOLOM_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Lokasi note = new Lokasi();
                note.setId(cursor.getInt(cursor.getColumnIndex(KOLOM_ID)));
                note.setUsername(cursor.getString(cursor.getColumnIndex(KOLOM_NAMA)));
                note.setLatitude(cursor.getDouble(cursor.getColumnIndex(KOLOM_LATITUDE)));
                note.setLongitude(cursor.getDouble(cursor.getColumnIndex(KOLOM_LONGITUDE)));
                note.setDayaDukungTanah(cursor.getDouble(cursor.getColumnIndex(KOLOM_DAYA_DUKUNG_TANAH)));
                note.setKetersediaanAir(cursor.getDouble(cursor.getColumnIndex(KOLOM_KETERSEDIAAN_AIR)));
                note.setKemiringanLereng(cursor.getDouble(cursor.getColumnIndex(KEMIRINGAN_LERENG)));
                note.setAksebilitas(cursor.getDouble(cursor.getColumnIndex(AKSEBILITAS)));
                note.setPerubahanLahan(cursor.getDouble(cursor.getColumnIndex(PERUBAHAN_LAHAN)));
                note.setKerawananBencana(cursor.getDouble(cursor.getColumnIndex(KERAWANAN_BENCANA)));
                note.setJarakKeBandara(cursor.getDouble(cursor.getColumnIndex(JARAK_KE_BANDARA)));
                note.setStatus(cursor.getInt(cursor.getColumnIndex(KOLOM_STATUS)));
                byte[] image = cursor.getBlob(cursor.getColumnIndex(GAMBAR));
                note.setBitmap(getImage(image));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }*/

    public int getLokasiCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    //update nama lokasi
    public int updateLokasi(Lokasi lokasi) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KOLOM_NAMA, lokasi.getNama());

        // updating row
        return db.update(TABLE_NAME, values, KOLOM_ID + " = ?",
                new String[]{String.valueOf(lokasi.getId())});
    }

    //update status lokasi
    public int updateLokasiStatus(Lokasi lokasi) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KOLOM_STATUS, lokasi.getStatus());

        // updating row
        return db.update(TABLE_NAME, values, KOLOM_ID + " = ?",
                new String[]{String.valueOf(lokasi.getId())});
    }

    public void deleteLokasi() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    public List<Lokasi> getLatLongLokasi() {
        List<Lokasi> loc = new ArrayList<>();
        // Select All Query
        String selectQueryLat = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQueryLat, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Lokasi temp = new Lokasi();
                temp.setLatitude(cursor.getDouble(cursor.getColumnIndex(KOLOM_LATITUDE)));
                temp.setLongitude(cursor.getDouble(cursor.getColumnIndex(KOLOM_LONGITUDE)));
                loc.add(temp);
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();

        // return notes list
        return loc;
    }

    /*public List<Lokasi> getDataForRank() {
        List<Lokasi> loc = new ArrayList<>();
        // Select All Query
        String selectQueryLat = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQueryLat, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Lokasi temp = new Lokasi();
                temp.setId(cursor.getInt(cursor.getColumnIndex(KOLOM_ID)));
                temp.setUsername(cursor.getString(cursor.getColumnIndex(KOLOM_NAMA)));
                temp.setLatitude(cursor.getDouble(cursor.getColumnIndex(KOLOM_LATITUDE)));
                temp.setLongitude(cursor.getDouble(cursor.getColumnIndex(KOLOM_LONGITUDE)));
                temp.setDayaDukungTanah(cursor.getDouble(cursor.getColumnIndex(KOLOM_DAYA_DUKUNG_TANAH)));
                temp.setKetersediaanAir(cursor.getDouble(cursor.getColumnIndex(KOLOM_KETERSEDIAAN_AIR)));
                temp.setKemiringanLereng(cursor.getDouble(cursor.getColumnIndex(KEMIRINGAN_LERENG)));
                temp.setAksebilitas(cursor.getDouble(cursor.getColumnIndex(AKSEBILITAS)));
                temp.setPerubahanLahan(cursor.getDouble(cursor.getColumnIndex(PERUBAHAN_LAHAN)));
                temp.setKerawananBencana(cursor.getDouble(cursor.getColumnIndex(KERAWANAN_BENCANA)));
                temp.setJarakKeBandara(cursor.getDouble(cursor.getColumnIndex(JARAK_KE_BANDARA)));
                loc.add(temp);
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();

        // return notes list
        return loc;
    }*/

    private static byte[] getPictureByteOfArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
