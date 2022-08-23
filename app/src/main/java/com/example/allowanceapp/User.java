package com.example.allowanceapp;

import java.util.ArrayList;

/**
 * It creates a user with suitable params
 *
 * @author Vico Lau
 * @version 0.1
 */

public class User {

    private String email;
    private String password;
    private double allowance;

    public User(String email,
                String password,
                double allowance) {

        this.email = email;
        this.password = password;
        this.allowance = allowance;
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

    public double getAllowance() {
        return allowance;
    }

    public void setAllowance(double allowance) {
        this.allowance = allowance;
    }
}
