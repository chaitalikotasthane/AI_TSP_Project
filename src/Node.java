public class Node implements Comparable<Node> {
    int node;
    double weight;
    Node(int node, double weight)
    {
        this.node = node;
        this.weight = weight;
    }

    @Override
    public int compareTo(Node o) {
        return (int) (this.weight - o.weight);
    }
}
