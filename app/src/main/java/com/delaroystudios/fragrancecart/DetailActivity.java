package com.delaroystudios.fragrancecart;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.delaroystudios.fragrancecart.count.Utils;
import com.delaroystudios.fragrancecart.data.FragranceContract;
import com.delaroystudios.fragrancecart.data.FragranceDbHelper;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import static com.delaroystudios.fragrancecart.data.FragranceContract.FragranceEntry.CART_TABLE;
import static com.delaroystudios.fragrancecart.data.FragranceContract.FragranceEntry.COLUMN_WISH_NAME;

/**
 * Created by delaroy on 9/3/17.
 */

public class DetailActivity extends AppCompatActivity {

    public static final String  FRAGRANCE_NAME = "fragranceName";
    public static final String  FRAGRANCE_DESCRIPTION = "fragranceDescription";
    public static final String  FRAGRANCE_RATING = "fragranceRating";
    public static final String  FRAGRANCE_IMAGE = "fragranceImage";
    public static final String  FRAGRANCE_PRICE = "fragrancePrice";
    public static final String  FRAGRANCE_ID = "fragranceId";

    private ImageView mImage;


    String fragranceName, description, fragImage;
    int rating, iD;
    Double price;
    private int mQuantity = 1;
    private double mTotalPrice;
    String imagePath;
    TextView costTextView;
    ContentResolver mContentResolver;
    private SQLiteDatabase mDb;

    private int mNotificationsCount = 0;
    Button addToCartButton;

    MaterialFavoriteButton toolbarFavorite;
    Toolbar toolbar;
    final ContentValues wishValues = new ContentValues();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragrance_details);


        mContentResolver = this.getContentResolver();
        FragranceDbHelper dbHelper = new FragranceDbHelper(this);
        mDb = dbHelper.getWritableDatabase();


        mImage = (ImageView) findViewById(R.id.fragranceImage);
        Intent intentThatStartedThisActivity = getIntent();
        addToCartButton = (Button) findViewById(R.id.cart_button);

        costTextView = (TextView) findViewById(
                R.id.cost_text_view);

        if (intentThatStartedThisActivity.hasExtra(FRAGRANCE_NAME)) {
            iD = getIntent().getExtras().getInt(FRAGRANCE_ID);
            fragranceName = getIntent().getExtras().getString(FRAGRANCE_NAME);
            description = getIntent().getExtras().getString(FRAGRANCE_DESCRIPTION);
            rating = getIntent().getExtras().getInt(FRAGRANCE_RATING);
            fragImage = getIntent().getExtras().getString(FRAGRANCE_IMAGE);
            price = getIntent().getExtras().getDouble(FRAGRANCE_PRICE);

            TextView desc = (TextView) findViewById(R.id.description);
            desc.setText(description);

            TextView fragmentPrice = (TextView) findViewById(R.id.price);
            DecimalFormat precision = new DecimalFormat("0.00");
            fragmentPrice.setText("$" + precision.format(price));

            float f = Float.parseFloat(Double.toString(rating));

            setTitle(fragranceName);

            RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingLevel);
            ratingBar.setRating(f);

            imagePath = "http://boombox.ng/images/fragrance/" + fragImage;

            Glide.with(this)
                    .load(imagePath)
                    .placeholder(R.drawable.load)
                    .into(mImage);


        }


        if (mQuantity == 1){

            mTotalPrice = price;
            displayCost(mTotalPrice);
        }


        //in the toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        wishValues.put(FragranceContract.FragranceEntry.COLUMN_WISH_NAME, fragranceName);
        wishValues.put(FragranceContract.FragranceEntry.COLUMN_WISH_DESCRIPTION, description);
        wishValues.put(FragranceContract.FragranceEntry.COLUMN_WISH_IMAGE, fragImage);
        wishValues.put(FragranceContract.FragranceEntry.COLUMN_WISH_PRICE, price);
        wishValues.put(FragranceContract.FragranceEntry.COLUMN_WISH_USERRATING, rating);

        addWish();

    }

    public void addWish(){
        if (Exists(fragranceName)){
            toolbarFavorite = new MaterialFavoriteButton.Builder(this) //
                    .favorite(true)
                    .color(MaterialFavoriteButton.STYLE_WHITE)
                    .type(MaterialFavoriteButton.STYLE_HEART)
                    .rotationDuration(400)
                    .create();
            toolbar.addView(toolbarFavorite);
            toolbarFavorite.setOnFavoriteChangeListener(
                    new MaterialFavoriteButton.OnFavoriteChangeListener() {
                        @Override
                        public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                            if (favorite == true) {
                                mContentResolver.insert(FragranceContract.FragranceEntry.CONTENT_URI_WISH, wishValues);
                                Snackbar.make(buttonView, getString(R.string.item_added),
                                        Snackbar.LENGTH_SHORT).show();
                            } else {
                                String stringId = Integer.toString(iD);
                                Uri uri = FragranceContract.FragranceEntry.CONTENT_URI_WISH;
                                uri = uri.buildUpon().appendPath(stringId).build();
                                getContentResolver().delete(uri, null, null);
                                Snackbar.make(buttonView, getString(R.string.item_removed),
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });


        }else {
            toolbarFavorite = new MaterialFavoriteButton.Builder(this) //
                    .color(MaterialFavoriteButton.STYLE_WHITE)
                    .type(MaterialFavoriteButton.STYLE_HEART)
                    .rotationDuration(400)
                    .create();
            toolbar.addView(toolbarFavorite);
            toolbarFavorite.setOnFavoriteChangeListener(
                    new MaterialFavoriteButton.OnFavoriteChangeListener() {
                        @Override
                        public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                            if (favorite == true) {
                                mContentResolver.insert(FragranceContract.FragranceEntry.CONTENT_URI_WISH, wishValues);
                                Snackbar.make(buttonView, getString(R.string.item_added),
                                        Snackbar.LENGTH_SHORT).show();
                            } else {
                                String stringId = Integer.toString(iD);
                                Uri uri = FragranceContract.FragranceEntry.CONTENT_URI_WISH;
                                uri = uri.buildUpon().appendPath(stringId).build();
                                getContentResolver().delete(uri, null, null);
                                Snackbar.make(buttonView, getString(R.string.item_removed),
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });


        }
    }

    public boolean Exists(String searchItem) {

        String[] projection = {
                FragranceContract.FragranceEntry._WISHID,
                FragranceContract.FragranceEntry.COLUMN_WISH_NAME,
                FragranceContract.FragranceEntry.COLUMN_WISH_DESCRIPTION,
                FragranceContract.FragranceEntry.COLUMN_WISH_IMAGE,
                FragranceContract.FragranceEntry.COLUMN_WISH_PRICE,
                FragranceContract.FragranceEntry.COLUMN_WISH_USERRATING
        };
        String selection = COLUMN_WISH_NAME + " =?";
        String[] selectionArgs = { searchItem };
        String limit = "1";

        Cursor cursor = mDb.query(FragranceContract.FragranceEntry.WISH_TABLE, projection, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Get the notifications MenuItem and
        // its LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.action_notifications);
        LayerDrawable icon = (LayerDrawable) item.getIcon();

        // Update LayerDrawable's BadgeDrawable
        Utils.setBadgeCount(this, icon, mNotificationsCount);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_notifications:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        new FetchCountTask().execute();
    }

    public void increment(View view){

        price = getIntent().getExtras().getDouble(FRAGRANCE_PRICE);
        mQuantity = mQuantity + 1;
        displayQuantity(mQuantity);
        mTotalPrice = mQuantity * price;
        displayCost(mTotalPrice);
    }

    public void decrement(View view){
        if (mQuantity > 1){

            mQuantity = mQuantity - 1;
            displayQuantity(mQuantity);
            mTotalPrice = mQuantity * price;
            displayCost(mTotalPrice);

        }
    }

    private void displayQuantity(int numberOfItems) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText(String.valueOf(numberOfItems));
    }

    private void displayCost(double totalPrice) {

        String convertPrice = NumberFormat.getCurrencyInstance().format(totalPrice);
        costTextView.setText(convertPrice);
    }

    private void addValuesToCart() {

        ContentValues cartValues = new ContentValues();

        cartValues.put(FragranceContract.FragranceEntry.COLUMN_CART_NAME, fragranceName);
        cartValues.put(FragranceContract.FragranceEntry.COLUMN_CART_IMAGE, fragImage);
        cartValues.put(FragranceContract.FragranceEntry.COLUMN_CART_QUANTITY, mQuantity);
        cartValues.put(FragranceContract.FragranceEntry.COLUMN_CART_TOTAL_PRICE, mTotalPrice);



        mContentResolver.insert(FragranceContract.FragranceEntry.CONTENT_URI, cartValues);

        Toast.makeText(this, "Successfully added to Cart",
                Toast.LENGTH_SHORT).show();


    }

    public void addToCart(View view) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.add_to_cart);
        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                addValuesToCart();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the items.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void updateNotificationsBadge(int count) {
        mNotificationsCount = count;

        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
    }


    /*
Sample AsyncTask to fetch the notifications count
*/
    class FetchCountTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            String countQuery = "SELECT  * FROM " + CART_TABLE;
            Cursor cursor = mDb.rawQuery(countQuery, null);
            int count = cursor.getCount();
            cursor.close();
            return count;

        }

        @Override
        public void onPostExecute(Integer count) {
            updateNotificationsBadge(count);
        }
    }


}
