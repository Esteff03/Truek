package com.sbjs.truek;

public class Producto {
    private String nombre;
    private String precio;
    private String descripcion;
    private String imagenUrl;

    public Producto(String nombre, String precio, String descripcion, String imagenUrl) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }
}
