package com.truek;

import android.os.Bundle;
import android.view.View;
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

        TextView btnLogin = findViewById(R.id.btn_login);
        ImageView arrowIcon = findViewById(R.id.arrow_icon);

        View.OnClickListener loginClickListener = v -> {
            loadFragment();
        };

        // Asignar mismo listener TextView e ImageView
        btnLogin.setOnClickListener(loginClickListener);
        arrowIcon.setOnClickListener(loginClickListener);
    }

    private void loadFragment() {
        // Crear una instancia del fragmento
        LoginFragment loginFragment = new LoginFragment();

        //transición del fragmento
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //Aplicar las animaciones de entrada y salida
        fragmentTransaction.setCustomAnimations(
                R.anim.anim_rigth_left,
                R.anim.anim_left_rigth
        );

        //Reemplazar el fragmento
        fragmentTransaction.replace(R.id.fragment_container, loginFragment);
        fragmentTransaction.addToBackStack(null); //Hace posible regresar al estado inicial
        fragmentTransaction.commit();

        // Hacer visible el contenedor
        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
    }
}
