package org.lucidity.assignment.util;

import org.lucidity.assignment.dao.Location;

public interface DistanceCalculator {

    //This method can be overridden by different classes that implement Distance calculator using different logic
    public double calculateDistance(Location location1, Location location2);
}
