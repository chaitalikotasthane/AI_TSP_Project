import java.util.*;

public class SimulatedAnnealing{

    public LinkedList<Integer> getInititalTourUsingFurthestInsertion(HashMap<Integer, HashMap<Integer,Double>>
                                                                             adjacencyList)
    {
        LinkedList<Integer> tour = new LinkedList<Integer>();
        PriorityQueue<Node> queue = new PriorityQueue<>();
        HashMap<Integer, PriorityQueue<Node>> entryMap = new HashMap<>();
        int start = new Random().nextInt(adjacencyList.size());
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

    private void populateEdges(int start, HashMap<Integer, HashMap<Integer, Double>>
            adjacencyList, LinkedList<Integer> tour, PriorityQueue<Node> queue) {

        HashMap<Integer,Double> neighbours = adjacencyList.get(start);
        for(Map.Entry<Integer,Double> entry : neighbours.entrySet())
        {
            int key = entry.getKey();
            Double value = entry.getValue();
            if(!tour.contains(key))
            {
                if(!queue.contains(key))
                {
                    queue.add(new Node(key,value));
                }
            }
            else
            {
                Node node = findNodeInQueue(queue,key,value);
                if(node.weight > value)
                {
                    queue.remove(key);
                    queue.add(new Node(key,value));
                }
            }
        }
    }

    private Node findNodeInQueue(PriorityQueue<Node> queue, int key, double value) {

        for(Node node: queue)
        {
            if(node.node == key && node.weight == value)
            {
                return node;
            }
        }
        return null;
    }


    public Edge getEdgeWhereNextNodeIsInserted(int nextNode,
                                                    HashMap<Integer,HashMap<Integer,Double>> adjacencyList, LinkedList<Integer> tour)
    {
        Double minWeight = Double.MIN_VALUE;
        Edge edge = null;
        for(int i=0; i < tour.size() -1; i++)
        {
            if(adjacencyList.get(nextNode).get(i) +
                    adjacencyList.get(nextNode).get(i+1) - adjacencyList.get(i).get(i+1)
                    < minWeight)
            {
                minWeight = adjacencyList.get(nextNode).get(i) +
                        adjacencyList.get(nextNode).get(i+1) - adjacencyList.get(i).get(i+1);
                edge = new Edge(i,i+1);
            }
        }
        return edge;
    }


    public Double tspUsingSimulatedAnnealing(HashMap<Integer, HashMap<Integer,Double>>
                                                     adjacencyList, long startTime)
    {

        Double bestCost = Double.MAX_VALUE;
        LinkedList<Integer> bestTourPath = null;
        Double temperature = 10000.00;
        Double coolingRate = 0.00001;

        LinkedList<Integer> tourPathSoFar = getInititalTourUsingFurthestInsertion(adjacencyList);

        Double tourCostSoFar = calcTspCost(bestTourPath,adjacencyList);
        bestCost = tourCostSoFar;
        bestTourPath = tourPathSoFar;

        tourPathSoFar.removeLast();

        while(temperature > 1)
        {
            LinkedList<Integer> tour = tourPathSoFar;
            performKOptTour(tour,2);
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

            if(System.nanoTime() - startTime == 60000000000L)
            {
                System.out.println("Simulated Annhealing taking too much time. Exiting with the best found so far");
                print(bestCost, bestTourPath);
                return 0.0;
            }
        }
        print(bestCost, bestTourPath);
        return 0.0;
    }

    private Double calcTspCost(LinkedList<Integer> tourPathSoFar, HashMap<Integer, HashMap<Integer, Double>> adjacencyList) {

        Double tspCost = 0.0;
        for(int i=0; i < tourPathSoFar.size() -1 ; i++)
        {
            tspCost+= adjacencyList.get(tourPathSoFar.get(i)).get(tourPathSoFar.get(i+1));
        }
        if(tourPathSoFar.getFirst() != tourPathSoFar.getLast())
        {
            tspCost+= adjacencyList.get(tourPathSoFar.getLast()).get(tourPathSoFar.getFirst());
        }
        return tspCost;

    }

    private Double calculateAcceptanceProbability(Double tourCostSoFar, Double tourCost, Double temperature) {
        return Math.exp ((tourCostSoFar - tourCost)/ temperature);
    }

    static void print(Double bestCost, LinkedList<Integer> bestTourPath)
    {
        System.out.println("BestCost" +bestCost);
        System.out.println("BestTour:");

        for(int node : bestTourPath) {
            System.out.println(node);
        }
    }


}
