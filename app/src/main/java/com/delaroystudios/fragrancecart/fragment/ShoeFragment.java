package com.delaroystudios.fragrancecart.fragment;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delaroystudios.fragrancecart.FragranceAdapter;
import com.delaroystudios.fragrancecart.R;
import com.delaroystudios.fragrancecart.ShoeAdapter;
import com.delaroystudios.fragrancecart.data.FragranceContract;

/**
 * Created by delaroy on 1/15/18.
 */

public class ShoeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recyclerView;
    ShoeAdapter shoeAdapter;
    private static final int MENSHOE_LOADER = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shoe_fragment, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        }

        shoeAdapter = new ShoeAdapter(getActivity(), null);
        recyclerView.setAdapter(shoeAdapter);

        getLoaderManager().initLoader(MENSHOE_LOADER,null,this);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                FragranceContract.FragranceEntry._ID,
                FragranceContract.FragranceEntry.COLUMN_MENSHOENAME,
                FragranceContract.FragranceEntry.COLUMN_MENSHOEDESCRIPTION,
                FragranceContract.FragranceEntry.COLUMN_MENSHOEIMAGE,
                FragranceContract.FragranceEntry.COLUMN_MENSHOEPRICE,
                FragranceContract.FragranceEntry.COLUMN_MENSHOESIZE,
                FragranceContract.FragranceEntry.COLUMN_MENSHOEUSERRATING
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(getActivity(),   // Parent activity context
                FragranceContract.FragranceEntry.CONTENT_URI_MENSHOE,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        shoeAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        shoeAdapter.swapCursor(null);

    }
}

