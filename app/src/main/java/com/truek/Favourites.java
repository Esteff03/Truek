package com.truek;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Favourites extends AppCompatActivity {

        RecyclerView recyclerView;
        FavouritesAdapter favoritesAdapter;
        List<Product> favoriteProducts;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_favourites);

            recyclerView = findViewById(R.id.recycler_view_favoriutes);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Inicializar la lista de productos favoritos
            favoriteProducts = new ArrayList<>();
            favoriteProducts.add(new Product("Producto 1", "Descripción del producto 1", "https://urlimagen1.com"));
            favoriteProducts.add(new Product("Producto 2", "Descripción del producto 2", "https://urlimagen2.com"));
            favoriteProducts.add(new Product("Producto 3", "Descripción del producto 3", "https://urlimagen3.com"));

            // Configurar el adaptador
            favoritesAdapter = new FavouritesAdapter(favoriteProducts);
            recyclerView.setAdapter(favoritesAdapter);
        }
}