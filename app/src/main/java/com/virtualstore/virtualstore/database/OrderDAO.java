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

    public int addOrder(String date, double total) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put("date", date);
        v.put("totalPrice", total); // Updated to match DatabaseHelper column

        long id = db.insert("orders", null, v);
        db.close();

        return (int) id;
    }

    public void addOrderLine(int orderId, CartItem item) {
        // This table should be created in DatabaseHelper if needed, 
        // for now let's ensure it doesn't crash if it exists.
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put("order_id", orderId);
        v.put("product_name", item.getName());
        v.put("quantity", item.getQuantity());
        v.put("unit_price", item.getPrice());

        try {
            db.insert("order_lines", null, v);
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public Cursor getAllOrders() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Using TABLE_ORDERS constant or "orders" string. 
        // DatabaseHelper defines it as "orders".
        // Column names should match DatabaseHelper: id, date, totalPrice
        return db.rawQuery("SELECT id as order_id, date, totalPrice as total FROM orders ORDER BY id DESC", null);
    }

    public List<CartItem> getOrderLines(int orderId) {
        List<CartItem> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            Cursor c = db.rawQuery(
                    "SELECT * FROM order_lines WHERE order_id=?",
                    new String[]{String.valueOf(orderId)}
            );

            while (c.moveToNext()) {
                list.add(new CartItem(
                        "0",
                        c.getString(c.getColumnIndexOrThrow("product_name")),
                        c.getDouble(c.getColumnIndexOrThrow("unit_price")),
                        c.getInt(c.getColumnIndexOrThrow("quantity"))
                ));
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}