package fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.truek.AdaptadorProducto;
import com.truek.Producto;
import com.truek.R;
import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {

    private RecyclerView recyclerProductos;
    private AdaptadorProducto adaptadorProductos;
    private List<Producto> listaProductos;

    public FragmentHome() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Configuración de Toolbar
        ImageView logo = view.findViewById(R.id.logo);
        ImageView messageIcon = view.findViewById(R.id.message);
        EditText searchField = view.findViewById(R.id.buscador);
        Button btnCategories = view.findViewById(R.id.button1);
        Button btnFavorites = view.findViewById(R.id.button2);

        // Eventos de clic
        logo.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Configuraciones", Toast.LENGTH_SHORT).show());

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
}
