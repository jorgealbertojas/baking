package com.example.jorge.mybaking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.jorge.mybaking.models.Baking;

import java.util.List;

public class DetailActivity extends AppCompatActivity  implements StepsListFragment.OnImageClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }


    @Override
    public void onImageSelected(int position) {

    }

}
