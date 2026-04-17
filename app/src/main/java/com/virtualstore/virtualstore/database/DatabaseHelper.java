package com.virtualstore.virtualstore.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.virtualstore.virtualstore.model.Categorie;
import com.virtualstore.virtualstore.model.Order;
import com.virtualstore.virtualstore.model.Product;
import com.virtualstore.virtualstore.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "virtualstore.db";
    public static final int DB_VERSION = 9; // Incremented to 9

    public static final String TABLE_USERS = "users";
    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_CATEGORIES = "categories";
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_CART = "cart";
    public static final String TABLE_AVIS = "avis";

    public static final String COL_ID = "id";

    // USERS
    public static final String COL_FIRST_NAME = "first_name";
    public static final String COL_SECOND_NAME = "second_name";
    public static final String COL_LAST_NAME = "last_name";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_FAVORITE_COLOR = "favorite_color";
    public static final String COL_PHONE = "phone";
    public static final String COL_ADDRESS = "address";
    public static final String COL_ROLE = "role";

    // PRODUCTS
    public static final String COL_P_NAME = "name";
    public static final String COL_P_PRICE = "price";
    public static final String COL_P_CATEGORY = "category";
    public static final String COL_P_DESC = "description";
    public static final String COL_P_IMAGE = "imageUrl";
    public static final String COL_P_RATING = "rating";
    public static final String COL_P_STOCK = "stockQuantity";

    // CATEGORIES
    public static final String COL_C_NAME = "name";
    public static final String COL_C_DESC = "description";

    // ORDERS
    public static final String COL_O_USER_ID = "userId";
    public static final String COL_O_USER_NAME = "userName";
    public static final String COL_O_TOTAL = "totalPrice";
    public static final String COL_O_STATUS = "status";
    public static final String COL_O_DATE = "date";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // USERS
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_FIRST_NAME + " TEXT," +
                COL_SECOND_NAME + " TEXT," +
                COL_LAST_NAME + " TEXT," +
                COL_EMAIL + " TEXT UNIQUE," +
                COL_PASSWORD + " TEXT," +
                COL_FAVORITE_COLOR + " TEXT," +
                COL_PHONE + " TEXT," +
                COL_ADDRESS + " TEXT," +
                COL_ROLE + " INTEGER)");

        // PRODUCTS
        db.execSQL("CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_P_NAME + " TEXT," +
                COL_P_PRICE + " REAL," +
                COL_P_CATEGORY + " TEXT," +
                COL_P_DESC + " TEXT," +
                COL_P_IMAGE + " TEXT," +
                COL_P_RATING + " REAL," +
                COL_P_STOCK + " INTEGER)");

        // CATEGORIES
        db.execSQL("CREATE TABLE " + TABLE_CATEGORIES + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_C_NAME + " TEXT," +
                COL_C_DESC + " TEXT)");

        // ORDERS
        db.execSQL("CREATE TABLE " + TABLE_ORDERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_O_USER_ID + " TEXT," +
                COL_O_USER_NAME + " TEXT," +
                COL_O_TOTAL + " REAL," +
                COL_O_STATUS + " TEXT," +
                COL_O_DATE + " TEXT)");

        // CART (Table missing before)
        db.execSQL("CREATE TABLE " + TABLE_CART + " (" +
                "id TEXT PRIMARY KEY," +
                "name TEXT," +
                "price REAL," +
                "quantity INTEGER)");

        // AVIS (Table missing before)
        db.execSQL("CREATE TABLE " + TABLE_AVIS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "productId TEXT," +
                "auteur TEXT," +
                "commentaire TEXT," +
                "note REAL)");

        insertInitialData(db);
    }

    private void insertInitialData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (name, description) VALUES ('Meubles', 'Mobilier')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (name, description) VALUES ('Décoration', 'Decoration')");

        db.execSQL("INSERT INTO " + TABLE_PRODUCTS + " (name, price, category, description, imageUrl, rating, stockQuantity) " +
                "VALUES ('Chaise Moderne', 150.0, 'Meubles', 'Une belle chaise moderne pour votre salon.', '', 4.5, 10)");

        db.execSQL("INSERT INTO " + TABLE_PRODUCTS + " (name, price, category, description, imageUrl, rating, stockQuantity) " +
                "VALUES ('Vase en verre', 45.0, 'Décoration', 'Un vase élégant en verre soufflé.', '', 4.2, 20)");

        // Admin default account
        db.execSQL("INSERT INTO " + TABLE_USERS +
                " (first_name, last_name, email, password, role) VALUES " +
                "('Admin', 'User', 'admin@store.com', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 1)");

        // Sample Avis
        db.execSQL("INSERT INTO " + TABLE_AVIS + " (productId, auteur, commentaire, note) VALUES ('1', 'Jean', 'Superbe chaise !', 5.0)");
    }

    // --- CRUD Methods (remained the same) ---
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
        while (c.moveToNext()) {
            list.add(new Product(
                    c.getInt(c.getColumnIndexOrThrow(COL_ID)) + "",
                    c.getString(c.getColumnIndexOrThrow(COL_P_NAME)),
                    c.getString(c.getColumnIndexOrThrow(COL_P_DESC)),
                    c.getDouble(c.getColumnIndexOrThrow(COL_P_PRICE)),
                    c.getString(c.getColumnIndexOrThrow(COL_P_CATEGORY)),
                    c.getString(c.getColumnIndexOrThrow(COL_P_IMAGE)),
                    (float) c.getDouble(c.getColumnIndexOrThrow(COL_P_RATING)),
                    c.getInt(c.getColumnIndexOrThrow(COL_P_STOCK))
            ));
        }
        c.close();
        return list;
    }

    public long addProduct(Product p) {
        ContentValues v = new ContentValues();
        v.put(COL_P_NAME, p.getName());
        v.put(COL_P_PRICE, p.getPrice());
        v.put(COL_P_CATEGORY, p.getCategory());
        v.put(COL_P_DESC, p.getDescription());
        v.put(COL_P_IMAGE, p.getImageUrl());
        v.put(COL_P_RATING, p.getRating());
        v.put(COL_P_STOCK, p.getStockQuantity());
        return getWritableDatabase().insert(TABLE_PRODUCTS, null, v);
    }

    public int updateProduct(Product p) {
        ContentValues v = new ContentValues();
        v.put(COL_P_NAME, p.getName());
        v.put(COL_P_PRICE, p.getPrice());
        v.put(COL_P_CATEGORY, p.getCategory());
        v.put(COL_P_DESC, p.getDescription());
        v.put(COL_P_IMAGE, p.getImageUrl());
        v.put(COL_P_RATING, p.getRating());
        v.put(COL_P_STOCK, p.getStockQuantity());
        return getWritableDatabase().update(TABLE_PRODUCTS, v, COL_ID + "=?", new String[]{p.getId()});
    }

    public int deleteProduct(String id) {
        return getWritableDatabase().delete(TABLE_PRODUCTS, COL_ID + "=?", new String[]{id});
    }

    public List<Categorie> getAllCategories() {
        List<Categorie> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_CATEGORIES, null);
        while (c.moveToNext()) {
            list.add(new Categorie(
                    c.getInt(c.getColumnIndexOrThrow(COL_ID)) + "",
                    c.getString(c.getColumnIndexOrThrow(COL_C_NAME)),
                    c.getString(c.getColumnIndexOrThrow(COL_C_DESC))
            ));
        }
        c.close();
        return list;
    }

    public long addCategory(Categorie c) {
        ContentValues v = new ContentValues();
        v.put(COL_C_NAME, c.getName());
        v.put(COL_C_DESC, c.getDescription());
        return getWritableDatabase().insert(TABLE_CATEGORIES, null, v);
    }

    public int updateCategory(Categorie c) {
        ContentValues v = new ContentValues();
        v.put(COL_C_NAME, c.getName());
        v.put(COL_C_DESC, c.getDescription());
        return getWritableDatabase().update(TABLE_CATEGORIES, v, COL_ID + "=?", new String[]{c.getId()});
    }

    public int deleteCategory(String id) {
        return getWritableDatabase().delete(TABLE_CATEGORIES, COL_ID + "=?", new String[]{id});
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_USERS, null);
        while (c.moveToNext()) {
            list.add(new User(
                    c.getInt(c.getColumnIndexOrThrow(COL_ID)) + "",
                    c.getString(c.getColumnIndexOrThrow(COL_FIRST_NAME)),
                    c.getString(c.getColumnIndexOrThrow(COL_SECOND_NAME)),
                    c.getString(c.getColumnIndexOrThrow(COL_LAST_NAME)),
                    c.getString(c.getColumnIndexOrThrow(COL_EMAIL)),
                    c.getString(c.getColumnIndexOrThrow(COL_PASSWORD)),
                    c.getString(c.getColumnIndexOrThrow(COL_FAVORITE_COLOR)),
                    c.getString(c.getColumnIndexOrThrow(COL_PHONE)),
                    c.getString(c.getColumnIndexOrThrow(COL_ADDRESS)),
                    c.getInt(c.getColumnIndexOrThrow(COL_ROLE))
            ));
        }
        c.close();
        return list;
    }

    public int updateUser(User u) {
        ContentValues v = new ContentValues();
        v.put(COL_FIRST_NAME, u.getFirstName());
        v.put(COL_SECOND_NAME, u.getSecondName());
        v.put(COL_LAST_NAME, u.getLastName());
        v.put(COL_EMAIL, u.getEmail());
        v.put(COL_PASSWORD, u.getPassword());
        v.put(COL_FAVORITE_COLOR, u.getFavoriteColor());
        v.put(COL_PHONE, u.getPhone());
        v.put(COL_ADDRESS, u.getAddress());
        v.put(COL_ROLE, u.getRole());
        return getWritableDatabase().update(TABLE_USERS, v, COL_ID + "=?", new String[]{u.getId()});
    }

    public int deleteUser(String id) {
        return getWritableDatabase().delete(TABLE_USERS, COL_ID + "=?", new String[]{id});
    }

    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_ORDERS, null);
        while (c.moveToNext()) {
            list.add(new Order(
                    c.getInt(c.getColumnIndexOrThrow(COL_ID)) + "",
                    c.getString(c.getColumnIndexOrThrow(COL_O_USER_ID)),
                    c.getString(c.getColumnIndexOrThrow(COL_O_USER_NAME)),
                    c.getDouble(c.getColumnIndexOrThrow(COL_O_TOTAL)),
                    c.getString(c.getColumnIndexOrThrow(COL_O_STATUS)),
                    c.getString(c.getColumnIndexOrThrow(COL_O_DATE))
            ));
        }
        c.close();
        return list;
    }

    public int updateOrder(Order o) {
        ContentValues v = new ContentValues();
        v.put(COL_O_USER_ID, o.getUserId());
        v.put(COL_O_USER_NAME, o.getUserName());
        v.put(COL_O_TOTAL, o.getTotalPrice());
        v.put(COL_O_STATUS, o.getStatus());
        v.put(COL_O_DATE, o.getDate());
        return getWritableDatabase().update(TABLE_ORDERS, v, COL_ID + "=?", new String[]{o.getId()});
    }

    public int deleteOrder(String id) {
        return getWritableDatabase().delete(TABLE_ORDERS, COL_ID + "=?", new String[]{id});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AVIS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }
}