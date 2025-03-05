package com.sbjs.truek;

public class Producto {
    private String name;
    private String price;
    private String description;
    private String imageUrl;

    public Producto(String name, String price, String description, String imageUrl) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
}
