public class Node implements Comparable<Node> {
    int node;
    double weight;
    Node(int node, double weight)
    {
        this.node = node;
        this.weight = weight;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(obj.getClass() != this.getClass())
            return false;
        final Node n = (Node) obj;
        if(this.node == n.node && this.weight == n.weight) {
            return true;
        } else
            return false;
    }

    @Override
    public int compareTo(Node o) {
        return (int) (this.weight - o.weight);
    }
}
