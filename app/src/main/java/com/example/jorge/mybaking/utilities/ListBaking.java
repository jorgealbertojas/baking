package com.example.jorge.mybaking.utilities;

import com.example.jorge.mybaking.models.Baking;
import com.example.jorge.mybaking.models.Ingredients;
import com.example.jorge.mybaking.models.Steps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jorge on 05/12/2017.
 */


public class ListBaking<T> implements Serializable{
    public int id;
    public String name;
    public int servings;
    public String image;
    public ArrayList<Ingredients> ingredients;
    public ArrayList<Steps> steps;





}


/*public class ListBaking implements Serializable {
    public int id;
    public String name;
    public int servings;
    public String image;

    public List<Ingredients> ingredients;

    public List<Steps> steps;





}*/
