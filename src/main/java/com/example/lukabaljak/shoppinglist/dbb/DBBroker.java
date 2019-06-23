package com.example.lukabaljak.shoppinglist.dbb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

public class DBBroker extends SQLiteOpenHelper {


    public DBBroker(Context context) {
        super(context, "ShoppingItemDB", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL upit za kreiranje tabele.
        String sql = "CREATE TABLE ShoppingItem ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "photoURL TEXT, " +
                "description TEXT )";

        // Izvršavanje upita.
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS ShoppingItem");
        this.onCreate(db);
    }


    public int addShoppingItem(ShoppingItem shoppingItem) {


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("photoURL", shoppingItem.getPhotoURL());
        values.put("description", shoppingItem.getDescription());

        int id = (int) db.insert("ShoppingItem",
                null,
                values);

        db.close();
        return id;
    }


    public List<ShoppingItem> getAllShoppingItems() {
        List<ShoppingItem> shoppingItems = new LinkedList<ShoppingItem>();

        String query = "SELECT  * FROM ShoppingItem";


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ShoppingItem shoppingItem = null;

        while (cursor.moveToNext()) {
            shoppingItem = new ShoppingItem();

            shoppingItem.setId(cursor.getInt(0));
            shoppingItem.setPhotoURL(cursor.getString(1));
            shoppingItem.setDescription(cursor.getString(2));

            shoppingItems.add(shoppingItem);
        }

        return shoppingItems;
    }

    public int updateShoppingItem(ShoppingItem shoppingItem) {

        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put("description", shoppingItem.getDescription()); // get title

        int i = db.update("ShoppingItem",
                values,
                "id = ?",
                new String[]{String.valueOf(shoppingItem.getId())});
        db.close();

        return i;
    }

    public void deleteShoppingItem(ShoppingItem shoppingItem){

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("ShoppingItem",
                "id = ?",
                new String[] { String.valueOf(shoppingItem.getId()) });

        db.close();
    }



}
