package com.truek;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Cargar el idioma guardado en SharedPreferences
        loadLocale();
    }

    // Método para cargar el idioma guardado en SharedPreferences
    public void loadLocale() {
        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String language = preferences.getString("language", "es"); // Por defecto, español
        setLocale(language);
    }

    // Método para cambiar el idioma
    public void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
}