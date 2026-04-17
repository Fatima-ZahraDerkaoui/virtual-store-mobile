package com.virtualstore.virtualstore.model;
public class Order {

    private String id;
    private String userId;
    private String userName;
    private double totalPrice;
    private String status; // Pending, Shipped, Delivered, Cancelled
    private String date;

    // 🔹 Empty constructor (important for Firebase / APIs)
    public Order() {
        this.status = "Pending";
    }

    // 🔹 Constructor
    public Order(String id,
                 String userId,
                 String userName,
                 double totalPrice,
                 String status,
                 String date) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.totalPrice = totalPrice;
        this.status = status != null ? status : "Pending";
        this.date = date;
    }

    // 🔹 Getters
    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    // 🔹 Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
