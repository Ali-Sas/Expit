package com.example.pockerguide.profile.recycler;

public class StateProfile {

    private String nameLevel, nameBonus, nameActive, privilegi;

    public StateProfile(String nameLevel, String nameBonus, String nameActive, String privilegi) {

        this.nameLevel = nameLevel;
        this.nameBonus = nameBonus;
        this.nameActive = nameActive;
        this.privilegi = privilegi;

    }

    public String getNameLevel() {
        return this.nameLevel;
    }
    public String getNameActive() {
        return this.nameActive;
    }
    public String getNameBonus() {
        return this.nameBonus;
    }
    public String getPrivelegi() {
        return this.privilegi;
    }


    public String getCapital() {
        return this.nameActive;
    }
}
