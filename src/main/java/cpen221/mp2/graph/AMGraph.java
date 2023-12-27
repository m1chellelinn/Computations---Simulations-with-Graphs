package cpen221.mp2.graph;

import java.util.Arrays;
import java.util.HashMap;
import java.util.*;

/**
 * Abstraction Function: Maps each instance of AMGraph to a mathematical graph.
 *      A mathematical graph then contains a collection of vertices connected by edges
 * Rep Invariant:
 *      -Each vertex and edge is not null
 *      -Each vertex and edge is a valid subtype of classes Vertex and Edge respectively
 *      -Graph contains no duplicate vertices (vertices that have the same ID and name)
 *      -Graph contains no duplicate edges (edges that connect to the same vertices)
 */
public class AMGraph<V extends Vertex, E extends Edge<V>>
        implements IGraph<V, E> {

    private final double[][] _adjacencyMatrix;
    private final boolean[] _occupiedIndices;
    public final int _maxVertices;
    private int _numVertices;
    private final HashMap<V, Integer> _indexMap;
    private final Set<E> _edgeSet;

    /**
     * Create an empty graph with an upper-bound on the number of vertices
     * @param maxVertices is greater or equal to 1
     */
    public AMGraph(int maxVertices) {
        _adjacencyMatrix = new double[maxVertices][maxVertices];
        for (int i = 0; i < maxVertices; i++) {
            Arrays.fill(_adjacencyMatrix[i], -1);
        }
        _occupiedIndices = new boolean[maxVertices];
        _maxVertices = maxVertices;
        _numVertices = 0;
        _indexMap = new HashMap<>();

        // This is purely to keep track of all the Edge objects passed into the graph,
        // so that we can return the exact Edge objects when needed
        _edgeSet = new HashSet<>();

    }

    /**
     * Attempts to add a new vertex to the graph. This operation is unsuccessful if
     *      v is null, or
     *      there already exists a duplicate vertex (it shares the same name and ID as v), or
     *      the maximum number of vertices has already been reached.
     * @param newVertex the Vertex to attempt to add.
     * @return true if the operation was successful, and false if it's unsuccessful.
     */
    public boolean addVertex(V newVertex) {

        //changed this to "OR", changed "<" to ">=", and added a check against nulls
        if(hasVertex(newVertex) || _numVertices >= _maxVertices || newVertex == null) {
            return false;
        }

        int lowestUnoccupiedIndex = -1; // this should never stay -1, but it gets mad at me if I don't initialize

        for(int index = 0; index < _maxVertices; index++) {
            if(!_occupiedIndices[index]) {
                lowestUnoccupiedIndex = index;
                break;
            }
        }

        _indexMap.put(newVertex, lowestUnoccupiedIndex);
        _occupiedIndices[lowestUnoccupiedIndex] = true;
        _numVertices++;

        return true;
    }

    /**
     * Finds if a given vertex is in the graph.
     * @param checkVertex the vertex to find.
     * @return true if vertex v is in the graph, and false otherwise.
     */
    public boolean hasVertex(V checkVertex) {
        return _indexMap.containsKey(checkVertex);
    }

    /**
     * Attempts to add a new edge to the graph. This operation is unsuccessful if
     *      e is null, or
     *      there already exists a duplicate edge (it shares the same vertices as e), or
     *      e's vertices aren't in the graph, or they're null.
     * @param newEdge the edge to attempt to add.
     * @return true if the operation is successful, and false otherwise.
     */
    public boolean addEdge(E newEdge) {
        if(hasEdge(newEdge) || newEdge == null) { //added protection against nulls
            return false;
        }

        //If newEdge's vertices are null or aren't in the list, then the old .get() would flip absolute shit
        //So I changed this to .getOrDefault()
        int v1Index = _indexMap.getOrDefault(newEdge.v1(), -1);
        int v2Index = _indexMap.getOrDefault(newEdge.v2(), -1);

        if (v1Index == -1 || v2Index == -1) return false;

        _adjacencyMatrix[v1Index][v2Index] = newEdge.length();
        _adjacencyMatrix[v2Index][v1Index] = newEdge.length();
        _edgeSet.add(newEdge);

        return true;
    }

    /**
     * Finds if a given edge is in the graph.
     * @param checkEdge the edge to attempt to find.
     * @return true if the edge is in the graph, and false otherwise.
     */
    public boolean hasEdge(E checkEdge) {
        if (checkEdge == null) return false; //added protection against nulls

        V v1 = checkEdge.v1();
        V v2 = checkEdge.v2();

        if (!_indexMap.containsKey(v1) || !_indexMap.containsKey(v2)) {
            return false;
        }

        int v1Index = _indexMap.get(v1);
        int v2Index = _indexMap.get(v2);

        return _adjacencyMatrix[v1Index][v2Index] != -1;
    }

    /**
     * Finds if two given vertices are connected by an edge in the graph.
     * @param vertex1 the first vertex the edge connects to.
     * @param vertex2 the second vertex the edge connects to.
     * @return true if an edge connects the two vertices, and false otherwise.
     */
    public boolean hasEdge(V vertex1, V vertex2) {
        return edgeLength(vertex1, vertex2) != -1;
    }

    /**
     * Finds the length of the edge connecting two given vertices in the graph.
     * @param vertex1 the first vertex the edge connects to.
     * @param vertex2 the second vertex the edge connects to.
     * @return the length of the edge if it exists, and zero otherwise.
     */
    public int edgeLength(V vertex1, V vertex2) {
        int v1Index = _indexMap.getOrDefault(vertex1, -1);
        int v2Index = _indexMap.getOrDefault(vertex2, -1);

        if(v1Index != -1 && v2Index != -1 && _adjacencyMatrix[v1Index][v2Index] != -1) {

            //u called "[v2index][v2index]" here xD
            return (int) _adjacencyMatrix[v1Index][v2Index];
        }

        return -1;
    }

    /**
     * Finds the sum of all edge lengths in the graph.
     * @return the sum of all edge lengths.
     */
    public int edgeLengthSum() {
        int sum = 0;

        for(double[] row : _adjacencyMatrix) {
            for(double length : row) {
                if (length != -1) sum += length;
            }
        }
        sum /= 2; // each edge is counted twice in the adjacency matrix

        return sum;
    }

    /**
     * Attempts to remove an edge from the graph. This operation is unsuccessful if
     *      the edge isn't in the graph, or if it's null.
     * @param e the edge to attempt to remove.
     * @return true if the operation was successful, and false otherwise.
     */
    public boolean removeEdge(E e) {
        if (hasEdge(e)) {
            _adjacencyMatrix[_indexMap.get(e.v1())][_indexMap.get(e.v2())] = -1;
            _adjacencyMatrix[_indexMap.get(e.v2())][_indexMap.get(e.v1())] = -1;
            _edgeSet.remove(e);
            return true;
        }

        return false;
    }

    /**
     * Attempts to remove an edge from the graph given the two vertices it connects to.
     *      This operation is unsuccessful if the edge isn't in the graph.
     * @param v1 the first vertex the edge connects to.
     * @param v2 the second vertex the edge connects to.
     * @return true if the operation was successful, and false otherwise.
     */
    public boolean removeEdge(V v1, V v2) {
        if (hasEdge(v1, v2)) {
            _adjacencyMatrix[_indexMap.get(v1)][_indexMap.get(v2)] = -1;
            _adjacencyMatrix[_indexMap.get(v2)][_indexMap.get(v1)] = -1;

            for (E e : _edgeSet) {
                if ((e.v1().equals(v1) && e.v2().equals(v2)) ||
                        (e.v1().equals(v2) && e.v2().equals(v1))) {
                    _edgeSet.remove(e);
                    break;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Attempts to remove a vertex from the graph. This operation is unsuccessful if
     *      v is not in the graph, or if it's null.
     * @param v the vertex to remove.
     * @return true if the operation was successful, and false otherwise.
     */
    public boolean removeVertex(V v) {
        if (hasVertex(v)) {
            int vIndex = _indexMap.get(v);
            _occupiedIndices[vIndex] = false;
            _indexMap.remove(v);
            _numVertices--;
            return true;
        }
        return false;
    }

    /**
     * Attempts to find an edge that connects to two given vertices.
     * @param v1 the first vertex the edge connects to.
     * @param v2 the second vertex the edge connects to.
     * @return the edge object if the edge is found, and a null pointer otherwise.
     */
    public E getEdge(V v1, V v2) {
        if (hasEdge(v1, v2)) {
            return (E) new Edge<>(v1, v2, edgeLength(v1, v2));
        }
        return null;
    }

    /**
     * Provides all the vertices in the graph.
     * @return a Set of all vertices.
     */
    public Set<V> allVertices() {
        return _indexMap.keySet();
    }

    /**
     * Provides all the edges in the graph.
     * @return a Set of all edges.
     */
    public Set<E> allEdges() {
        return new HashSet<>(_edgeSet);
    }

    /**
     * Provides all the edges connected to a given vertex in the graph.
     * @param v the vertex that the edges connect to.
     * @return a Set of all edges connecting to v.
     */
    public Set<E> allEdges(V v) {
        Set<E> allEdges = new HashSet<>();
        for (E e : _edgeSet) {
            if (e.incident(v) || e.incident(v)) {
                allEdges.add(e);
            }
        }
        return allEdges;
    }

    /**
     * Provides the "neighbourhood" of a given vertex v.
     *      This consists of all edges that are connected to v, and all vertices
     *      that are connected to those edges.
     * @param v the vertex to take the neighbourhood from
     * @return a collection of all mappings of the neighbourhood, where
     *      the key is the vertex connected to the neighbouring edge, and
     *      the value is the edge.
     */
    public Map<V, E> getNeighbourhood(V v) {
        Map<V, E> neighbourhoodMap = new HashMap<>();
        for (E e : _edgeSet) {
            if (e.incident(v)) {
                neighbourhoodMap.put((V) e.distinctVertex(v), e);
            }
        }
        return neighbourhoodMap;
    }

    /* === TASK 3 === */

    /**
     * Gets the cost of a path (a list of vertices), depending on the PathCostType metric
     * @param path The list of vertices representing the path
     * @param costType The measure of path cost to be used
     * @return The cost of the path
     */
    public int pathCost(List<V> path, PathCostType costType) {
        if (path == null) return -1;

        int len = path.size();
        int cost = 0;

        switch (costType) {
            case SUM_EDGES:
                for(int i = 0; i+1 < len; i ++){
                    cost += edgeLength(path.get(i), path.get(i+1));
                }
                break;
            case MAX_EDGE:
                for(int i = 0; i+1 < len; i ++){
                    cost = Math.max(cost, edgeLength(path.get(i), path.get(i+1)));
                }
                break;
            case MIN_EDGE:
                cost = Integer.MAX_VALUE;
                for(int i = 0; i+1 < len; i ++){
                    cost = Math.min(cost, edgeLength(path.get(i), path.get(i+1)));
                }
        }
        return cost;
    }
    /**
     * Finds the shortest path between two vertices
     * @param source The vertex at the start of the path
     * @param sink The vertex at the end of the path
     * @return A list of vertices that corresponds to the shortest path between
     *         source and sink
     */
    private List<V> minimumSumEdges(V source, V sink) {
        Set<V> visitedNodes = new HashSet<>();
        Set<V> unvisitedNodes = new HashSet<>(this.allVertices()); //all nodes in unvisited list
        List<V> sumEdgePath = new ArrayList<>();

        //keep track of previous node
        Map<V,V> prevNode = new HashMap<>();
        //map for node and shortest distance
        Map<V,Double> distanceMap = new HashMap<>();

        for(V v: this.allVertices()){
            if(v == source){
                distanceMap.put(v,0.0);
            }
            else{
                distanceMap.put(v,Double.MAX_VALUE);
            }
        }

        //updating distanceMap based on the neighbors of source
        Map<V,E> neighbors =  getNeighbourhood(source);

        for(V v: neighbors.keySet()){
            if(unvisitedNodes.contains(v) && neighbors.get(v).length() < distanceMap.get(v)){

                distanceMap.put(v,neighbors.get(v).length());
                prevNode.put(v,source);
            }
        }
        unvisitedNodes.remove(source);
        visitedNodes.add(source);

        boolean hasNeighbors = true;
        while(hasNeighbors) {
            V currNode = null;
            double minValue = Integer.MAX_VALUE;

            // Iterate through the map entries to find the minimum value
            for (V n: distanceMap.keySet()) {
                if (distanceMap.get(n) < minValue && !visitedNodes.contains(n)) {
                    minValue =distanceMap.get(n);
                    currNode = n;
                }
            }
            neighbors = getNeighbourhood(currNode);
            /*boolean*/ hasNeighbors = false;

            for (V v : neighbors.keySet()) {
                if (unvisitedNodes.contains(v)) {
                    hasNeighbors = true;


                    if (minValue + neighbors.get(v).length() < distanceMap.get(v)) {
                        distanceMap.put(v, minValue + neighbors.get(v).length());
                        prevNode.put(v, currNode);
                    }
                }
            }

            unvisitedNodes.remove(currNode);
            visitedNodes.add(currNode);

            if(!hasNeighbors && !unvisitedNodes.isEmpty()){
                hasNeighbors = true;
            }
        }

        V tempNode = sink;
        sumEdgePath.add(tempNode);

        while(tempNode!=source){
            tempNode = prevNode.get(tempNode);
            sumEdgePath.add(tempNode);
        }


        return sumEdgePath;
    }
    /**
     * Finds a valid path between source and sink which contains the max edge
     * @param source The vertex at the start of the path
     * @param sink The vertex at the end of the path
     * @return A list of vertices that corresponds to the path between the
     *         source and sink that contains the max edge
     */
    private List<V> minimumMaxEdge(V source, V sink) {
        //Calls the recursive function. Two empty lists signify that we have visited no nodes, and
        // our current path consists of nothing
        List<List<V>> allPaths = pathStep(source, sink, new ArrayList<>(), new ArrayList<>());

        int maxCost = Integer.MAX_VALUE;
        List<V> maxCostPath = new ArrayList<>();

        for (List<V> path : allPaths) {

            int currentPathCost = pathCost(path, PathCostType.MAX_EDGE);
            if ( currentPathCost < maxCost ) {
                maxCost = currentPathCost;
                maxCostPath = path;
            }
        }
        return maxCostPath;
    }
    /**
     * Finds a valid path between source and sink which contains the min edge
     * @param source The vertex at the start of the path
     * @param sink The vertex at the end of the path
     * @return A list of vertices that corresponds to the path between the
     *         source and sink that contains the min edge
     */
    private List<V> minimumMinEdge(V source, V sink) {
        //Calls the recursive function. Two empty lists signify that we have visited no nodes, and
        // our current path consists of nothing
        List<List<V>> allPaths = pathStep(source, sink, new ArrayList<>(), new ArrayList<>());

        int minCost = Integer.MAX_VALUE;
        List<V> minCostPath = new ArrayList<>();

        for (List<V> path : allPaths) {

            int currentPathCost = pathCost(path, PathCostType.MIN_EDGE);
            if ( currentPathCost < minCost ) {
                minCost = currentPathCost;
                minCostPath = path;
            }
        }
        return minCostPath;
    }
    /**
     * Finds a valid path between source and sink which contains the minimum max edge
     * @param current The current vertex
     * @param sink The vertex at the end of the path
     * @param visitedNodes List of vertices that have already been visited
     * @param currentPath List of vertices that tracks the current path
     * @return A list of vertices within a list that corresponds to the all the possible path from
     *         the source to the sink. Adds an empty list within the list if it is not a valid path
     */
    private List<List<V>> pathStep(V current, V sink, List<V> visitedNodes, List<V> currentPath) {
        Set<V> neighbours =  getNeighbourhood(current).keySet();
        visitedNodes.add(current);
        currentPath.add(current);
        List<List<V>> allPaths = new ArrayList<>();

        //Base case: no unvisited neighbours.
        if (current.equals(sink)) {
            allPaths.add(currentPath);
            return allPaths;
        }
        if (neighbours.isEmpty()) {
            return allPaths;
        }

        //Inductive step: go find paths for the next node,
        // marking this one as visited and adding this one to the current path
        for(V v: neighbours){
            if(!visitedNodes.contains(v)){

                allPaths.addAll(pathStep(v, sink, new ArrayList<>(visitedNodes), new ArrayList<>(currentPath)));
            }
        }
        return allPaths;
    }

    /**
     * Finds the path between two vertices with the minimum cost, depending on the PathCostType metric
     * @param source The vertex at the start of the path
     * @param sink  The vertex at the end of the path
     * @param costType The measure of path cost to be used
     * @return A list of the vertices in the path
     */
    public List<V> minimumCostPath(V source, V sink, PathCostType costType) {
        if (source == null || sink == null ||
                !allVertices().contains(source) || !allVertices().contains(sink)) {
            return null;
        }

        //Calls the appropriate child method based on costType
        switch (costType) {
            case MAX_EDGE :
                return minimumMaxEdge(source, sink);
            case MIN_EDGE :
                return minimumMinEdge(source, sink);
            case SUM_EDGES :
                return minimumSumEdges(source, sink);
        }
        return null;
    }

    /**
     * Finds the set of vertices within a certain length range of vertex v
     * @param v The vertex at the centre of the neighbourhood
     * @param range The range about v which constitutes the neighbourhood
     * @return A map with keys representing the vertices in the neighbourhood,
     *         and values representing the last edge on the shortest path from
     *         v to that vertex
     */
    public Map<V, E> getNeighbourhood(V v, int range) {
        Map<V, E> neighbourhoodMap = new HashMap<>();

        for(V vertex : allVertices()) {
            if(!vertex.equals(v)) {
                List<V> minPath = minimumCostPath(v, vertex, PathCostType.SUM_EDGES);
                double cost = pathCost(minPath, PathCostType.SUM_EDGES);

                if(cost <= range) {
                    V secondLastVertex = minPath.get(1);
                    E lastEdge = getEdge(secondLastVertex, vertex);
                    neighbourhoodMap.put(vertex, lastEdge);
                }
            }
        }

        return neighbourhoodMap;
    }

    /* === TASK 4 === */
    /**
     * Finds the maximum eccentricity of given vertices
     * @param costType The measure of path cost to be used
     * @return An integer corresponding to the max eccentricity
     */
    public int getDiameter(PathCostType costType) {

        int maxNum = 0;
        for(V vertex : allVertices()){
            maxNum = Math.max(maxNum, getEccentricity(vertex,costType));
        }

        return maxNum;
    }

    /**
     * Find the central vertex of the graph. The central vertex is the
     * vertex with the minimum eccentricity.
     * @param costType the cost type metric used to calculate eccentricity
     * @return the central vertex
     */
    public V getCenter(PathCostType costType) {
        int minEccentricity = Integer.MAX_VALUE;
        V center = null;

        for(V vertex : allVertices()) {
            if(getEccentricity(vertex, costType) < minEccentricity) {
                minEccentricity = getEccentricity(vertex, costType);
                center = vertex;
            }
        }

        return center;
    }

    /**
     * Find the eccentricity of a vertex, defined as the maximum mincost to
     * get to another vertex
     * @param v the source vertex
     * @param costType the cost type metric used to calculate eccentricity
     * @return the eccentricity of the vertex
     */
    private int getEccentricity(V v, PathCostType costType) {
        int maxCost = 0;

        for(V vertex : allVertices()) {
            if(!vertex.equals(v)) {
                List<V> minPath = minimumCostPath(v, vertex, costType);
                int cost = pathCost(minPath, costType);

                maxCost = Math.max(maxCost, cost);
            }
        }

        return maxCost;
    }
}
