package com.truek;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class CreateAccount extends AppCompatActivity {
    private Spinner countrySpinner;
    private ImageView countryFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);

        // Inicializar vistas
        countrySpinner = findViewById(R.id.country_spinner);
        countryFlag = findViewById(R.id.country_flag);

        // Cargar lista de países desde resources
        String[] countryCodes = getResources().getStringArray(R.array.country_codes);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, countryCodes);
        countrySpinner.setAdapter(adapter);

        // Manejar selección de países
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Cambiar bandera según el país seleccionado
                switch (position) {
                    case 0: // United Kingdom
                        countryFlag.setImageResource(R.drawable.flag_ic_uk);
                        break;
                    case 1: // United States
                        countryFlag.setImageResource(R.drawable.flag_ic_us);
                        break;
                    case 2: // Canada
                        countryFlag.setImageResource(R.drawable.flag_ic_canada);
                        break;
                    case 3: // India
                        countryFlag.setImageResource(R.drawable.flag_ic_india);
                        break;
                    case 4: // Australia
                        countryFlag.setImageResource(R.drawable.flag_ic_australia);
                        break;
                    case 5: // France
                        countryFlag.setImageResource(R.drawable.flag_ic_australia);
                        break;
                    case 6: // Germany
                        countryFlag.setImageResource(R.drawable.flag_ic_germany);
                        break;
                    case 7: // Spain
                        countryFlag.setImageResource(R.drawable.flag_ic_spain);
                        break;
                    case 8: // Italy
                        countryFlag.setImageResource(R.drawable.flag_ic_italy);
                        break;
                    case 9: // Japan
                        countryFlag.setImageResource(R.drawable.flag_ic_japan);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se selecciona nada
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}