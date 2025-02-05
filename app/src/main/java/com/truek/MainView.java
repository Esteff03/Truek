package com.truek;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import fragments.Fragment_CreateAccount;
import fragments.Fragment_Login;

public class MainView extends BaseActivity { // Hereda de BaseActivity

    private ImageView flagIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        Button btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(v -> loadLoginFragment());

        TextView btnRegister = findViewById(R.id.btn_register);
        ImageView arrowRegister = findViewById(R.id.arrow_register);

        View.OnClickListener goToCreateAccountListener = v -> loadCreateAccountFragment();
        btnRegister.setOnClickListener(goToCreateAccountListener);
        arrowRegister.setOnClickListener(goToCreateAccountListener);

        // Selector de idioma
        LinearLayout languageSelector = findViewById(R.id.language_selector);
        flagIcon = findViewById(R.id.flag_icon);

        // Cargar la bandera guardada desde SharedPreferences
        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String savedFlag = preferences.getString("flag", "flag_ic_spain");
        updateFlagIcon(savedFlag);

        // Menu desplegable
        languageSelector.setOnClickListener(view -> showLanguageMenu(languageSelector));
    }

    private void showLanguageMenu(LinearLayout languageSelector) {
        // Crea un menú desplegable PopupMenu
        PopupMenu popupMenu = new PopupMenu(this, languageSelector);
        popupMenu.getMenuInflater().inflate(R.menu.language_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            String langCode = "";
            String flagResource = "";

            if (item.getItemId() == R.id.language_spanish) {
                Log.d("LanguageChange", "Spanish selected");
                langCode = "es";
                flagResource = "flag_ic_spain";
            } else if (item.getItemId() == R.id.language_english) {
                Log.d("LanguageChange", "English selected");
                langCode = "en";
                flagResource = "flag_ic_us";
            } else if (item.getItemId() == R.id.language_french) {
                Log.d("LanguageChange", "French selected");
                langCode = "fr";
                flagResource = "flag_ic_france";
            } else if (item.getItemId() == R.id.language_italian) {
                Log.d("LanguageChange", "Italian selected");
                langCode = "it";
                flagResource = "flag_ic_italy";
            }

            if (!langCode.isEmpty()) {
                // Guardar el idioma seleccionado en SharedPreferences
                SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("language", langCode);
                editor.putString("flag", flagResource);
                editor.apply();

                setLocale(langCode);
                updateFlagIcon(flagResource);
                recreate(); // Recrear la actividad para aplicar el cambio de idioma
            }
            return true;
        });

        popupMenu.show();
    }

    // Método para actualizar el icono de la bandera
    private void updateFlagIcon(String flagResource) {
        int flagResId = getResources().getIdentifier(flagResource, "drawable", getPackageName());
        if (flagResId != 0) {
            flagIcon.setImageResource(flagResId);
        } else {
            flagIcon.setImageResource(R.drawable.flag_ic_spain);
        }
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
