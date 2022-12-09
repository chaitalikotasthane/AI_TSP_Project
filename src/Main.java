import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {


    private static HashMap<Integer, HashMap<Integer,Double>>
            adjacencyList = new HashMap<>();


    public static void main(String[] args)
    {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the file name for input:");
        String filename = scanner.nextLine();
        System.out.println("Enter algorithm to use: \n 1 - BnB \n 2 - SLS");
        int option = scanner.nextInt();

        adjacencyList = getAdjacencyList(filename+".txt");
        switch(option)
        {
            case 1:
            {
                double[][] adjacencyMatrix = prepareAdjacencyMatrix(adjacencyList);
                System.out.println("------------------Simulating TSP using Branch and Bound ------------------------");
                long startTime = System.nanoTime();
                BranchAndBound bb = new BranchAndBound(adjacencyMatrix);
                bb.tsp();
                System.out.println();
                System.out.println("Total runtime in seconds: " + TimeUnit.SECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS));
                break;
            }
            case 2:
            {
                System.out.println("------------------Simulating TSP using SLS(Simulated Annealing) ------------------------");
                long startTime = System.nanoTime();
                SimulatedAnnealing sc = new SimulatedAnnealing();
                sc.tspUsingSimulatedAnnealing(adjacencyList, startTime);
                System.out.println();
                System.out.println("Total runtime in seconds: " + TimeUnit.SECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS));
                break;
            }
            default:
            {
                System.out.println("Wrong input. Please try again");
            }
        }
    }

    private static HashMap<Integer, HashMap<Integer, Double>> getAdjacencyList(String filename) {

        ReadInput readInput = new ReadInput();
        try {
            adjacencyList = readInput.readCompetitionFiles(filename);
        } catch (IOException e) {
            System.out.println("Exception occurred " + e);
        }

        return  adjacencyList;
    }

    /*
      Function to prepare adjacencyMatrix using adjacencyList
      Inputs:
      adjacencyList
   */
    private static double[][] prepareAdjacencyMatrix(HashMap<Integer, HashMap<Integer, Double>> adjacencyList) {

        int numberOfNodes = adjacencyList.size();
        double[][] adjMatrix = new double[numberOfNodes][numberOfNodes];

        for(Map.Entry<Integer,HashMap<Integer,Double>> nodes : adjacencyList.entrySet())
        {
            int i = nodes.getKey();
            for(Map.Entry<Integer,Double> node : nodes.getValue().entrySet())
            {
                int j = node.getKey();
                if(i==j)
                {
                    adjMatrix[i][j] = Double.MAX_VALUE;
                }
                else
                {
                    adjMatrix[i][j] = node.getValue();
                }
            }
        }
        return adjMatrix;
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


