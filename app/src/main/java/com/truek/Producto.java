package com.truek;

public class Producto {
    private String nombre;
    private String precio;
    private int imagenResId;

    public Producto(String nombre, String precio, int imagenResId) {
        this.nombre = nombre;
        this.precio = precio;
        this.imagenResId = imagenResId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public int getImagenResId() {
        return imagenResId;
    }
}
