package com.example.jorge.mybaking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.jorge.mybaking.models.Baking;
import com.example.jorge.mybaking.models.Steps;

import java.util.ArrayList;
import static com.example.jorge.mybaking.utilities.Utility.KEY_BUNDLE_BAKING;
import static com.example.jorge.mybaking.utilities.Utility.KEY_EXTRA_FILE;
import static com.example.jorge.mybaking.utilities.Utility.KEY_LIST_BAKING;

/**
 * Created by jorge on 08/12/2017.
 */

public class Part1Fragment extends Fragment {


    // Tag for logging
    private static final String TAG = "BodyPartFragment";

    // Variables to store a list of image resources and the index of the image that this fragment displays
    private Steps mSteps;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public Part1Fragment() {
    }

    /**
     * Inflates the fragment layout file and sets the correct resource for the image to display
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Load the saved state (the list of images and list index) if there is one
        if(savedInstanceState != null) {
           // mImageIds = savedInstanceState.getIntegerArrayList(IMAGE_ID_LIST);
           // mListIndex = savedInstanceState.getInt(LIST_INDEX);
        }

        // Inflate the Android-Me fragment layout
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        TextView textViewRecipe =  (TextView) rootView.findViewById(R.id.tv_recipe);
        Button buttonVideo =  (Button) rootView.findViewById(R.id.b_video);


        textViewRecipe.setText(mSteps.getDescription());


            // Set a click listener on the image view
        buttonVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Class destinationClass = PlayerActivity.class;
                    Intent intentToStartDetailActivity = new Intent(getContext(), destinationClass);
                    intentToStartDetailActivity.putExtra(KEY_EXTRA_FILE, mSteps.getVideoURL());
                    startActivity(intentToStartDetailActivity);

                }
            });



        // Return the rootView*/
        return rootView;
    }



    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {

    }

    public void setListIndex(Steps steps) {
      mSteps   = steps;
    }


}
