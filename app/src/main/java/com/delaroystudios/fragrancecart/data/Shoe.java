package com.delaroystudios.fragrancecart.data;

import android.database.Cursor;

/**
 * Created by delaroy on 1/16/18.
 */
public class Shoe {

    public int id;

    public String name;
    public String description;
    public String imageUrl;
    public Double price;
    public String size;
    public int userRating;


    public Shoe(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex(FragranceContract.FragranceEntry._ID));
        this.name = cursor.getString(cursor.getColumnIndex(FragranceContract.FragranceEntry.COLUMN_MENSHOENAME));
        this.description = cursor.getString(cursor.getColumnIndex(FragranceContract.FragranceEntry.COLUMN_MENSHOEDESCRIPTION));
        this.imageUrl = cursor.getString(cursor.getColumnIndex(FragranceContract.FragranceEntry.COLUMN_MENSHOEIMAGE));
        this.price = cursor.getDouble(cursor.getColumnIndex(FragranceContract.FragranceEntry.COLUMN_MENSHOEPRICE));
        this.size = cursor.getString(cursor.getColumnIndex(FragranceContract.FragranceEntry.COLUMN_MENSHOESIZE));
        this.userRating = cursor.getInt(cursor.getColumnIndex(FragranceContract.FragranceEntry.COLUMN_MENSHOEUSERRATING));
    }

}
