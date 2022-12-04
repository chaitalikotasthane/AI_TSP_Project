import java.util.*;

public class Main {



    public static void main(String[] args)
    {

        HashMap<Integer, HashMap<Integer,Double>>
                adjacencyList = new HashMap<>();

        // Adding edges one by one
        addEdge(adjacencyList, 0, 1, 10);
        addEdge(adjacencyList, 0, 2, 15);
        addEdge(adjacencyList, 0, 3, 20);
        addEdge(adjacencyList, 1, 0, 10);
        addEdge(adjacencyList, 1, 2, 35);
        addEdge(adjacencyList, 1, 3, 25);
        addEdge(adjacencyList, 2, 0, 15);
        addEdge(adjacencyList, 2, 1, 35);
        addEdge(adjacencyList, 2, 3, 30);
        addEdge(adjacencyList, 3, 1, 20);
        addEdge(adjacencyList, 3, 2, 25);
        addEdge(adjacencyList, 3, 3, 30);


        long startTime = System.nanoTime();

        SimulatedAnnealing sc = new SimulatedAnnealing();
        sc.tspUsingSimulatedAnnealing(
                adjacencyList, startTime);

    }


    static void addEdge(HashMap<Integer, HashMap<Integer, Double>> adj,
                        int u, int v, double cost)
    {
        HashMap<Integer,Double> map = new HashMap<>();
        if(adj.containsKey(u))
        {
            map = adj.get(u);
        }
        map.put(v,cost);
        adj.put(u,map);
    }


}


