package com.virtualstore.virtualstore.model;

public class User {
    private String firstName, secondName, lastName, email, password, favoriteColor;
    private int isAdmin; // 0 for normal user, 1 for admin

    public User(String firstName, String secondName, String lastName, String email, String password, String favoriteColor) {
        this(firstName, secondName, lastName, email, password, favoriteColor, 0);
    }

    public User(String firstName, String secondName, String lastName, String email, String password, String favoriteColor, int isAdmin) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.favoriteColor = favoriteColor;
        this.isAdmin = isAdmin;
    }

    public String getFirstName() { return firstName; }
    public String getSecondName() { return secondName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getFavoriteColor() { return favoriteColor; }
    public int getIsAdmin() { return isAdmin; }
    public void setIsAdmin(int isAdmin) { this.isAdmin = isAdmin; }
}
