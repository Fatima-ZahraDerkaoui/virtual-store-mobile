package com.virtualstore.virtualstore.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.virtualstore.virtualstore.model.Categorie;
import java.util.ArrayList;
import java.util.List;

public class CategorieDAO {

    private DatabaseHelper dbHelper;

    public CategorieDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public List<Categorie> getAllCategories() {
        List<Categorie> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_CATEGORIES, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new Categorie(
                        String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("id"))),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description"))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}