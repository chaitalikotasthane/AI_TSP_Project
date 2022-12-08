import java.io.IOException;
import java.util.*;

public class Main {



    public static void main(String[] args)
    {

        HashMap<Integer, HashMap<Integer,Double>>
                adjacencyList = new HashMap<>();

        // Adding edges one by one
//        addEdge(adjacencyList, 0, 1, 10);
//        addEdge(adjacencyList, 0, 2, 15);
//        addEdge(adjacencyList, 0, 3, 20);
//
//        addEdge(adjacencyList, 1, 0, 5);
//        addEdge(adjacencyList, 1, 2, 9);
//        addEdge(adjacencyList, 1, 3, 10);
//
//        addEdge(adjacencyList, 2, 0, 6);
//        addEdge(adjacencyList, 2, 1, 13);
//        addEdge(adjacencyList, 2, 3, 12);
//
//        addEdge(adjacencyList, 3, 0, 8);
//        addEdge(adjacencyList, 3, 1, 8);
//        addEdge(adjacencyList, 3, 2, 9);

        //addEdge(adjacencyList, 4, 0, 8);
        //addEdge(adjacencyList, 4, 1, 2);
        //addEdge(adjacencyList, 4, 2, 20);
        //addEdge(adjacencyList, 4, 3, 4);

        ReadInput readInput = new ReadInput();
        try {
            adjacencyList = readInput.readCompetitionFiles();
            System.out.println("Adjacency list: ");
            for(Map.Entry<Integer, HashMap<Integer,Double>> m : adjacencyList.entrySet()) {
                System.out.println("Key: "+ m.getKey()+" List Value: "+m.getValue().toString());
            }
        } catch (IOException e) {
            System.out.println("Exception occurred " + e);
        }

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


