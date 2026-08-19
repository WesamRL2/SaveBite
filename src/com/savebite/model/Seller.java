package com.savebite.model;

public class Seller extends User {

    private String businessName;
    private String businessType;

    public Seller(String id, String name, String email,
                  String businessName, String businessType) {

        super(id, name, email);
        this.businessName = businessName;
        this.businessType = businessType;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    @Override
    public String getRole() {
        return "Seller";
    }
}