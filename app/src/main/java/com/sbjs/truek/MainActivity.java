package com.sbjs.truek;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;



import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import fragments.FragmentGuardado;
import fragments.FragmentHome;
import fragments.FragmentProfile;
import fragments.FragmentShare;
import fragments.FragmentVideo;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new FragmentHome())
                    .commit();
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.home) {
                selectedFragment = new FragmentHome();
            } else if (item.getItemId() == R.id.heart) {
                selectedFragment = new FragmentGuardado();
            } else if (item.getItemId() == R.id.share) {
                selectedFragment = new FragmentShare();
            } else if (item.getItemId() == R.id.video) {
                selectedFragment = new FragmentVideo();
            } else if (item.getItemId() == R.id.profile) {
                selectedFragment = new FragmentProfile();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }

            return false;
        });

    }

    private void loadFragment(Fragment fragment) {
        // Realiza una transacción de fragmento, reemplazando el fragmento actual por el nuevo
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }



}
