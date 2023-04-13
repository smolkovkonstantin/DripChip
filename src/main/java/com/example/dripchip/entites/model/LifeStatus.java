package com.example.dripchip.entites.model;

public enum LifeStatus {
    ALIVE,
    DEAD;

    public static boolean isLifeStatus(String name){
        for (LifeStatus lifeStatus: values()){
            if (lifeStatus.name().equals(name)){
                return true;
            }
        }
        return false;
    }
}
