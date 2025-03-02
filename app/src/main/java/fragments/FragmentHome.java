package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.truek.AdaptadorProducto;
import com.truek.Categories;
import com.truek.Notifications;
import com.truek.FavoritosRepository;
import com.truek.Producto;
import com.truek.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {

    private RecyclerView recyclerProductos;
    private TextView textViewNoData;
    private AdaptadorProducto adaptadorProductos;
    private List<Producto> listaProductos;
    private List<Producto> productosFavoritos;

    public FragmentHome() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        productosFavoritos = new ArrayList<>();

        ImageView ajustes = view.findViewById(R.id.ajustes);
        ImageView messageIcon = view.findViewById(R.id.message);
        Button btnCategories = view.findViewById(R.id.button1);
        Button btnFavorites = view.findViewById(R.id.button2);
        textViewNoData = view.findViewById(R.id.tv_no_data);

        ajustes.setOnClickListener(v -> mostrarMenu(v));

        messageIcon.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Notifications.class);
            startActivity(intent);
        });

        btnCategories.setOnClickListener(v ->{
            Intent intent = new Intent(getActivity(), Categories.class);
            startActivity(intent);
        });

        btnFavorites.setOnClickListener(v -> {
            FragmentGuardado fragmentGuardado = new FragmentGuardado();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragmentGuardado)
                    .addToBackStack(null)
                    .commit();

            BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottom_nav_bar);
            bottomNav.setSelectedItemId(R.id.heart);
        });

        recyclerProductos = view.findViewById(R.id.recycler_productos);
        recyclerProductos.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerProductos.setHasFixedSize(true);

        listaProductos = new ArrayList<>();
        listaProductos.add(new Producto("PlayStation 5", "$499.00", R.drawable.chip, ""));
        listaProductos.add(new Producto("Smartphone 5G", "$799.00", R.drawable.chip, ""));
        listaProductos.add(new Producto("Laptop Gaming", "$1299.00", R.drawable.chip, ""));
        listaProductos.add(new Producto("Curso UX", "$29.00", R.drawable.chip, ""));
        listaProductos.add(new Producto("Guitarra", "$250.00", R.drawable.chip, ""));
        listaProductos.add(new Producto("Libro de Diseño", "$45.00", R.drawable.chip, ""));
        listaProductos.add(new Producto("Chaqueta", "$60.00", R.drawable.clothes, ""));
        listaProductos.add(new Producto("Zapatillas", "$120.00", R.drawable.clothes, ""));
        listaProductos.add(new Producto("Reloj de Lujo", "$999.00", R.drawable.clothes, ""));

        adaptadorProductos = new AdaptadorProducto(listaProductos, new AdaptadorProducto.OnFavoritoClickListener() {
            @Override
            public void onFavoritoClick(Producto producto) {
                if (FavoritosRepository.getInstance().getFavoritos().contains(producto)) {
                    FavoritosRepository.getInstance().removeFavorito(producto);
                    Toast.makeText(getActivity(), producto.getNombre() + " eliminado de favoritos", Toast.LENGTH_SHORT).show();
                } else {
                    FavoritosRepository.getInstance().addFavorito(producto);
                    Toast.makeText(getActivity(), producto.getNombre() + " agregado a favoritos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerProductos.setAdapter(adaptadorProductos);

        if (listaProductos.isEmpty()) {
            textViewNoData.setVisibility(View.VISIBLE);
            recyclerProductos.setVisibility(View.GONE);
        } else {
            textViewNoData.setVisibility(View.GONE);
            recyclerProductos.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void mostrarMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_settings, popupMenu.getMenu());

        popupMenu.show();
    }

    public List<Producto> getProductosFavoritos() {
        return productosFavoritos;
    }
}
