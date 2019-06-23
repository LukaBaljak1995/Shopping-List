package com.example.lukabaljak.shoppinglist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

public class PlaceViewHolder extends RecyclerView.ViewHolder {

    ImageView mPlace;

    public PlaceViewHolder(@NonNull View itemView) {
        super(itemView);

        mPlace = itemView.findViewById(R.id.ivPlace);
    }

    public void setmPlace(ImageView mPlace) {
        this.mPlace = mPlace;
    }


}
