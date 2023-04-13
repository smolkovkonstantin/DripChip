package com.example.dripchip.entites.model;

public enum Gender {
    MALE,
    FEMALE,
    OTHER;

    public static boolean isGender(String name){
        for (Gender gender: values()){
            if (gender.name().equals(name)){
                return true;
            }
        }
        return false;
    }
}
