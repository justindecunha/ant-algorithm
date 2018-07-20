import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * This algorithm works by following the formula:
 *
 * 1. The graph is initialized with initialPheromone amount
 * 2. one ant is placed in each city
 * 3. The ants find their paths based on transition probability formula
 * 4. The ants update their pheromone trails as well as applying evaporation
 * 5. The best ant path is recorded
 */
public class AntColonyAlgorithm {

    //File Data
    private final String fileName = "bays29.txt"; // name of the file to read
    private static final int GRAPH_SIZE = 29; // The size of the graph to input

    //ACO algorithm constants
    private final int MAX_ITERATIONS = 900;
    private final double intialPheromone = 1.0; //the amount of pheromone to start initialization with
    private final int alpha = 1; // 1 is the standard alpha value
    private final int beta = 10; //Higher beta values seem to generate better results
    private final double evaporationRate = 0.5; // Rate pheromones evaporate at
    private final int Q = 500; // Q coefficient used in algorithm

    private static ACOGraph graph; // The graph we will use
    private Ant hive[]; // the hive of ants

    /**
     *
     * @param fileName - The name/path of the file to read
     * @param GRAPH_SIZE - the size of the graph
     * @return a 2D int array containing the graph
     *
     * Simply reads the graph from file and returns the graph as a 2D int array
     */
    private int[][] graphReader(String fileName, int GRAPH_SIZE) {
        int graph[][] = new int[GRAPH_SIZE][GRAPH_SIZE];

        try {
            Scanner scanner = new Scanner(new File(fileName));
            for(int i = 0; i < GRAPH_SIZE; i ++) {
                for(int j = 0; j < GRAPH_SIZE; j++) {
                    graph[i][j] = scanner.nextInt();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return graph;
    }

    /**
     * resets each ants tabu list, and places one ant at each location
     */
    private void resetAnts() {
        for(int i = 0; i < GRAPH_SIZE; i++) {
            hive[i].reset();
            hive[i].visit(i);
        }
    }

    /**
     * Tour length = graph size, meaning graphSize - 1 "moves" need to take place
     *
     * findPaths iterates through each of the ants, exhausting all its moves, resulting in each ant completing
     * its tour
     */
    private void findPaths() {
        for(int i = 0; i < GRAPH_SIZE - 1; i++)
            for (Ant a : hive)
                a.nextTown();
    }

    /**
     * This method causes the ants to update their pheromone trails following the formula given on slide 49
     *
     * First, evaporation is applied, then each ant contributes its share to the pheromone trail
     */
    private void pheromoneUpdate() {

        //evaporation part of pheromone update formula given (slide 49)
        for(int i = 0; i < GRAPH_SIZE; i++)
            for(int j = 0; j < GRAPH_SIZE; j++)
                graph.setTrail(i,j, graph.getTrail(i,j) * evaporationRate);

        // Each ant applies its DeltaT pheromone contribution, as detailed by formula
        for(Ant a : hive)
            a.pheromonate();

    }

    /**
     *  Checks to see if any of the paths the ants followed is shorter than the global best, if so, the best is updated
     */
    private void findBestSolution() {
        for(Ant a : hive)
            a.bestTourUpdate();
    }


    AntColonyAlgorithm() {
        //Reads in graph from file, and sets initial pheromone amounts
        graph = new ACOGraph(graphReader(fileName, GRAPH_SIZE), GRAPH_SIZE, intialPheromone);

        //Declare and intialize the hive
        hive = new Ant[GRAPH_SIZE];
        for (int j = 0; j < hive.length; j++)
            hive[j] = new Ant(GRAPH_SIZE, graph, alpha, beta, Q);

        // simply follows the algorithm steps required to generate the solutions

            for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
                resetAnts();
                findPaths();
                pheromoneUpdate();
                findBestSolution();
            }


            graph.printBest(); // prints the best tour once algorithm iterations have finished
    }
    public static void main(String[]args) {
        new AntColonyAlgorithm();
    }
}