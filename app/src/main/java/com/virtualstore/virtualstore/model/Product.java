package com.virtualstore.virtualstore.model;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private String category;
    private String imageUrl;
    private float rating;

    public Product(int id, String name, String description, double price, String category, String imageUrl, float rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
        this.rating = rating;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public String getImageUrl() { return imageUrl; }
    public float getRating() { return rating; }
}