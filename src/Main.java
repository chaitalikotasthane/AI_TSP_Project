import java.io.IOException;
import java.util.*;

public class Main {



    public static void main(String[] args)
    {

        HashMap<Integer, HashMap<Integer,Double>>
                adjacencyList = new HashMap<>();

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

        System.out.println("------------------Simulating TSP using SLS(Simulated Annealing) ------------------------");
        SimulatedAnnealing sc = new SimulatedAnnealing();
        sc.tspUsingSimulatedAnnealing(adjacencyList, startTime);
        System.out.println();
        System.out.println("------------------Simulating TSP using Branch and Bound ------------------------");
        //BranchAndBound bb = new BranchAndBound();
        //bb.tsp();
    }


    /*
   Function to addEdge for creation of adjacencyList
   Inputs:
   adjacencyList
   node1
   node2
   cost
    */
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


