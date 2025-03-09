package org.lucidity.assignment;

import org.lucidity.assignment.dao.*;
import org.lucidity.assignment.exception.DeliveryPersonNotFound;
import org.lucidity.assignment.service.HeldKarpRouteFinderService;
import org.lucidity.assignment.service.RouteFinderService;

import java.util.*;

public class DeliveryApplication {
    public static void main(String[] args) {

        //This list contains a list of nodes, where each node is either delivery person, customer or restaurant
        List<Node> nodes = new ArrayList<>();

        //initializes the delivery person object
        DeliveryPerson aman = new DeliveryPerson(0, "Aman", new Location(12.9352, 77.6245));
        nodes.add(aman);

        //creates restaurants
        Restaurant r1 = new Restaurant(1, "R1", 0.5,  new Location(12.9367, 77.6268));
        Restaurant r2 = new Restaurant(2, "R2", 0.6,  new Location(12.9341, 77.6221));
//        Restaurant r3 = new Restaurant(2, "R2", 0.7,  new Location(10.9341, 75.6221));
        nodes.add(r1);
        nodes.add(r2);
//        nodes.add(r3);

        //creates customers
        Customer c1 = new Customer(3, "C1", new Location(12.9381, 77.6284));
        Customer c2 = new Customer(4, "C2", new Location(12.9328, 77.6203));
//        Customer c3 = new Customer(4, "C2", new Location(11.9328, 76.6203));
        nodes.add(c1);
        nodes.add(c2);

        //creates a mapping between each customer and the corresponding restaurant they ordered from
        //This will be used to ensure that delivery person reaches restaurant first and then to the
        //corresponding customer.
        Map<Customer, Restaurant> customerToRestaurant = new HashMap<>();
        customerToRestaurant.put(c1, r1);
        customerToRestaurant.put(c2, r2);
//        customerToRestaurant.put(c3, r3);

        //This method takes all the nodes and the customer to restaurant mapping to find the min time
        RouteFinderService routeFinder = new HeldKarpRouteFinderService(nodes, customerToRestaurant);
        double minTime = 0;

        //try-catch block for exception handling
        try {

            minTime = routeFinder.findOptimalRoute(aman);
            System.out.println("Minimum Delivery Time: " + minTime + " hours");

        } catch (DeliveryPersonNotFound e){

            System.out.println(e.getMessage());

        } catch (Exception e){

            System.out.println(e.getMessage());
        }


    }
}