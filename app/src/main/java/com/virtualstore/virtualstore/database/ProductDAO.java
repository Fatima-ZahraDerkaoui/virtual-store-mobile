package com.virtualstore.virtualstore.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.virtualstore.virtualstore.model.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private DatabaseHelper dbHelper;

    public ProductDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCTS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursorToProduct(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public List<Product> getProductsByCategory(String category) {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCTS, null,
                "category = ?", new String[]{category}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursorToProduct(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public List<Product> searchProducts(String keyword) {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCTS, null,
                "name LIKE ?", new String[]{"%" + keyword + "%"}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursorToProduct(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    private Product cursorToProduct(Cursor cursor) {
        return new Product(
                String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID))),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_P_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_P_DESC)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_P_PRICE)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_P_CATEGORY)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_P_IMAGE)),
                cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_P_RATING)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_P_STOCK))
        );
    }
}
