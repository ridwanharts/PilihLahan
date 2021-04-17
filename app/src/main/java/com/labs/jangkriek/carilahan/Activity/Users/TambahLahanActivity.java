package com.labs.jangkriek.carilahan.Activity.Users;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.labs.jangkriek.carilahan.POJO.Respon;
import com.labs.jangkriek.carilahan.PrefConfig;
import com.labs.jangkriek.carilahan.R;
import com.labs.jangkriek.carilahan.Utils.RegisterApi;
import com.labs.jangkriek.carilahan.Utils.Utils;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TambahLahanActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView tvLatitude, tvLongitude, tvPlotArea, tvJarakBandara, tvAksebilitas;
    private double lat, longi;
    private ImageView ivPlotArea, imageView1, imageView2, imageView3;
    private List<Point> fillLayerPointList = new ArrayList<>();
    //private Bitmap imageBitmap;
    private ImageView pilihLokasi;
    private Button btnKirimDataKeServer, btnLihatJenisTanah, btnLihatKair, btnLihatKlereng, btnLihatKbencana, btnLihat;
    private static final int LATLONG = 10001;
    private static final int ADD_IMAGE_GALERI = 10002;
    private static final int ADD_IMAGE_CAMERA = 10003;
    private static final int LOKASI = 10004;
    private static final String URL = "https://ridwanharts.000webhostapp.com/";
    private DateFormat dateCreated;
    Date date = new Date();
    private String createdAt;
    private EditText etNamaLahan, etHargaLahan, etLuasLahan;
    private RelativeLayout rvLoading;
    private String filePath;
    private String ImagePath;
    private Boolean isEdit, isPilihLokasi;

    private Spinner spJenisTanah, spKemiringan, spKerawanan, spKair;

    private List<LatLng> pointJalanWates = new ArrayList<LatLng>();
    private List<Double> jarakPointkeJalanWates = new ArrayList<Double>();

    private String namaLahan, createdAtIntent, gambarString;
    private double hargaLahan, luasLahan;
    private int id;

    //upload image
    public static final String UPLOAD_URL = "http://ridwanharts.000webhostapp.com/upload_images.php";
    private static final int STORAGE_PERMISSION_CODE = 4655;
    private static final int PICK_IMAGE_REQUEST1 = 1;
    private static final int PICK_IMAGE_REQUEST2 = 2;
    private static final int PICK_IMAGE_REQUEST3 = 3;
    private Uri filepath1, filepath2, filepath3;
    private Bitmap bitmap1, bitmap2, bitmap3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_lahan);

        Toolbar toolbar = findViewById(R.id.toolbar);

        requestStoragePermission();

        isEdit = false;
        isPilihLokasi = false;
        Intent intent = getIntent();
        isEdit = intent.getBooleanExtra("is_edit", isEdit);
        lat = intent.getDoubleExtra("latitude", lat);
        longi = intent.getDoubleExtra("longitude", longi);
        if (isEdit){
            id = intent.getIntExtra("id_lahan", id);
            hargaLahan = intent.getDoubleExtra("harga_lahan",hargaLahan);
            namaLahan = intent.getStringExtra("nama_lahan");
            luasLahan = intent.getDoubleExtra("luas_lahan", luasLahan);
            createdAtIntent = intent.getStringExtra("created_at");
            gambarString = intent.getStringExtra("gambar");
        }

        if (getSupportActionBar() == null){
            setSupportActionBar(toolbar);
        }

        InitViewAndAction();

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            if (isEdit){
                getSupportActionBar().setTitle("Edit Lahan "+namaLahan);
                isPilihLokasi = true;
                tvLatitude.setText(""+lat);
                tvLongitude.setText(""+longi);
            }else {
                getSupportActionBar().setTitle("Tambah Lahanku");
                tvLatitude.setText("0");
                tvLongitude.setText("0");
            }

        }

        jarakLokasiKePointJalanWates();
        jarakKeBandara();

        Spinner();

    }

    private void jarakLokasiKePointJalanWates(){
        if (!isPilihLokasi) {
            //Toast.makeText(getApplicationContext(), "Pilih lokasi lahan dulu", Toast.LENGTH_SHORT).show();
            tvAksebilitas.setText("0");
        }else {
            Utils point = new Utils();
            pointJalanWates = point.listPointJalanRayaWates();
            jarakPointkeJalanWates.clear();
            LatLng lokasi = new LatLng(lat, longi);
            double jarak = 0;
            for (int i = 0; i < pointJalanWates.size(); i++) {
                jarak = lokasi.distanceTo(pointJalanWates.get(i));
                jarakPointkeJalanWates.add(jarak);
            }
            Collections.sort(jarakPointkeJalanWates);
            Utils a = new Utils();
            tvAksebilitas.setText("" + a.formatDecimal(jarakPointkeJalanWates.get(0)));
        }

    }

    private void jarakKeBandara(){
        if (!isPilihLokasi) {
            tvJarakBandara.setText("0");
        }else {
            LatLng bandara = new LatLng(-7.902952534425651, 110.0573943750627);
            LatLng lokasiLahan = new LatLng(lat, longi);
            double jarakKebandara = (lokasiLahan.distanceTo(bandara)) / 1000;
            Utils a = new Utils();
            tvJarakBandara.setText(a.formatDecimal(jarakKebandara) + "");
        }
    }

    public void InitViewAndAction (){

        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);

        spJenisTanah = findViewById(R.id.add_jenis_lahan);
        spKemiringan = findViewById(R.id.add_kemiringan);
        spKair = findViewById(R.id.add_ketersediaan_air);
        spKerawanan = findViewById(R.id.add_kerawanan_bencana);

        tvLatitude = findViewById(R.id.latitude_add);
        tvLongitude = findViewById(R.id.longitude_add);
        ivPlotArea = findViewById(R.id.iv_plotarea);
        pilihLokasi = findViewById(R.id.pilih_lokasi);
        rvLoading = findViewById(R.id.rv_loading);
        //example 20191206-112440222
        dateCreated = new SimpleDateFormat("yyyyMMdd-HHmmssSSS", Locale.getDefault());
        createdAt = dateCreated.format(date);

        etNamaLahan = findViewById(R.id.add_nama_lahan);
        etHargaLahan = findViewById(R.id.add_harga_lahan);
        etLuasLahan = findViewById(R.id.add_luas_lahan);
        tvAksebilitas = findViewById(R.id.add_aksebilitas);
        tvJarakBandara = findViewById(R.id.add_jarak_bandara);

        pilihLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPilihLokasi = true;
                Intent i = new Intent(getApplicationContext(), LihatKriteriaOnMap.class);
                i.putExtra("latitude", lat);
                i.putExtra("longitude", longi);
                i.putExtra("tipe_kriteria", "pilih_lokasi");
                //i.putExtra("nama", nama);
                startActivityForResult(i, LOKASI);
            }
        });

        btnLihatJenisTanah = findViewById(R.id.btn_lihat_jtanah);
        btnLihatJenisTanah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LihatKriteriaOnMap.class);
                i.putExtra("latitude", lat);
                i.putExtra("longitude", longi);
                i.putExtra("tipe_kriteria", "jenis_tanah");
                //i.putExtra("nama", nama);
                startActivity(i);
            }
        });

        btnLihatKlereng = findViewById(R.id.btn_lihat_klereng);
        btnLihatKlereng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LihatKriteriaOnMap.class);
                i.putExtra("latitude", lat);
                i.putExtra("longitude", longi);
                i.putExtra("tipe_kriteria", "kemiringan_lereng");
                //i.putExtra("nama", nama);
                startActivity(i);
            }
        });

        btnLihatKair = findViewById(R.id.btn_lihat_kair);
        btnLihatKair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LihatKriteriaOnMap.class);
                i.putExtra("latitude", lat);
                i.putExtra("longitude", longi);
                i.putExtra("tipe_kriteria", "ketersediaan_air");
                //i.putExtra("nama", nama);
                startActivity(i);
            }
        });

        btnLihatKbencana = findViewById(R.id.btn_lihat_kbencana);
        btnLihatKbencana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LihatKriteriaOnMap.class);
                i.putExtra("latitude", lat);
                i.putExtra("longitude", longi);
                i.putExtra("tipe_kriteria", "kerawanan_bencana");
                //i.putExtra("nama", nama);
                startActivity(i);
            }
        });


        btnKirimDataKeServer = findViewById(R.id.btn_kirim_server);
        btnKirimDataKeServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etNamaLahan.getText().toString().isEmpty()) {
                    if (!etLuasLahan.getText().toString().isEmpty()) {
                        if (!etHargaLahan.getText().toString().isEmpty()) {
                            if (isPilihLokasi) {
                                if (bitmap1 != null || bitmap2 != null || bitmap3 != null) {
                                    if (fillLayerPointList.size() >= 1) {
                                        rvLoading.setVisibility(View.VISIBLE);
                                        String namaLahan = etNamaLahan.getText().toString();
                                        double hargaLahan = Double.parseDouble(etHargaLahan.getText().toString());
                                        double luasLahan = Double.parseDouble(etLuasLahan.getText().toString());
                                        String dayaDukungT = spJenisTanah.getSelectedItem().toString();
                                        String ketersediaanAir = spKair.getSelectedItem().toString();
                                        double aksebilitas = Double.parseDouble(tvAksebilitas.getText().toString());
                                        String kemiringanLereng = spKemiringan.getSelectedItem().toString();
                                        String kerawananBencana = spKerawanan.getSelectedItem().toString();
                                        double jarakKeBandara = Double.parseDouble(tvJarakBandara.getText().toString());
                                        int idUser = PrefConfig.getLoggedInUser(getApplicationContext());
                                        ByteArrayOutputStream out = new ByteArrayOutputStream();

                                        if (!isEdit) {
                                            insertDatakeServer(namaLahan, hargaLahan, luasLahan, lat, longi,
                                                    dayaDukungT, ketersediaanAir, kemiringanLereng, aksebilitas, kerawananBencana, jarakKeBandara,
                                                    createdAt, idUser);
                                        } else {

                                            updateData(namaLahan, hargaLahan, luasLahan, lat, longi,
                                                    dayaDukungT, ketersediaanAir, kemiringanLereng, aksebilitas, kerawananBencana, jarakKeBandara,
                                                    createdAt, idUser);
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Plot lahan tidak boleh kosong", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(), "Harus ada satu foto lahan", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Pilih lokasi lahan dulu", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Anda belum memasukkan Harga Lahan", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Anda belum memasukkan Luas Lahan", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Anda belum memasukkan Nama Lahan", Toast.LENGTH_SHORT).show();
                }

            }
        });

        tvPlotArea = findViewById(R.id.point_plot);
        ivPlotArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PlottingAreaActivity.class);
                i.putExtra("latitude", lat);
                i.putExtra("longitude", longi);
                //i.putExtra("nama", nama);
                startActivityForResult(i, LATLONG);
            }
        });

        if (isEdit){
            etHargaLahan.setText(hargaLahan+"");
            etNamaLahan.setText(namaLahan+"");
            etLuasLahan.setText(luasLahan+"");

        }
    }

    private void Spinner(){
        // Spinner click listener
        spJenisTanah.setOnItemSelectedListener(this);
        spKemiringan.setOnItemSelectedListener(this);
        spKair.setOnItemSelectedListener(this);
        spKerawanan.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> catJenisTanah = new ArrayList<String>();
        catJenisTanah.add("Regosol");
        catJenisTanah.add("Latosol");
        catJenisTanah.add("Kambisol");
        catJenisTanah.add("Grumusol");

        List<String> catKemiringan = new ArrayList<String>();
        catKemiringan.add("0 - 2 %");
        catKemiringan.add("2 - 5 %");
        catKemiringan.add("5 - 15 %");
        catKemiringan.add("15 - 25 %");
        catKemiringan.add("25 - 40 %");
        catKemiringan.add("> 40 %");

        List<String> catKair = new ArrayList<String>();
        catKair.add("5 - 25 liter/detik");
        catKair.add("< 5 liter/detik");
        catKair.add("Tidak Ada");

        List<String> catKerawanan = new ArrayList<String>();
        catKerawanan.add("Banjir");
        catKerawanan.add("Tanah Longsor");
        catKerawanan.add("Tsunami");
        catKerawanan.add("Kekeringan");
        catKerawanan.add("Tidak Ada");

        // Creating adapter for spJenisTanah
        ArrayAdapter<String> dataAdapterJenisTanah = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catJenisTanah);
        ArrayAdapter<String> dataAdapterKemiringan = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catKemiringan);
        ArrayAdapter<String> dataAdapterKair = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catKair);
        ArrayAdapter<String> dataAdapterKerawanan = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catKerawanan);

        // Drop down bubble_info style - list view with radio button
        dataAdapterJenisTanah.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterKemiringan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterKair.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterKerawanan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spJenisTanah
        spJenisTanah.setAdapter(dataAdapterJenisTanah);
        spKemiringan.setAdapter(dataAdapterKemiringan);
        spKair.setAdapter(dataAdapterKair);
        spKerawanan.setAdapter(dataAdapterKerawanan);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_IMAGE_CAMERA:
                    if (data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        //imageView.setImageBitmap(selectedImage);
                    }
                    break;
                /*case ADD_IMAGE_GALERI:
                    Toast.makeText(TambahLahanActivity.this, "add image galeri", Toast.LENGTH_SHORT).show();
                    if (data != null) {
                        Uri selectedImage =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                try {
                                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                cursor.close();
                            }
                        }

                    }
                    break;*/
                case LATLONG:
                    if (requestCode == LATLONG) {

                        fillLayerPointList = (ArrayList<Point>) data.getSerializableExtra("latlong");

                        tvPlotArea.setText("");

                        for (int i=0;i<fillLayerPointList.size();i++){
                            int j=i+1;
                            tvPlotArea.append("Point "+j+" : "+fillLayerPointList.get(i).latitude()+", "+fillLayerPointList.get(i).longitude()+"\n");
                        }
                    }
                    break;
                case LOKASI:

                    String sLat="", sLongi="";
                    if (data != null){
                        sLat = data.getStringExtra("latitude");
                        sLongi = data.getStringExtra("longitude");
                        if (sLat==null || sLongi==null){
                            sLat=lat+"";
                            sLongi=longi+"";
                        }
                        tvLatitude.setText(sLat);
                        tvLongitude.setText(sLongi);
                        lat = Double.parseDouble(sLat);
                        longi = Double.parseDouble(sLongi);
                    }else {
                        isPilihLokasi = false;
                    }

                    jarakLokasiKePointJalanWates();
                    jarakKeBandara();
                    break;

                case PICK_IMAGE_REQUEST1:
                    filepath1 = data.getData();
                    try {

                        bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath1);
                        imageView1.setImageBitmap(bitmap1);
                    } catch (Exception ex) {

                    }
                    break;

                case PICK_IMAGE_REQUEST2:
                    filepath2 = data.getData();
                    try {

                        bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath2);
                        imageView2.setImageBitmap(bitmap2);
                    } catch (Exception ex) {

                    }
                    break;

                case PICK_IMAGE_REQUEST3:
                    filepath3 = data.getData();
                    try {

                        bitmap3 = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath3);
                        imageView3.setImageBitmap(bitmap3);
                    } catch (Exception ex) {

                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + requestCode);
            }
        }
    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void insertDatakeServer(String namaLokasi, double hargaLahan, double luasLahan, double lat, double longi,
                                    String ddTanah, String kAir, String kLereng, double aksebilitas, String kBencana, double jBandara,
                                    String createdAt, int id_user){

        uploadImages(id_user, createdAt);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //beginretrofit upload
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.d("", "message : "+message);
                    }
                });
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build();

                //String encodedBitmap = ImageUtils.bitmapToBase64String(bitmap, 30);
                String urlPathImage = URL+"image/"+id_user+"/"+createdAt+"/";

                RegisterApi api = retrofit.create(RegisterApi.class);

                //insert lokasi
                Call<Respon> call = api.insert_lokasi(
                        namaLokasi, hargaLahan, luasLahan, lat,longi,
                        ddTanah,kAir,kLereng,
                        aksebilitas,kBencana,jBandara,
                        createdAt, id_user, urlPathImage
                );
                call.enqueue(new Callback<Respon>() {
                    @Override
                    public void onResponse(Call<Respon> call, retrofit2.Response<Respon> response) {

                        String value = response.body().getValue();
                        String message = response.body().getMessage();
                        if(value.equals("1")){

                            //insert data plot area
                            for (int i=0;i<fillLayerPointList.size();i++){
                                Call<Respon> callInsertPlot = api.insert_latlong(i,
                                        fillLayerPointList.get(i).latitude(),fillLayerPointList.get(i).longitude(), PrefConfig.getLoggedInUser(getApplicationContext()),createdAt);
                                callInsertPlot.enqueue(new Callback<Respon>() {
                                    @Override
                                    public void onResponse(Call<Respon> call, retrofit2.Response<Respon> response) {
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                        rvLoading.setVisibility(View.INVISIBLE);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Call<Respon> call, Throwable t) {
                                        rvLoading.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }

                        }else {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            rvLoading.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Respon> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Jaringan Error", Toast.LENGTH_SHORT).show();
                        rvLoading.setVisibility(View.INVISIBLE);
                    }
                });
                // end retrofit upload
            }
        }, 7000L);


    }

    private void updateData(String namaLokasi, double hargaLahan, double luasLahan, double lat, double longi,
                            String ddTanah, String kAir, String kLereng, double aksebilitas, String kBencana, double jBandara,
                            String createdAt, int id_user) {

        uploadImages(id_user, createdAt);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                //update data
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.d("", "message : " + message);
                    }
                });
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                String urlPathImage = URL + "image/" + id_user + "/" + createdAt + "/";

                RegisterApi api = retrofit.create(RegisterApi.class);

                //insert lokasi
                Call<Respon> callUpdate = api.update(
                        id, namaLokasi, hargaLahan, luasLahan, lat, longi,
                        ddTanah, kAir, kLereng,
                        aksebilitas, kBencana, jBandara,
                        createdAt, id_user, urlPathImage
                );
                callUpdate.enqueue(new Callback<Respon>() {
                    @Override
                    public void onResponse(Call<Respon> call, retrofit2.Response<Respon> response) {
                        String value = response.body().getValue();
                        String message = response.body().getMessage();
                        if (value.equals("1")) {

                            //insert data plot area
                            for (int i = 0; i < fillLayerPointList.size(); i++) {
                                Call<Respon> callInsertPlot = api.insert_latlong(i,
                                        fillLayerPointList.get(i).latitude(), fillLayerPointList.get(i).longitude(), PrefConfig.getLoggedInUser(getApplicationContext()), createdAt);
                                callInsertPlot.enqueue(new Callback<Respon>() {
                                    @Override
                                    public void onResponse(Call<Respon> call, retrofit2.Response<Respon> response) {
                                        String messageFill = response.body().getMessage();
                                        Toast.makeText(getApplicationContext(), messageFill, Toast.LENGTH_SHORT).show();
                                        Log.e("message", messageFill);
                                        Log.e("message", fillLayerPointList.size() + "");
                                        Log.e("create", createdAt);
                                        Log.e("create intent", createdAtIntent);

                                        //delete point
                                        deletePolygon(id_user, createdAtIntent);

                                        rvLoading.setVisibility(View.INVISIBLE);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Call<Respon> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            rvLoading.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Respon> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Jaringan Error" + t, Toast.LENGTH_SHORT).show();
                        rvLoading.setVisibility(View.INVISIBLE);
                    }
                });

            }

            private void deletePolygon(int idUser, String createdAt) {

                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.d("", "message : " + message);
                    }
                });
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build();

                RegisterApi api = retrofit.create(RegisterApi.class);
                Call<Respon> callInsertPlot = api.delete_polygon(idUser, createdAt);
                callInsertPlot.enqueue(new Callback<Respon>() {
                    @Override
                    public void onResponse(Call<Respon> call, retrofit2.Response<Respon> response) {
                        String value = response.body().getValue();
                        String message = response.body().getMessage();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Respon> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Koneksi bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });

                // end retrofit upload
            }
        }, 7000L);
    }


    //
    //
    //Upload images multiple
    //
    //
    public void iv1(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST1);
    }

    public void iv2(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST2);
    }

    public void iv3(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST3);
    }


    private String getPath(Uri uri) {

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + "=?", new String[]{document_id}, null
        );
        if (cursor != null) {
            cursor.moveToFirst();
        }
        String path = null;
        if (cursor != null) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        }
        if (cursor != null) {
            cursor.close();
        }
        return path;
    }

    public void uploadImages(int idUser, String created) {
        String idToUpload = Integer.toString(idUser);
        String path[] = new String[3];
        int count = 0;
        if (filepath1 != null){
            path[0] = getPath(filepath1);
            count++;
        }
        if (filepath2 != null){
            path[1] = getPath(filepath2);
            count++;
        }
        if (filepath3 != null){
            path[2] = getPath(filepath3);
            count++;
        }

        try {
            String uploadId = UUID.randomUUID().toString();
            MultipartUploadRequest upload = new MultipartUploadRequest(this, uploadId, UPLOAD_URL);

            for(int i=0;i<count;i++){
                upload.addFileToUpload(path[i], "image")
                        .addParameter("no",i+"")
                        .addParameter("name", idToUpload)
                        .addParameter("created", created);
                upload.setMaxRetries(3);
                upload.startUpload();
            }



        } catch (Exception ex) {


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void requestStoragePermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }


}
