package com.example.nutrobud.ui.home;

import java.util.List;
import java.util.Map;

public class User {
    private static int id;
//    private int id;
    private static String email;
    private static String password;
    private static String firstName;
    private static String secondName;
    private static int age;
    private static String gender;
    private static int weight;
    private static List<String> ingredientsNo;
    private List<String> ingredientsYes;
    private List<Integer> ingredientsYesGoalsQty;
    private List<Integer> ingredientsYesTrackedQty;
    private static int calorieGoalsQty;
    private static int calorieTrackedQty;
    private Map<String, Stats> stats;

    public User(){

    }

    public User(int id, String email, String password, String firstName, String secondName, int age, String gender, int weight, List<String> ingredientsNo, List<String> ingredientsYes, List<Integer> ingredientsYesGoalsQty, List<Integer> ingredientsYesTrackedQty, int calorieGoalsQty, int calorieTrackedQty) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.secondName = secondName;
        this.age = age;
        this.gender = gender;
        this.weight = weight;
        this.ingredientsNo = ingredientsNo;
        this.ingredientsYes = ingredientsYes;
        this.ingredientsYesGoalsQty = ingredientsYesGoalsQty;
        this.ingredientsYesTrackedQty = ingredientsYesTrackedQty;
        this.calorieGoalsQty = calorieGoalsQty;
        this.calorieTrackedQty = calorieTrackedQty;
        this.stats = stats;
    }

    public static int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public static String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public static int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public static int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public static List<String> getIngredientsNo() {
        return ingredientsNo;
    }

    public void setIngredientsNo(List<String> ingredientsNo) {
        this.ingredientsNo = ingredientsNo;
    }

    public List<String> getIngredientsYes() {
        return ingredientsYes;
    }

    public void setIngredientsYes(List<String> ingredientsYes) {
        this.ingredientsYes = ingredientsYes;
    }

    public List<Integer> getIngredientsYesGoalsQty() {
        return ingredientsYesGoalsQty;
    }

    public void setIngredientsYesGoalsQty(List<Integer> ingredientsYesGoalsQty) {
        this.ingredientsYesGoalsQty = ingredientsYesGoalsQty;
    }

    public List<Integer> getIngredientsYesTrackedQty() {
        return ingredientsYesTrackedQty;
    }

    public void setIngredientsYesTrackedQty(List<Integer> ingredientsYesTrackedQty) {
        this.ingredientsYesTrackedQty = ingredientsYesTrackedQty;
    }

    public int getCalorieGoalsQty() {
        return calorieGoalsQty;
    }

    public void setCalorieGoalsQty(int calorieGoalsQty) {
        this.calorieGoalsQty = calorieGoalsQty;
    }

    public int getcalorieTrackedQty() {
        return calorieTrackedQty;
    }

    public void setcalorieTrackedQty(int calorieTrackedQty) {
        this.calorieTrackedQty = calorieTrackedQty;
    }

    public Map<String, Stats> getStats() {
        return stats;
    }

    public void setStats(Map<String, Stats> stats) {
        this.stats = stats;
    }
}
