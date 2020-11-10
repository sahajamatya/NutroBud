package com.example.nutrobud.ui.home;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class Stats implements Parcelable {
    private int caloriesTrackedQty;
    private HashMap<String, Integer> nutrients;

    public Stats(){

    }

    public Stats(int caloriesTrackedQty, Map<String, Integer> nutrients) {
        this.caloriesTrackedQty = caloriesTrackedQty;
        this.nutrients = (HashMap<String, Integer>) nutrients;
    }

    private Stats(Parcel in) {
        caloriesTrackedQty = in.readInt();
    }

    public static final Parcelable.Creator<Stats> CREATOR = new Parcelable.Creator<Stats>() {
        @Override
        public Stats createFromParcel(Parcel in) {
            return new Stats(in);
        }

        @Override
        public Stats[] newArray(int size) {
            return new Stats[size];
        }
    };

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
        this.nutrients = (HashMap<String, Integer>) nutrients;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(caloriesTrackedQty);
    }
}
