package fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sbjs.truek.AdaptadorProducto;
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
        adapter = new AdaptadorProducto(getContext(), productList);

        // Configurar GridLayout para mostrar productos en 2 columnas
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);

        fetchProductsFromSupabase();
        return view;
    }

    private void fetchProductsFromSupabase() {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(SUPABASE_URL)
                    .header("apikey", SUPABASE_API_KEY)
                    .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                    .header("Content-Type", "application/json")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body() != null ? response.body().string() : "Respuesta vacía";

                Log.d("SupabaseResponse", "Respuesta completa de Supabase: " + responseBody);

                if (response.isSuccessful() && !responseBody.equals("[]")) {
                    JSONArray jsonArray = new JSONArray(responseBody);

                    List<Producto> tempList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.optString("name", "Sin nombre");
                        String price = jsonObject.optString("price", "0");
                        String description = jsonObject.optString("description", "Sin descripción");
                        String imageUrl = jsonObject.optString("image_url", "");

                        Log.d("ProductData", "Producto: " + name + ", Precio: " + price + ", Imagen: " + imageUrl);

                        tempList.add(new Producto(name, price, description, imageUrl));
                    }

                    getActivity().runOnUiThread(() -> {
                        productList.clear();
                        productList.addAll(tempList);
                        adapter.notifyDataSetChanged();

                        tvNoData.setVisibility(productList.isEmpty() ? View.VISIBLE : View.GONE);
                    });

                } else {
                    Log.e("SupabaseResponse", "Error en la petición o no hay productos: " + response.message());
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                Log.e("SupabaseError", "Error al obtener los productos", e);
            }
        }).start();
    }

}
