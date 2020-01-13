package com.labs.jangkriek.carilahan.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.labs.jangkriek.carilahan.Activity.LogInSignUp.LoginUsersFragment;
import com.labs.jangkriek.carilahan.PrefConfig;
import com.labs.jangkriek.carilahan.R;
import com.labs.jangkriek.carilahan.mainViewFragment.AccountFragment;
import com.labs.jangkriek.carilahan.mainViewFragment.UsersHomeFragment;
import com.labs.jangkriek.carilahan.mainViewFragment.GuestHomeFragment;
import com.labs.jangkriek.carilahan.mainViewFragment.MapFragment;
import com.mapbox.mapboxsdk.Mapbox;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static String loginType;
    public static int idUser;
    private ProgressDialog progressDialog;
    public static PrefConfig prefConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_main);

        prefConfig = new PrefConfig(this);

        if(findViewById(R.id.fragment_container) != null){
            if (prefConfig.readLoginStatus()){
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new LoginUsersFragment());
            }
        }

        Intent i = getIntent();
        loginType = i.getStringExtra("LOGIN");
        idUser = i.getIntExtra("ID_USER", idUser);

        // kita set default nya Home Fragment
        if(loginType.equals("GUEST")){
            //Toast.makeText(getApplicationContext(),"Masuk USER = "+loginType, Toast.LENGTH_SHORT).show();
            loadFragment(new GuestHomeFragment());
        }else {
            //Toast.makeText(getApplicationContext(),"Masuk ADMIN = "+loginType, Toast.LENGTH_SHORT).show();
            loadFragment(new UsersHomeFragment());
        }
        // inisialisasi BottomNavigaionView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bn_main);
        // beri listener pada saat item/menu bottomnavigation terpilih
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        //getSupportActionBar().setElevation(0);

    }

    public static String getUsername(){
        return loginType;
    }

    public static int getIdUser(){
        return idUser;
    }

    private boolean loadFragment(Fragment fragment) {

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;
        switch (menuItem.getItemId()){
            case R.id.home_menu:
                if(loginType.equals("GUEST")){
                    fragment = new GuestHomeFragment();
                }else {
                    fragment = new UsersHomeFragment();
                }
                //getSupportActionBar().show();
                break;
            case R.id.map_menu:
                fragment = new MapFragment();
                //getSupportActionBar().hide();
                break;
            case R.id.account_menu:
                fragment = new AccountFragment();
                break;
        }
        return loadFragment(fragment);
    }

}
