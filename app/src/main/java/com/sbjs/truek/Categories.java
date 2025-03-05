package com.sbjs.truek;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Categories extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_categories);

        ImageView arrowBack = findViewById(R.id.arrow_back);
        Button buttonmain = findViewById(R.id.button);

        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Volver a MainActivity
                Intent intent = new Intent(Categories.this, MainActivity.class);
                startActivity(intent);
                finish(); // Cierra la actividad actual para que no se acumule en la pila
            }
        });

        buttonmain.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Volver a MainActivity
                Intent intent = new Intent(Categories.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}