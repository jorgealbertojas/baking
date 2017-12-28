package com.example.jorge.mybaking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import static com.example.jorge.mybaking.utilities.Utility.KEY_INGREDIENTS;
import static com.example.jorge.mybaking.utilities.Utility.KEY_LIST_BAKING;
import static com.example.jorge.mybaking.utilities.Utility.KEY_POSITION;
import static com.example.jorge.mybaking.utilities.Utility.KEY_TWO_PANE;

/**
 * Created by jorge on 06/12/2017.
 */

public class MasterListFragment extends Fragment implements StepsListAdapter.StepsListAdapterOnClickHandler{

    private RecyclerView mRecyclerView;
    StepsListAdapter mAdapterListSteps;

    // Define a new interface OnImageClickListener that triggers a callback in the host activity

    private List<Baking> mListBanking;

    private String mIngredients;

    private View mRootView;

    private boolean mTwoPane;

    private Intent intent;


    // Mandatory empty constructor
    public MasterListFragment() {
    }




    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        intent = getActivity().getIntent();
        Bundle bundle = intent.getBundleExtra(KEY_BUNDLE_BAKING);
        mListBanking = (ArrayList<Baking>) bundle.getSerializable(KEY_LIST_BAKING);
        mIngredients = bundle.getString(KEY_INGREDIENTS);

        mRootView = inflater.inflate(R.layout.fragment_list_steps, container, false);



        initRecyclerView(mListBanking.get(0).getSteps());

        ArrayList<Steps> arrayListSteps =  mListBanking.get(0).getSteps();

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

    @Override
    public void onClick(View view) {

        if(getActivity().findViewById(R.id.android_me_linear_layout) != null) {
            mTwoPane = true;
        }else{
            mTwoPane = false;
        }

        if (mTwoPane){
            Part1Fragment part1Fragment = new Part1Fragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            part1Fragment.setListIndex(Integer.parseInt(view.getTag().toString()));
            fragmentManager.beginTransaction()
                    .replace(R.id.part1_container, part1Fragment)
                    .commit();

            Part2Fragment part2Fragment = new Part2Fragment();
            FragmentManager fragmentManager2 = getActivity().getSupportFragmentManager();
            part2Fragment.setListIndex(mListBanking.get(0).getSteps().get(Integer.parseInt(view.getTag().toString())).getVideoURL());
            fragmentManager2.beginTransaction()
                    .replace(R.id.part2_container, part2Fragment)
                    .commit();
        }else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(KEY_LIST_BAKING, (Serializable) mListBanking);
            bundle.putString(KEY_INGREDIENTS, mIngredients);
            bundle.putInt(KEY_POSITION, Integer.parseInt(view.getTag().toString()));

            final Intent intent = new Intent(mRootView.getContext(), RecipeActivity.class);
            intent.putExtra(KEY_BUNDLE_BAKING, bundle);
            startActivity(intent);
        }
    }
}
