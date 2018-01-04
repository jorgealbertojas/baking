package com.example.jorge.mybaking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jorge.mybaking.models.Steps;

/**
 * Created by jorge on 08/12/2017.
 */

public class InformationIngredientsFragment extends Fragment {


    // Tag for logging
    private static final String TAG = "BodyPartFragment";

    // Variables to store a list of image resources and the index of the image that this fragment displays
    private Steps mSteps;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public InformationIngredientsFragment() {
    }

    /**
     * Inflates the fragment layout file and sets the correct resource for the image to display
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the Android-Me fragment layout
        View rootView = inflater.inflate(R.layout.fragment_information, container, false);

        TextView textViewRecipe = (TextView) rootView.findViewById(R.id.tv_recipe);

        textViewRecipe.setText(mSteps.getDescription());

        // Return the rootView*/
        return rootView;
    }

    public void setListIndex(Steps steps) {
        mSteps = steps;
    }


}
