package com.virtualstore.virtualstore.database;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.virtualstore.virtualstore.model.CartItem;

import java.util.*;

public class OrderDAO {

    private DatabaseHelper dbHelper;

    public OrderDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // ✅ FIX méthode propre
    public int addOrder(String date, double total) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put("date", date);
        v.put("total", total);

        long id = db.insert("orders", null, v);
        db.close();

        return (int) id;
    }

    // ✅ garde CartItem (BON CHOIX)
    public void addOrderLine(int orderId, CartItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put("order_id", orderId);
        v.put("product_name", item.getName());
        v.put("quantity", item.getQuantity());
        v.put("unit_price", item.getPrice());

        db.insert("order_lines", null, v);
        db.close();
    }

    public Cursor getAllOrders() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery("SELECT * FROM orders ORDER BY order_id DESC", null);
    }

    public List<CartItem> getOrderLines(int orderId) {
        List<CartItem> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT * FROM order_lines WHERE order_id=?",
                new String[]{String.valueOf(orderId)}
        );

        while (c.moveToNext()) {
            list.add(new CartItem(
                    0,
                    c.getString(c.getColumnIndexOrThrow("product_name")),
                    c.getDouble(c.getColumnIndexOrThrow("unit_price")),
                    c.getInt(c.getColumnIndexOrThrow("quantity"))
            ));
        }

        c.close();
        return list;
    }
}