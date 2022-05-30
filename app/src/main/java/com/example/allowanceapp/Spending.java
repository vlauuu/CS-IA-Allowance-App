package com.example.allowanceapp;

public class Spending {

    private String email;
    private String item;
    private String date;
    private double cost;
    private String description;
    private boolean deleted;

    public Spending(String email,
                    String item,
                    String date,
                    double cost,
                    String description,
                    boolean deleted) {

        this.email = email;
        this.item = item;
        this.date = date;
        this.cost = cost;
        this.description = description;
        this.deleted = deleted;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
