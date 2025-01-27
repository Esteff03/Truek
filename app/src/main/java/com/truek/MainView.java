package com.truek;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        // Botón "Let's Get Started"
        Button btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(v -> loadLoginFragment());

        // Botón "I already have an account" y flecha
        TextView btnRegister = findViewById(R.id.btn_register);
        ImageView arrowRegister = findViewById(R.id.arrow_register);

        View.OnClickListener goToCreateAccountListener = v -> loadCreateAccountFragment();
        btnRegister.setOnClickListener(goToCreateAccountListener);
        arrowRegister.setOnClickListener(goToCreateAccountListener);
    }

    private void loadLoginFragment() {
        Fragment_Login fragmentLogin = new Fragment_Login();
        replaceFragment(fragmentLogin);
    }

    private void loadCreateAccountFragment() {
        Fragment_CreateAccount fragmentCreateAccount = new Fragment_CreateAccount();
        replaceFragment(fragmentCreateAccount);
    }

    private void replaceFragment(androidx.fragment.app.Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Animaciones para las transiciones entre fragmentos
        fragmentTransaction.setCustomAnimations(
                R.anim.anim_rigth_left, // Entrada
                R.anim.anim_left_rigth // Salida
        );

        // Reemplazar el fragmento en el contenedor
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        // Hacer visible el contenedor del fragmento
        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
    }
}
