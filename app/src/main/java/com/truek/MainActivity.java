package com.truek;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;
import fragments.FragmentGuardado;
import fragments.FragmentHome;
import fragments.FragmentProfile;
import fragments.FragmentShare;
import fragments.FragmentVideo;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerTopVentas, recyclerElectronica, recyclerEducacion, recyclerModa;
    private AdaptadorProducto adaptadorTopVentas, adaptadorElectronica, adaptadorEducacion, adaptadorModa;
    private List<Producto> listaTopVentas, listaElectronica, listaEducacion, listaModa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Configurar BottomNavigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        loadFragment(new FragmentHome()); // Cargar Fragmento Home por defecto

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

        // 🔹 Configurar RecyclerViews para las categorías
        recyclerTopVentas = findViewById(R.id.recycler_top_ventas);
        recyclerElectronica = findViewById(R.id.recycler_electronica);
        //recyclerEducacion = findViewById(R.id.recycler_educacion);
        //recyclerModa = findViewById(R.id.recycler_moda);

        // 🔹 IMPORTANTE: Configurar cada RecyclerView con LinearLayoutManager.HORIZONTAL
        setRecyclerView(recyclerTopVentas);
        setRecyclerView(recyclerElectronica);
        //setRecyclerView(recyclerEducacion);
        //setRecyclerView(recyclerModa);

        // 🔹 Agregar datos de ejemplo a cada categoría
        listaTopVentas = new ArrayList<>();
        listaTopVentas.add(new Producto("Gafas de sol", "$50.00", R.drawable.clothes));
        listaTopVentas.add(new Producto("Bicicleta semi nueva", "$120.00", R.drawable.clothes));
        listaTopVentas.add(new Producto("Cámara DSLR", "$300.00", R.drawable.chip));

        listaElectronica = new ArrayList<>();
        listaElectronica.add(new Producto("PlayStation 5", "$499.00", R.drawable.clothes));
        listaElectronica.add(new Producto("Smartphone 5G", "$799.00", R.drawable.chip));
        listaElectronica.add(new Producto("Laptop Gaming", "$1299.00", R.drawable.clothes));

        listaEducacion = new ArrayList<>();
        listaEducacion.add(new Producto("Curso UX", "$29.00", R.drawable.chip));
        listaEducacion.add(new Producto("Guitarra", "$250.00", R.drawable.clothes));
        listaEducacion.add(new Producto("Libro de Diseño", "$45.00", R.drawable.chip));

        listaModa = new ArrayList<>();
        listaModa.add(new Producto("Chaqueta", "$60.00", R.drawable.clothes));
        listaModa.add(new Producto("Zapatillas", "$120.00", R.drawable.chip));
        listaModa.add(new Producto("Reloj de Lujo", "$999.00", R.drawable.clothes));

        // 🔹 Asignar los adaptadores a cada RecyclerView
        adaptadorTopVentas = new AdaptadorProducto(listaTopVentas);
        adaptadorElectronica = new AdaptadorProducto(listaElectronica);
        adaptadorEducacion = new AdaptadorProducto(listaEducacion);
        adaptadorModa = new AdaptadorProducto(listaModa);

        recyclerTopVentas.setAdapter(adaptadorTopVentas);
        recyclerElectronica.setAdapter(adaptadorElectronica);
        recyclerEducacion.setAdapter(adaptadorEducacion);
        recyclerModa.setAdapter(adaptadorModa);
    }

    private void setRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);  //Evita conflictos con ScrollView
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
