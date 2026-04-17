package com.virtualstore.virtualstore.model;

public class Categorie {

    private String id;        // from your friend (String)
    private String name;
    private String description; // from your version

    // 🔹 Empty constructor (IMPORTANT for Firebase / APIs)
    public Categorie() {
    }

    // 🔹 Constructor with 2 arguments
    public Categorie(String id, String name) {
        this.id = id;
        this.name = name;
        this.description = "";
    }

    // 🔹 Full constructor
    public Categorie(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // 🔹 Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // 🔹 Setters (IMPORTANT if you modify data later)
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}