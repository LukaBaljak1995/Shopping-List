package com.example.lukabaljak.shoppinglist;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lukabaljak.shoppinglist.dbb.DBBroker;
import com.example.lukabaljak.shoppinglist.dbb.ShoppingItem;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    //ATTRIBUTES
    ImageView imageHolder;
    FloatingActionButton fabCamera;
    FloatingActionButton fabGallery;
    FloatingActionButton fabAdd;

    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add, container, false);

        imageHolder = view.findViewById(R.id.imageHolder);

        fabCamera = (FloatingActionButton) view.findViewById(R.id.fabCamera);
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });

        fabGallery = view.findViewById(R.id.fabGallery);
        fabGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        fabAdd = view.findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddDialog();
            }
        });

        return view;
    }

    public void openAddDialog() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this.getContext());
        dialog.setMessage("Add a description (optional)");

        LinearLayout layout = new LinearLayout(this.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        //naziv
        final EditText descriptionEditText = new EditText(this.getContext());
        descriptionEditText.setHint("Description");
        layout.addView(descriptionEditText);

        dialog.setView(layout);

        dialog.setNeutralButton("Add",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DBBroker dbBroker = new DBBroker(getContext());

                        String description = descriptionEditText.getText().toString();
                        if (description == null)
                            description = "";

                        ShoppingItem shoppingItem = new ShoppingItem("", description);
                        int id = dbBroker.addShoppingItem(shoppingItem);
                        shoppingItem.setId(id);

                        String filename = "photo" + id;
                        FileOutputStream outputStream;

                        Bitmap bitmap = ((BitmapDrawable) imageHolder.getDrawable()).getBitmap();

                        try {

                            outputStream = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                            outputStream.close();
                            mListener.addedItem(shoppingItem);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        imageHolder.setImageDrawable(getResources().getDrawable(R.drawable.ic_image_black_24dp));
                        alterButtonDisplay(GET_IMAGE);


                    }
                });
        dialog.show();
    }


    private static final int CAMERA_REQUEST = 1888;

    private void openCamera() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    2);
            return;
        }

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        startActivityForResult(intent, CAMERA_REQUEST);
    }

    public static final int SELECT_IMAGE = 1;

    private void openGallery() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    2);
            return;
        }

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
    {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");

                photo = getResizedBitmap(photo, 300, 300);
                imageHolder.setImageBitmap(photo);
                alterButtonDisplay(ADD_IMAGE);
            } else {
                Toast.makeText(getContext(), "Omg whyyy", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());

                        imageHolder.setImageBitmap(bitmap);
                        alterButtonDisplay(ADD_IMAGE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
            }
        }


    }


    static final int GET_IMAGE = 1;
    static final int ADD_IMAGE = 2;

    @SuppressLint("RestrictedApi")
    private void alterButtonDisplay(int code) {
        if (code == GET_IMAGE) {
            fabCamera.setVisibility(View.VISIBLE);
            fabGallery.setVisibility(View.VISIBLE);
            fabAdd.setVisibility(View.GONE);

        }

        if (code == ADD_IMAGE) {
            fabCamera.setVisibility(View.GONE);
            fabGallery.setVisibility(View.GONE);
            fabAdd.setVisibility(View.VISIBLE);
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void addedItem(ShoppingItem shoppingItem);
    }
}
