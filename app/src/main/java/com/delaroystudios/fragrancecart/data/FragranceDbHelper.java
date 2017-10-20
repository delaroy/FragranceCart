package com.delaroystudios.fragrancecart.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.delaroystudios.fragrancecart.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.delaroystudios.fragrancecart.data.FragranceContract.FragranceEntry.CART_TABLE;

/**
 * Created by delaroy on 9/3/17.
 */

public class FragranceDbHelper extends SQLiteOpenHelper {
    private static final String TAG = FragranceDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "fragrances.db";
    private static final int DATABASE_VERSION = 1;
    Context context;
    SQLiteDatabase db;
    ContentResolver mContentResolver;



    //Used to read data from res/ and assets/
    private Resources mResources;



    public FragranceDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mResources = context.getResources();
        mContentResolver = context.getContentResolver();

        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FRAGRANCE_TABLE = "CREATE TABLE " + FragranceContract.FragranceEntry.TABLE_NAME + " (" +
                FragranceContract.FragranceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FragranceContract.FragranceEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
                FragranceContract.FragranceEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                FragranceContract.FragranceEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +
                FragranceContract.FragranceEntry.COLUMN_PRICE + " REAL NOT NULL, " +
                FragranceContract.FragranceEntry.COLUMN_USERRATING + " INTEGER NOT NULL, " +
                FragranceContract.FragranceEntry.COLUMN_ITEMID + " INTEGER UNIQUE NOT NULL " + " );";

        final String SQL_CREATE_CART_FRAGRANCE_TABLE = "CREATE TABLE " + FragranceContract.FragranceEntry.CART_TABLE + " (" +
                FragranceContract.FragranceEntry._CARTID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FragranceContract.FragranceEntry.COLUMN_CART_NAME + " TEXT UNIQUE NOT NULL, " +
                FragranceContract.FragranceEntry.COLUMN_CART_IMAGE + " TEXT NOT NULL, " +
                FragranceContract.FragranceEntry.COLUMN_CART_QUANTITY + " INTEGER NOT NULL, " +
                FragranceContract.FragranceEntry.COLUMN_CART_TOTAL_PRICE + " REAL NOT NULL " + " );";

        //TODO
        final String SQL_CREATE_WISHLIST_FRAGRANCE_TABLE = "CREATE TABLE " + FragranceContract.FragranceEntry.WISH_TABLE + " (" +
                FragranceContract.FragranceEntry._WISHID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FragranceContract.FragranceEntry.COLUMN_WISH_NAME + " TEXT UNIQUE NOT NULL, " +
                FragranceContract.FragranceEntry.COLUMN_WISH_DESCRIPTION + " TEXT NOT NULL, " +
                FragranceContract.FragranceEntry.COLUMN_WISH_IMAGE + " TEXT NOT NULL, " +
                FragranceContract.FragranceEntry.COLUMN_WISH_PRICE + " REAL NOT NULL, " +
                FragranceContract.FragranceEntry.COLUMN_WISH_USERRATING + " INTEGER NOT NULL " + " );";



        db.execSQL(SQL_CREATE_FRAGRANCE_TABLE);
        db.execSQL(SQL_CREATE_CART_FRAGRANCE_TABLE);
        db.execSQL(SQL_CREATE_WISHLIST_FRAGRANCE_TABLE);
        Log.d(TAG, "Database Created Successfully" );


       /* try {
            readFragrancesFromResources(db);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: Handle database version upgrades
        db.execSQL("DROP TABLE IF EXISTS " + FragranceContract.FragranceEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FragranceContract.FragranceEntry.CART_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FragranceContract.FragranceEntry.WISH_TABLE);
        onCreate(db);
    }


    /*private void readFragrancesFromResources(SQLiteDatabase db) throws IOException, JSONException {
        //db = this.getWritableDatabase();
        StringBuilder builder = new StringBuilder();
        InputStream in = mResources.openRawResource(R.raw.fragrance);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line + "\n");
        }

        //Parse resource into key/values
        final String rawJson = builder.toString();

        final String BGS_FRAGRANCES = "fragrances";

        final String BGS_FRAGRANCENAME = "fragranceName";
        final String BGS_DESCRIPTION = "description";
        final String BGS_IMAGEURL = "imageUrl";
        final String BGS_PRICE = "price";
        final String BGS_USERRATING = "userRating";

        try {
            JSONObject fragranceJson = new JSONObject(rawJson);
            JSONArray fragranceArray = fragranceJson.getJSONArray(BGS_FRAGRANCES);


            for (int i = 0; i < fragranceArray.length(); i++) {

                String fragranceName;
                String description;
                String imageUrl;
                Double price;
                int userRating;

                JSONObject fragranceDetails = fragranceArray.getJSONObject(i);

                fragranceName = fragranceDetails.getString(BGS_FRAGRANCENAME);
                description = fragranceDetails.getString(BGS_DESCRIPTION);
                imageUrl = fragranceDetails.getString(BGS_IMAGEURL);
                price = fragranceDetails.getDouble(BGS_PRICE);
                userRating = fragranceDetails.getInt(BGS_USERRATING);


                ContentValues fragranceValues = new ContentValues();

                fragranceValues.put(FragranceContract.FragranceEntry.COLUMN_NAME, fragranceName);
                fragranceValues.put(FragranceContract.FragranceEntry.COLUMN_DESCRIPTION, description);
                fragranceValues.put(FragranceContract.FragranceEntry.COLUMN_IMAGE, imageUrl);
                fragranceValues.put(FragranceContract.FragranceEntry.COLUMN_PRICE, price);
                fragranceValues.put(FragranceContract.FragranceEntry.COLUMN_USERRATING, userRating);

                 db.insert(FragranceContract.FragranceEntry.TABLE_NAME, null, fragranceValues);

                Log.d(TAG, "Inserted Successfully " + fragranceValues );
            }

            Log.d(TAG, "Inserted Successfully " + fragranceArray.length() );

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }
*/

}
