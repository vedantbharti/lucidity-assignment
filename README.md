# lucidity-assignment

Problem statement:


Imagine a delivery executive called Aman standing idle in Koramangala somewhere when suddenly his
phone rings and notifies that he’s just been assigned a batch of 2 orders meant to be delivered in the
shortest possible timeframe.
All the circles in the figure above represent geo-locations :
● C1 : Consumer 1
● C2 : Consumer 2
● R1 : Restaurant C1 has ordered from. Average time it takesto prepare a meal is pt1
R2 : Restaurant C2 has ordered from. Average time it takes to prepare a meal is pt2
Since there are multiple ways to go about delivering these orders, your task is to help Aman figure out
the best way to finish the batch in the shortest possible time.
For the sake of simplicity, you can assume that Aman, R1 and R2 were informed about these orders at
the exact same time and all of them confirm on doing it immediately. Also, for travel time between
any two geo-locations, you can use the haversine formula with an average speed of 20km/hr (
basically ignore actual road distance or confirmation delays everywhere although the real world is
hardly that simple ;) )
Note: Code should be of production quality and should take into consideration the best practices of
the language chosen

Solution:

Identifying the objects:
1. DeliveryPerson
2. Customer
3. Restaurant
4. Location
5. Node (representing each of the delivery person, customer and restaurant, we will need that to solve the problem)

DeliveryPerson, Customer and Restaurant "have a" Location
DeliveryPerson, Customer and Restaurant "is a" node

Assumption:
1. We have made an assumption that since DeliveryPerson, Customer and Restaurant are all Node, Node has the id and a Customer and a Restaurant doesn't have same id, or similarly with DeliveryPerson

Identifying the service classes:
1. RouteFinderService
In our case, we have only one service class, which is RouterFinderService which is an interface
This interface can be implemented by many classes implementing their own logic of finding optimal route

Identifying the utility classes:
1. DistanceCalculator
This is also an interface which can be implemented by many other classes with their own logic of distance calculation

DeliveryApplication: Class containing main method

Custom exception:
1. DeliveryPersonNotFound: Throws an exception when we have not added a delivery person
   (in real world scenario, when no delivery person is available)

Have implemented try-catch block in DeliveryApplication to handle exceptions gracefully

Optimal delivery time logic:

Constraints:
1. We need to visit all nodes once
2. Delivery person must reach the restaurant first from which the customer has ordered before reaching the customer
3. Delivery person waits if the food is not ready yet

Solution approach:

Tried to implement the travelling salesman problem with our constraints
Used bitmask to iterate over all the subsets of nodes and find minimum time taken to reach each node in each subset.

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
                        double readyTime = restaurant.getPrepTime();
                        // Wait if food is not ready
                        arrivalTime = Math.max(arrivalTime, readyTime);
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


1. minTimeRecord[][] is a 2D DP array to store the minimum time in subset "mask" of visited nodes and ending at node "i". Each set bit in mask is a visited node
2. 1st for loop: Iterates over all the subsets
3. check for each node (i.e; loop over each node)
4. If u is not in the current subset of visited nodes, skip processing. This is done to ensure that we only process the locations which are in current subset
5. for (int v = 0; v < numberOfNodes; v++) iterates over all nodes to determine the next node to visit
Basically, "u" iterates over all nodes that has been visited and "v" iterates over all the nodes that can be visited
6. if ((mask & (1 << v)) != 0 || u == v) continue;
   if (isCustomer(v) && !isRestaurantVisited(mask, v)) continue;
   These two lines of code filter all the nodes that have been visited or the node is a customer and it's respective restaurant has not been visited
7. int nextMask = mask | (1 << v) : updates the node v to have been visited in this new subset
8. Then we update arrival time at v as time taken to arrive at u + travel time
9. We also take into consideration food preparation time, and if arrival time is less than preparation time, we update arrivalTime to prepTime ( because delivery person will have to wait)
10. Update the minTimeRecord[nextMask][v] to arrivalTime if it has current value more than arrivalTime
11. We get the minimum time for delivery by taking the subset which has all the nodes visited and see for which end node we have the minimum value


Implemented the code keeping in mind modularity, extensibility, readability and encapsulation.






