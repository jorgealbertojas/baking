package com.example.jorge.mybaking;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.jorge.mybaking.models.Steps;

import java.util.ArrayList;

import static com.example.jorge.mybaking.utilities.Utility.KEY_EXTRA_LIST_STEPS;
import static com.example.jorge.mybaking.utilities.Utility.KEY_EXTRA_LIST_STEPS_BUNDLE;
import static com.example.jorge.mybaking.utilities.Utility.KEY_POSITION;

public class VideoWithInformationActivity extends AppCompatActivity {

    private int mPosition;
    private ArrayList<Steps> mListSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Button bNextRecipe = (Button) findViewById(R.id.b_next_recipe);
        bNextRecipe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {




                if (mPosition < mListSteps.size() - 1) {
                     mPosition++;
                     String nameButton =  getResources().getString(R.string.Next).toString();
                     String positionButton = Integer.toString(mPosition + 1);
                     ((Button) v).setText(nameButton + " "+ positionButton);
                } else {
                    mPosition = 0;
                    ((Button) v).setText(Resources.getSystem().getText(R.string.Next));
                }



                getDataIngredients(mPosition);
            }
        });
        Bundle bundle = getIntent().getBundleExtra(KEY_EXTRA_LIST_STEPS_BUNDLE);

        mListSteps = (ArrayList<Steps>) bundle.getSerializable(KEY_EXTRA_LIST_STEPS);
        mPosition = Integer.parseInt(bundle.getString(KEY_POSITION));

        // Only create new fragments when there is no previously saved state
        if (savedInstanceState == null) {

            getDataIngredients(mPosition);

        }
    }


    private void getDataIngredients(int position) {
        InformationIngredientsFragment part1Fragment = new InformationIngredientsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        part1Fragment.setListIndex(mListSteps.get(position));
        fragmentManager.beginTransaction()
                .replace(R.id.part1_container, part1Fragment)
                .commit();

        VideoIngredientsFragment part2Fragment = new VideoIngredientsFragment();
        FragmentManager fragmentManager2 = getSupportFragmentManager();
        part2Fragment.setListIndex(mListSteps.get(position).getVideoURL());
        fragmentManager2.beginTransaction()
                .replace(R.id.part2_container, part2Fragment)
                .commit();
    }
}
