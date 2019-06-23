package com.example.lukabaljak.shoppinglist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class SlideshowPlaceViewHolder extends RecyclerView.ViewHolder {

    ImageView mPlace;
    TextView descriptionTextView;
    ImageButton leftArrow, rightArrow;

    public SlideshowPlaceViewHolder(@NonNull View itemView) {
        super(itemView);

        mPlace = itemView.findViewById(R.id.imageSlideShow);
        descriptionTextView = itemView.findViewById(R.id.descriptionSlideShow);
        leftArrow = itemView.findViewById(R.id.leftArrow);
        rightArrow = itemView.findViewById(R.id.rightArrow);
    }


}