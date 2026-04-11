package com.virtualstore.virtualstore.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.virtualstore.virtualstore.model.User;

public class UserDAO {
    private final DatabaseHelper dbHelper;

    public UserDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // INSERT USER
    public boolean insertUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_FIRST_NAME, user.getFirstName());
        cv.put(DatabaseHelper.COL_SECOND_NAME, user.getSecondName());
        cv.put(DatabaseHelper.COL_LAST_NAME, user.getLastName());
        cv.put(DatabaseHelper.COL_EMAIL, user.getEmail());
        cv.put(DatabaseHelper.COL_PASSWORD, user.getPassword());
        cv.put(DatabaseHelper.COL_FAVORITE_COLOR, user.getFavoriteColor());
        cv.put(DatabaseHelper.COL_IS_ADMIN, user.getIsAdmin());

        long result = db.insert(DatabaseHelper.TABLE_USERS, null, cv);
        db.close();
        return result != -1;
    }

    // LOGIN CHECK
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT " + DatabaseHelper.COL_USER_ID +
                        " FROM " + DatabaseHelper.TABLE_USERS +
                        " WHERE " + DatabaseHelper.COL_EMAIL + "=? AND " +
                        DatabaseHelper.COL_PASSWORD + "=?",
                new String[]{email, password}
        );

        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    // FORGOT PASSWORD CHECK
    public boolean checkUserForReset(String email, String secondName, String favoriteColor) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT " + DatabaseHelper.COL_USER_ID +
                        " FROM " + DatabaseHelper.TABLE_USERS +
                        " WHERE " + DatabaseHelper.COL_EMAIL + "=? AND " +
                        DatabaseHelper.COL_SECOND_NAME + "=? AND " +
                        DatabaseHelper.COL_FAVORITE_COLOR + "=?",
                new String[]{email, secondName, favoriteColor}
        );

        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    // CHECK IF ADMIN
    public int getIsAdmin(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int isAdmin = 0;

        Cursor cursor = db.rawQuery(
                "SELECT " + DatabaseHelper.COL_IS_ADMIN +
                        " FROM " + DatabaseHelper.TABLE_USERS +
                        " WHERE " + DatabaseHelper.COL_EMAIL + "=?",
                new String[]{email}
        );

        if (cursor.moveToFirst()) {
            isAdmin = cursor.getInt(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_IS_ADMIN)
            );
        }

        cursor.close();
        db.close();
        return isAdmin;
    }

    // CHECK EMAIL EXISTS
    public boolean checkEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT " + DatabaseHelper.COL_USER_ID +
                        " FROM " + DatabaseHelper.TABLE_USERS +
                        " WHERE " + DatabaseHelper.COL_EMAIL + "=?",
                new String[]{email}
        );

        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    // UPDATE PASSWORD
    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_PASSWORD, newPassword);

        int updated = db.update(
                DatabaseHelper.TABLE_USERS,
                cv,
                DatabaseHelper.COL_EMAIL + "=?",
                new String[]{email}
        );

        db.close();
        return updated > 0;
    }

    // UPDATE USER PROFILE
    public boolean updateUser(String oldEmail, String firstName, String lastName, String email, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_FIRST_NAME, firstName);
        cv.put(DatabaseHelper.COL_LAST_NAME, lastName);
        cv.put(DatabaseHelper.COL_EMAIL, email);

        if (password != null && !password.isEmpty()) {
            cv.put(DatabaseHelper.COL_PASSWORD, password);
        }

        int updated = db.update(
                DatabaseHelper.TABLE_USERS,
                cv,
                DatabaseHelper.COL_EMAIL + "=?",
                new String[]{oldEmail}
        );

        db.close();
        return updated > 0;
    }

    // GET FIRST NAME BY EMAIL
    public String getFirstNameByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String name = null;

        Cursor cursor = db.rawQuery(
                "SELECT " + DatabaseHelper.COL_FIRST_NAME +
                        " FROM " + DatabaseHelper.TABLE_USERS +
                        " WHERE " + DatabaseHelper.COL_EMAIL + "=?",
                new String[]{email}
        );

        if (cursor.moveToFirst()) {
            name = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FIRST_NAME)
            );
        }

        cursor.close();
        db.close();
        return name;
    }

    // GET USER BY EMAIL
    public User getUserByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = null;

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DatabaseHelper.TABLE_USERS +
                        " WHERE " + DatabaseHelper.COL_EMAIL + "=?",
                new String[]{email}
        );

        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SECOND_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LAST_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FAVORITE_COLOR)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_IS_ADMIN))
            );
        }

        cursor.close();
        db.close();
        return user;
    }
}