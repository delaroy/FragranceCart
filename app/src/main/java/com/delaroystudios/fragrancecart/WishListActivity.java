package com.delaroystudios.fragrancecart;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.delaroystudios.fragrancecart.data.FragranceContract;

/**
 * Created by delaroy on 10/1/17.
 */

public class WishListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recyclerView;
    WishAdapter wishAdapter;
    private static final int WISH_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        wishAdapter = new WishAdapter(this, null);
        recyclerView.setAdapter(wishAdapter);

        getLoaderManager().initLoader(WISH_LOADER,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                FragranceContract.FragranceEntry._WISHID,
                FragranceContract.FragranceEntry.COLUMN_WISH_NAME,
                FragranceContract.FragranceEntry.COLUMN_WISH_DESCRIPTION,
                FragranceContract.FragranceEntry.COLUMN_WISH_IMAGE,
                FragranceContract.FragranceEntry.COLUMN_WISH_PRICE,
                FragranceContract.FragranceEntry.COLUMN_WISH_USERRATING
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                FragranceContract.FragranceEntry.CONTENT_URI_WISH,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        wishAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        wishAdapter.swapCursor(null);
    }
}
