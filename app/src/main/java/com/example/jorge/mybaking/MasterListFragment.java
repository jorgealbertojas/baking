package com.example.jorge.mybaking;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jorge.mybaking.adapters.StepsListAdapter;
import com.example.jorge.mybaking.models.Steps;

import java.util.ArrayList;

import static com.example.jorge.mybaking.utilities.Utility.KEY_BUNDLE_BAKING;
import static com.example.jorge.mybaking.utilities.Utility.KEY_EXTRA_LIST_STEPS;
import static com.example.jorge.mybaking.utilities.Utility.KEY_EXTRA_LIST_STEPS_BUNDLE;
import static com.example.jorge.mybaking.utilities.Utility.KEY_INGREDIENTS;
import static com.example.jorge.mybaking.utilities.Utility.KEY_INGREDIENTS_INGREDIENTS;
import static com.example.jorge.mybaking.utilities.Utility.KEY_LIST_BAKING;
import static com.example.jorge.mybaking.utilities.Utility.KEY_POSITION;

/**
 * Created by jorge on 06/12/2017.
 */

public class MasterListFragment extends Fragment implements StepsListAdapter.StepsListAdapterOnClickHandler {

    StepsListAdapter mAdapterListSteps;
    private RecyclerView mRecyclerView;

    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    private ArrayList<Steps> mListSteps;

    private String mIngredients;

    private View mRootView;

    private boolean mTwoPane;

    private Bundle mBundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mListSteps = (ArrayList<Steps>) savedInstanceState.getSerializable(KEY_LIST_BAKING);
            mIngredients = savedInstanceState.getString(KEY_INGREDIENTS);
        }
    }

    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_list_steps, container, false);

        return mRootView;
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.rv_steps);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mAdapterListSteps = new StepsListAdapter(this);
    }


    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putSerializable(KEY_LIST_BAKING, mListSteps);
        currentState.putString(KEY_INGREDIENTS, mIngredients);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBundle = getActivity().getIntent().getBundleExtra(KEY_BUNDLE_BAKING);

        mListSteps = (ArrayList<Steps>) mBundle.getSerializable(KEY_LIST_BAKING);
        mIngredients = mBundle.getString(KEY_INGREDIENTS);

        if (savedInstanceState == null) {
            Intent intent = getActivity().getIntent();
            mBundle = intent.getBundleExtra(KEY_BUNDLE_BAKING);
            mListSteps = (ArrayList<Steps>) mBundle.getSerializable(KEY_LIST_BAKING);
            mIngredients = mBundle.getString(KEY_INGREDIENTS);
        } else {
            mListSteps = (ArrayList<Steps>) savedInstanceState.getSerializable(KEY_LIST_BAKING);
            mIngredients = savedInstanceState.getString(KEY_INGREDIENTS);
        }

        initRecyclerView();

        ArrayList<Steps> arrayListSteps = mListSteps;

        // Create the adapter
        // This adapter takes in the context and an ArrayList of ALL the image resources to display
        StepsListAdapter mAdapter = new StepsListAdapter(arrayListSteps);

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(Steps steps, int position) {

        Resources res = getResources();
        mTwoPane = res.getBoolean(R.bool.adjust_view_bounds);


        if (mTwoPane) {
            InformationIngredientsFragment part1Fragment = new InformationIngredientsFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            part1Fragment.setListIndex(steps);
            fragmentManager.beginTransaction()
                    .replace(R.id.part1_container, part1Fragment)
                    .commit();

            VideoIngredientsFragment part2Fragment = new VideoIngredientsFragment();
            FragmentManager fragmentManager2 = getActivity().getSupportFragmentManager();
            part2Fragment.setListIndex(steps.getVideoURL());
            fragmentManager2.beginTransaction()
                    .replace(R.id.part2_container, part2Fragment)
                    .commit();
        } else {

            Class destinationClass = VideoWithInformationActivity.class;
            Intent intentToStartDetailActivity = new Intent(getContext(), destinationClass);

            Bundle bundle = new Bundle();
            bundle.putSerializable(KEY_EXTRA_LIST_STEPS, mListSteps);
            bundle.putString(KEY_POSITION, Integer.toString(position));
            bundle.putSerializable(KEY_INGREDIENTS_INGREDIENTS, steps.getDescription());

            intentToStartDetailActivity.putExtra(KEY_EXTRA_LIST_STEPS_BUNDLE, bundle);
            startActivity(intentToStartDetailActivity);
        }
    }
}
