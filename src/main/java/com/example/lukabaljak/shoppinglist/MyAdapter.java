package com.example.lukabaljak.shoppinglist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.lukabaljak.shoppinglist.R;
import com.example.lukabaljak.shoppinglist.dbb.ShoppingItem;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter {

    private Context mContext;
    List<ShoppingItem> mShoppingList;
    RecyclerView recyclerView;
    RecyclerView slideshowRecyclerView;
    RelativeLayout emptyListRelativeLayout;

    public MyAdapter(Context mContext, List<ShoppingItem> mShoppingList, RecyclerView recyclerView) {
        this.mContext = mContext;
        this.mShoppingList = mShoppingList;
        this.recyclerView = recyclerView;

    }

    public void setSlideshowRecyclerView(RecyclerView slideshowRecyclerView) {
        this.slideshowRecyclerView = slideshowRecyclerView;
    }

    public void setEmptyListRelativeLayout(RelativeLayout emptyListRelativeLayout) {
        this.emptyListRelativeLayout = emptyListRelativeLayout;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_custom_layout,
                parent, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewi) {

                int id = viewi.getId();
                int itemPosition = recyclerView.getChildLayoutPosition(viewi);

                slideshowRecyclerView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);

                /*ViewParent viewParent = recyclerView.getParent();
                View parent = (View) ((ViewGroup) viewParent);

                parent.setBackgroundResource(R.color.colorBlurGray);
*/
                slideshowRecyclerView.getLayoutManager().scrollToPosition(itemPosition);
                //Toast.makeText(mContext, "ID JE " + itemPosition, Toast.LENGTH_SHORT).show();
            }
        });
        return new PlaceViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        int id = mShoppingList.get(i).getId();
        try {

            FileInputStream in = mContext.openFileInput("photo" + id);


            Log.d("USAO", "USAOO");

            Bitmap bitmap = BitmapFactory.decodeStream(in);

            ((PlaceViewHolder) viewHolder).mPlace.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return mShoppingList.size();
    }


}
