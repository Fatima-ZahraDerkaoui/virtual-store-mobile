package com.virtualstore.virtualstore.model;

public class HistoryItem {

    private int id;
    private String date;
    private double total;

    public HistoryItem(int id, String date, double total) {
        this.id = id;
        this.date = date;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public double getTotal() {
        return total;
    }
}