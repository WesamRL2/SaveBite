package com.savebite.app;

import com.savebite.model.Customer;
import com.savebite.model.Seller;
import com.savebite.model.User;

public class Main {

    public static void main(String[] args) {

        User customer = new Customer(
                "C001",
                "Ali",
                "ali@email.com"
        );

        User seller = new Seller(
                "S001",
                "Ahmed",
                "ahmed@restaurant.com",
                "Ahmed Restaurant",
                "Restaurant"
        );

        System.out.println("SaveBite application started successfully!");

        System.out.println();
        System.out.println("User 1: " + customer.getName());
        System.out.println("Role: " + customer.getRole());

        System.out.println();

        System.out.println("User 2: " + seller.getName());
        System.out.println("Role: " + seller.getRole());
    }
}