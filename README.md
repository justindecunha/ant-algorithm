# Ant Algorithm

Demonstrates an ant algorithm applied to the classic traveling salesman problem. Inspired by the social behaviour of an ant hive, and how the ants communicate with pheromones to convey information regarding paths to desireable resources.

### Usage

Simply run the file with desired parameters set inside the AntColonyAlgorithm.java.

### Parameters

##### fileName

The name of the tsp file that will be parsed. File should have a similar format to the example file provided.

##### Graph_Size

The size of the graph contained in the tsp file.

##### Max_Iterations

The number of iterations of the algorithm before terminating.

##### Alpha

A tunable parameter of the algorithm. 1 is the standard value used, but can be tweaked based on user preference.

##### Beta

A tunable parameter for the algorithm. Higher beta values seemed to produce better results for this particular application, based on my own observations.

##### Evaporation Rate

The rate at which pheromones will evaporate at.

