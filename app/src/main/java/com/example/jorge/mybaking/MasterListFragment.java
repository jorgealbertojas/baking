package com.example.jorge.mybaking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.jorge.mybaking.adapters.StepsListAdapter;
import com.example.jorge.mybaking.models.Baking;
import com.example.jorge.mybaking.models.Steps;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import static com.example.jorge.mybaking.utilities.Utility.KEY_BUNDLE_BAKING;
import static com.example.jorge.mybaking.utilities.Utility.KEY_BUNDLE_BAKING_INGREDIENTS;
import static com.example.jorge.mybaking.utilities.Utility.KEY_INGREDIENTS;
import static com.example.jorge.mybaking.utilities.Utility.KEY_INGREDIENTS_INGREDIENTS;
import static com.example.jorge.mybaking.utilities.Utility.KEY_LIST_BAKING;
import static com.example.jorge.mybaking.utilities.Utility.KEY_LIST_BAKING_INGREDIENTS;
import static com.example.jorge.mybaking.utilities.Utility.KEY_POSITION_INGREDIENTS;

/**
 * Created by jorge on 06/12/2017.
 */

public class MasterListFragment extends Fragment implements StepsListAdapter.StepsListAdapterOnClickHandler{

    private RecyclerView mRecyclerView;
    StepsListAdapter mAdapterListSteps;

    // Define a new interface OnImageClickListener that triggers a callback in the host activity

    private ArrayList<Steps> mListSteps;

    private String mIngredients;

    private View mRootView;

    private boolean mTwoPane;

    private Bundle mBundle;


    FragmentActivity listener;

    // This event fires 1st, before creation of fragment or any views
    // The onAttach method is called when the Fragment instance is associated with an Activity.
    // This does not mean the Activity is fully initialized.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.listener = (IngredientsActivity) context;
        }
    }

    // This event fires 2nd, before views are created for the fragment
    // The onCreate method is called when the Fragment instance is being created, or re-created.
    // Use onCreate for any standard setup that does not require the activity to be fully created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if (savedInstanceState == null) {
            Intent intent = listener.getIntent();
            mBundle = intent.getBundleExtra(KEY_BUNDLE_BAKING);

            mListSteps = (ArrayList<Steps>) mBundle.getSerializable(KEY_LIST_BAKING);
            mIngredients = mBundle.getString(KEY_INGREDIENTS);
        }else{
            mListSteps = (ArrayList<Steps>) savedInstanceState.getSerializable(KEY_LIST_BAKING);
            mIngredients = savedInstanceState.getString(KEY_INGREDIENTS);
        }




        mRootView = inflater.inflate(R.layout.fragment_list_steps, container, false);



        initRecyclerView(mListSteps);

        ArrayList<Steps> arrayListSteps =  mListSteps;

        // Create the adapter
        // This adapter takes in the context and an ArrayList of ALL the image resources to display
        StepsListAdapter mAdapter = new StepsListAdapter(arrayListSteps);

        mRecyclerView.setAdapter(mAdapter);

        return mRootView;
    }

    private void initRecyclerView(ArrayList<Steps> listSteps) {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.rv_steps);

        mRecyclerView.setLayoutManager (new LinearLayoutManager(getActivity()));


        mRecyclerView.setHasFixedSize(true);
        mAdapterListSteps = new StepsListAdapter(this);
    }


    // This method is called when the fragment is no longer connected to the Activity
    // Any references saved in onAttach should be nulled out here to prevent memory leaks.
    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

    // This method is called after the parent Activity's onCreate() method has completed.
    // Accessing the view hierarchy of the parent activity must be done in the onActivityCreated.
    // At this point, it is safe to search for activity View objects by their ID, for example.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(Steps steps) {
        if(getActivity().findViewById(R.id.android_me_linear_layout) != null) {
            mTwoPane = true;
        }else{
            mTwoPane = false;
        }

        if (mTwoPane){
            Part1Fragment part1Fragment = new Part1Fragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            part1Fragment.setListIndex(steps);
            fragmentManager.beginTransaction()
                    .replace(R.id.part1_container, part1Fragment)
                    .commit();

            Part2Fragment part2Fragment = new Part2Fragment();
            FragmentManager fragmentManager2 = getActivity().getSupportFragmentManager();
            part2Fragment.setListIndex(steps.getVideoURL());
            fragmentManager2.beginTransaction()
                    .replace(R.id.part2_container, part2Fragment)
                    .commit();
        }else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(KEY_LIST_BAKING_INGREDIENTS, steps);
            bundle.putString(KEY_INGREDIENTS_INGREDIENTS, mIngredients);
            bundle.putInt(KEY_POSITION_INGREDIENTS, 0);

            Intent intent = new Intent(getActivity(), RecipeActivity.class);
            intent.putExtra(KEY_BUNDLE_BAKING_INGREDIENTS, bundle);
            startActivity(intent);
        }
    }
}
