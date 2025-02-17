package fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.truek.AdaptadorProducto;
import com.truek.FavoritosRepository;
import com.truek.Producto;
import com.truek.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class FragmentGuardado extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guardados, container, false);

        RecyclerView recyclerFavoritos = view.findViewById(R.id.recycler_favoritos);
        recyclerFavoritos.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerFavoritos.setHasFixedSize(true);

        List<Producto> favoritos = FavoritosRepository.getInstance().getFavoritos();

        AdaptadorProducto adaptador = new AdaptadorProducto(favoritos, producto -> {
            // Puedes implementar una acción si es necesario
        });
        recyclerFavoritos.setAdapter(adaptador);

        return view;
    }



    public static FragmentGuardado newInstance(List<Producto> favoritos) {
        FragmentGuardado fragment = new FragmentGuardado();
        Bundle args = new Bundle();
        args.putSerializable("favoritos", (Serializable) favoritos);
        fragment.setArguments(args);
        return fragment;
    }

}

