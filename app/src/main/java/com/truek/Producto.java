package com.truek;

import java.io.Serializable;

import java.io.Serializable;

public class Producto implements Serializable {
    private String nombre;
    private String precio;
    private int imagen;
    private String descripcion;

    public Producto(String nombre, String precio, int imagen, String descripcion) {
        this.nombre = nombre;
        this.precio = precio;
        this.imagen = imagen;
        this.descripcion = descripcion;
    }

    public String getNombre() { return nombre; }
    public String getPrecio() { return precio; }
    public int getImagen() { return imagen; }
    public String getDescripcion() { return descripcion; }
}
