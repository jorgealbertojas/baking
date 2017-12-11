package com.example.jorge.mybaking;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.jorge.mybaking.models.Baking;

import java.io.Serializable;
import java.util.ArrayList;

import static com.example.jorge.mybaking.utilities.Utility.KEY_BUNDLE_BAKING;
import static com.example.jorge.mybaking.utilities.Utility.KEY_INGREDIENTS;
import static com.example.jorge.mybaking.utilities.Utility.KEY_LIST_BAKING;
import static com.example.jorge.mybaking.utilities.Utility.KEY_POSITION;

public class DetailActivity extends AppCompatActivity  implements MasterListFragment.OnImageClickListener {

    private boolean mTwoPane;
    private ArrayList<Baking> mListBaking;
    private String mIngredients;

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

            // Change the GridView to space out the images more on tablet
            GridView gridView = (GridView) findViewById(R.id.gv_steps);
            gridView.setNumColumns(1);


            if(savedInstanceState == null) {
                // In two-pane mode, add initial BodyPartFragments to the screen
                FragmentManager fragmentManager = getSupportFragmentManager();



                // New body fragment
                Part1Fragment part1Fragment = new Part1Fragment();
                part1Fragment.setListIndex(0);
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


    @Override
    public void onImageSelected(int position) {

        // Create a Toast that displays the position that was clicked
        Toast.makeText(this, "Position clicked = " + position, Toast.LENGTH_SHORT).show();


        // Handle the two-pane case and replace existing fragments right when a new image is selected from the master list
        if (mTwoPane) {
            // Create two=pane interaction
            Part1Fragment part1Fragment = new Part1Fragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            part1Fragment.setListIndex(position);
            fragmentManager.beginTransaction()
                  .replace(R.id.part1_container, part1Fragment)
                  .commit();

            Part2Fragment part2Fragment = new Part2Fragment();
            FragmentManager fragmentManager2 = getSupportFragmentManager();
            part2Fragment.setListIndex(mListBaking.get(0).getSteps().get(position).getVideoURL());
            fragmentManager2.beginTransaction()
                    .replace(R.id.part2_container, part2Fragment)
                    .commit();




        } else {

            Bundle bundle = new Bundle();
            bundle.putSerializable(KEY_LIST_BAKING, (Serializable) mListBaking);
            bundle.putString(KEY_INGREDIENTS,mIngredients);
            bundle.putInt(KEY_POSITION,position);

            final Intent intent = new Intent(this, RecipeActivity.class);
            intent.putExtra(KEY_BUNDLE_BAKING,bundle);
            startActivity(intent);
            }
        }

    }



