package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sbjs.truek.AdaptadorProducto;
import com.sbjs.truek.Categories;
import com.sbjs.truek.Notifications;
import com.sbjs.truek.Producto;
import com.sbjs.truek.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentHome extends Fragment {
    private static final String SUPABASE_URL = "https://pgosafydlwskwtvnuokk.supabase.co/rest/v1/products?select=*";
    private static final String SUPABASE_API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InBnb3NhZnlkbHdza3d0dm51b2trIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Mzk0MzgyMTcsImV4cCI6MjA1NTAxNDIxN30.EmB_NLAqXji3UhtgaQsc4VmGrtnUHQlNiAb6Oau3fQo";

    private RecyclerView recyclerView;
    private AdaptadorProducto adapter;
    private List<Producto> productList;
    private TextView tvNoData;
    private Button categorias, favoritos;
    private ImageView notificaciones;

    public FragmentHome() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycler_productos);
        tvNoData = view.findViewById(R.id.tv_no_data);
        productList = new ArrayList<>();
        adapter = new AdaptadorProducto(requireContext(), productList);

        categorias = view.findViewById(R.id.button1);
        favoritos = view.findViewById(R.id.button2);
        notificaciones = view.findViewById(R.id.message);

        categorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Categories.class);
                startActivity(intent);
            }
        });

        favoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentGuardado fragmentGuardado = new FragmentGuardado();

                // Reemplaza el fragmento
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragmentGuardado)
                        .addToBackStack(null)
                        .commit();

                // 🔹 Cambia la selección del BottomNavigationView
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav_bar);
                bottomNavigationView.setSelectedItemId(R.id.heart); // ID del botón de favoritos en el navbar
            }
        });

        notificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Notifications.class);
                startActivity(intent);
            }
        });


        // Configurar GridLayoutManager para mostrar productos en 2 columnas
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(adapter);

        fetchProductsFromSupabase();
        return view;
    }

    private void fetchProductsFromSupabase() {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(SUPABASE_URL)
                        .header("apikey", SUPABASE_API_KEY)
                        .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                        .header("Content-Type", "application/json")
                        .build();

                Response response = client.newCall(request).execute();
                if (response.body() == null) {
                    Log.e("SupabaseError", "La respuesta de Supabase es nula");
                    updateUIWithNoData();
                    return;
                }

                String responseBody = response.body().string();
                Log.d("SupabaseResponse", "Respuesta de Supabase: " + responseBody);

                if (!response.isSuccessful()) {
                    Log.e("SupabaseError", "Error en la petición. Código: " + response.code());
                    updateUIWithNoData();
                    return;
                }

                JSONArray jsonArray = new JSONArray(responseBody);
                if (jsonArray.length() == 0) {
                    Log.e("SupabaseError", "La respuesta de Supabase está vacía.");
                    updateUIWithNoData();
                    return;
                }

                List<Producto> tempList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    // Extraer valores del JSON
                    String name = jsonObject.optString("name", "Sin nombre");
                    String price = jsonObject.optString("price", "0");
                    String description = jsonObject.optString("description", "Sin descripción");
                    String imageUrl = jsonObject.optString("image_url", ""); // URL de la base de datos

                    // Validar si la URL ya es completa o si necesita el prefijo
                    if (!imageUrl.startsWith("https://")) {
                        String baseUrl = "https://pgosafydlwskwtvnuokk.supabase.co/storage/v1/object/public/Imagenes_1/";
                        imageUrl = baseUrl + imageUrl;
                    }
                    Log.d("ProductData", "Producto: " + name + ", Precio: " + price + ", Imagen URL: " + imageUrl);

                    tempList.add(new Producto(name, price, description, imageUrl));
                }

                updateUIWithProducts(tempList);

            } catch (IOException | JSONException e) {
                Log.e("SupabaseError", "Error al obtener los productos", e);
                updateUIWithNoData();
            }
        }).start();
    }


    private void updateUIWithProducts(List<Producto> products) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                productList.clear();
                productList.addAll(products);
                adapter.notifyDataSetChanged();
                tvNoData.setVisibility(productList.isEmpty() ? View.VISIBLE : View.GONE);
            });
        }
    }

    private void updateUIWithNoData() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                productList.clear();
                adapter.notifyDataSetChanged();
                tvNoData.setVisibility(View.VISIBLE);
            });
        }
    }
}
