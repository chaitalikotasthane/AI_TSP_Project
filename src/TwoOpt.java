import java.util.LinkedList;

public class TwoOpt {

    public static LinkedList<Integer>  performKOptTour(LinkedList<Integer> tour, int numberOfNeighbours) {

        int node1 = (int) (Math.random()*(tour.size() - 0));
        int node2 = (int) (Math.random()*(tour.size() - 0));
        while(node1 == node2) {
            node2 = (int) (Math.random()*(tour.size() - 0));
        }
        if(tour.indexOf(node1) < tour.indexOf(node2))
            return swap(tour, tour.indexOf(node1), tour.indexOf(node2));
        else
            return swap(tour, tour.indexOf(node2), tour.indexOf(node1));
    }


    private static LinkedList<Integer> swap(LinkedList<Integer> tour, int i, int j) {
        LinkedList<Integer> updatedTour = new LinkedList<>();
        for (int p = 0; p <= i - 1; p++) {
            updatedTour.add(p);
        }

        for(int p = j; p>=i; p--) {
            updatedTour.add(p);
        }

        for(int p = j+1; p<tour.size(); p++) {
            updatedTour.add(p);
        }
        return updatedTour;

    }

}
