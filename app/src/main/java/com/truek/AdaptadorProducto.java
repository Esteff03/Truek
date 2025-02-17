package com.truek;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdaptadorProducto extends RecyclerView.Adapter<AdaptadorProducto.ViewHolder> {

    private List<Producto> listaProductos;
    private OnFavoritoClickListener listener;

    public interface OnFavoritoClickListener {
        void onFavoritoClick(Producto producto);
    }

    public AdaptadorProducto(List<Producto> listaProductos, OnFavoritoClickListener listener) {
        this.listaProductos = listaProductos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);
        holder.bind(producto);
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nombreProducto;
        private ImageView imagenProducto, iconoFavorito;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreProducto = itemView.findViewById(R.id.nombre_producto);
            imagenProducto = itemView.findViewById(R.id.imagen_producto);
            iconoFavorito = itemView.findViewById(R.id.icono_favorito);
        }

        public void bind(Producto producto) {
            nombreProducto.setText(producto.getNombre());
            imagenProducto.setImageResource(producto.getImagen());

            // Mostrar el icono según el estado actual del producto
            if (FavoritosRepository.getInstance().getFavoritos().contains(producto)) {
                iconoFavorito.setImageResource(R.drawable.ic_heart);  // Producto marcado como favorito
            } else {
                iconoFavorito.setImageResource(R.drawable.ic_empty_heart);  // No es favorito
            }


            iconoFavorito.setOnClickListener(v -> {
                // Manejar el toggle directamente en el adaptador
                if (FavoritosRepository.getInstance().getFavoritos().contains(producto)) {
                    FavoritosRepository.getInstance().removeFavorito(producto);
                    iconoFavorito.setImageResource(R.drawable.ic_empty_heart);
                    Toast.makeText(v.getContext(), producto.getNombre() + " eliminado de favoritos", Toast.LENGTH_SHORT).show();
                } else {
                    FavoritosRepository.getInstance().addFavorito(producto);
                    iconoFavorito.setImageResource(R.drawable.ic_heart);
                    Toast.makeText(v.getContext(), producto.getNombre() + " agregado a favoritos", Toast.LENGTH_SHORT).show();
                }

            });
        }


    }
}
