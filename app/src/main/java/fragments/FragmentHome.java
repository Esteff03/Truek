package fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.truek.AdaptadorProducto;
import com.truek.Producto;
import com.truek.R;
import com.truek.MainView;
import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {

    private RecyclerView recyclerProductos;
    private AdaptadorProducto adaptadorProductos;
    private List<Producto> listaProductos;
    private FirebaseAuth auth;

    public FragmentHome() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        auth = FirebaseAuth.getInstance();

        // Configuración de Toolbar
        ImageView ajustes = view.findViewById(R.id.ajustes);
        ImageView messageIcon = view.findViewById(R.id.message);
        EditText searchField = view.findViewById(R.id.buscador);
        Button btnCategories = view.findViewById(R.id.button1);
        Button btnFavorites = view.findViewById(R.id.button2);

        // Evento de clic para mostrar el menú desplegable
        ajustes.setOnClickListener(v -> mostrarMenu(v));

        messageIcon.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Abrir mensajes", Toast.LENGTH_SHORT).show());

        btnCategories.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Abrir categorías", Toast.LENGTH_SHORT).show());

        btnFavorites.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Abrir favoritos", Toast.LENGTH_SHORT).show());

        // Configurar RecyclerView con GridLayoutManager
        recyclerProductos = view.findViewById(R.id.recycler_productos);
        recyclerProductos.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columnas
        recyclerProductos.setHasFixedSize(true);

        // Agregar datos de ejemplo
        listaProductos = new ArrayList<>();
        listaProductos.add(new Producto("PlayStation 5", "$499.00", R.drawable.chip));
        listaProductos.add(new Producto("Smartphone 5G", "$799.00", R.drawable.chip));
        listaProductos.add(new Producto("Laptop Gaming", "$1299.00", R.drawable.chip));
        listaProductos.add(new Producto("Curso UX", "$29.00", R.drawable.chip));
        listaProductos.add(new Producto("Guitarra", "$250.00", R.drawable.chip));
        listaProductos.add(new Producto("Libro de Diseño", "$45.00", R.drawable.chip));
        listaProductos.add(new Producto("Chaqueta", "$60.00", R.drawable.clothes));
        listaProductos.add(new Producto("Zapatillas", "$120.00", R.drawable.clothes));
        listaProductos.add(new Producto("Reloj de Lujo", "$999.00", R.drawable.clothes));

        // Asignar el adaptador
        adaptadorProductos = new AdaptadorProducto(listaProductos);
        recyclerProductos.setAdapter(adaptadorProductos);

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
}
