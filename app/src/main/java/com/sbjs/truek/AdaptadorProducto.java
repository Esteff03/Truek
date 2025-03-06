package com.sbjs.truek;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdaptadorProducto extends RecyclerView.Adapter<AdaptadorProducto.ProductViewHolder> {
    private Context context;
    private List<Producto> productoList;

    public AdaptadorProducto(Context context, List<Producto> productoList) {
        this.context = context;
        this.productoList = productoList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Producto producto = productoList.get(position);
        holder.name.setText(producto.getNombre());
        holder.price.setText("€" + producto.getPrecio());

        // Verificar si la URL no está vacía y tiene un formato válido
        if (producto.getImagenUrl() != null && !producto.getImagenUrl().trim().isEmpty()) {
            Glide.with(context)
                    .load(producto.getImagenUrl())
                    .placeholder(R.drawable.rounded_image) // Imagen temporal mientras carga
                    .error(R.drawable.ic_heart) // Imagen en caso de error
                    .into(holder.image);
        } else {
            // Si la URL está vacía, usa una imagen por defecto
            holder.image.setImageResource(R.drawable.rounded_image);
        }
    }


    @Override
    public int getItemCount() {
        return productoList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        ImageView image;

        public ProductViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            image = itemView.findViewById(R.id.product_image);
        }
    }
}
