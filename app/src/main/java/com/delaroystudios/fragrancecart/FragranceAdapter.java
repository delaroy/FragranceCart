package com.delaroystudios.fragrancecart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.delaroystudios.fragrancecart.data.Fragrance;
import com.delaroystudios.fragrancecart.data.FragranceContract;

import static com.delaroystudios.fragrancecart.DetailActivity.FRAGRANCE_DESCRIPTION;
import static com.delaroystudios.fragrancecart.DetailActivity.FRAGRANCE_ID;
import static com.delaroystudios.fragrancecart.DetailActivity.FRAGRANCE_IMAGE;
import static com.delaroystudios.fragrancecart.DetailActivity.FRAGRANCE_NAME;
import static com.delaroystudios.fragrancecart.DetailActivity.FRAGRANCE_PRICE;
import static com.delaroystudios.fragrancecart.DetailActivity.FRAGRANCE_RATING;

/**
 * Created by delaroy on 9/3/17.
 */

public class FragranceAdapter extends RecyclerView.Adapter<FragranceAdapter.ViewHolder> {

    Cursor dataCursor;
    Context context;
    int id;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, userrating, description, price;
        public ImageView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
             name = (TextView) itemView.findViewById(R.id.title);
            //  userrating = (TextView) itemView.findViewById(R.id.userrating);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra(FRAGRANCE_ID, getItem(pos).id);
                        intent.putExtra(FRAGRANCE_NAME, getItem(pos).name);
                        intent.putExtra(FRAGRANCE_DESCRIPTION, getItem(pos).description);
                        intent.putExtra(FRAGRANCE_PRICE, getItem(pos).price);
                        intent.putExtra(FRAGRANCE_IMAGE, getItem(pos).imageUrl);
                        intent.putExtra(FRAGRANCE_RATING, getItem(pos).userRating);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    public FragranceAdapter(Activity mContext, Cursor cursor) {
        dataCursor = cursor;
        context = mContext;
    }

    @Override
    public FragranceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragrance_card, parent, false);
        return new ViewHolder(cardview);
    }

    @Override
    public void onBindViewHolder(FragranceAdapter.ViewHolder holder, int position) {


        dataCursor.moveToPosition(position);

        id = dataCursor.getInt(dataCursor.getColumnIndex(FragranceContract.FragranceEntry._ID));
        String mName = dataCursor.getString(dataCursor.getColumnIndex(FragranceContract.FragranceEntry.COLUMN_NAME));
        String mDescription = dataCursor.getString(dataCursor.getColumnIndex(FragranceContract.FragranceEntry.COLUMN_DESCRIPTION));
        String mImageUrl = dataCursor.getString(dataCursor.getColumnIndex(FragranceContract.FragranceEntry.COLUMN_IMAGE));
        int mPrice = dataCursor.getInt(dataCursor.getColumnIndex(FragranceContract.FragranceEntry.COLUMN_PRICE));
        int mUserrating = dataCursor.getInt(dataCursor.getColumnIndex(FragranceContract.FragranceEntry.COLUMN_USERRATING));


        holder.name.setText(mName);

        String poster = "http://boombox.ng/images/fragrance/" + mImageUrl;

        Glide.with(context)
                .load(poster)
                .placeholder(R.drawable.load)
                .into(holder.thumbnail);


    }


    public Cursor swapCursor(Cursor cursor) {
        if (dataCursor == cursor) {
            return null;
        }
        Cursor oldCursor = dataCursor;
        this.dataCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    @Override
    public int getItemCount() {
        return (dataCursor == null) ? 0 : dataCursor.getCount();
    }

    public Fragrance getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            throw new IllegalArgumentException("Item position is out of adapter's range");
        } else if (dataCursor.moveToPosition(position)) {
            return new Fragrance(dataCursor);
        }
        return null;
    }
}



