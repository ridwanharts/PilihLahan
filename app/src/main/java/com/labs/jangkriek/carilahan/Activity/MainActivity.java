package com.labs.jangkriek.carilahan.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.labs.jangkriek.carilahan.Activity.LogInSignUp.LoginFragment.LoginAdminFragment;
import com.labs.jangkriek.carilahan.PrefConfig;
import com.labs.jangkriek.carilahan.R;
import com.labs.jangkriek.carilahan.mainViewFragment.AccountFragment;
import com.labs.jangkriek.carilahan.mainViewFragment.HomeFragment;
import com.labs.jangkriek.carilahan.mainViewFragment.HomeUserFragment;
import com.labs.jangkriek.carilahan.mainViewFragment.MapFragment;
import com.mapbox.mapboxsdk.Mapbox;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static String loginType;
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
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new LoginAdminFragment());
            }
        }

        Intent i = getIntent();
        loginType = i.getStringExtra("LOGIN");

        // kita set default nya Home Fragment
        if(loginType.equals("USER")){
            //Toast.makeText(getApplicationContext(),"Masuk USER = "+loginType, Toast.LENGTH_SHORT).show();
            loadFragment(new HomeUserFragment());
        }else if (loginType.equals("ADMIN")){
            //Toast.makeText(getApplicationContext(),"Masuk ADMIN = "+loginType, Toast.LENGTH_SHORT).show();
            loadFragment(new HomeFragment());
        }
        // inisialisasi BottomNavigaionView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bn_main);
        // beri listener pada saat item/menu bottomnavigation terpilih
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        //getSupportActionBar().setElevation(0);

    }

    public static String getLoginType(){
        return loginType;
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
                if(loginType.equals("USER")){
                    fragment = new HomeUserFragment();
                }else if (loginType.equals("ADMIN")){
                    fragment = new HomeFragment();
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
