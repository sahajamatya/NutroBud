package com.example.nutrobud.ui.home;

import java.util.HashMap;
import java.util.Map;

public class Stats {
    private int caloriesTracked;
    private HashMap<String, Integer> nutrients;

    public Stats(){

    }

    public Stats(int caloriesTracked, HashMap<String, Integer> nutrients) {
        this.caloriesTracked = caloriesTracked;
        this.nutrients = nutrients;
    }

    public int getCaloriesTracked() {
        return caloriesTracked;
    }

    public void setCaloriesTracked(int caloriesTracked) {
        this.caloriesTracked = caloriesTracked;
    }

    public HashMap<String, Integer> getNutrients() {
        return nutrients;
    }

    public void setNutrients(HashMap<String, Integer> nutrients) {
        this.nutrients = nutrients;
    }
}
