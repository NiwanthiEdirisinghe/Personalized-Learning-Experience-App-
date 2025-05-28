package com.example.personalizedlearningexperienceapp.entity;

public class SubscriptionPlan {
    private String planName;
    private double price;
    private String features;
    private int durationDays;
    private boolean isPopular;

    public SubscriptionPlan(String planName, double price, String features) {
        this.planName = planName;
        this.price = price;
        this.features = features;
        this.durationDays = 30;
        this.isPopular = false;
    }

    public SubscriptionPlan(String planName, double price, String features, int durationDays, boolean isPopular) {
        this.planName = planName;
        this.price = price;
        this.features = features;
        this.durationDays = durationDays;
        this.isPopular = isPopular;
    }


    public String getPlanName() {
        return planName;
    }

    public double getPrice() {
        return price;
    }

    public String getFeatures() {
        return features;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public boolean isPopular() {
        return isPopular;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public void setPopular(boolean popular) {
        isPopular = popular;
    }


    public String getFormattedPrice() {
        return String.format("$%.2f", price);
    }

    public String getPlanType() {
        switch (planName.toLowerCase()) {
            case "starter":
                return "Basic";
            case "intermediate":
                return "Professional";
            case "advanced":
                return "Premium";
            default:
                return "Standard";
        }
    }

    public String[] getFeaturesList() {
        if (features != null) {
            return features.split("\n");
        }
        return new String[0];
    }

    @Override
    public String toString() {
        return "SubscriptionPlan{" +
                "planName='" + planName + '\'' +
                ", price=" + price +
                ", features='" + features + '\'' +
                ", durationDays=" + durationDays +
                ", isPopular=" + isPopular +
                '}';
    }
}
