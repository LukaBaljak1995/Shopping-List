package com.example.lukabaljak.shoppinglist;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.lukabaljak.shoppinglist.dbb.DBBroker;
import com.example.lukabaljak.shoppinglist.dbb.ShoppingItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class SlideShowAdapter extends RecyclerView.Adapter {

    private Context mContext;
    List<ShoppingItem> mShoppingList;
    RecyclerView slideshowRecyclerView, listRecyclerView;
    RecyclerView.Adapter listAdapter;
    RelativeLayout emptyListRelativeLayout;

    public SlideShowAdapter(Context mContext, List<ShoppingItem> mShoppingList, RecyclerView slideshowRecyclerView) {
        this.mContext = mContext;
        this.mShoppingList = mShoppingList;
        this.slideshowRecyclerView = slideshowRecyclerView;
    }

    public void setListAdapter(RecyclerView.Adapter listAdapter) {
        this.listAdapter = listAdapter;
    }

    public void setListRecyclerView(RecyclerView listRecyclerView) {
        this.listRecyclerView = listRecyclerView;
    }

    public void setEmptyListRelativeLayout(RelativeLayout emptyListRelativeLayout) {
        this.emptyListRelativeLayout = emptyListRelativeLayout;
    }

    @NonNull
    @Override
    public SlideshowPlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slideshow_recyclerview_custom_layout,
                parent, false);


        if (mShoppingList.isEmpty()) {
            emptyListRelativeLayout.setVisibility(View.VISIBLE);
        } else {
            emptyListRelativeLayout.setVisibility(View.INVISIBLE);
        }

        ImageButton leftArrow = view.findViewById(R.id.leftArrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewi) {
                int itemPosition = slideshowRecyclerView.getChildLayoutPosition(view);
                slideshowRecyclerView.getLayoutManager().scrollToPosition(itemPosition - 1);
            }
        });

        ImageButton rightArrow = view.findViewById(R.id.rightArrow);
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewi) {
                int itemPosition = slideshowRecyclerView.getChildLayoutPosition(view);
                slideshowRecyclerView.getLayoutManager().scrollToPosition(itemPosition + 1);
            }
        });


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewi) {

                int itemPosition = slideshowRecyclerView.getChildLayoutPosition(viewi);
                ShoppingItem shoppingItem = mShoppingList.get(itemPosition);
                Log.d("SLIDEINFO", "VELICINA NIZA: " + mShoppingList.size()
                        + ", " + itemPosition);
//                Toast.makeText(mContext, "VELICINA NIZA: " + mShoppingList.size()
//                        + ", " + itemPosition, Toast.LENGTH_SHORT).show();
            }
        });

        Button deleteButton = view.findViewById(R.id.deleteShoppingItem);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewi) {
                int itemPosition = slideshowRecyclerView.getChildLayoutPosition(view);
                ShoppingItem shoppingItem = mShoppingList.get(itemPosition);

                DBBroker dbBroker = new DBBroker(mContext);
                dbBroker.deleteShoppingItem(shoppingItem);
                mShoppingList.remove(itemPosition);
                notifyItemRemoved(itemPosition);
                listAdapter.notifyDataSetChanged();

                if (mShoppingList.isEmpty()) {
                    emptyListRelativeLayout.setVisibility(View.VISIBLE);
                } else {
                    emptyListRelativeLayout.setVisibility(View.INVISIBLE);
                }

                File dir = mContext.getFilesDir();
                File file = new File(dir, "photo" + shoppingItem.getId());
                boolean deleted = file.delete();
                Log.d("OBRISANO", String.valueOf(deleted));
            }
        });


        Button editButton = view.findViewById(R.id.editShoppingItem);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewi) {
                final int itemPosition = slideshowRecyclerView.getChildLayoutPosition(view);
                final ShoppingItem shoppingItem = mShoppingList.get(itemPosition);
//ovo je dijalog
                final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setMessage("Alter the description");

                LinearLayout layout = new LinearLayout(mContext);
                layout.setOrientation(LinearLayout.VERTICAL);
                //description
                final EditText descriptionEditText = new EditText(mContext);
                descriptionEditText.setText(shoppingItem.getDescription());
                descriptionEditText.setHint("Description");
                layout.addView(descriptionEditText);

                dialog.setView(layout);

                dialog.setPositiveButton("Alter",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DBBroker dbBroker = new DBBroker(mContext);

                                String description = descriptionEditText.getText().toString();

                                shoppingItem.setDescription(description);

                                DBBroker dbBroker1 = new DBBroker(mContext);
                                dbBroker1.updateShoppingItem(shoppingItem);
                                mShoppingList.get(itemPosition).setDescription(description);
                                notifyItemChanged(itemPosition);
                                listAdapter.notifyDataSetChanged();
                            }
                        });
                dialog.setNegativeButton("Dismiss",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                dialog.show();


            }
        });

        FloatingActionButton fabDismissSlideshow = view.findViewById(R.id.dismissSlideshow);
        fabDismissSlideshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listRecyclerView.setVisibility(View.VISIBLE);
                slideshowRecyclerView.setVisibility(View.INVISIBLE);
            }
        });

        return new SlideshowPlaceViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        int id = mShoppingList.get(i).getId();
        try {

            FileInputStream in = mContext.openFileInput("photo" + id);

            Log.d("USAO", "USAOO");

            Bitmap bitmap = BitmapFactory.decodeStream(in);

            ((SlideshowPlaceViewHolder) viewHolder).mPlace.setImageBitmap(bitmap);
            ((SlideshowPlaceViewHolder) viewHolder).descriptionTextView.setText(mShoppingList.get(i).getDescription());

            if (i == 0) {
                ((SlideshowPlaceViewHolder) viewHolder).leftArrow.setVisibility(View.INVISIBLE);
            } else {
                ((SlideshowPlaceViewHolder) viewHolder).leftArrow.setVisibility(View.VISIBLE);
            }

            if (i == mShoppingList.size() - 1) {
                ((SlideshowPlaceViewHolder) viewHolder).rightArrow.setVisibility(View.INVISIBLE);
            } else {
                ((SlideshowPlaceViewHolder) viewHolder).rightArrow.setVisibility(View.VISIBLE);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mShoppingList.size();
    }


}
