import java.util.*;

public class BranchAndBound {

    private static int n;

    /* private static double[][] adjMatrix = {
        { Double.MAX_VALUE, 10, 15, 20 },
        { 10, Double.MAX_VALUE, 35, 25 },
        { 15, 35, Double.MAX_VALUE, 30 },
        { 20, 25, 30, Double.MAX_VALUE }
    }; */

    private static double[][] adjMatrix = {
        { Double.MAX_VALUE, 20, 15, 30, 45 },
        { 20, Double.MAX_VALUE, 40, 10, 5 },
        { 15, 40, Double.MAX_VALUE, 50, 15 },
        { 30, 10, 50, Double.MAX_VALUE, 25 },
        { 45, 5, 15, 25, Double.MAX_VALUE }
    };

    public BranchAndBound() {
        n = adjMatrix.length;
    }

    private static class Pair<T1, T2> {
        T1 source;
        T2 dest;

        private Pair(T1 source, T2 dest) {
            this.source = source;
            this.dest = dest;
        }
    }

    private static class Node {
        int cityNum;
        double cost;
        double[][] reducedMatrix;
        int level;
        List<Pair<Integer, Integer>> path;
    }

    private static Node createNode(int parentNode, int childNode, double[][] parentMatrix, int level, List<Pair<Integer, Integer>> path) {
        Node node = new Node();
        // node.path = path;
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

    private static void printPath(List<Pair<Integer, Integer>> path) {
        System.out.println("Path: ");
        for (Pair<Integer, Integer> p: path) {
            System.out.println(p.source + 1 + " --> " + (p.dest + 1));
        }
    }
        
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
