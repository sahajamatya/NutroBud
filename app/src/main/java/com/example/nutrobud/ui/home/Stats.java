package com.example.nutrobud.ui.home;

import java.util.HashMap;
import java.util.Map;

public class Stats {
    private int caloriesTrackedQty;
    private HashMap<String, Integer> nutrients;

    public Stats(){

    }

    public Stats(int caloriesTrackedQty, HashMap<String, Integer> nutrients) {
        this.caloriesTrackedQty = caloriesTrackedQty;
        this.nutrients = nutrients;
    }

    public int getCaloriesTrackedQty() {
        return caloriesTrackedQty;
    }

    public void setCaloriesTrackedQty(int caloriesTrackedQty) {
        this.caloriesTrackedQty = caloriesTrackedQty;
    }

    public HashMap<String, Integer> getNutrients() {
        return nutrients;
    }

    public void setNutrients(HashMap<String, Integer> nutrients) {
        this.nutrients = nutrients;
    }
}
