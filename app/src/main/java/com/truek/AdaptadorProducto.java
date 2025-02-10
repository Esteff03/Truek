package com.truek;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdaptadorProducto extends RecyclerView.Adapter<AdaptadorProducto.ViewHolder> {

    private List<Producto> listaProductos;

    public AdaptadorProducto(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
        notifyDataSetChanged();
    }

    public void setListaProductos(List<Producto> nuevaLista) {
        this.listaProductos = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (listaProductos != null && !listaProductos.isEmpty()) {
            Producto producto = listaProductos.get(position);
            holder.nombreProducto.setText(producto.getNombre());
            holder.precioProducto.setText(producto.getPrecio());
            holder.imagenProducto.setImageResource(producto.getImagenResId());
        }
    }

    @Override
    public int getItemCount() {
        return (listaProductos != null) ? listaProductos.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreProducto, precioProducto;
        ImageView imagenProducto;

        public ViewHolder(View itemView) {
            super(itemView);
            nombreProducto = itemView.findViewById(R.id.nombre_producto);
            precioProducto = itemView.findViewById(R.id.precio_producto);
            imagenProducto = itemView.findViewById(R.id.imagen_producto);
        }
    }
}
