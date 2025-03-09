package fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sbjs.truek.AdaptadorProducto;
import com.sbjs.truek.Producto;
import com.sbjs.truek.R;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FragmentGuardado extends Fragment {

    private RecyclerView recyclerFavoritos;
    private AdaptadorProducto adapter;
    private List<Producto> productList;
    private SwipeRefreshLayout swipeRefreshLayout; // 🔹 Agregado para Refresh

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guardados, container, false);

        recyclerFavoritos = view.findViewById(R.id.recycler_favoritos);
        recyclerFavoritos.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerFavoritos.setHasFixedSize(true);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh); //
        ImageView back = view.findViewById(R.id.arrow_back);
        back.setOnClickListener(v -> {
            if (getActivity() != null) {
                BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottom_nav_bar);
                if (bottomNav != null) {
                    bottomNav.setSelectedItemId(R.id.home);
                }
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        productList = new ArrayList<>();
        adapter = new AdaptadorProducto(requireContext(), productList);
        recyclerFavoritos.setAdapter(adapter);

        // Cargar favoritos al abrir el fragmento
        loadFavorites();

        // 🔹 Configurar SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadFavorites();  // Recargar la lista de favoritos
            swipeRefreshLayout.setRefreshing(false); // Detener la animación
        });

        return view;
    }

    public static FragmentGuardado newInstance(List<Producto> favoritos) {
        FragmentGuardado fragment = new FragmentGuardado();
        Bundle args = new Bundle();
        args.putSerializable("favoritos", favoritos != null ? (Serializable) favoritos : new ArrayList<>());
        fragment.setArguments(args);
        return fragment;
    }

    private void loadFavorites() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("favoritos", Context.MODE_PRIVATE);
        Set<String> favorites = sharedPreferences.getStringSet("productos", new HashSet<>());

        List<Producto> favoriteProducts = new ArrayList<>();
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Producto>>() {}.getType();

        for (String json : favorites) {
            Producto producto = gson.fromJson(json, Producto.class);
            favoriteProducts.add(producto);
        }

        productList.clear();
        productList.addAll(favoriteProducts);
        adapter.notifyDataSetChanged();
    }
}
