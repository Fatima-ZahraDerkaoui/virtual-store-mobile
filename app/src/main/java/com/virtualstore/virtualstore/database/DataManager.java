package com.virtualstore.virtualstore.database;

import android.content.Context;

import com.virtualstore.virtualstore.model.Categorie;
import com.virtualstore.virtualstore.model.Product;
import com.virtualstore.virtualstore.model.User;
import com.virtualstore.virtualstore.model.Order;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static DatabaseHelper dbHelper;

    public static List<Product> products = new ArrayList<>();
    public static List<Categorie> categories = new ArrayList<>();
    public static List<User> users = new ArrayList<>();
    public static List<Order> orders = new ArrayList<>();

    // 🔹 Init
    public static void init(Context context) {
        if (dbHelper == null) {
            dbHelper = new DatabaseHelper(context);
        }
        loadAllData();
    }

    // 🔹 Load all data from DB
    public static void loadAllData() {
        if (dbHelper == null) return;

        products.clear();
        products.addAll(dbHelper.getAllProducts());

        categories.clear();
        categories.addAll(dbHelper.getAllCategories());

        users.clear();
        users.addAll(dbHelper.getAllUsers());

        orders.clear();
        orders.addAll(dbHelper.getAllOrders());
    }

    // =====================================================
    // PRODUCT CRUD
    // =====================================================

    public static void addProduct(Product product) {
        long id = dbHelper.addProduct(product);

        if (id != -1) {
            product.setId(String.valueOf(id));
            products.add(product);
        }
    }

    public static void updateProduct(Product product) {
        int rows = dbHelper.updateProduct(product);

        if (rows > 0) {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getId().equals(product.getId())) {
                    products.set(i, product);
                    break;
                }
            }
        }
    }

    public static void deleteProduct(String id) {
        int rows = dbHelper.deleteProduct(id);

        if (rows > 0) {
            products.removeIf(p -> p.getId().equals(id));
        }
    }

    // =====================================================
    // CATEGORY CRUD
    // =====================================================

    public static void addCategory(Categorie category) {
        long id = dbHelper.addCategory(category);

        if (id != -1) {
            category.setId(String.valueOf(id));
            categories.add(category);
        }
    }

    public static void updateCategory(Categorie category) {
        int rows = dbHelper.updateCategory(category);

        if (rows > 0) {
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).getId().equals(category.getId())) {
                    categories.set(i, category);
                    break;
                }
            }
        }
    }

    public static void deleteCategory(String id) {
        int rows = dbHelper.deleteCategory(id);

        if (rows > 0) {
            categories.removeIf(c -> c.getId().equals(id));
        }
    }

    // =====================================================
    // USER MANAGEMENT
    // =====================================================

    public static void updateUser(User user) {
        int rows = dbHelper.updateUser(user);

        if (rows > 0) {
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId().equals(user.getId())) {
                    users.set(i, user);
                    break;
                }
            }
        }
    }

    public static void deleteUser(String id) {
        int rows = dbHelper.deleteUser(id);

        if (rows > 0) {
            users.removeIf(u -> u.getId().equals(id));
        }
    }

    // =====================================================
    // ORDER MANAGEMENT
    // =====================================================

    public static void updateOrder(Order order) {
        int rows = dbHelper.updateOrder(order);

        if (rows > 0) {
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).getId().equals(order.getId())) {
                    orders.set(i, order);
                    break;
                }
            }
        }
    }

    public static void deleteOrder(String id) {
        int rows = dbHelper.deleteOrder(id);

        if (rows > 0) {
            orders.removeIf(o -> o.getId().equals(id));
        }
    }
}
