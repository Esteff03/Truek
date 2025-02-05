package fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.truek.R;

public class FragmentHome extends Fragment {

    public FragmentHome() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar la vista del fragmento
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Obtener referencias a los elementos de la Toolbar
        ImageView logo = view.findViewById(R.id.logo);
        ImageView messageIcon = view.findViewById(R.id.message);
        EditText searchField = view.findViewById(R.id.buscador);
        Button btnCategories = view.findViewById(R.id.button1);
        Button btnFavorites = view.findViewById(R.id.button2);

        // Agregar eventos de clic a los botones/iconos
        logo.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Configuraciones", Toast.LENGTH_SHORT).show()
        );

        messageIcon.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Abrir mensajes", Toast.LENGTH_SHORT).show()
        );

        btnCategories.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Abrir categorías", Toast.LENGTH_SHORT).show()
        );

        btnFavorites.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Abrir favoritos", Toast.LENGTH_SHORT).show()
        );

        return view;
    }
}
