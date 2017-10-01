package com.delaroystudios.fragrancecart.data;

import android.database.Cursor;

/**
 * Created by delaroy on 10/1/17.
 */

public class Wish {

    public int id;

    public String name;
    public String description;
    public String imageUrl;
    public Double price;
    public int userRating;





    public Wish(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex(FragranceContract.FragranceEntry._WISHID));
        this.name = cursor.getString(cursor.getColumnIndex(FragranceContract.FragranceEntry.COLUMN_WISH_NAME));
        this.description = cursor.getString(cursor.getColumnIndex(FragranceContract.FragranceEntry.COLUMN_WISH_DESCRIPTION));
        this.imageUrl = cursor.getString(cursor.getColumnIndex(FragranceContract.FragranceEntry.COLUMN_WISH_IMAGE));
        this.price = cursor.getDouble(cursor.getColumnIndex(FragranceContract.FragranceEntry.COLUMN_WISH_PRICE));
        this.userRating = cursor.getInt(cursor.getColumnIndex(FragranceContract.FragranceEntry.COLUMN_WISH_USERRATING));
    }
}
