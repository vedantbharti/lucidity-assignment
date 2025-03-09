package org.lucidity.assignment.service;

import org.lucidity.assignment.dao.DeliveryPerson;

public interface RouteFinderService {

    //This method can be overridden by different routing services if needed, by implementing this interface
    double findOptimalRoute(DeliveryPerson deliveryPerson);

}
