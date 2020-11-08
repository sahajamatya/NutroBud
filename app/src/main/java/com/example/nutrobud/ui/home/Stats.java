package com.example.nutrobud.ui.home;

import java.util.HashMap;
import java.util.Map;

public class Stats {
    private int caloriesTrackedQty;
    private Map<String, Integer> nutrients;

    public Stats(){

    }

    public Stats(int caloriesTrackedQty, Map<String, Integer> nutrients) {
        this.caloriesTrackedQty = caloriesTrackedQty;
        this.nutrients = nutrients;
    }

    public int getCaloriesTrackedQty() {
        return caloriesTrackedQty;
    }

    public void setCaloriesTrackedQty(int caloriesTrackedQty) {
        this.caloriesTrackedQty = caloriesTrackedQty;
    }

    public Map<String, Integer> getNutrients() {
        return nutrients;
    }

    public void setNutrients(Map<String, Integer> nutrients) {
        this.nutrients = nutrients;
    }
}
