package com.sbjs.truek;

import java.util.ArrayList;
import java.util.List;

public class FavoritosRepository {
    private static FavoritosRepository instance;
    private List<Producto> favoritos;

    private FavoritosRepository() {
        favoritos = new ArrayList<>();
    }

    public static FavoritosRepository getInstance() {
        if (instance == null) {
            instance = new FavoritosRepository();
        }
        return instance;
    }

    public List<Producto> getFavoritos() {
        return favoritos;
    }

    public void addFavorito(Producto producto) {
        if (!favoritos.contains(producto)) {
            favoritos.add(producto);
        }
    }

    public void removeFavorito(Producto producto) {
        favoritos.remove(producto);
    }
}
