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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.labs.jangkriek.carilahan.Database.DbUserLokasi;
import com.labs.jangkriek.carilahan.POJO.Lokasi;
import com.labs.jangkriek.carilahan.Adapter.KelolaLahankuAdapter;
import com.labs.jangkriek.carilahan.POJO.Prioritas;
import com.labs.jangkriek.carilahan.POJO.RankingLokasi;
import com.labs.jangkriek.carilahan.Adapter.RankingLokasiAdapter;
import com.labs.jangkriek.carilahan.Database.DbRangkingLokasi;
import com.labs.jangkriek.carilahan.Database.DbSavePencarian;
import com.labs.jangkriek.carilahan.R;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
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
import java.util.List;
import java.util.Locale;

import static com.labs.jangkriek.carilahan.Activity.MainActivity.getUsername;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class RankingActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener {

    double nilaiVektorBobot[];
    private DbUserLokasi dbLokasi;
    //private DbLokasi dbLokasi;
    private DbRangkingLokasi dbRangkingLokasi;
    private DbSavePencarian dbSavePencarian;
    private List<Lokasi> lokasiList = new ArrayList<>();
    private List<Lokasi> rangkingLokasiList = new ArrayList<>();
    private KelolaLahankuAdapter kelolaLahankuAdapter;
    private Boolean k1 = false, k2 = false, k3 = false, k4 = false, k5 = false, k6 = false, k7 = false;
    double nilaiK1 = 1, nilaiK2 = 1, nilaiK3 = 1, nilaiK4 = 1, nilaiK5 = 1, nilaiK6 = 1, nilaiK7 = 1;
    private RecyclerView recyclerView;
    private RankingLokasiAdapter rankingLokasiAdapter;
    public static ArrayList<Prioritas> prioritas = new ArrayList<Prioritas>();
    public String inputTipe;

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

    TextView tvBoolean, tvNilai;
    Button btnSaveRank;

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

        nilaiVektorBobot = HitungFuzzyAHP.getVektorBobot();
        dbLokasi = new DbUserLokasi(this);
        dbRangkingLokasi = new DbRangkingLokasi(this);
        dbSavePencarian = new DbSavePencarian(this);
        //lokasiList.addAll(dbLokasi.getDataForRank());

        dateIdGroup = new SimpleDateFormat("HHmmssSSS", Locale.getDefault());
        dateCreateAt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        create = dateCreateAt.format(date);
        idGroup = dateIdGroup.format(date);

        btnSaveRank = findViewById(R.id.btn_save_hasil);

        Intent intent = getIntent();
        inputTipe = intent.getStringExtra(getString(R.string.INPUT_TIPE));
        k1 = intent.getBooleanExtra("k1", k1);
        k2 = intent.getBooleanExtra("k2", k2);
        k3 = intent.getBooleanExtra("k3", k3);
        k4 = intent.getBooleanExtra("k4", k4);
        k5 = intent.getBooleanExtra("k5", k5);
        k6 = intent.getBooleanExtra("k6", k6);
        k7 = intent.getBooleanExtra("k7", k7);

        Toast.makeText(getApplicationContext(),""+prioritas.size(), Toast.LENGTH_SHORT).show();

        tvBoolean = findViewById(R.id.tvBoolean);
        tvNilai = findViewById(R.id.tvNilai);

        btnSaveRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePencarian();
                selesai = true;
            }
        });

    }

    private void savePencarian() {
        dbSavePencarian.insertSaveLokasi(
                Integer.parseInt(idGroup),
                "P-" + idGroup,
                rangkingLokasiList.get(0).getLatitude(),
                rangkingLokasiList.get(0).getLongitude(),
                create);

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
                    Toast.makeText(getApplicationContext(),""+inputTipe, Toast.LENGTH_SHORT).show();
                }
                if (inputTipe.equals(getString(R.string.INPUT_OTOMATIS))){
                    nilaiBobotxData();
                    Toast.makeText(getApplicationContext(),""+inputTipe, Toast.LENGTH_SHORT).show();
                }
                insertDbRank();
                initRecyclerViewRank();
                initDataLokasiCollection();
                showBlueMarker(style);
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
            if (tempK1) {
                nilaiK1 = nilaiVektorBobot[i];
                tempK1 = false;
                i++;
            }
            if (tempK2) {
                nilaiK2 = nilaiVektorBobot[i];
                tempK2 = false;
                i++;
            }
            if (tempK3) {
                nilaiK3 = nilaiVektorBobot[i];
                tempK3 = false;
                i++;
            }
            if (tempK4) {
                nilaiK4 = nilaiVektorBobot[i];
                tempK4 = false;
                i++;
            }
            if (tempK5) {
                nilaiK5 = nilaiVektorBobot[i];
                tempK5 = false;
                i++;
            }
            if (tempK6) {
                nilaiK6 = nilaiVektorBobot[i];
                tempK6 = false;
                i++;
            }
            if (tempK7) {
                nilaiK7 = nilaiVektorBobot[i];
                tempK7 = false;
                i++;
            }
        }
    }

    //
    //
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

        /*for (int i = 0; i < lokasiList.size(); i++) {
            if (k1) {
                ddTanah[i] = lokasiList.get(i).getDayaDukungTanah() * nilaiBobotK1;
            }
            if (k2) {
                kAir[i] = lokasiList.get(i).getKetersediaanAir() * nilaiBobotK2;
            }
            if (k3) {
                kLereng[i] = lokasiList.get(i).getKemiringanLereng() * nilaiBobotK3;
            }
            if (k4) {
                aksebilitas[i] = lokasiList.get(i).getAksebilitas() * nilaiBobotK4;
            }
            if (k5) {
                pLahan[i] = lokasiList.get(i).getPerubahanLahan() * nilaiBobotK5;
            }
            if (k6) {
                kBencana[i] = lokasiList.get(i).getKerawananBencana() * nilaiBobotK6;
            }
            if (k7) {
                jBandara[i] = lokasiList.get(i).getJarakKeBandara() * nilaiBobotK7;
            }
            lokasiList.get(i).setJumlah(ddTanah[i] + kAir[i] + kLereng[i] + aksebilitas[i] + pLahan[i] + kBencana[i] + jBandara[i]);
            Utils a = new Utils();
            tvNilai.append(" Alternatif " + i
                    + "\n-k1" + a.formatDecimal(ddTanah[i])
                    + " -k2" + a.formatDecimal(kAir[i])
                    + " -k3" + a.formatDecimal(kLereng[i])
                    + " -k4" + a.formatDecimal(aksebilitas[i])
                    + " -k5" + a.formatDecimal(pLahan[i])
                    + " -k6" + a.formatDecimal(kBencana[i])
                    + " -k7" + a.formatDecimal(jBandara[i])
                    + "\n- " + a.formatDecimal(lokasiList.get(i).getJumlah())
                    + "\n\n");
        }*/
    }






    //save hasil ranking
    private void insertDbRank() {
        sortToRank();
        for (int i = 0; i < 3; i++) {
            createRankLokasi(Integer.parseInt(idGroup), rangkingLokasiList.get(i).getNama(),
                    rangkingLokasiList.get(i).getLatitude(), rangkingLokasiList.get(i).getLongitude(),
                    rangkingLokasiList.get(i).getJumlah(), create);
        }

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

        dbRangkingLokasi.insertRankLokasi(idgroup, nama, lat, lng, jumlah, create);
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

    private void initDataLokasiCollection() {

        List<RankingLokasi> l = dbRangkingLokasi.getLatLongRankLokasi(Integer.parseInt(idGroup));
        LatLng[] latLngs = new LatLng[l.size()];

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
        return super.onOptionsItemSelected(item);
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
            a.putExtra("LOGIN", getUsername());
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
            finish();
        }
    }

}
