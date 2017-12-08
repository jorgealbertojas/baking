package com.example.jorge.mybaking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.jorge.mybaking.adapters.StepsListAdapter;
import com.example.jorge.mybaking.models.Baking;
import com.example.jorge.mybaking.models.Steps;

import org.w3c.dom.Text;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.example.jorge.mybaking.utilities.Utility.KEY_BUNDLE_BAKING;
import static com.example.jorge.mybaking.utilities.Utility.KEY_INGREDIENTS;
import static com.example.jorge.mybaking.utilities.Utility.KEY_LIST_BAKING;

/**
 * Created by jorge on 06/12/2017.
 */

public class
StepsListFragment extends Fragment {

    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    OnImageClickListener mCallback;
    private List<Baking> mListBanking;

    // OnImageClickListener interface, calls a method in the host activity named onImageSelected
    public interface OnImageClickListener {
        void onImageSelected(int position);


    }



    // Mandatory empty constructor
    public StepsListFragment() {
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        Intent intent = null;
        try {
            intent = Intent.getIntentOld(DetailActivity.ACCESSIBILITY_SERVICE);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnImageClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }


    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getBundleExtra(KEY_BUNDLE_BAKING);
        mListBanking = (ArrayList<Baking>) bundle.getSerializable(KEY_LIST_BAKING);
        String ingredients = bundle.getString(KEY_INGREDIENTS);



        final View rootView = inflater.inflate(R.layout.fragment_list_steps, container, false);

        TextView textViewIngredients = (TextView) rootView.findViewById(R.id.tv_id_ingredients);
        textViewIngredients.setText(ingredients);

        // Get a reference to the GridView in the fragment_master_list xml layout file
        GridView gridView = (GridView) rootView.findViewById(R.id.gv_steps);


        ArrayList<Steps> arrayListSteps =  mListBanking.get(0).getSteps();

        // Create the adapter
        // This adapter takes in the context and an ArrayList of ALL the image resources to display
        StepsListAdapter mAdapter = new StepsListAdapter(getContext(), arrayListSteps);

        // Set the adapter on the GridView
        gridView.setAdapter(mAdapter);

        // Set a click listener on the gridView and trigger the callback onImageSelected when an item is clicked
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Trigger the callback method and pass in the position that was clicked
                mCallback.onImageSelected(position);
            }
        });



        // Return the root view
        return rootView;
    }

}
