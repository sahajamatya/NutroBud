package com.example.nutrobud.ui.home;

import java.util.List;

public class User {
    private String email;
    private String password;
    private String firstName;
    private String secondName;
    private int age;
    private String gender;
    private int weight;
    private List<String> ingredientsNo;
    private List<String> ingredientsYes;
    private List<Integer> ingredientsYesGoalsQty;
    private List<Integer> ingredientsYesTrackedQty;
    private int calorieGoalsQty;
    private int calorieTrackedQty;
    private Stats stats;

    public User(){

    }

    public User(String email, String password, String firstName, String secondName, int age, String gender, int weight, List<String> ingredientsNo, List<String> ingredientsYes, List<Integer> ingredientsYesGoalsQty, List<Integer> ingredientsYesTrackedQty, int calorieGoalsQty, int calorieTrackedQty, Stats stats) {
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getsecondName() {
        return secondName;
    }

    public void setsecondName(String secondName) {
        this.secondName = secondName;
    }

    public int getAge() {
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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<String> getIngredientsNo() {
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

    public int getCalorieTrackedQty() {
        return calorieTrackedQty;
    }

    public void setCalorieTrackedQty(int calorieTrackedQty) {
        this.calorieTrackedQty = calorieTrackedQty;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }
}
