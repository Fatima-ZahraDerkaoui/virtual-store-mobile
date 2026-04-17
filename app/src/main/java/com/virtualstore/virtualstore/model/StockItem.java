package com.virtualstore.virtualstore.model;

public class StockItem {

    private String productId;
    private String productName;
    private int quantity;

    // 🔹 Empty constructor (important for Firebase / APIs)
    public StockItem() {
    }

    // 🔹 Constructor
    public StockItem(String productId, String productName, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
    }

    // 🔹 Getters
    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    // 🔹 Setters
    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}