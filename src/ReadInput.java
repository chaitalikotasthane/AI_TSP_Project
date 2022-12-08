import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class ReadInput {

    public static HashMap<Integer, HashMap<Integer,Double>> readCompetitionFiles() throws IOException {

        HashMap<Integer, HashMap<Integer,Double>> adjacencyList = new HashMap<>();

        File dir = new File("D:/Simran/projects and misc/AI_TSP_Project/src/resources/Competion");
        File[] files = dir.listFiles();
        // Fetching all the files
        for (File file : files) {
            String name = file.getName();
            if(file.isFile() && name.contains("tsp-problem-")) {
                BufferedReader inputStream = null;
                String line;
                int numOfLocations = 0;
                int lineNumber = 0;
                try {
                    inputStream = new BufferedReader(new FileReader(file));
                    while ((line = inputStream.readLine()) != null) {
                        String[] lineVals = line.split(" ");
                        if(lineVals.length == 1) {
                            numOfLocations = Integer.parseInt(lineVals[0]);
                        }
                        if(numOfLocations > 0 && lineVals.length > 1) {
                            for (int i = 0; i<numOfLocations; i++) {
                                addEdge(adjacencyList, lineNumber, i, Double.parseDouble(lineVals[i]));
                            }
                            lineNumber++;
                        }
                    }
                }catch(IOException e) {
                    System.out.println(e);
                }
                finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
//                System.out.println(adjacencyList);
            }
        }
        return adjacencyList;
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
