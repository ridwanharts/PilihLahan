package com.labs.jangkriek.carilahan.Activity;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.labs.jangkriek.carilahan.POJO.Lokasi;
import com.labs.jangkriek.carilahan.Adapter.LokasiAdapter;
import com.labs.jangkriek.carilahan.Database.DbLokasi;
import com.labs.jangkriek.carilahan.POJO.Respon;
import com.labs.jangkriek.carilahan.R;
import com.labs.jangkriek.carilahan.RegisterApi;
import com.labs.jangkriek.carilahan.Utils.RecyclerTouchListener;
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

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.ScaleInAnimator;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.labs.jangkriek.carilahan.Activity.MainActivity.getLoginType;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

/**
 * Use a recyclerview with a Mapbox map to easily explore content all on one screen
 */
public class LokasiActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener {

    private static final String URL = "https://ridwanharts.000webhostapp.com/";
    private static final String SYMBOL_ICON_ID = "SYMBOL_ICON_ID";
    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String LAYER_ID = "LAYER_ID";
    private static final int GALLERY_REQUEST_CODE = 7;
    public MapboxMap mapboxMap;
    private MapView mapView;
    private FeatureCollection lokasiCollection;
    private FloatingActionButton fabOpAddLocation;
    private ImageView ivCekUpload;

    private LokasiAdapter mAdapter;

    private List<Lokasi> lokasiList = new ArrayList<>();

    private RecyclerView recyclerView;

    private DbLokasi db;

    private ValueAnimator valueAnimator;
    private LatLng currentPos = new LatLng(-7.8855268466, 110.062440920);

    private GeoJsonSource geoJsonSource;
    private TextView tvLatitudeLokasi,tvLongitudeLokasi;
    private Style style;

    private String lat, longi;
    private Context context;

    private ImageView ivUploadGambar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.access_token));

        setContentView(R.layout.activity_lokasi);
        fabOpAddLocation = findViewById(R.id.op_add_location);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        tvLatitudeLokasi = findViewById(R.id.tv_latitude_lokasi);
        tvLongitudeLokasi = findViewById(R.id.tv_longitude_lokasi);
        tvLatitudeLokasi.setText("-7.8855268466");
        tvLongitudeLokasi.setText("110.062440920");
        ivCekUpload = findViewById(R.id.iv_cek_upload);
        ivUploadGambar = findViewById(R.id.iv_upload_lokasi);

        fabOpAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNoteDialog(false, null, -1);

            }
        });

        db = new DbLokasi(this);

        lokasiList.addAll(db.getAllLokasi());

    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        LokasiActivity.this.mapboxMap = mapboxMap;
        geoJsonSource = new GeoJsonSource("source-id",
                Feature.fromGeometry(Point.fromLngLat(currentPos.getLongitude(),
                        currentPos.getLatitude())));
        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/ridwanharts/cjytgnl6o073w1cnv460nfx46"), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                initDataLokasiCollection();
                initMarkerIcons(style);
                initRecyclerviewLatlong();
                showRedMarker(style);
            }
        });
    }

    private void initMarkerIcons(@NonNull Style loadedMapStyle) {

        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_loc_blue, null);
        Bitmap bitmap = BitmapUtils.getBitmapFromDrawable(drawable);

        loadedMapStyle.addImage(SYMBOL_ICON_ID, bitmap);
        loadedMapStyle.addSource(new GeoJsonSource(SOURCE_ID, lokasiCollection));
        loadedMapStyle.addLayer(new SymbolLayer(LAYER_ID, SOURCE_ID).withProperties(
                iconImage(SYMBOL_ICON_ID),
                iconAllowOverlap(true),
                iconOffset(new Float[] {0f, -4f})
        ));
    }

    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNoteDialog(true, lokasiList.get(position), position);
                } else {
                    deleteLokasi(position);
                }
            }
        });
        builder.show();
    }

    private void showNoteDialog(final boolean shouldUpdate, final Lokasi lokasi, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.dialog_lokasi, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(LokasiActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputNote = view.findViewById(R.id.et_nama_lokasi);
        TextView etLatitude = view.findViewById(R.id.et_latitude);
        TextView etLongitude = view.findViewById(R.id.et_longitude);
        EditText etDayaDukungTanah = view.findViewById(R.id.et_dd_tanah);
        EditText etKetersediaanAir = view.findViewById(R.id.et_k_air);
        EditText etKemiringanLereng = view.findViewById(R.id.et_k_lereng);
        EditText etAksebilitas = view.findViewById(R.id.et_aksebilitas);
        EditText etPerubahanLahan = view.findViewById(R.id.et_p_lahan);
        EditText etKerawananBencana = view.findViewById(R.id.et_k_bencana);
        EditText etJarakBandara = view.findViewById(R.id.et_j_bandara);

        ImageView ivUploadGambar = view.findViewById(R.id.iv_upload_lokasi);
        ivUploadGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });

        if (shouldUpdate && lokasi != null) {
            inputNote.setText(lokasi.getNama());

        }
        etLatitude.setText(tvLatitudeLokasi.getText());
        etLongitude.setText(tvLongitudeLokasi.getText());

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                double tempDDTanah = Double.parseDouble(etDayaDukungTanah.getText().toString());
                double tempKAir = Double.parseDouble(etKetersediaanAir.getText().toString());
                double tempKLereng = Double.parseDouble(etKemiringanLereng.getText().toString());
                double tempAksebilitas = Double.parseDouble(etAksebilitas.getText().toString());
                double tempPLahan = Double.parseDouble(etPerubahanLahan.getText().toString());
                double tempKBencana = Double.parseDouble(etKerawananBencana.getText().toString());
                double tempJBandara = Double.parseDouble(etJarakBandara.getText().toString());



                if (TextUtils.isEmpty(inputNote.getText().toString())) {
                    Toast.makeText(LokasiActivity.this, "!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && lokasi != null) {
                    // update note by it's id
                    updateLokasi(inputNote.getText().toString(), position);
                } else {
                    // create new note

                    createLokasi(inputNote.getText().toString(), Double.parseDouble(lat), Double.parseDouble(longi),
                            tempDDTanah,tempKAir,tempKLereng,
                            tempAksebilitas,tempPLahan,tempKBencana,tempJBandara,
                            0);

                    Log.i("Daya Dukung Tanah",""+tempDDTanah);
                    Log.i("Ketersediaan Air",""+tempKAir);
                    Log.i("Kemiringan Lereng",""+tempKLereng);
                    Log.i("Aksebilitas",""+tempAksebilitas);
                    Log.i("Perubahan Lahan",""+tempPLahan);
                    Log.i("Kerawanan Bencana",""+tempKBencana);
                    Log.i("Jarak ke Bandara",""+tempJBandara);

                }
            }
        });
    }

    private void createLokasi(String namaLokasi, double lat, double longi,
                              double ddTanah, double kAir, double kLereng, double aksebilitas,
                              double pLahan, double kBencana, double jBandara, int a) {
        // inserting note in db and getting
        // newly inserted note id
        long id = db.insertLokasi(namaLokasi, lat, longi, ddTanah, kAir, kLereng, aksebilitas, pLahan, kBencana, jBandara, a);

        // get the newly inserted note from db
        Lokasi n = db.getLokasi(id);

        if (n != null) {
            // adding new note to array list at 0 position
            lokasiList.add(0, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

            toggleEmptyNotes();
        }
    }

    private void updateLokasi(String note, int position) {
        Lokasi n = lokasiList.get(position);
        // updating note text
        n.setNama(note);

        // updating note in db
        db.updateLokasi(n);

        // refreshing the list
        lokasiList.set(position, n);
        mAdapter.notifyItemChanged(position);

        toggleEmptyNotes();
    }

    private void deleteLokasi(int position) {

        // delete pada database server
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
        Call<Respon> call = api.delete(
                lokasiList.get(position).getNama(),
                lokasiList.get(position).getLatitude(),
                lokasiList.get(position).getLongitude()

        );
        call.enqueue(new Callback<Respon>() {
            @Override
            public void onResponse(Call<Respon> call, retrofit2.Response<Respon> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                if(value.equals("1")){
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Respon> call, Throwable t) {
                Toast.makeText(context, "Jaringan Error", Toast.LENGTH_SHORT).show();
                Log.d("cek lagi", ""+call);
            }
        });

        // deleting the note from db lokal
        db.deleteLokasi(lokasiList.get(position));

        // removing the note from the list
        lokasiList.remove(position);
        mAdapter.notifyItemRemoved(position);



        toggleEmptyNotes();
    }

    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0

        if (db.getLokasiCount() > 0) {
            //noNotesView.setVisibility(View.GONE);
        } else {
            //noNotesView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        if (!style.isFullyLoaded()) {
            return false;
        }
        if (valueAnimator != null && valueAnimator.isStarted()) {
            currentPos = (LatLng) valueAnimator.getAnimatedValue();
            valueAnimator.cancel();
        }

        valueAnimator = ObjectAnimator
                .ofObject(latLngEvaluator, currentPos, point)
                .setDuration(1000);
        valueAnimator.addUpdateListener(animatorUpdateListener);
        valueAnimator.start();

        currentPos = point;

        tvLatitudeLokasi.setText(String.valueOf(currentPos.getLatitude()));
        tvLongitudeLokasi.setText(String.valueOf(currentPos.getLongitude()));
        lat = tvLatitudeLokasi.getText().toString();
        longi = tvLongitudeLokasi.getText().toString();
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    private static final TypeEvaluator<LatLng> latLngEvaluator = new TypeEvaluator<LatLng>() {

        private final LatLng latLng = new LatLng();

        @Override
        public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
            latLng.setLatitude(startValue.getLatitude()
                    + ((endValue.getLatitude() - startValue.getLatitude()) * fraction));
            latLng.setLongitude(startValue.getLongitude()
                    + ((endValue.getLongitude() - startValue.getLongitude()) * fraction));
            return latLng;
        }
    };

    private final ValueAnimator.AnimatorUpdateListener animatorUpdateListener =
            new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    LatLng animatedPosition = (LatLng) valueAnimator.getAnimatedValue();

                    geoJsonSource.setGeoJson(Point.fromLngLat(animatedPosition.getLongitude(), animatedPosition.getLatitude()));
                }
            };

    public void showRedMarker(Style styleRed){
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_place, null);
        Bitmap bitmap = BitmapUtils.getBitmapFromDrawable(drawable);

        styleRed.addImage(("marker_icon"), bitmap);
        styleRed.addSource(geoJsonSource);
        styleRed.addLayer(new SymbolLayer("layer-id", "source-id")
                .withProperties(
                        iconImage("marker_icon"),
                        PropertyFactory.iconIgnorePlacement(true),
                        iconAllowOverlap(true)
                ));

        mapboxMap.addOnMapClickListener(LokasiActivity.this);
        LokasiActivity.this.style = styleRed;
    }

    public void initRecyclerviewLatlong(){
        recyclerView = findViewById(R.id.rv_list_lokasi);
        mAdapter = new LokasiAdapter(this, lokasiList, mapboxMap);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new ScaleInAnimator());
        recyclerView.setAdapter(mAdapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        //snapHelper.attachToRecyclerView(recyclerView);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
    }

    private void initDataLokasiCollection() {

        List<Lokasi> l = db.getLatLongLokasi();
        LatLng[] lokasi = new LatLng[l.size()];

        for(int i=0;i<l.size();i++) {
            //Log.e("cek", l.get(i).getLatitude() + ""+l.get(i).getLongitude());
            lokasi[i] = new LatLng(l.get(i).getLatitude(),l.get(i).getLongitude());
        }

        lokasiCollection = FeatureCollection.fromFeatures(new Feature[] {});
        List<Feature> lokasiList = new ArrayList<>();
        if (lokasiCollection != null) {
            for (LatLng latLngLoc : lokasi) {
                lokasiList.add(Feature.fromGeometry(Point.fromLngLat(latLngLoc.getLongitude(), latLngLoc.getLatitude())));
            }
            lokasiCollection = FeatureCollection.fromFeatures(lokasiList);
        }
    }

    private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case GALLERY_REQUEST_CODE:
                    //data.getData return the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    // Get the cursor
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();
                    //Get the column index of MediaStore.Images.Media.DATA
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    //Gets the String value in the column
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    // Set the Image in ImageView after decoding the String
                    //imageView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                    break;

            }
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

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
        Intent a = new Intent(this, MainActivity.class);
        a.putExtra("LOGIN", getLoginType());
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
        finish();
    }


}