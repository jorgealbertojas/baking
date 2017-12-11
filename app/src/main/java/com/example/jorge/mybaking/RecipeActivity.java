package com.example.jorge.mybaking;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.jorge.mybaking.models.Baking;

import java.util.ArrayList;

import static com.example.jorge.mybaking.utilities.Utility.KEY_BUNDLE_BAKING;
import static com.example.jorge.mybaking.utilities.Utility.KEY_INGREDIENTS;
import static com.example.jorge.mybaking.utilities.Utility.KEY_LIST_BAKING;
import static com.example.jorge.mybaking.utilities.Utility.KEY_POSITION;

public class RecipeActivity extends AppCompatActivity {

    private ArrayList<Baking> mListBaking;
    private String mIngredients;
    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Bundle bundle = this.getIntent().getBundleExtra(KEY_BUNDLE_BAKING);
        mListBaking =  new ArrayList<Baking>();
        mListBaking = (ArrayList<Baking>) bundle.getSerializable(KEY_LIST_BAKING);
        mIngredients = bundle.getString(KEY_INGREDIENTS);
        mPosition = bundle.getInt(KEY_POSITION);
        // Only create new fragments when there is no previously saved state
        if (savedInstanceState == null) {

            Part1Fragment part1Fragment = new Part1Fragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            part1Fragment.setListIndex(mPosition);


            fragmentManager.beginTransaction()
                    .add(R.id.part1_container, part1Fragment)
                    .commit();


        }
    }
}
