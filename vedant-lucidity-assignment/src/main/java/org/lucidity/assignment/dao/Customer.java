package org.lucidity.assignment.dao;

public class Customer extends Node {
    private String name;
    public Customer(int id, String name, Location location){
        super(id,location);

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
