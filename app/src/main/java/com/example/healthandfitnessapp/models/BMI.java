package com.example.healthandfitnessapp.models;

public class BMI {

    public static float calculateBMI(float weight, float height, boolean useKilograms, boolean useCentimeters) {
        if (!useKilograms) // convert lb to kg
        {
            weight *= 0.45359237f;
        }
        if (!useCentimeters) // convert feet to cm
        {
            height *= 30.48f;
        }

        //convert cm to m
        height /= 100f;

        return (weight / height / height);
    }

    public enum BMI_Category {
        VERY_SEVERELY_UNDERWEIGHT,
        SEVERELY_UNDERWEIGHT,
        UNDERWEIGHT,
        NORMAL,
        OVERWEIGHT,
        MODERATELY_OBESE,
        SEVERELY_OBESE,
        VERY_SEVERELY_OBESE
    }

    public static BMI_Category getBMICategory(float BMI) {
        if (BMI <= 15f) return BMI_Category.VERY_SEVERELY_UNDERWEIGHT;
        if (BMI <= 16f) return BMI_Category.SEVERELY_UNDERWEIGHT;
        if (BMI <= 18.5f) return BMI_Category.UNDERWEIGHT;
        if (BMI <= 25f) return BMI_Category.NORMAL;
        if (BMI <= 30f) return BMI_Category.OVERWEIGHT;
        if (BMI <= 35f) return BMI_Category.MODERATELY_OBESE;
        if (BMI <= 40f) return BMI_Category.SEVERELY_OBESE;
        return BMI_Category.VERY_SEVERELY_OBESE;
    }
}
