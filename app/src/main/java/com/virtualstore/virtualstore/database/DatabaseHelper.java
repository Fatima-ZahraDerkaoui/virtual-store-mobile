package com.virtualstore.virtualstore.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // ================= DATABASE INFO =================
    public static final String DB_NAME = "virtualstore.db";
    public static final int DB_VERSION = 6; // merged version

    // ================= USERS =================
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "id";
    public static final String COL_FIRST_NAME = "first_name";
    public static final String COL_SECOND_NAME = "second_name";
    public static final String COL_LAST_NAME = "last_name";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_FAVORITE_COLOR = "favorite_color";
    public static final String COL_IS_ADMIN = "is_admin";

    // ================= CART =================
    public static final String TABLE_CART = "cart";

    // ================= ORDERS =================
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_ORDER_LINES = "order_lines";

    // ================= CATEGORIES =================
    public static final String TABLE_CATEGORIES = "categories";

    // ================= PRODUCTS =================
    public static final String TABLE_PRODUCTS = "products";

    // ================= AVIS =================
    public static final String TABLE_AVIS = "avis";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // ================= USERS =================
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "first_name TEXT NOT NULL," +
                "second_name TEXT," +
                "last_name TEXT NOT NULL," +
                "email TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "favorite_color TEXT," +
                "is_admin INTEGER DEFAULT 0)");

        // ================= CART =================
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CART + " (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT," +
                "price REAL," +
                "quantity INTEGER)");

        // ================= ORDERS =================
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ORDERS + " (" +
                "order_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date TEXT," +
                "total REAL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ORDER_LINES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "order_id INTEGER," +
                "product_name TEXT," +
                "quantity INTEGER," +
                "unit_price REAL)");

        // ================= CATEGORIES =================
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "description TEXT)");

        // ================= PRODUCTS =================
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCTS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "description TEXT," +
                "price REAL NOT NULL," +
                "category TEXT," +
                "imageUrl TEXT," +
                "rating REAL)");

        // ================= AVIS =================
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_AVIS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "productId INTEGER," +
                "auteur TEXT," +
                "commentaire TEXT," +
                "note REAL)");

        // ================= DEFAULT DATA =================

        // Categories (both versions merged)
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (name, description) VALUES ('Tous', 'Tous les produits')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (name, description) VALUES ('Armchairs', 'Fauteuils confortables')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (name, description) VALUES ('Sofas', 'Canapés modernes')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (name, description) VALUES ('Beds', 'Lits et literie')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (name, description) VALUES ('Salon', 'Meubles pour le séjour')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (name, description) VALUES ('Chambre', 'Tout pour votre repos')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (name, description) VALUES ('Bureau', 'Espace de travail optimisé')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (name, description) VALUES ('Cuisine', 'Ustensiles et meubles de cuisine')");

        // Products (MERGED BOTH DATASETS)

        db.execSQL("INSERT INTO " + TABLE_PRODUCTS + " (name, description, price, category, imageUrl, rating) VALUES " +
                "('Chair Elegant', 'Chaise élégante en bois naturel', 190.00, 'Armchairs', 'https://images.unsplash.com/photo-1567538096630-e0c55bd6374c?w=400', 4.5)");

        db.execSQL("INSERT INTO " + TABLE_PRODUCTS + " (name, description, price, category, imageUrl, rating) VALUES " +
                "('Armchair Loft', 'Fauteuil style loft industriel', 250.00, 'Armchairs', 'https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=400', 4.7)");

        db.execSQL("INSERT INTO " + TABLE_PRODUCTS + " (name, description, price, category, imageUrl, rating) VALUES " +
                "('Armchair Minimal', 'Fauteuil minimaliste scandinave', 320.00, 'Armchairs', 'https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=400', 4.3)");

        db.execSQL("INSERT INTO " + TABLE_PRODUCTS + " (name, description, price, category, imageUrl, rating) VALUES " +
                "('Chair Nord', 'Chaise nordique légère', 280.00, 'Armchairs', 'https://images.unsplash.com/photo-1581539250439-c96689b516dd?w=400', 4.6)");

        db.execSQL("INSERT INTO " + TABLE_PRODUCTS + " (name, description, price, category, imageUrl, rating) VALUES " +
                "('Modern Loft Sofa', 'Canapé moderne style loft', 250.00, 'Sofas', 'https://images.unsplash.com/photo-1493663284031-b7e3aefcae8e?w=400', 4.8)");

        db.execSQL("INSERT INTO " + TABLE_PRODUCTS + " (name, description, price, category, imageUrl, rating) VALUES " +
                "('Nordic Comfort', 'Chaise Nordic Comfort', 220.00, 'Beds', 'https://images.unsplash.com/photo-1538688525198-9b88f6f53126?w=400', 4.9)");

        db.execSQL("INSERT INTO " + TABLE_PRODUCTS + " (name, description, price, category, imageUrl, rating) VALUES " +
                "('Chaise Scandinave', 'Chaise élégante en bois et tissu gris.', 450.0, 'Salon', 'https://images.unsplash.com/photo-1567538096630-e0c55bd6374c', 4.5)");

        db.execSQL("INSERT INTO " + TABLE_PRODUCTS + " (name, description, price, category, imageUrl, rating) VALUES " +
                "('Lampe de Bureau LED', 'Lampe avec variateur d''intensité et port USB.', 120.0, 'Bureau', 'https://images.unsplash.com/photo-1534073828943-f801091bb18c', 4.0)");

        db.execSQL("INSERT INTO " + TABLE_PRODUCTS + " (name, description, price, category, imageUrl, rating) VALUES " +
                "('Canapé Velvet Bleu', 'Canapé 3 places grand confort en velours.', 3200.0, 'Salon', 'https://images.unsplash.com/photo-1555041469-a586c61ea9bc', 5.0)");

        db.execSQL("INSERT INTO " + TABLE_PRODUCTS + " (name, description, price, category, imageUrl, rating) VALUES " +
                "('Table Basse Marbre', 'Design moderne avec plateau en marbre véritable.', 850.0, 'Salon', 'https://images.unsplash.com/photo-1533090161767-e6ffed986c88', 4.2)");

        db.execSQL("INSERT INTO " + TABLE_PRODUCTS + " (name, description, price, category, imageUrl, rating) VALUES " +
                "('Lit King Size', 'Cadre de lit robuste avec sommier inclus.', 1800.0, 'Chambre', 'https://images.unsplash.com/photo-1505691722718-4684375e797e', 4.8)");

        // Avis (MERGED)
        db.execSQL("INSERT INTO " + TABLE_AVIS + " (productId, auteur, commentaire, note) VALUES (1, 'Ahmed', 'Très confortable, je recommande !', 5.0)");
        db.execSQL("INSERT INTO " + TABLE_AVIS + " (productId, auteur, commentaire, note) VALUES (1, 'Sara', 'Belle chaise mais montage un peu difficile.', 4.0)");
        db.execSQL("INSERT INTO " + TABLE_AVIS + " (productId, auteur, commentaire, note) VALUES (3, 'Karim', 'Un peu cher mais la qualité est là.', 5.0)");
        db.execSQL("INSERT INTO " + TABLE_AVIS + " (productId, auteur, commentaire, note) VALUES (2, 'Karim', 'Lampe parfaite pour mon bureau.', 4.5)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AVIS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_LINES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        onCreate(db);
    }
}