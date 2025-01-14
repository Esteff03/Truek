package com.truek;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        Button btnStart = findViewById(R.id.btn_start);

        btnStart.setOnClickListener(v -> {
            loadFragment();
        });


    }

    private void loadFragment() {

        // Crear una instancia del fragmento
        LoginFragment loginFragment = new LoginFragment();

        // Realizar la transicion del fragmento
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Aplicar las animaciones de entrada y salida
        fragmentTransaction.setCustomAnimations(
                R.anim.anim_rigth_left,  // Animación para la entrada
                R.anim.anim_left_rigth   // Animación para la salida
        );

        // Reemplazar el fragmento
        fragmentTransaction.replace(R.id.fragment_container, loginFragment);
        fragmentTransaction.addToBackStack(null); // Permite regresar al estado anterior
        fragmentTransaction.commit();

        // Hacer visible el contenedor
        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
    }
}
