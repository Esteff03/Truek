package fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sbjs.truek.FavoritosRepository;
import com.sbjs.truek.Producto;
import com.sbjs.truek.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FragmentGuardado extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guardados, container, false);

        RecyclerView recyclerFavoritos = view.findViewById(R.id.recycler_favoritos);
        recyclerFavoritos.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerFavoritos.setHasFixedSize(true);

        ImageView back = view.findViewById(R.id.arrow_back);
        back.setOnClickListener(v -> {
            if (getActivity() != null) {
                // Actualizar la selección del BottomNavigationView antes de volver atrás
                BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottom_nav_bar);
                if (bottomNav != null) {
                    bottomNav.setSelectedItemId(R.id.home); // Cambia a la pestaña que corresponda
                }

                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        // Recuperar favoritos desde argumentos o el repositorio
        List<Producto> favoritos;
        if (getArguments() != null && getArguments().containsKey("favoritos")) {
            favoritos = (List<Producto>) getArguments().getSerializable("favoritos");
        } else {
            favoritos = FavoritosRepository.getInstance().getFavoritos();
        }

        // Asegurar que la lista no sea null
        if (favoritos == null) {
            favoritos = new ArrayList<>();
        }


        return view;
    }

    public static FragmentGuardado newInstance(List<Producto> favoritos) {
        FragmentGuardado fragment = new FragmentGuardado();
        Bundle args = new Bundle();
        args.putSerializable("favoritos", favoritos != null ? (Serializable) favoritos : new ArrayList<>());
        fragment.setArguments(args);
        return fragment;
    }
}
