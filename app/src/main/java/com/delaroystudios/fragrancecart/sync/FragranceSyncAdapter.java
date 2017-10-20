package com.delaroystudios.fragrancecart.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.delaroystudios.fragrancecart.R;
import com.delaroystudios.fragrancecart.data.FragranceContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by delaroy on 10/18/17.
 */

public class FragranceSyncAdapter extends AbstractThreadedSyncAdapter {

    public final String LOG_TAG = FragranceSyncAdapter.class.getSimpleName();

    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    ContentResolver mContentResolver;


    public FragranceSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        mContentResolver = context.getContentResolver();

    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");
        BufferedReader reader = null;

        HttpURLConnection urlConnection = null;

        // Will contain the raw JSON response as a string.
        String fragranceJsonStr = null;

        try {

            final String FRAGRANCE_URL =
                    "http://unitypuzzlegame.com/fragrance/fragrance.json";

            Uri builtUri = Uri.parse(FRAGRANCE_URL).buildUpon()
                    .build();


            URL url = new URL(builtUri.toString());

            Log.d(LOG_TAG, "The URL link is  " + url);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(1000000 /* milliseconds */);
            urlConnection.setConnectTimeout(1500000/* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return;
            }

            fragranceJsonStr = buffer.toString();
            getFragranceData(fragranceJsonStr);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error passing data ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return;
    }

    private void getFragranceData(String fragranceJsonStr)
            throws JSONException {

        final String BGS_FRAGRANCES = "fragrances";

        final String BGS_FRAGRANCENAME = "fragranceName";
        final String BGS_DESCRIPTION = "description";
        final String BGS_IMAGEURL = "imageUrl";
        final String BGS_PRICE = "price";
        final String BGS_USERRATING = "userRating";
        final String BGS_ITEMID = "itemid";

        try {
            JSONObject fragranceJson = new JSONObject(fragranceJsonStr);
            JSONArray fragranceArray = fragranceJson.getJSONArray(BGS_FRAGRANCES);


            for (int i = 0; i < fragranceArray.length(); i++) {

                String fragranceName;
                String description;
                String imageUrl;
                Double price;
                int userRating;
                int itemId;

                JSONObject fragranceDetails = fragranceArray.getJSONObject(i);

                fragranceName = fragranceDetails.getString(BGS_FRAGRANCENAME);
                description = fragranceDetails.getString(BGS_DESCRIPTION);
                imageUrl = fragranceDetails.getString(BGS_IMAGEURL);
                price = fragranceDetails.getDouble(BGS_PRICE);
                userRating = fragranceDetails.getInt(BGS_USERRATING);
                itemId = fragranceDetails.getInt(BGS_ITEMID);


                ContentValues fragranceValues = new ContentValues();

                fragranceValues.put(FragranceContract.FragranceEntry.COLUMN_NAME, fragranceName);
                fragranceValues.put(FragranceContract.FragranceEntry.COLUMN_DESCRIPTION, description);
                fragranceValues.put(FragranceContract.FragranceEntry.COLUMN_IMAGE, imageUrl);
                fragranceValues.put(FragranceContract.FragranceEntry.COLUMN_PRICE, price);
                fragranceValues.put(FragranceContract.FragranceEntry.COLUMN_USERRATING, userRating);
                fragranceValues.put(FragranceContract.FragranceEntry.COLUMN_ITEMID, itemId);

                mContentResolver.insert(FragranceContract.FragranceEntry.CONTENT_URI,  fragranceValues);

                Log.d(LOG_TAG, "Inserted Successfully " + fragranceValues );
            }

            Log.d(LOG_TAG, "Inserted Successfully " + fragranceArray.length() );

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        FragranceSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }


}
