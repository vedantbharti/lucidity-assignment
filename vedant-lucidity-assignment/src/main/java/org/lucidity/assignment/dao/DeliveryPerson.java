package org.lucidity.assignment.dao;

public class DeliveryPerson extends Node {

    private String name;
    public DeliveryPerson(int id, String name, Location location){
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
