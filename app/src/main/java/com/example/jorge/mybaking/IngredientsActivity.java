package com.example.jorge.mybaking;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.jorge.mybaking.adapters.StepsListAdapter;
import com.example.jorge.mybaking.models.Baking;
import com.example.jorge.mybaking.models.Steps;

import java.util.ArrayList;

import static com.example.jorge.mybaking.utilities.Utility.KEY_BUNDLE_BAKING;
import static com.example.jorge.mybaking.utilities.Utility.KEY_INGREDIENTS;
import static com.example.jorge.mybaking.utilities.Utility.KEY_LIST_BAKING;

public class IngredientsActivity extends AppCompatActivity {

    private boolean mTwoPane;
    private ArrayList<Baking> mListBaking;
    private String mIngredients;
    private RecyclerView mRecyclerView;
    StepsListAdapter mAdapterListSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = this.getIntent().getBundleExtra(KEY_BUNDLE_BAKING);
        mListBaking =  new ArrayList<Baking>();
        mListBaking = (ArrayList<Baking>) bundle.getSerializable(KEY_LIST_BAKING);
        mIngredients = bundle.getString(KEY_INGREDIENTS);

        // Determine if you're creating a two-pane or single-pane display
        if(findViewById(R.id.android_me_linear_layout) != null) {
            // This LinearLayout will only initially exist in the two-pane tablet case
            mTwoPane = true;



            initRecyclerView(mListBaking.get(0).getSteps());

            mRecyclerView.setAdapter(mAdapterListSteps);


            if(savedInstanceState == null) {
                // In two-pane mode, add initial BodyPartFragments to the screen
                FragmentManager fragmentManager = getSupportFragmentManager();



                // New body fragment
                Part1Fragment part1Fragment = new Part1Fragment();
                part1Fragment.setListIndex(mListBaking.get(0).getSteps().get(0));
                fragmentManager.beginTransaction()
                        .add(R.id.part1_container, part1Fragment)
                        .commit();

                // New leg fragment
                Part2Fragment part2Fragment = new Part2Fragment();
                part2Fragment.setListIndex(mListBaking.get(0).getSteps().get(0).getVideoURL());
                fragmentManager.beginTransaction()
                        .add(R.id.part2_container, part2Fragment)
                        .commit();
            }
        } else {
            // We're in single-pane mode and displaying fragments on a phone in separate activities
            mTwoPane = false;
        }
    }





    private void initRecyclerView(ArrayList<Steps> listSteps) {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_steps);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mRecyclerView.setHasFixedSize(true);
        mAdapterListSteps = new StepsListAdapter(listSteps, this);
    }


}



