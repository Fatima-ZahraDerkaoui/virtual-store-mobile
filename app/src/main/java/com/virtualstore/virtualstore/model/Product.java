package com.virtualstore.virtualstore.model;

public class Product {

    private String id;              // from your friend (String instead of int)
    private String name;
    private String description;
    private double price;
    private String category;
    private String imageUrl;

    private float rating;           // from your version
    private int stockQuantity;      // from your friend

    // 🔹 Empty constructor (IMPORTANT for Firebase / APIs)
    public Product() {
    }

    // 🔹 Full constructor
    public Product(String id, String name, String description, double price,
                   String category, String imageUrl, float rating, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.stockQuantity = stockQuantity;
    }

    // 🔹 Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public String getImageUrl() { return imageUrl; }
    public float getRating() { return rating; }
    public int getStockQuantity() { return stockQuantity; }

    // 🔹 Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(double price) { this.price = price; }
    public void setCategory(String category) { this.category = category; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setRating(float rating) { this.rating = rating; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
}