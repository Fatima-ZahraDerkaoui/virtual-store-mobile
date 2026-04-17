package com.virtualstore.virtualstore.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.virtualstore.virtualstore.model.CartItem;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    private DatabaseHelper dbHelper;

    public CartDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void addItem(CartItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", item.getId());
        values.put("name", item.getName());
        values.put("price", item.getPrice());
        values.put("quantity", item.getQuantity());

        db.insertWithOnConflict(DatabaseHelper.TABLE_CART, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void updateQuantity(String id, int qty) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantity", qty);
        db.update(DatabaseHelper.TABLE_CART, values, "id=?", new String[]{id});
        db.close();
    }

    public void deleteItem(String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_CART, "id=?", new String[]{id});
        db.close();
    }

    public void clearCart() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_CART, null, null);
        db.close();
    }

    public List<CartItem> getAllItems() {
        List<CartItem> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_CART, null);

        if (c.moveToFirst()) {
            do {
                list.add(new CartItem(
                        c.getString(c.getColumnIndexOrThrow("id")),
                        c.getString(c.getColumnIndexOrThrow("name")),
                        c.getDouble(c.getColumnIndexOrThrow("price")),
                        c.getInt(c.getColumnIndexOrThrow("quantity"))
                ));
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }
}