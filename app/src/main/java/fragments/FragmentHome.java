package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.truek.AdaptadorProducto;
import com.truek.Categories;
import com.truek.FavoritosRepository;
import com.truek.Producto;
import com.truek.R;
import com.truek.MainView;
import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {

    private RecyclerView recyclerProductos;
    private TextView textViewNoData; // Agregado para manejar pantalla en blanco
    private AdaptadorProducto adaptadorProductos;
    private List<Producto> listaProductos;
    private List<Producto> productosFavoritos;
    private FirebaseAuth auth;

    public FragmentHome() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        auth = FirebaseAuth.getInstance();
        productosFavoritos = new ArrayList<>();

        // Configuración de la UI
        ImageView ajustes = view.findViewById(R.id.ajustes);
        ImageView messageIcon = view.findViewById(R.id.message);
        EditText searchField = view.findViewById(R.id.buscador);
        Button btnCategories = view.findViewById(R.id.button1);
        Button btnFavorites = view.findViewById(R.id.button2);
        textViewNoData = view.findViewById(R.id.tv_no_data); // Inicializar el mensaje de no hay datos

        ajustes.setOnClickListener(v -> mostrarMenu(v));

        messageIcon.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Abrir mensajes", Toast.LENGTH_SHORT).show());

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

        // Configurar RecyclerView
        recyclerProductos = view.findViewById(R.id.recycler_productos);
        recyclerProductos.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerProductos.setHasFixedSize(true);

        // Agregar datos de prueba
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

        // Asignar el adaptador
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

        // ✅ Verificar si la lista de productos está vacía
        if (listaProductos.isEmpty()) {
            textViewNoData.setVisibility(View.VISIBLE); // Mostrar mensaje
            recyclerProductos.setVisibility(View.GONE); // Ocultar lista
        } else {
            textViewNoData.setVisibility(View.GONE); // Ocultar mensaje
            recyclerProductos.setVisibility(View.VISIBLE); // Mostrar lista
        }

        return view;
    }

    private void mostrarMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_settings, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_logout) {
                cerrarSesion();
                return true;
            } else if (item.getItemId() == R.id.menu_delete_account) {
                confirmarEliminacionCuenta();
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    private void cerrarSesion() {
        auth.signOut();
        Toast.makeText(getActivity(), "Sesión cerrada", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), MainView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void confirmarEliminacionCuenta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Eliminar cuenta");
        builder.setMessage("¿Estás seguro de que deseas eliminar tu cuenta? Esta acción no se puede deshacer.");
        builder.setPositiveButton("Sí", (dialog, which) -> eliminarCuenta());
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void eliminarCuenta() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            user.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Cuenta eliminada", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainView.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Error al eliminar cuenta", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public List<Producto> getProductosFavoritos() {
        return productosFavoritos;
    }
}
