package com.delaroystudios.fragrancecart.fragment;

import android.support.v4.content.CursorLoader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;

import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delaroystudios.fragrancecart.FragranceAdapter;
import com.delaroystudios.fragrancecart.R;
import com.delaroystudios.fragrancecart.data.FragranceContract;
import com.delaroystudios.fragrancecart.data.FragranceDbHelper;

/**
 * Created by delaroy on 1/15/18.
 */

public class FragranceFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recyclerView;
    FragranceAdapter fragranceAdapter;
    private static final int FRAGRANCE_LOADER = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragrance_fragment, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        }

        fragranceAdapter = new FragranceAdapter(getActivity(), null);
        recyclerView.setAdapter(fragranceAdapter);

        getLoaderManager().initLoader(FRAGRANCE_LOADER,null,this);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                FragranceContract.FragranceEntry._ID,
                FragranceContract.FragranceEntry.COLUMN_NAME,
                FragranceContract.FragranceEntry.COLUMN_DESCRIPTION,
                FragranceContract.FragranceEntry.COLUMN_IMAGE,
                FragranceContract.FragranceEntry.COLUMN_PRICE,
                FragranceContract.FragranceEntry.COLUMN_USERRATING
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(getActivity(),   // Parent activity context
                FragranceContract.FragranceEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        fragranceAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        fragranceAdapter.swapCursor(null);

    }
}
