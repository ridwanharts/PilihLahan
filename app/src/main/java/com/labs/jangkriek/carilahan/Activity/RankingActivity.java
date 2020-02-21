package com.labs.jangkriek.carilahan.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.labs.jangkriek.carilahan.Database.DbUserLokasi;
import com.labs.jangkriek.carilahan.POJO.Lokasi;
import com.labs.jangkriek.carilahan.Adapter.KelolaLahankuAdapter;
import com.labs.jangkriek.carilahan.POJO.PointLatLong;
import com.labs.jangkriek.carilahan.POJO.Prioritas;
import com.labs.jangkriek.carilahan.POJO.RankingLokasi;
import com.labs.jangkriek.carilahan.Adapter.RankingLokasiAdapter;
import com.labs.jangkriek.carilahan.Database.DbRangkingLokasi;
import com.labs.jangkriek.carilahan.Database.DbSavePencarian;
import com.labs.jangkriek.carilahan.PrefConfig;
import com.labs.jangkriek.carilahan.R;
import com.labs.jangkriek.carilahan.Utils.Utils;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.labs.jangkriek.carilahan.mainViewFragment.GuestHomeFragment.getListLokasitoProcessGuest;
import static com.labs.jangkriek.carilahan.mainViewFragment.GuestHomeFragment.getListPointtoProcessGuest;
import static com.labs.jangkriek.carilahan.mainViewFragment.UsersHomeFragment.getListLokasitoProcess;
import static com.labs.jangkriek.carilahan.mainViewFragment.UsersHomeFragment.getListPointtoProcess;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class RankingActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener {

    double nilaiVektorBobot[];
    private DbUserLokasi dbLokasi;
    //private DbLokasi dbLokasi;
    //private DbRangkingLokasi dbRangkingLokasi;
    private DbSavePencarian dbSavePencarian;
    private List<Lokasi> lokasiList = new ArrayList<>();
    private List<Lokasi> rangkingLokasiList = new ArrayList<>();
    private List<PointLatLong> pointLatLongList = new ArrayList<>();

    private static HashMap<String, List<Point>> dataPointHash = new HashMap<String, List<Point>>();
    private List<List<Point>> POINTS = new ArrayList<>();
    private ArrayList<String> listCreatedAt = new ArrayList<String>();
    private static final String LAYER_ID = "LAYER_ID";

    private Boolean k1 = false, k2 = false, k3 = false, k4 = false, k5 = false, k6 = false, k7 = false;
    double nilaiK1 = 1, nilaiK2 = 1, nilaiK3 = 1, nilaiK4 = 1, nilaiK5 = 1, nilaiK6 = 1, nilaiK7 = 1;
    private RecyclerView recyclerView;
    private RankingLokasiAdapter rankingLokasiAdapter;
    public static ArrayList<Prioritas> prioritas = new ArrayList<Prioritas>();
    public String inputTipe, metode;

    public MapboxMap mapboxMap;
    private MapView mapView;
    private ValueAnimator valueAnimator;
    private LatLng currentPos = new LatLng(-7.905, 110.06);
    private GeoJsonSource geoJsonSource;
    private FeatureCollection lokasiCollection;
    private Style style;

    DateFormat dateIdGroup;
    DateFormat dateCreateAt;
    Date date = new Date();
    String create;
    String idGroup;
    String saveMetode="";
    String kriteria="";

    //Button btnSaveRank;

    private boolean selesai = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_ranking);

        mapView = findViewById(R.id.map_view_rank);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Rank Hasil");
        }

        lokasiList = getListLokasitoProcess();
        pointLatLongList = getListPointtoProcess();
        if (PrefConfig.getTypeLogin(getApplicationContext()).equals("GUEST")){
            lokasiList = getListLokasitoProcessGuest();
            pointLatLongList = getListPointtoProcessGuest();
        }


        //dbLokasi = new DbUserLokasi(this);
        //dbRangkingLokasi = new DbRangkingLokasi(this);
        dbSavePencarian = new DbSavePencarian(this);
        //lokasiList.addAll(dbLokasi.getDataForRank());

        dateIdGroup = new SimpleDateFormat("HHmmss", Locale.getDefault());
        dateCreateAt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        create = dateCreateAt.format(date);
        idGroup = dateIdGroup.format(date);

        Intent intent = getIntent();
        inputTipe = intent.getStringExtra(getString(R.string.INPUT_TIPE));
        metode = intent.getStringExtra(getString(R.string.metode));
        k1 = intent.getBooleanExtra("k1", k1);
        k2 = intent.getBooleanExtra("k2", k2);
        k3 = intent.getBooleanExtra("k3", k3);
        k4 = intent.getBooleanExtra("k4", k4);
        k5 = intent.getBooleanExtra("k5", k5);
        k6 = intent.getBooleanExtra("k6", k6);
        k7 = intent.getBooleanExtra("k7", k7);

        Log.e("eror metode ",""+metode);
        if (metode.equals("AHP")){
            nilaiVektorBobot = PilihKriteria.getVektorBobotAhp();
        }else {
            nilaiVektorBobot = PilihKriteria.getVektorBobot();
        }

        //Toast.makeText(getApplicationContext(),""+prioritas.size(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail_perhitungan, menu);

        return true;
    }

    private void savePencarian() {
        Log.e("metode",""+metode);

        if (k1){
            kriteria = kriteria + getString(R.string.K1_DAYA_DUKUNG_TANAH);
        }
        if (k2){
            kriteria = kriteria + ", " + getString(R.string.K2_KETERSEDIAAN_AIR);
        }
        if (k3){
            kriteria = kriteria + ", " + getString(R.string.K3_KEMIRINGAN_LERENG);
        }
        if (k4){
            kriteria = kriteria + ", " + getString(R.string.K4_AKSEBILITAS);
        }
        if (k5){
            kriteria = kriteria + ", " + getString(R.string.K5_HARGA_LAHAN);
        }
        if (k6){
            kriteria = kriteria + ", " + getString(R.string.K6_KERAWANAN_BENCANA);
        }
        if (k7){
            kriteria = kriteria + ", " + getString(R.string.K7_JARAK_KE_BANDARA);
        }

        saveMetode = "FAHP";
        if (metode.equals("AHP")){
            saveMetode = "AHP";
        }

        String group = idGroup+create+ PrefConfig.getLoggedInUser(getApplicationContext());
        Log.e("",""+group);
        for (int i=0;i<3;i++){
            dbSavePencarian.insertSaveLokasi(
                    group,
                    rangkingLokasiList.get(i).getNama(),
                    rangkingLokasiList.get(i).getHargaLahan(),
                    rangkingLokasiList.get(i).getLuasLahan(),
                    rangkingLokasiList.get(i).getLatitude(),
                    rangkingLokasiList.get(i).getLongitude(),

                    rangkingLokasiList.get(i).getDayaDukungTanah(),
                    rangkingLokasiList.get(i).getKetersediaanAir(),
                    rangkingLokasiList.get(i).getKemiringanLereng(),
                    rangkingLokasiList.get(i).getAksebilitas(),
                    rangkingLokasiList.get(i).getKerawananBencana(),
                    rangkingLokasiList.get(i).getJarakKeBandara(),

                    rangkingLokasiList.get(i).getId_user(),
                    rangkingLokasiList.get(i).getGambar(),
                    create,
                    saveMetode,
                    kriteria);
        }


        Toast.makeText(getApplicationContext(),"Successfully saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        RankingActivity.this.mapboxMap = mapboxMap;
        geoJsonSource = new GeoJsonSource("source_rank",
                Feature.fromGeometry(Point.fromLngLat(currentPos.getLongitude(),
                        currentPos.getLatitude())));
        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/ridwanharts/cjytgnl6o073w1cnv460nfx46"), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                if (inputTipe.equals(getString(R.string.INPUT_MANUAL))) {
                    initKriteriaTrue();
                    hitungDataxVektorBobot();
                    //Toast.makeText(getApplicationContext(),""+inputTipe, Toast.LENGTH_SHORT).show();
                }
                if (inputTipe.equals(getString(R.string.INPUT_OTOMATIS))){
                    nilaiBobotxData();
                    //Toast.makeText(getApplicationContext(),""+inputTipe, Toast.LENGTH_SHORT).show();
                }
                insertDbRank();
                initRecyclerViewRank();
                initDataLokasiCollection();
                showBlueMarker(style);
                initFillLayer(style);
            }
        });
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        return false;
    }

    //
    //
    //memasukkan nilai vektor bobot ke kriteria yg sesuai dengan nomor
    private void initKriteriaTrue() {
        Boolean tempK1 = k1, tempK2 = k2, tempK3 = k3, tempK4 = k4, tempK5 = k5, tempK6 = k6, tempK7 = k7;
        for (int i = 0; i < nilaiVektorBobot.length; i++) {
            if (tempK1) { nilaiK1 = nilaiVektorBobot[i]; tempK1 = false; i++; }
            if (tempK2) { nilaiK2 = nilaiVektorBobot[i]; tempK2 = false; i++; }
            if (tempK3) { nilaiK3 = nilaiVektorBobot[i]; tempK3 = false; i++; }
            if (tempK4) { nilaiK4 = nilaiVektorBobot[i]; tempK4 = false; i++; }
            if (tempK5) { nilaiK5 = nilaiVektorBobot[i]; tempK5 = false; i++; }
            if (tempK6) { nilaiK6 = nilaiVektorBobot[i]; tempK6 = false; i++; }
            if (tempK7) { nilaiK7 = nilaiVektorBobot[i]; tempK7 = false; i++; }
        }
    }

    //nilai vektor bobot dikali dengan list data yang diambil dari database dblokasiuser
    private void hitungDataxVektorBobot() {

        double ddTanah[] = new double[lokasiList.size()];
        double kAir[] = new double[lokasiList.size()];
        double kLereng[] = new double[lokasiList.size()];
        double aksebilitas[] = new double[lokasiList.size()];
        double pLahan[] = new double[lokasiList.size()];
        double kBencana[] = new double[lokasiList.size()];
        double jBandara[] = new double[lokasiList.size()];

        /*for (int i = 0; i < lokasiList.size(); i++) {
            if (k1) {
                ddTanah[i] = lokasiList.get(i).getDayaDukungTanah() * nilaiK1;
            }
            if (k2) {
                kAir[i] = lokasiList.get(i).getKetersediaanAir() * nilaiK2;
            }
            if (k3) {
                kLereng[i] = lokasiList.get(i).getKemiringanLereng() * nilaiK3;
            }
            if (k4) {
                aksebilitas[i] = lokasiList.get(i).getAksebilitas() * nilaiK4;
            }
            if (k5) {
                pLahan[i] = lokasiList.get(i).getPerubahanLahan() * nilaiK5;
            }
            if (k6) {
                kBencana[i] = lokasiList.get(i).getKerawananBencana() * nilaiK6;
            }
            if (k7) {
                jBandara[i] = lokasiList.get(i).getJarakKeBandara() * nilaiK7;
            }
            lokasiList.get(i).setJumlah(ddTanah[i] + kAir[i] + kLereng[i] + aksebilitas[i] + pLahan[i] + kBencana[i] + jBandara[i]);
            Utils a = new Utils();
            tvNilai.append(" Alternatif " + i
                    + "\n " + a.formatDecimal(ddTanah[i])
                    + " " + a.formatDecimal(kAir[i])
                    + " " + a.formatDecimal(kLereng[i])
                    + " " + a.formatDecimal(aksebilitas[i])
                    + " " + a.formatDecimal(pLahan[i])
                    + " " + a.formatDecimal(kBencana[i])
                    + " " + a.formatDecimal(jBandara[i])
                    + "\n " + a.formatDecimal(lokasiList.get(i).getJumlah())
                    + "\n\n");
        }*/

    }

    //
    //
    //
    //
    //nilai otomatis
    public void nilaiBobotxData(){
        double ddTanah[] = new double[lokasiList.size()];
        double kAir[] = new double[lokasiList.size()];
        double kLereng[] = new double[lokasiList.size()];
        double aksebilitas[] = new double[lokasiList.size()];
        double pLahan[] = new double[lokasiList.size()];
        double kBencana[] = new double[lokasiList.size()];
        double jBandara[] = new double[lokasiList.size()];

        prioritas = PilihKriteria.getPrioritasKriteria();

        double nilaiBobotK1=0, nilaiBobotK2=0, nilaiBobotK3=0, nilaiBobotK4=0, nilaiBobotK5=0, nilaiBobotK6=0, nilaiBobotK7=0;
        for (int k=0;k<prioritas.size();k++){
            if (prioritas.get(k).getKriteria().equals(getString(R.string.K1_DAYA_DUKUNG_TANAH))){
                nilaiBobotK1 = prioritas.get(k).getNilai();
            }
            if (prioritas.get(k).getKriteria().equals(getString(R.string.K2_KETERSEDIAAN_AIR))){
                nilaiBobotK2 = prioritas.get(k).getNilai();
            }
            if (prioritas.get(k).getKriteria().equals(getString(R.string.K3_KEMIRINGAN_LERENG))){
                nilaiBobotK3 = prioritas.get(k).getNilai();
            }
            if (prioritas.get(k).getKriteria().equals(getString(R.string.K4_AKSEBILITAS))){
                nilaiBobotK4 = prioritas.get(k).getNilai();
            }
            if (prioritas.get(k).getKriteria().equals(getString(R.string.K5_HARGA_LAHAN))){
                nilaiBobotK5 = prioritas.get(k).getNilai();
            }
            if (prioritas.get(k).getKriteria().equals(getString(R.string.K6_KERAWANAN_BENCANA))){
                nilaiBobotK6 = prioritas.get(k).getNilai();
            }
            if (prioritas.get(k).getKriteria().equals(getString(R.string.K7_JARAK_KE_BANDARA))){
                nilaiBobotK7 = prioritas.get(k).getNilai();
            }
        }

        Log.e("nilai bobot 1"," "+nilaiBobotK1);
        Log.e("nilai bobot 2"," "+nilaiBobotK2);
        Log.e("nilai bobot 3"," "+nilaiBobotK3);
        Log.e("nilai bobot 4"," "+nilaiBobotK4);
        Log.e("nilai bobot 5"," "+nilaiBobotK5);
        Log.e("nilai bobot 6"," "+nilaiBobotK6);
        Log.e("nilai bobot 7"," "+nilaiBobotK7);

        String kriteria, textKriteria;
        double nilaiK = 0;
        double nilai = 0;

        for (int i = 0; i < lokasiList.size(); i++) {
            if (k1) {
                kriteria = "k1";
                textKriteria = lokasiList.get(i).getDayaDukungTanah();
                nilai = konversiStringtoNilai(textKriteria, kriteria);
                ddTanah[i] = nilai * nilaiBobotK1;
            }
            if (k2) {
                kriteria = "k2";
                textKriteria = lokasiList.get(i).getKetersediaanAir();
                nilai = konversiStringtoNilai(textKriteria, kriteria);
                kAir[i] = nilai * nilaiBobotK2;
            }
            if (k3) {
                kriteria = "k3";
                textKriteria = lokasiList.get(i).getKemiringanLereng();
                nilai = konversiStringtoNilai(textKriteria, kriteria);
                kLereng[i] = nilai * nilaiBobotK3;
            }
            if (k4) {
                kriteria = "k4";
                nilaiK = lokasiList.get(i).getAksebilitas();
                nilai = konversiDoubletoNilai(nilaiK, kriteria);
                aksebilitas[i] = nilai * nilaiBobotK4;
            }
            if (k5) {
                kriteria = "k5";
                nilaiK = lokasiList.get(i).getHargaLahan();
                nilai = konversiDoubletoNilai(nilaiK, kriteria);
                pLahan[i] = nilai * nilaiBobotK5;
            }
            if (k6) {
                kriteria = "k6";
                textKriteria = lokasiList.get(i).getKerawananBencana();
                nilai = konversiStringtoNilai(textKriteria, kriteria);
                kBencana[i] = nilai * nilaiBobotK6;
            }
            if (k7) {
                kriteria = "k7";
                nilaiK = lokasiList.get(i).getJarakKeBandara();
                nilai = konversiDoubletoNilai(nilaiK, kriteria);
                jBandara[i] = nilai * nilaiBobotK7;
            }
            lokasiList.get(i).setJumlah(ddTanah[i] + kAir[i] + kLereng[i] + aksebilitas[i] + pLahan[i] + kBencana[i] + jBandara[i]);
            Log.i(""+i+" k1 ",""+ddTanah[i]/nilaiBobotK1+" x " +ddTanah[i]);
            Log.i(""+i+" k2 ",""+ kAir[i]/nilaiBobotK2+" x " +kAir[i]);
            Log.i(""+i+" k3 ",""+ kLereng[i]/nilaiBobotK3+" x " +kLereng[i]);
            Log.i(""+i+" k4 ",""+ aksebilitas[i]/nilaiBobotK4+" x " +aksebilitas[i]);
            Log.i(""+i+" k5 ",""+ pLahan[i]/nilaiBobotK5+" x " +pLahan[i]);
            Log.i(""+i+" k6 ",""+ kBencana[i]/nilaiBobotK6+" x " +kBencana[i]);
            Log.i(""+i+" k7 ",""+ jBandara[i]/nilaiBobotK7+" x " +jBandara[i]);
            Log.i("Jumlah nilai "+i," -- "+lokasiList.get(i).getJumlah());

        }

    }


    private double konversiStringtoNilai(String text, String kriteria) {

        double nilai = 0;
        if (kriteria.equals("k1")) {
            if (text.equals("Regosol")) {
                nilai = 1;
            } else if (text.equals("Kambisol")) {
                nilai = 2;
            } else if (text.equals("Grumusol")) {
                nilai = 3;
            } else if (text.equals("Latosol")) {
                nilai = 4;
            }
        } else if (kriteria.equals("k2")) {
            if (text.equals("5 - 25 liter/detik")) {
                nilai = 4;
            } else if (text.equals("< 5 liter/detik")) {
                nilai = 2.5;
            } else if (text.equals("Tidak Ada")) {
                nilai = 1;
            }
        }else if (kriteria.equals("k3")) {
            if (text.equals("0 - 2 %")) {
                nilai = 4;
            } else if (text.equals("2 - 5 %")) {
                nilai = 3;
            } else if (text.equals("5 - 15 %")) {
                nilai = 2.5;
            } else if (text.equals("15 - 25 %")) {
                nilai = 2;
            } else if (text.equals("25 - 40 %")) {
                nilai = 1.5;
            } else if (text.equals("> 40 %")) {
                nilai = 1;
            }
        }else if (kriteria.equals("k6")) {
            if (text.equals("Kekeringan")) {
                nilai = 1;
            } else if (text.equals("Banjir")) {
                nilai = 1;
            } else if (text.equals("Tanah Longsor")) {
                nilai = 1;
            } else if (text.equals("Tsunami")) {
                nilai = 1;
            } else if (text.equals("Tidak Ada")) {
                nilai = 4;
            }
        }

            return nilai;
        }

    private int konversiDoubletoNilai(Double text, String kriteria) {

        int nilai = 0;
        switch (kriteria) {
            case "k4":
                if (text < 100) {
                    nilai = 4;
                } else if (text >= 100 && text < 500) {
                    nilai = 3;
                } else if (text >= 500 && text < 1000) {
                    nilai = 2;
                } else if (text >= 1000) {
                    nilai = 1;
                }
                break;
            case "k5":
                if (text < 500000) {
                    nilai = 4;
                } else if (text >= 500000 && text < 800000) {
                    nilai = 3;
                } else if (text >= 800000 && text < 1000000) {
                    nilai = 2;
                }else if (text >= 1000000) {
                    nilai = 1;
                }
                break;
            case "k7":
                if (text < 5) {
                    nilai = 4;
                } else if (text >= 5 && text < 10) {
                    nilai = 3;
                } else if (text >= 10 && text < 20) {
                    nilai = 2;
                } else if (text >= 20) {
                    nilai = 1;
                }
                break;
        }
        return nilai;
    }

    //save hasil ranking
    private void insertDbRank() {
        sortToRank();
        /*for (int i = 0; i < 3; i++) {
            createRankLokasi(Integer.parseInt(idGroup), rangkingLokasiList.get(i).getNama(),
                    rangkingLokasiList.get(i).getLatitude(), rangkingLokasiList.get(i).getLongitude(),
                    rangkingLokasiList.get(i).getJumlah(), create);
        }
*/
    }

    //inisiasi recycler view berdasarkan kelas Lokasi dengan ditampilkan 3 item
    private void initRecyclerViewRank() {
        recyclerView = findViewById(R.id.rv_list_rank_lokasi);
        rankingLokasiAdapter = new RankingLokasiAdapter(this, rangkingLokasiList, mapboxMap);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(rankingLokasiAdapter);
    }

    //method menampung data yang akan diinput ke dbRankLokasi
    private void createRankLokasi(int idgroup, String nama, double lat, double lng, double jumlah, String create) {

        //dbRangkingLokasi.insertRankLokasi(idgroup, nama, lat, lng, jumlah, create);
        /*// get the newly inserted note from db
        Lokasi n = dbRangkingLokasi.getNote(id);

        if (n != null) {
            // adding new note to array list at 0 position
            lokasiList.add(0, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

            toggleEmptyNotes();
        }*/
    }

    //rank dengan sorting berdasarkan getJumlah
    private void sortToRank() {
        rangkingLokasiList = lokasiList;
        Collections.sort(rangkingLokasiList, new Comparator<Lokasi>() {
            @Override
            public int compare(Lokasi o1, Lokasi o2) {
                return Double.compare(o2.getJumlah(), o1.getJumlah());
            }
        });
    }

    private void initFillLayer(@NonNull Style loadedMapStyle) {

        for (int i=0;i<listCreatedAt.size();i++){
            POINTS.add(dataPointHash.get(listCreatedAt.get(i)));
        }

        loadedMapStyle.addSource(new GeoJsonSource("source-id11", Polygon.fromLngLats(POINTS)));
        loadedMapStyle.addLayerBelow(new FillLayer("layer-id11", "source-id11").withProperties(
                fillOpacity(0.4f),
                fillColor(Color.parseColor("#f74e4e"))
                ), "layer_rank"
        );

    }

    private void initDataLokasiCollection() {

        List<Lokasi> l = rangkingLokasiList;
        LatLng[] latLngs = new LatLng[l.size()];

        for (int i=0;i<lokasiList.size();i++){
            listCreatedAt.add(lokasiList.get(i).getCreated_at());
        }

        for (int i=0;i<listCreatedAt.size();i++){

            List<Point> points = new ArrayList<>();

            Collections.sort(pointLatLongList, new Comparator<PointLatLong>() {
                @Override
                public int compare(PointLatLong o1, PointLatLong o2) {
                    return o1.getNo_point() - o2.getNo_point();
                }
            });
            for (int j=0;j<pointLatLongList.size();j++){
                String createdAt = pointLatLongList.get(j).getCreated_at();
                Double lat = pointLatLongList.get(j).getLatitude();
                Double longi = pointLatLongList.get(j).getLongitude();

                if (listCreatedAt.get(i).equals(createdAt)){
                    points.add(Point.fromLngLat(longi, lat));
                    dataPointHash.put(createdAt, points);
                }

            }
        }

        for (int i = 0; i < l.size(); i++) {
            //Log.e("cek", l.get(i).getLatitude() + ""+l.get(i).getLongitude());
            latLngs[i] = new LatLng(l.get(i).getLatitude(), l.get(i).getLongitude());
        }

        lokasiCollection = FeatureCollection.fromFeatures(new Feature[]{});
        List<Feature> lokasiList = new ArrayList<>();
        if (lokasiCollection != null) {
            for (LatLng latLngLoc : latLngs) {
                lokasiList.add(Feature.fromGeometry(Point.fromLngLat(latLngLoc.getLongitude(), latLngLoc.getLatitude())));
            }
            lokasiCollection = FeatureCollection.fromFeatures(lokasiList);
        }
    }

    public void showBlueMarker(Style styleBlue) {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_loc_blue, null);
        Bitmap bitmap = BitmapUtils.getBitmapFromDrawable(drawable);

        styleBlue.addImage(("marker_blue_rank"), bitmap);
        styleBlue.addSource(new GeoJsonSource("source_rank", lokasiCollection));
        styleBlue.addLayer(new SymbolLayer("layer_rank", "source_rank")
                .withProperties(
                        iconImage("marker_blue_rank"),
                        PropertyFactory.iconIgnorePlacement(true),
                        iconAllowOverlap(true),
                        iconOffset(new Float[]{0f, -4f})
                ));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        if (item.getItemId()==R.id.detail){
            int jumlah = dbSavePencarian.getRankLokasiCount();
            for (int i=0;i<nilaiVektorBobot.length;i++){
                Log.e("",""+nilaiVektorBobot[i]);
            }
            Toast.makeText(getApplicationContext(),"Detail Perhitungan : "+jumlah, Toast.LENGTH_SHORT).show();
        }

        if (item.getItemId()==R.id.save){
            Toast.makeText(getApplicationContext(),"Simpan Perhitungan"+prioritas.size(), Toast.LENGTH_SHORT).show();
            savePencarian();
            //selesai = true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (PrefConfig.getTypeLogin(getApplicationContext()).equals("GUEST")){
            menu.findItem(R.id.save).setVisible(false);
            this.invalidateOptionsMenu();
        }

        return super.onPrepareOptionsMenu(menu);
    }

    // Add the mapView lifecycle to the activity's lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();

        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (selesai) {
            Intent a = new Intent(this, MainActivity.class);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
            finish();
        }
    }

}
