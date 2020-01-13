package com.labs.jangkriek.carilahan.Activity.Users;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
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

import com.labs.jangkriek.carilahan.Activity.MainActivity;
import com.labs.jangkriek.carilahan.POJO.Respon;
import com.labs.jangkriek.carilahan.R;
import com.labs.jangkriek.carilahan.Utils.RegisterApi;
import com.labs.jangkriek.carilahan.Utils.ImageUtils;
import com.labs.jangkriek.carilahan.Utils.Utils;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.labs.jangkriek.carilahan.Activity.MainActivity.getIdUser;
import static com.labs.jangkriek.carilahan.Activity.MainActivity.getUsername;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class TambahLahanActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView tvLatitude, tvLongitude, tvPlotArea, tvJarakBandara, tvAksebilitas;
    private double lat, longi;
    private ImageView ivPlotArea;
    private List<Point> fillLayerPointList = new ArrayList<>();
    private Bitmap imageBitmap;
    private ImageView ivUploadGambar, pilihLokasi;
    private Button btnSelectFoto, btnKirimDataKeServer, btnLihatJenisTanah, btnLihatKair, btnLihatKlereng, btnLihatKbencana, btnLihat;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_lahan);

        Toolbar toolbar = findViewById(R.id.toolbar);

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
        jarakAksebilitas();

        Spinner();

    }

    private void jarakLokasiKePointJalanWates(){
        if (!isPilihLokasi) {
            //Toast.makeText(getApplicationContext(), "Pilih lokasi lahan dulu", Toast.LENGTH_SHORT).show();
            tvAksebilitas.setText("0");
        }else {
            Utils point = new Utils();
            pointJalanWates = point.listPointJalanRayaWates();

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

    private void jarakAksebilitas(){
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

        spJenisTanah = findViewById(R.id.add_jenis_lahan);
        spKemiringan = findViewById(R.id.add_kemiringan);
        spKair = findViewById(R.id.add_ketersediaan_air);
        spKerawanan = findViewById(R.id.add_kerawanan_bencana);

        tvLatitude = findViewById(R.id.latitude_add);
        tvLongitude = findViewById(R.id.longitude_add);
        ivPlotArea = findViewById(R.id.iv_plotarea);
        ivUploadGambar = findViewById(R.id.image_upload);
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
                                if (imageBitmap != null) {
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
                                        int idUser = getIdUser();
                                        ByteArrayOutputStream out = new ByteArrayOutputStream();

                                        if (!isEdit) {
                                            insertDatakeServer(namaLahan, hargaLahan, luasLahan, lat, longi,
                                                    dayaDukungT, ketersediaanAir, kemiringanLereng, aksebilitas, kerawananBencana, jarakKeBandara,
                                                    createdAt, idUser, imageBitmap);
                                        } else {

                                            updateData(namaLahan, hargaLahan, luasLahan, lat, longi,
                                                    dayaDukungT, ketersediaanAir, kemiringanLereng, aksebilitas, kerawananBencana, jarakKeBandara,
                                                    createdAt, idUser, imageBitmap);
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Plot lahan tidak boleh kosong", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(), "Foto lahan tidak boleh kosong", Toast.LENGTH_SHORT).show();
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

        btnSelectFoto = findViewById(R.id.btn_select_foto);
        btnSelectFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
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

            byte[] decodedString = Base64.decode(gambarString, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageBitmap = decodedByte;
            ivUploadGambar.setImageBitmap(decodedByte);
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
                case ADD_IMAGE_GALERI:
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
                                ivUploadGambar.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                try {
                                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                cursor.close();
                            }
                        }

                    }
                    break;
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

                    String sLat, sLongi;
                    if (data != null){
                        sLat = data.getStringExtra("latitude");
                        sLongi = data.getStringExtra("longitude");
                        tvLatitude.setText(sLat);
                        tvLongitude.setText(sLongi);
                        lat = Double.parseDouble(sLat);
                        longi = Double.parseDouble(sLongi);
                    }else {
                        isPilihLokasi = false;
                    }

                    jarakLokasiKePointJalanWates();
                    jarakAksebilitas();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + requestCode);
            }
        }


    }

    private void pickFromGallery(){

        final CharSequence[] options = { "Buka Kamera", "Pilih dari Galeri","Batal" };

        AlertDialog.Builder builder = new AlertDialog.Builder(TambahLahanActivity.this);
        builder.setTitle("Pilih Gambar Lahan");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                final int MyVersion = Build.VERSION.SDK_INT;

                if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    if (!checkIfAlreadyhavePermission()) {
                        ActivityCompat.requestPermissions(TambahLahanActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        if (options[item].equals("Buka Kamera")) {
                            Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, ADD_IMAGE_CAMERA );

                        } else if (options[item].equals("Pilih dari Galeri")) {
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto , ADD_IMAGE_GALERI);

                        } else if (options[item].equals("Batal")) {
                            dialog.dismiss();
                        }
                    }
                }


            }
        });
        builder.show();
    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    /*public void uploadBitmap(final Bitmap bitmap) {

        //getting the tag from the edittext
        final String tags = createdAt;

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, "https://ridwanharts.000webhostapp.com/upload_images.php",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            filePath = "https://ridwanharts.000webhostapp.com/"+obj.getString("file_path");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            *//*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * *//*
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tags", tags);
                return params;
            }

            *//*
             * Here we are passing image by renaming it with a unique name
             * *//*
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }*/

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void insertDatakeServer(String namaLokasi, double hargaLahan, double luasLahan, double lat, double longi,
                                    String ddTanah, String kAir, String kLereng, double aksebilitas, String kBencana, double jBandara,
                                    String createdAt, int id_user, Bitmap bitmap){

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

        String encodedBitmap = ImageUtils.bitmapToBase64String(bitmap, 30);

        RegisterApi api = retrofit.create(RegisterApi.class);

        //insert lokasi
        Call<Respon> call = api.insert_lokasi(
                namaLokasi, hargaLahan, luasLahan, lat,longi,
                ddTanah,kAir,kLereng,
                aksebilitas,kBencana,jBandara,
                createdAt, id_user, encodedBitmap
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
                                fillLayerPointList.get(i).latitude(),fillLayerPointList.get(i).longitude(), getIdUser(),createdAt);
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
    }

    private void updateData(String namaLokasi, double hargaLahan, double luasLahan, double lat, double longi,
                            String ddTanah, String kAir, String kLereng, double aksebilitas, String kBencana, double jBandara,
                            String createdAt, int id_user, Bitmap bitmap){

        //update data
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
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String encodedBitmap = ImageUtils.bitmapToBase64String(bitmap, 30);

        RegisterApi api = retrofit.create(RegisterApi.class);

        //insert lokasi
        Call<Respon> callUpdate = api.update(
                id, namaLokasi, hargaLahan, luasLahan, lat,longi,
                ddTanah,kAir,kLereng,
                aksebilitas,kBencana,jBandara,
                createdAt, id_user, encodedBitmap
        );
        callUpdate.enqueue(new Callback<Respon>() {
            @Override
            public void onResponse(Call<Respon> call, retrofit2.Response<Respon> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                if(value.equals("1")){

                    //insert data plot area
                    for (int i=0;i<fillLayerPointList.size();i++){
                        Call<Respon> callInsertPlot = api.insert_latlong(i,
                                fillLayerPointList.get(i).latitude(),fillLayerPointList.get(i).longitude(), getIdUser(),createdAt);
                        callInsertPlot.enqueue(new Callback<Respon>() {
                            @Override
                            public void onResponse(Call<Respon> call, retrofit2.Response<Respon> response) {
                                String messageFill = response.body().getMessage();
                                Toast.makeText(getApplicationContext(), messageFill, Toast.LENGTH_SHORT).show();
                                Log.e("message", messageFill);
                                Log.e("message", fillLayerPointList.size()+"");
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

                }else {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    rvLoading.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Respon> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Jaringan Error"+ t, Toast.LENGTH_SHORT).show();
                rvLoading.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void deletePolygon(int idUser, String createdAt) {

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
