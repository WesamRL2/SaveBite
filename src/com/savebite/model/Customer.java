package com.savebite.model;

public class Customer extends User {

    private int rewardPoints;

    public Customer(String id, String name, String email) {
        super(id, name, email);
        this.rewardPoints = 0;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void addRewardPoints(int points) {
        if (points > 0) {
            rewardPoints += points;
        }
    }

    @Override
    public String getRole() {
        return "Customer";
    }
}