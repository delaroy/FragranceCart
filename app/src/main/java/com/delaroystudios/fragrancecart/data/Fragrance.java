package com.delaroystudios.fragrancecart.data;

import android.database.Cursor;

/**
 * Created by delaroy on 9/3/17.
 */
public class Fragrance {

    public int id;

    public String name;
    public String description;
    public String imageUrl;
    public Double price;
    public int userRating;


    public Fragrance(Cursor cursor) {
        this.name = cursor.getString(cursor.getColumnIndex(FragranceContract.FragranceEntry.COLUMN_NAME));
        this.description = cursor.getString(cursor.getColumnIndex(FragranceContract.FragranceEntry.COLUMN_DESCRIPTION));
        this.imageUrl = cursor.getString(cursor.getColumnIndex(FragranceContract.FragranceEntry.COLUMN_IMAGE));
        this.price = cursor.getDouble(cursor.getColumnIndex(FragranceContract.FragranceEntry.COLUMN_PRICE));
        this.userRating = cursor.getInt(cursor.getColumnIndex(FragranceContract.FragranceEntry.COLUMN_USERRATING));
    }

}
