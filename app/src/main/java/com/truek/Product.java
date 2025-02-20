package com.truek;

public class Product {
    private String name;
    private String precio;
    private String description;
    private String imageUrl;// Si almacenas imágenes desde una URL
    private String uid;
    private String email;
    // Constructor
    public Product(String name, String precio,String description, String imageUrl,String uid,String email) {
        this.name = name;
        this.precio = precio;
        this.description = description;
        this.imageUrl = imageUrl;
        this.uid = uid;
        this.email = email;
    }

    // Getters y Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrecio(){return precio; }

    public void setPrecio(String precio){ this.name = name;}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUid(){return name;}
    public void setUid(String uid){this.uid = uid;}

    public String getEmail(){return email;}
    public void setEmail(String email){this.email = email;}
}


