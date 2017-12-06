package com.example.jorge.mybaking.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jorge on 05/12/2017.
 * Model Baning
 */

public class Baking  implements Serializable {
    private int id;
    private String name;
    private ArrayList<Ingredients> ingredients;
    private ArrayList<Steps> steps;
    private int servings;
    private String image;

    public String getId() {
        return  Integer.toString(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }



    public String getServings() {
        return Integer.toString(servings);
    }

    public void setServings(int servings) {
        this.servings = servings;
    }
}
