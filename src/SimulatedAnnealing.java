import java.util.*;
import java.util.concurrent.TimeUnit;

public class SimulatedAnnealing{

    TwoOpt twoOpt = new TwoOpt();

    /*
      Function for furthest insertion algorithm
      Inputs:
      adjacencyList
       */
    public LinkedList<Integer> getInititalTourUsingFurthestInsertion(HashMap<Integer, HashMap<Integer,Double>>
                                                                             adjacencyList)
    {
        LinkedList<Integer> tour = new LinkedList<Integer>();
        PriorityQueue<Node> queue = new PriorityQueue<>();
        HashMap<Integer, PriorityQueue<Node>> entryMap = new HashMap<>();
        int start = new Random().nextInt(adjacencyList.size()-1);
        tour.add(start);
        populateEdges(start, adjacencyList, tour, queue);
        int nextNode = queue.poll().node;
        tour.add(nextNode);
        populateEdges(nextNode, adjacencyList, tour, queue);
        for(int i=0; i < adjacencyList.size() -2 ; i++)
        {
            nextNode = queue.poll().node;
            Edge edge = getEdgeWhereNextNodeIsInserted(nextNode, adjacencyList, tour);
            tour.add(tour.indexOf(edge.node2), nextNode);
            populateEdges(nextNode, adjacencyList, tour, queue);
        }
        tour.addLast(tour.getFirst());
        return tour;
    }

    /*
       Function to populate edges of a given node to queue
       Inputs:
       start - given node
       adjacencyList
       tour - tour till now ( to ensure nodes are not duplicated in the tour )
       queue
        */
    private void populateEdges(int start, HashMap<Integer, HashMap<Integer, Double>>
            adjacencyList, LinkedList<Integer> tour, PriorityQueue<Node> queue) {

        HashMap<Integer,Double> neighbours = adjacencyList.get(start);
        for(Map.Entry<Integer,Double> entry : neighbours.entrySet())
        {
            int key = entry.getKey();
            Double value = entry.getValue();
            if(!tour.contains(key))
            {
                Node node = findNodeInQueue(queue,key,value);
                if(node==null)
                    queue.add(new Node(key,value));
                else if(node.weight > value)
                {
                    queue.remove(node);
                    queue.add(new Node(key,value));
                }
            }
        }
    }

    /*
       Function to find a particular node in queue
       Inputs:
       queue
       key - key of the node to search for
       value - value of the node to search for
        */
    private Node findNodeInQueue(PriorityQueue<Node> queue, int key, double value) {
        for(Node node : queue)
        {
            if(node.node == key)
            {
                return node;
            }
        }
        return null;
    }

    /*
       Function to append a node in the tour using furthest Insertion Algorithm
       Inputs:
       nextNode - node to append to tour
       adjacencyList
       tour - tourList till now
        */
    public Edge getEdgeWhereNextNodeIsInserted(int nextNode,
                                                    HashMap<Integer,HashMap<Integer,Double>> adjacencyList, LinkedList<Integer> tour)
    {
        Double minWeight = Double.MAX_VALUE;
        Edge edge = null;
        for(int i=0; i < tour.size() -1; i++)
        {
            Double value = adjacencyList.get(nextNode).get(tour.get(i)) +
                    adjacencyList.get(nextNode).get(tour.get(i+1)) - adjacencyList.get(tour.get(i)).get(tour.get(i+1));
            if(value < minWeight)
            {
                minWeight = value;
                edge = new Edge(tour.get(i),tour.get(i+1));
            }
        }
        return edge;
    }


    /*
    Main function
    Inputs:
    adjacencyList
    startTime
     */
    public void tspUsingSimulatedAnnealing(HashMap<Integer, HashMap<Integer,Double>>
                                                     adjacencyList, long startTime)
    {

        Double bestCost = Double.MAX_VALUE;
        LinkedList<Integer> bestTourPath = null;
        Double temperature = 100000.00;
        Double coolingRate = 0.0001;
        Double noOfIterations = 0.0;

        LinkedList<Integer> tourPathSoFar = getInititalTourUsingFurthestInsertion(adjacencyList);

        Double tourCostSoFar = calcTspCost(tourPathSoFar,adjacencyList);
        bestCost = tourCostSoFar;
        bestTourPath = tourPathSoFar;
        tourPathSoFar.removeLast();

        while(temperature > 1)
        {
            System.out.println("---------------Iteration:" + noOfIterations++ + "----------------");
            LinkedList<Integer> tour = tourPathSoFar;
            System.out.print("Tour before: ");
            print(tour);

            tour = twoOpt.performKOptTour(tour,2);

            System.out.print("Tour after 2-opt: ");
            print(tour);

            Double tourCost = calcTspCost(tour, adjacencyList);
            if(tourCost <= tourCostSoFar)
            {
                tourPathSoFar = tour;
                tourCostSoFar = tourCost;
            }
            else
            {
                if(calculateAcceptanceProbability(tourCostSoFar,tourCost, temperature) >
                        new Random().nextDouble()) {
                    tourPathSoFar = tour;
                    tourCostSoFar = tourCost;
                }
            }

            if(tourCost < bestCost)
            {
                bestCost = tourCost;
                bestTourPath = tour;
            }

            temperature = temperature - temperature*coolingRate;

            if(System.nanoTime() - startTime == 540000000000L)
            {
                System.out.println("BestCost till now " +bestCost);
                System.out.println("BestTour till now: ");
                print(bestTourPath);
            }
            System.out.println("BestCost till now " +bestCost);
            System.out.println("BestTour till now: ");
            print(tour);
        }
        System.out.println("BestCost till now " +bestCost);
        System.out.println("BestTour till now: ");
        print(bestTourPath);
        System.out.println("Total runtime in seconds: " + TimeUnit.SECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS));
    }

    /*
    Function to calculate cost of a particular tour
    Inputs:
    tourPathSoFar - tourList
    adjacencyList
     */
    private Double calcTspCost(LinkedList<Integer> tourPathSoFar, HashMap<Integer, HashMap<Integer, Double>> adjacencyList) {

        Double tspCost = 0.0;
        for(int i=0; i < tourPathSoFar.size()-1 ; i++)
        {
            tspCost+= adjacencyList.get(tourPathSoFar.get(i)).get(tourPathSoFar.get(i+1));
        }
        if(tourPathSoFar.getFirst() != tourPathSoFar.getLast())
        {
            tspCost+= adjacencyList.get(tourPathSoFar.getLast()).get(tourPathSoFar.getFirst());
        }
        return tspCost;

    }

    /*
   Function to calculate acceptance probability
   Inputs:
   tourCostSoFar - costTillNow
   tourCost - newCost
   temperature - annealing temperature
    */
    private Double calculateAcceptanceProbability(Double tourCostSoFar, Double tourCost, Double temperature) {
        return Math.exp ((tourCostSoFar - tourCost)/ temperature);
    }

    /*
   Utility function to print tour
   Inputs:
   bestTourPath - tourList
    */
    static void print(LinkedList<Integer> bestTourPath)
    {
        for(int node : bestTourPath) {
            System.out.print(node+ " ");
        }
        System.out.println();
    }
}
