package com.truek;

import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainBab extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_bab);

            BottomAppBar bottomAppBar = findViewById(R.id.bottom_app_bar);
            FloatingActionButton myfab = findViewById(R.id.fab);

            // Inflar el menú en el BottomAppBar
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.bottom_app_bar_menu, bottomAppBar.getMenu());

            // click event en el FAB
            myfab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MainBab.this, "FAB Clicked", Toast.LENGTH_SHORT).show();
                }
            });

            // click event en el Hamburguer menu
            bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MainBab.this, "Menu clicked", Toast.LENGTH_SHORT).show();
                }
            });

            // click event en el Bottom bar menu item
            /*bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.home) {
                        Toast.makeText(MainBab.this, "Added to favourites", Toast.LENGTH_SHORT).show();
                        return true; // Indica que se ha manejado el ítem
                    } else if (item.getItemId() == R.id.heart) {
                        Toast.makeText(MainBab.this, "Beginning search", Toast.LENGTH_SHORT).show();
                        return true; // Indica que se ha manejado el ítem
                    } else {
                        return false; // El sistema manejará la acción predeterminada
                    }
                }
            });*/
        }
}