package cpen221.mp2.sealevels;

import cpen221.mp2.graph.ALGraph;
import cpen221.mp2.graph.Edge;
import cpen221.mp2.graph.PathCostType;
import cpen221.mp2.graph.Vertex;

import java.util.Arrays;
import java.util.List;

// No fields so no rep invariants
public class SeaLevels {

    /**
     * Finds which parts of the terrain will be submerged given a terrain, water source locations,
     * and water levels
     * @param terrain A 2D array representing an elevation map of the terrain
     * @param waterSources A 1D array listing the water source locations
     * @param waterLevel the water level
     * @return A 2D boolean array indicating which tiles are submerged (true)
     *         or not submerged (false)
     */
    public static boolean[][] isSubmerged(double[][] terrain,
                                          GridLocation[] waterSources,
                                          double waterLevel) {
        int rows = terrain.length;
        int cols = terrain[0].length;
        boolean[][] isSubmerged = new boolean[rows][cols];
        double[][] dangerLevel = dangerLevel(terrain,waterSources);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                isSubmerged[i][j] = dangerLevel[i][j] <= waterLevel;
            }
        }

        return isSubmerged;
    }

    /**
     * Finds the required water level for each part of the terrain to be submerged
     * @param terrain A 2D array representing an elevation map of the terrain
     * @param waterSources the water level
     * @return A 2D array indicating the water level required to submerge each tile
     */
    public static double[][] dangerLevel(double[][] terrain,
                                         GridLocation[] waterSources) {
        int rows = terrain.length;
        int cols = terrain[0].length;
        ALGraph<Vertex, Edge<Vertex>> terrainGraph = new ALGraph<>();
        double[][] dangerLevel = new double[rows][cols];
        for (double[] row : dangerLevel) {
            Arrays.fill(row, Double.MAX_VALUE);
        }

        //Setting up the graph. Adds a vertex for every element in the terrain array,
        // and adds an edge if there are nearby vertices.
        // Edge lengths represent elevation
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Vertex newVertex = new Vertex(100*i+j,"(" + i + ", " + j + ")"); // could maybe use hash function?
                terrainGraph.addVertex(newVertex);

                if (i > 0) {
                    terrainGraph.addEdge(new Edge<Vertex>(
                            newVertex, new Vertex(100*(i-1)+j, "(" + (i-1) + ", " + j + ")"), Math.max(terrain[i][j], terrain[i-1][j])));
                }
                if (j > 0) {
                    terrainGraph.addEdge(new Edge<Vertex>(
                            newVertex, new Vertex(100*i+(j-1), "(" + i + ", " + (j-1) + ")"), Math.max(terrain[i][j], terrain[i][j-1])));
                }
            }
        }

        for (GridLocation waterSource : waterSources) {
            int waterRow = waterSource.row;
            int waterCol = waterSource.col;
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    if (row != waterRow || col != waterCol) {

//                        System.out.println("Path  for vertex (" + row + ", " + col + ")");
                        //Finds the minimum height required for THIS waterSource to submerge THIS index
                        List<Vertex> path = terrainGraph.minimumCostPath(
                                new Vertex(100 * row + col, "(" + row + ", " + col + ")"),
                                new Vertex(100 * waterRow + waterCol, "(" + waterRow + ", " + waterCol + ")"), PathCostType.SUM_EDGES);
//                        System.out.println(": passed");

                        //Stores the minimum water level required for any waterSource to submerge this index
                        dangerLevel[row][col] = Math.min(
                                dangerLevel[row][col],
                                terrainGraph.pathCost(path, PathCostType.MAX_EDGE));
//                        System.out.println(dangerLevel[row][col]);
                    }
                    else dangerLevel[row][col] = terrain[row][col];
                }
            }
        }

//        System.out.println(Arrays.deepToString(dangerLevel));
        return dangerLevel;
    }
}
