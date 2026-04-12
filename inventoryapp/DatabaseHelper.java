package edu.snhu.cs360.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 1;

    // ===== ITEMS TABLE =====
    private static final String TABLE_ITEMS = "items";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_QTY = "quantity";
    private static final String COL_CATEGORY = "category";
    private static final String COL_NOTES = "notes";

    // ===== USERS TABLE =====
    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "user_id";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create items table with validation to prevent empty names and negative quantities
        String createItemsTable = "CREATE TABLE " + TABLE_ITEMS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_QTY + " INTEGER NOT NULL CHECK(" + COL_QTY + " >= 0), " +
                COL_CATEGORY + " TEXT, " +
                COL_NOTES + " TEXT)";
        db.execSQL(createItemsTable);

        // Create users table for login functionality
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT NOT NULL)";
        db.execSQL(createUsersTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Recreate tables if database version changes
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // ===== CRUD FOR ITEMS =====

    // Add a new item after checking for invalid input
    public boolean addItem(String name, int quantity, String category, String notes) {

        // Check for empty name
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: Name cannot be empty");
            return false;
        }

        // Check for negative quantity
        if (quantity < 0) {
            System.out.println("Error: Quantity cannot be negative");
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_NAME, name);
        values.put(COL_QTY, quantity);
        values.put(COL_CATEGORY, category);
        values.put(COL_NOTES, notes);

        long result = db.insert(TABLE_ITEMS, null, values);

        if (result == -1) {
            System.out.println("Error inserting item into database");
            return false;
        } else {
            System.out.println("Item inserted successfully");
            return true;
        }
    }

    // Return all items from the database
    public Cursor getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ITEMS, null);
    }

    // Return items sorted by name (improves data organization)
    public Cursor getItemsSortedByName() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM " + TABLE_ITEMS + " ORDER BY " + COL_NAME + " ASC",
                null
        );
    }

    // Update an existing item after validation
    public boolean updateItem(int id, String name, int qty, String category, String notes) {

        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        if (qty < 0) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_NAME, name);
        values.put(COL_QTY, qty);
        values.put(COL_CATEGORY, category);
        values.put(COL_NOTES, notes);

        int result = db.update(
                TABLE_ITEMS,
                values,
                COL_ID + "=?",
                new String[]{String.valueOf(id)}
        );

        return result > 0;
    }

    // Delete an item using its id
    public boolean deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(
                TABLE_ITEMS,
                COL_ID + "=?",
                new String[]{String.valueOf(id)}
        );
        return result > 0;
    }

    // ===== LOGIN / USERS METHODS =====

    // Register a new user
    public boolean registerUser(String username, String password) {

        if (username == null || username.trim().isEmpty()) {
            return false;
        }

        if (password == null || password.trim().isEmpty()) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);

        if (result == -1) {
            System.out.println("Error registering user");
            return false;
        } else {
            System.out.println("User registered successfully");
            return true;
        }
    }

    // Check if login credentials match a stored user
    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS +
                        " WHERE " + COL_USERNAME + "=? AND " + COL_PASSWORD + "=?",
                new String[]{username, password}
        );

        boolean success = cursor.getCount() > 0;
        cursor.close();
        return success;
    }
}