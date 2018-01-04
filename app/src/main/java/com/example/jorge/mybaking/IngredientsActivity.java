package com.example.jorge.mybaking;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.jorge.mybaking.adapters.StepsListAdapter;
import com.example.jorge.mybaking.models.Steps;

import java.util.ArrayList;

import static com.example.jorge.mybaking.utilities.Utility.KEY_BUNDLE_BAKING;
import static com.example.jorge.mybaking.utilities.Utility.KEY_INGREDIENTS;
import static com.example.jorge.mybaking.utilities.Utility.KEY_LIST_BAKING;

public class IngredientsActivity extends AppCompatActivity {

    StepsListAdapter mAdapterListSteps;
    private boolean mTwoPane;
    private ArrayList<Steps> mListSteps;
    private String mIngredients;
    private RecyclerView mRecyclerView;
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        mBundle = getIntent().getBundleExtra(KEY_BUNDLE_BAKING);
        mListSteps = new ArrayList<Steps>();
        mListSteps = (ArrayList<Steps>) mBundle.getSerializable(KEY_LIST_BAKING);
        mIngredients = mBundle.getString(KEY_INGREDIENTS);

        Resources res = getResources();
        mTwoPane = res.getBoolean(R.bool.adjust_view_bounds);

        // Determine if you're creating a two-pane or single-pane display
        if (mTwoPane) {
            // This LinearLayout will only initially exist in the two-pane tablet case


            initRecyclerView(mListSteps);

            mRecyclerView.setAdapter(mAdapterListSteps);

            if (savedInstanceState == null) {
                // In two-pane mode, add initial BodyPartFragments to the screen
                FragmentManager fragmentManager = getSupportFragmentManager();

                // New body fragment
                InformationIngredientsFragment part1Fragment = new InformationIngredientsFragment();
                part1Fragment.setListIndex(mListSteps.get(0));
                fragmentManager.beginTransaction()
                        .add(R.id.part1_container, part1Fragment)
                        .commit();

                // New leg fragment
                VideoIngredientsFragment part2Fragment = new VideoIngredientsFragment();
                part2Fragment.setListIndex(mListSteps.get(0).getVideoURL());
                fragmentManager.beginTransaction()
                        .add(R.id.part2_container, part2Fragment)
                        .commit();
            }
        }
    }

    private void initRecyclerView(ArrayList<Steps> listSteps) {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_steps);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mAdapterListSteps = new StepsListAdapter(listSteps, this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // save RecyclerView state
        mBundle = new Bundle();
        mBundle.putString(KEY_INGREDIENTS, mIngredients);
        mBundle.putSerializable(KEY_LIST_BAKING, mListSteps);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // restore RecyclerView state
        if (mBundle != null) {
            mListSteps = new ArrayList<Steps>();
            mListSteps = (ArrayList<Steps>) mBundle.getSerializable(KEY_LIST_BAKING);
            mIngredients = mBundle.getString(KEY_INGREDIENTS);
        }
    }

}



