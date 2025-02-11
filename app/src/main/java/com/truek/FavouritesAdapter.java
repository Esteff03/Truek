package com.truek;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavoritesViewHolder> {

    private List<Product> favoriteProducts;

    public FavouritesAdapter(List<Product> favoriteProducts) {
        this.favoriteProducts = favoriteProducts;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favourite_product, parent, false);
        return new FavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        Product product = favoriteProducts.get(position);
        holder.productName.setText(product.getName());
        holder.productDescription.setText(product.getDescription());

        // Cargar imagen usando Glide
        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl())  // Aquí usas la URL de la imagen
                .placeholder(R.drawable.favourite_product)  // Imagen por defecto mientras se carga
                .into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return favoriteProducts.size();
    }

    // ViewHolder interno
    public static class FavoritesViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName;
        TextView productDescription;

        public FavoritesViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productDescription = itemView.findViewById(R.id.product_description);
        }
    }
}