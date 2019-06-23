package com.example.lukabaljak.shoppinglist;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.lukabaljak.shoppinglist.dbb.DBBroker;
import com.example.lukabaljak.shoppinglist.dbb.ShoppingItem;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    RecyclerView recyclerView;
    RecyclerView slideshowRecyclerView;

    Button buttonList, buttonSlideShow;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(mGridLayoutManager);

        DBBroker dbBroker = new DBBroker(getContext());
        List<ShoppingItem> shoppingItems = dbBroker.getAllShoppingItems();


        MyAdapter myAdapter = new MyAdapter(getContext(), shoppingItems, recyclerView);
        recyclerView.setAdapter(myAdapter);

        //SLIDESHOW
        slideshowRecyclerView = view.findViewById(R.id.slideshowRecyclerView);
        GridLayoutManager mGridLayoutManagerSH = new GridLayoutManager(getContext(), 1);
        slideshowRecyclerView.setLayoutManager(mGridLayoutManagerSH);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        slideshowRecyclerView.setLayoutManager(layoutManager);
        SlideShowAdapter slideShowAdapter = new SlideShowAdapter(getContext(), shoppingItems, slideshowRecyclerView);
        slideShowAdapter.setListAdapter(myAdapter);
        slideShowAdapter.setListRecyclerView(recyclerView);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(slideshowRecyclerView);
        slideshowRecyclerView.setAdapter(slideShowAdapter);

        slideshowRecyclerView.setVisibility(View.INVISIBLE);
        myAdapter.setSlideshowRecyclerView(slideshowRecyclerView);

        RelativeLayout emptyListRelativeLayout = view.findViewById(R.id.emptyListView);
        myAdapter.setEmptyListRelativeLayout(emptyListRelativeLayout);
        slideShowAdapter.setEmptyListRelativeLayout(emptyListRelativeLayout);

        buttonList = view.findViewById(R.id.buttonList);
        buttonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayList(null);
            }
        });

        buttonSlideShow = view.findViewById(R.id.buttonSlideShow);
        buttonSlideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaySlideshow(null);
            }
        });

        if (shoppingItems.isEmpty()) {
            emptyListRelativeLayout.setVisibility(View.VISIBLE);
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onResume() {
        super.onResume();
        Log.d("RESUMED", "NJAAS");
    }

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

    public void openItem(View view) {
        Toast.makeText(getContext(), "YASSSSSS", Toast.LENGTH_SHORT).show();
    }

    public void displayList(View view) {
        recyclerView.setVisibility(View.VISIBLE);
        slideshowRecyclerView.setVisibility(View.INVISIBLE);
    }

    public void displaySlideshow(View view) {
        recyclerView.setVisibility(View.INVISIBLE);
        slideshowRecyclerView.setVisibility(View.VISIBLE);
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
    }
}
