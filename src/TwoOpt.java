import java.util.LinkedList;

public class TwoOpt {

    public static LinkedList<Integer> performKOptTour(LinkedList<Integer> tour, int numberOfNeighbours) {

        int node1 = (int) (Math.random()*(tour.size() - 0));
        int node2 = (int) (Math.random()*(tour.size() - (node1+1)) + (node1+1));
        LinkedList<Integer> updatedTour = swap(tour, node1, node2);
        return updatedTour;
    }


    private static LinkedList<Integer> swap(LinkedList<Integer> tour, int i, int j) {
        LinkedList<Integer> updatedTour = new LinkedList<>();

        for (int p = 0; p <= i - 1; p++) {
            updatedTour.add(tour.get(p));
        }

        for(int p = j; p>=i; p--) {
            updatedTour.add(tour.get(p));
        }

        for(int p = j+1; p<tour.size(); p++) {
            updatedTour.add(tour.get(p));
        }
        return updatedTour;

    }

}
