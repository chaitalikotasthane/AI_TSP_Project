import java.util.*;

public class BranchAndBound {

    private static int n;

    private static double[][] adjMatrix = null;

    public BranchAndBound(double[][] adjacencyMatrix) {
        adjMatrix = adjacencyMatrix;
        n = adjacencyMatrix.length;
    }

    private static class Pair<T1, T2> {
        T1 source;
        T2 dest;

        private Pair(T1 source, T2 dest) {
            this.source = source;
            this.dest = dest;
        }
    }

    /**
     * Class Node represents a city node in the TSP. 
     */
    private static class Node {
        int cityNum;
        double cost;
        double[][] reducedMatrix;
        int level;
        List<Pair<Integer, Integer>> path;
    }

    /**
     * Method to create a city node given the parameters.
     * @param parentNode City number of the source / parent node
     * @param childNode City number of the destination / child node
     * @param parentMatrix Parent city's reduction matrix
     * @param level Level from the root city
     * @param path TSP path
     * @return Node object of the city
     */
    private static Node createNode(int parentNode, int childNode, double[][] parentMatrix, int level, List<Pair<Integer, Integer>> path) {
        Node node = new Node();
        node.path = new ArrayList<>();
        for (Pair<Integer, Integer> p: path) {
            node.path.add(p);
        }
        if (level != 0) {
            node.path.add(new Pair<Integer, Integer>(parentNode, childNode));
        }
        node.reducedMatrix = new double[parentMatrix.length][parentMatrix[0].length];

        for (int i = 0; i < parentMatrix.length; i++) {
            for (int j = 0; j < parentMatrix[0].length; j++) {
                node.reducedMatrix[i][j] = parentMatrix[i][j];
            }
        }
        
        for (int i = 0; level != 0 && i < n; i++) {
            node.reducedMatrix[parentNode][i] = Double.MAX_VALUE;
            node.reducedMatrix[i][childNode] = Double.MAX_VALUE;
        }
        
        node.reducedMatrix[childNode][0] = Double.MAX_VALUE;
        
        node.level = level;
        node.cityNum = childNode;
        return node;
    }

    /**
     * Method to reduce rows.
     * @param reductionMatrix Reduction Matrix to find optimal cost.
     * @return reduced row array
     */
    private static double[] reduceRows(double[][] reductionMatrix) {
        double[] minRow = new double[n];
        Arrays.fill(minRow, Double.MAX_VALUE);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                minRow[i] = Math.min(minRow[i], reductionMatrix[i][j]);
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (reductionMatrix[i][j] != Double.MAX_VALUE && minRow[i] != Double.MAX_VALUE) {
                    reductionMatrix[i][j] = reductionMatrix[i][j] - minRow[i];
                }
            }
        }        
    
        return minRow;
    }

    /**
     * Method to reduce columns.
     * @param reductionMatrix Reduction Matrix to find optimal cost.
     * @return reduced column array
     */
    private static double[] reduceCols(double[][] reductionMatrix) {
        double[] minCol = new double[n];
        Arrays.fill(minCol, Double.MAX_VALUE);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                minCol[j] = Math.min(minCol[j], reductionMatrix[i][j]);
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (reductionMatrix[i][j] != Double.MAX_VALUE && minCol[j] != Double.MAX_VALUE) {
                    reductionMatrix[i][j] = reductionMatrix[i][j] - minCol[j];
                }
            }
        }
        
        return minCol;
    }

    /**
     * Method to get reduced cost from the reduction matrix.
     * @param reductionMatrix Reduction Matrix to find optimal cost.
     * @return Reduced cost
     */
    private static double getReducedCost(double[][] reductionMatrix) {
        double reducedCost = 0;
        double minRow[] = reduceRows(reductionMatrix);
        double minCol[] = reduceCols(reductionMatrix);
        
        for (int i = 0; i < n; i++) {
            reducedCost += (minRow[i] != Double.MAX_VALUE ? minRow[i] : 0);
            reducedCost += (minCol[i] != Double.MAX_VALUE ? minCol[i] : 0);
        }

        return reducedCost;
    }

    /**
     * Comparator for priority queue such that a city with low cost is added at front.
     */
    private static class CostComparator implements Comparator<Node> {
        @Override
        public int compare(Node node1, Node node2) {
            if (node1.cost < node2.cost) {
                return -1;
            } else if (node1.cost > node2.cost) {
                return 1;
            }
            return 0;
        }
    }

    /**
     * Method to print the TSP path.
     * @param path TSP path
     */
    private static void printPath(List<Pair<Integer, Integer>> path) {
        System.out.println("Path: ");
        for (Pair<Integer, Integer> p: path) {
            System.out.println(p.source + 1 + " --> " + (p.dest + 1));
        }
    }
        
    /**
     * Method to calculate the TSP minimum cost.
     * @return Optimal cost
     */
    public double tsp() {
        PriorityQueue<Node> pq = new PriorityQueue<>(new CostComparator());
        List<Pair<Integer, Integer>> path = new ArrayList<>();
        Node root = createNode(0, 0, adjMatrix, 0, path);

        root.cost = getReducedCost(root.reducedMatrix);
        pq.add(root);

        while (!pq.isEmpty()) {
            Node minCostNode = pq.remove();
            int current = minCostNode.cityNum;

            if (minCostNode.level == n - 1) {
                minCostNode.path.add(new Pair<Integer, Integer>(current, 0));
                printPath(minCostNode.path);
                System.out.println("Optimal cost: " + minCostNode.cost);
                return minCostNode.cost;
            }

            for (int i = 0; i < n; i++) {
                if (minCostNode.reducedMatrix[current][i] != Double.MAX_VALUE) {
                    Node child = createNode(current, i, minCostNode.reducedMatrix, minCostNode.level + 1, minCostNode.path);
                    double calculatedCost = getReducedCost(child.reducedMatrix);
                    child.cost = minCostNode.cost + minCostNode.reducedMatrix[current][i] + calculatedCost;
                    pq.add(child);
                }
            }
        }

        return 0;
    }


}
