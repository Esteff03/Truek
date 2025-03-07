package com.sbjs.truek;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        // Verificar si la URL de la imagen es válida
        if (producto.getImagenUrl() != null && !producto.getImagenUrl().trim().isEmpty()) {
            Glide.with(context)
                    .load(producto.getImagenUrl())
                    .placeholder(R.drawable.rounded_image) // Imagen temporal mientras carga
                    .error(R.drawable.rounded_image) // Imagen en caso de error
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.rounded_image);
        }

        // Configurar el estado inicial del icono de favorito
        boolean isFavorite = isProductFavorite(context, producto.getNombre());
        if (isFavorite) {
            holder.favIcon.setImageResource(R.drawable.ic_heart); // Ícono lleno si es favorito
        } else {
            holder.favIcon.setImageResource(R.drawable.ic_empty_heart); // Ícono vacío si no es favorito
        }

        // Manejar el evento de clic en el icono de favorito
        holder.favIcon.setOnClickListener(v -> {
            boolean currentlyFavorite = isProductFavorite(context, producto.getNombre());

            if (currentlyFavorite) {
                removeProductFromFavorites(context, producto.getNombre());
                holder.favIcon.setImageResource(R.drawable.ic_empty_heart); // Ícono vacío
            } else {
                addProductToFavorites(context, producto);
                holder.favIcon.setImageResource(R.drawable.ic_heart); // Ícono lleno
            }
        });
    }

    @Override
    public int getItemCount() {
        return productoList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        ImageView image, favIcon; // 🔹 Agregamos favIcon

        public ProductViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            image = itemView.findViewById(R.id.product_image);
            favIcon = itemView.findViewById(R.id.heart); // Asegúrate de que este ID exista en tu XML
        }
    }

    private void addProductToFavorites(Context context, Producto producto) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("favoritos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String productJson = gson.toJson(producto);

        Set<String> favorites = sharedPreferences.getStringSet("productos", new HashSet<>());
        Set<String> updatedFavorites = new HashSet<>(favorites); // Crear una copia modificable
        updatedFavorites.add(productJson);

        editor.putStringSet("productos", updatedFavorites);
        editor.apply();
    }

    private void removeProductFromFavorites(Context context, String productName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("favoritos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Set<String> favorites = sharedPreferences.getStringSet("productos", new HashSet<>());
        Set<String> updatedFavorites = new HashSet<>();

        Gson gson = new Gson();
        for (String json : favorites) {
            Producto prod = gson.fromJson(json, Producto.class);
            if (!prod.getNombre().equals(productName)) { // Usa `getNombre()`
                updatedFavorites.add(json);
            }
        }

        editor.putStringSet("productos", updatedFavorites);
        editor.apply();
    }

    private boolean isProductFavorite(Context context, String productName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("favoritos", Context.MODE_PRIVATE);
        Set<String> favorites = sharedPreferences.getStringSet("productos", new HashSet<>());

        Gson gson = new Gson();
        for (String json : favorites) {
            Producto prod = gson.fromJson(json, Producto.class);
            if (prod.getNombre().equals(productName)) { // Usa `getNombre()`
                return true;
            }
        }
        return false;
    }
}
