package com.virtualstore.virtualstore.model;

public class User {

    private String id;

    private String firstName;
    private String secondName;
    private String lastName;

    private String email;
    private String password;

    private String favoriteColor;
    private String phone;
    private String address;

    private int role; // 0 = CLIENT, 1 = ADMIN

    // 🔹 Empty constructor (VERY IMPORTANT for Firebase / APIs)
    public User() {
    }

    // 🔹 Full constructor
    public User(String id,
                String firstName,
                String secondName,
                String lastName,
                String email,
                String password,
                String favoriteColor,
                String phone,
                String address,
                int role) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.favoriteColor = favoriteColor;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    // 🔹 Constructor for registration (default role = 0)
    public User(String firstName,
                String secondName,
                String lastName,
                String email,
                String password,
                String favoriteColor) {
        this("", firstName, secondName, lastName, email, password, favoriteColor, "", "", 0);
    }

    // 🔹 Constructor compatible with your version (easy creation like yours)
    public User(String firstName,
                String secondName,
                String lastName,
                String email,
                String password,
                String favoriteColor,
                int role) {
        this("", firstName, secondName, lastName, email, password, favoriteColor, "", "", role);
    }

    // 🔹 Getters
    public String getId() { return id; }

    public String getFirstName() { return firstName; }
    public String getSecondName() { return secondName; }
    public String getLastName() { return lastName; }

    public String getEmail() { return email; }
    public String getPassword() { return password; }

    public String getFavoriteColor() { return favoriteColor; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }

    public int getRole() { return role; }

    // 🔹 Setters
    public void setId(String id) { this.id = id; }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setSecondName(String secondName) { this.secondName = secondName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }

    public void setFavoriteColor(String favoriteColor) { this.favoriteColor = favoriteColor; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }

    public void setRole(int role) { this.role = role; }
}