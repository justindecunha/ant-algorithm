import java.util.Random;

/**
 * Class representing the ant.
 *
 * Stores tour, tabu list, transition probabilities and contant values vital to the algorithm
 */
public class Ant {
    private final int Q;
    private final int alpha;
    private final int beta;
    private int tour[]; //tour information of this specific ant
    private boolean visited[]; //tabu list
    private int idx = -1; //current index of tour
    private ACOGraph graph;
    private double transitionProbability[]; //The probability to transition to each town from the current position
    private Random random;

    public Ant(int graphSize, ACOGraph graph, int alpha, int beta, int Q) {
        this.alpha = alpha;
        this.beta = beta;
        tour = new int[graphSize];
        transitionProbability = new double[graphSize];
        visited = new boolean[graphSize];
        this.graph = graph;
        idx = -1; //starting at -1 because of how visit() works
        this.Q = Q;
        random = new Random();
    }

    public void visit(int town) {
        tour[++idx]  = town;
        visited[town] = true;
    }

    public boolean isVisited(int town) {
        return visited[town];
    }

    public void reset() {
        idx = -1;
        for(int b = 0; b < visited.length; b++)
            visited[b] = false;
    }

    /**
     * calculates and returns the tour distance
     */
    public int calcDistance() {
        int distance = 0;

        for(int i = 0; i < tour.length - 1; i++)
            distance += graph.getDist(tour[i], tour[i+1]);


        distance += graph.getDist(tour[tour.length - 1],tour[0]); // route back home

        return distance;
    }

    public void nextTown() {
        if(idx == -1) throw new RuntimeException("You need to be at a town to call this method");
        double tpNumerator;
        double tpDenominator = 0.0;
        int i = tour[idx];
        //Calculating the denominator for transition probability formula
        for(int k = 0; k < graph.getSize(); k++) {
            if (!isVisited(k)) {
                tpDenominator += Math.pow(graph.getTrail(i, k), alpha) * Math.pow((1.0 / graph.getDist(i, k)), beta);
            }
        }



        //Calculating transition probability for each town from current town : 0 if k is visited, otherwise use the formula to get the chance
        for(int j = 0; j < graph.getSize(); j++) {
            if(isVisited(j)) {
                transitionProbability[j] = 0.0;

            } else {
                tpNumerator = Math.pow(graph.getTrail(i,j), alpha) * Math.pow((1.0/graph.getDist(i,j)), beta);
                transitionProbability[j] = tpNumerator/tpDenominator;
            }
        }
        //0.0001 is added to ensure none of the 0 probability options get selected
        double moveChance = random.nextDouble() + 0.000000001; //Random value used to randomly select path
        double chanceSum = 0.0;

        // this loop adds the transition probabilities together, and once the chance sum is greater than the randomly
        // generated number, the corrosponding town is visited
        // towns have probability to be selected by this corosponding to their respective transition probabilities
        for(int l = 0; l < graph.getSize(); l++) {
            chanceSum += transitionProbability[l];
            if(chanceSum >= moveChance) {
                visit(l);
                return;
            }
        }

        // The code should never reach this point, but it does if memory gets exhausted ( iterations > 1020 i think)
        for(int x = 0; x < graph.getSize(); x++)
            System.out.println(transitionProbability[x]);

       throw new RuntimeException("If this point is reached, there is a problem with the chance calculations above");

    }

    /**
     * Adds deltaT (each ants contribution) to the current pheromone trail values
     */
    public void pheromonate() {
        double deltaT = Q / calcDistance();


        // updates all the edges in the trail used by the ant
        for(int i = 0; i < graph.getSize() - 1; i++ )
            graph.setTrail(tour[i], tour[i+1], graph.getTrail(tour[i], tour[i+1]) + deltaT);

        //update the last move home
        graph.setTrail(tour[graph.getSize() - 1], tour[0], graph.getTrail(tour[graph.getSize() - 1], tour[0]) + deltaT);

    }

    // updates the global best tour if the ants current tour is shorter distance
    public void bestTourUpdate() {
        if (calcDistance() < graph.getBestTourDist()) {
            graph.setBestTourDist(calcDistance());
            graph.setBestTour(tour.clone());
        }
    }

}
