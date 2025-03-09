package org.lucidity.assignment.dao;

public class Restaurant extends Node {

    private String name;
    private double prepTime;
    public Restaurant(int id, String name, double prepTime, Location location){
        super(id,location);

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(double prepTime) {
        this.prepTime = prepTime;
    }
}
