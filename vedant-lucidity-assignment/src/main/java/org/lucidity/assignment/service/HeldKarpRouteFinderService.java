package org.lucidity.assignment.service;

import org.lucidity.assignment.dao.Customer;
import org.lucidity.assignment.dao.DeliveryPerson;
import org.lucidity.assignment.dao.Node;
import org.lucidity.assignment.dao.Restaurant;
import org.lucidity.assignment.exception.DeliveryPersonNotFound;
import org.lucidity.assignment.util.DistanceCalculator;
import org.lucidity.assignment.util.HaversineDistanceCalculator;

import java.util.*;

//service class to implement the core logic of our problem statement, finding optimal time.
public class HeldKarpRouteFinderService implements RouteFinderService {
    private static final int INFINITY = Integer.MAX_VALUE;
    private final int numberOfNodes;
    private final double[][] minTimeRecord;
    private final Map<Integer,Integer> customerRestaurantMap;
    private final List<Node> nodes;
    private final Set<Integer> customerIds;
    private final DistanceCalculator distanceCalculator;

    public HeldKarpRouteFinderService(List<Node> nodes, Map<Customer, Restaurant> customerToRestaurant){
        this.numberOfNodes = nodes.size();
        this.minTimeRecord = new double[1 << numberOfNodes][numberOfNodes];
        this.nodes = nodes;
        this.customerIds = new HashSet<>();
        this.customerRestaurantMap = new HashMap<>();
        this.distanceCalculator = new HaversineDistanceCalculator();

        //initialize the minTimeRecord which is basically a memoization array to store shortest time to visit all nodes
        for (double[] row : minTimeRecord) Arrays.fill(row, INFINITY);

        //maps each customer id with their respective restaurant id
        for (Customer customer : customerToRestaurant.keySet()) {
            customerRestaurantMap.put(customer.getId(), customerToRestaurant.get(customer).getId());
            customerIds.add(customer.getId());
        }
    }

    //logic for finding optimal route and returning the time taken for that
    @Override
    public double findOptimalRoute(DeliveryPerson deliveryPerson){
        int deliveryPersonIndex = nodes.indexOf(deliveryPerson);
        if (deliveryPersonIndex == -1) {
            throw new DeliveryPersonNotFound("Delivery person must be in nodes list");
        }

        //base case, at delivery person's location, time is 0
        minTimeRecord[1 << deliveryPersonIndex][deliveryPersonIndex] = 0;

        for (int mask = 1; mask < (1 << numberOfNodes); mask++) {
            for (int u = 0; u < numberOfNodes; u++) {
                if ((mask & (1 << u)) == 0) continue;
                for (int v = 0; v < numberOfNodes; v++) {
                    if ((mask & (1 << v)) != 0 || u == v) continue;
                    if (isCustomer(v) && !isRestaurantVisited(mask, v)) continue;

                    int nextMask = mask | (1 << v);
                    double travelTime = distanceCalculator.calculateDistance(nodes.get(u).getLocation(), nodes.get(v).getLocation()) / 20.0;
                    double arrivalTime = minTimeRecord[mask][u] + travelTime;

                    if (nodes.get(v) instanceof Restaurant) {
                        Restaurant restaurant = (Restaurant) nodes.get(v);
                        double prepTime = restaurant.getPrepTime();
                        // Wait if food is not ready
                        arrivalTime = Math.max(arrivalTime, prepTime);
                    }

                    if (arrivalTime < minTimeRecord[nextMask][v]) {
                        minTimeRecord[nextMask][v] = arrivalTime;
                    }
                }
            }
        }

        double minTime = INFINITY;
        int finalMask = (1 << numberOfNodes) - 1;

        for (int i = 1; i < numberOfNodes; i++) {
            minTime = Math.min(minTime, minTimeRecord[finalMask][i]);
        }

        return minTime;
    }

    private boolean isCustomer(int id) {
        return customerIds.contains(id);
    }

    private boolean isRestaurantVisited(int mask, int customerId) {
        Integer restaurantId = customerRestaurantMap.get(customerId);
        return restaurantId != null && (mask & (1 << restaurantId)) != 0;
    }

}
