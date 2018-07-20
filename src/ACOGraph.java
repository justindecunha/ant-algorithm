/**
 * Graph representation that stores the graph, pheromone trail values, best tour, and best tour distance
 */
public class ACOGraph {
    private int bestTourDist;
    private int[] bestTour;
    private int graphSize;
    private int graph[][];    //Distances between locations
    private double trail[][]; //Amount of pheromone on specified section of the graph

    ACOGraph(int graph[][],int graphSize, double initPheromone) {
        this.graph = graph;
        this.graphSize = graphSize;
        trail = new double[graphSize][graphSize];
        bestTourDist = Integer.MAX_VALUE;
        initialize(initPheromone);
    }

    /**
     *
     * @param d - Amount of pheromone to initialize the graph with
     *
     *          Initializes the graph with pheromone amount d
     */
    public void initialize(double d) {
        for(int i = 0; i < graphSize; i++)
            for(int j = 0; j < graphSize; j++)
                trail[i][j] = d;
    }

    public int getDist(int i, int j) {
        return graph[i][j];
    }

    public double getTrail(int i, int j) {
        return trail[i][j];
    }

    public void setTrail(int i, int j, double d) {
        trail[i][j] = d;
    }

    public int getSize() {
        return graphSize;
    }

    public int getBestTourDist() {return bestTourDist;}

    public void setBestTourDist(int dist) {bestTourDist = dist;}

    public void setBestTour(int[] tour) {bestTour = tour;}

    public void printBest() {
        String best = "";
        for(int i : bestTour)
            best += " " +i;

        System.out.println("Best tour: " + best);
        System.out.println("Best distance: " + bestTourDist);
    }
}
