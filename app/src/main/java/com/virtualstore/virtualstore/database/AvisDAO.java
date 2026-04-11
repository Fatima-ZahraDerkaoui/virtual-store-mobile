package com.virtualstore.virtualstore.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.virtualstore.virtualstore.model.Avis;
import java.util.ArrayList;
import java.util.List;

public class AvisDAO {

    private DatabaseHelper dbHelper;

    public AvisDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public List<Avis> getAvisByProduct(int productId) {
        List<Avis> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("avis", null,
                "productId = ?", new String[]{String.valueOf(productId)},
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new Avis(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("productId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("auteur")),
                        cursor.getString(cursor.getColumnIndexOrThrow("commentaire")),
                        cursor.getFloat(cursor.getColumnIndexOrThrow("note"))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}