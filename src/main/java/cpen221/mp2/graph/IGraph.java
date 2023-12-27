package cpen221.mp2.graph;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IGraph<V extends Vertex, E extends Edge<V>> {

    /* === TASK 1 === */
    /**
     * Attempts to add a new vertex to the graph. This operation is unsuccessful if
     *      v is null, or
     *      there already exists a duplicate vertex (it shares the same name and ID as v).
     * @param v the Vertex to attempt to add.
     * @return true if the operation was successful, and false if it's unsuccessful.
     */
    boolean addVertex(V v);
    /**
     * Finds if a given vertex is in the graph.
     * @param v the vertex to find.
     * @return true if vertex v is in the graph, and false otherwise.
     */
    boolean hasVertex(V v);
    /**
     * Attempts to add a new edge to the graph. This operation is unsuccessful if
     *      e is null, or
     *      there already exists a duplicate edge (it shares the same vertices as e), or
     *      e's vertices aren't in the graph, or they're null.
     * @param e the edge to attempt to add.
     * @return true if the operation is successful, and false otherwise.
     */
    boolean addEdge(E e);
    /**
     * Finds if a given edge is in the graph.
     * @param e the edge to attempt to find.
     * @return true if the edge is in the graph, and false otherwise.
     */
    boolean hasEdge(E e);
    /**
     * Finds if two given vertices are connected by an edge in the graph.
     * @param v1 the first vertex the edge connects to.
     * @param v2 the second vertex the edge connects to.
     * @return true if an edge connects the two vertices, and false otherwise.
     */
    boolean hasEdge(V v1, V v2);
    /**
     * Finds the length of the edge connecting two given vertices in the graph.
     * @param v1 the first vertex the edge connects to.
     * @param v2 the second vertex the edge connects to.
     * @return the length of the edge if it exists, and zero otherwise.
     */
    int edgeLength(V v1, V v2);

    /* === TASK 2 === */
    /**
     * Finds the sum of all edge lengths in the graph.
     * @return the sum of all edge lengths.
     */
    int edgeLengthSum();
    /**
     * Attempts to remove an edge from the graph. This operation is unsuccessful if
     *      the edge isn't in the graph, or if it's null.
     * @param e the edge to attempt to remove.
     * @return true if the operation was successful, and false otherwise.
     */
    boolean removeEdge(E e);
    /**
     * Attempts to remove an edge from the graph given the two vertices it connects to.
     *      This operation is unsuccessful if the edge isn't in the graph.
     * @param v1 the first vertex the edge connects to.
     * @param v2 the second vertex the edge connects to.
     * @return true if the operation was successful, and false otherwise.
     */
    boolean removeEdge(V v1, V v2);
    /**
     * Attempts to remove a vertex from the graph. This operation is unsuccessful if
     *      v is not in the graph, or if it's null.
     * @param v the vertex to remove.
     * @return true if the operation was successful, and false otherwise.
     */
    boolean removeVertex(V v);
    /**
     * Attempts to find an edge that connects to two given vertices.
     * @param v1 the first vertex the edge connects to.
     * @param v2 the second vertex the edge connects to.
     * @return the edge object if the edge is found, and a null pointer otherwise.
     */
    E getEdge(V v1, V v2);
    /**
     * Provides all the vertices in the graph.
     * @return a Set of all vertices.
     */
    Set<V> allVertices();
    /**
     * Provides all the edges in the graph.
     * @return a Set of all edges.
     */
    Set<E> allEdges();
    /**
     * Provides all the edges connected to a given vertex in the graph.
     * @param v the vertex that the edges connect to.
     * @return a Set of all edges connecting to v.
     */
    Set<E> allEdges(V v);
    /**
     * Provides the "neighbourhood" of a given vertex v.
     *      This consists of all edges that are connected to v, and all vertices
     *      that are connected to those edges.
     * @param v the vertex to take the neighbourhood from
     * @return a collection of all mappings of the neighbourhood, where
     *      the key is the vertex connected to the neighbouring edge, and
     *      the value is the edge.
     */
    Map<V, E> getNeighbourhood(V v);

    /* === TASK 3 === */

    /**
     * Gets the cost of a path (a list of vertices), depending on the PathCostType metric
     * @param path The list of vertices representing the path
     * @param costType The measure of path cost to be used
     * @return The cost of the path
     */
    int pathCost(List<V> path, PathCostType costType);

    /**
     * Finds the path between two vertices with the minimum cost, depending on the PathCostType metric
     * @param source The vertex at the start of the path
     * @param sink  The vertex at the end of the path
     * @param costType The measure of path cost to be used
     * @return A list of the vertices in the path
     */
    List<V> minimumCostPath(V source, V sink, PathCostType costType);
    /**
     * Finds the set of vertices within a certain length range of vertex v
     * @param v The vertex at the centre of the neighbourhood
     * @param range The range about v which constitutes the neighbourhood
     * @return A map with keys representing the vertices in the neighbourhood,
     *         and values representing the last edge on the shortest path from
     *         v to that vertex
     */
    Map<V, E> getNeighbourhood(V v, int range);

    /* === TASK 4 === */
    /**
     * Finds the maximum eccentricity of given vertices
     * @param costType The measure of path cost to be used
     * @return An integer corresponding to the max eccentricity
     */
    int getDiameter(PathCostType costType);
    /**
     * Find the central vertex of the graph. The central vertex is the
     * vertex with the minimum eccentricity.
     * @param costType the cost type metric used to calculate eccentricity
     * @return the central vertex
     */
    V getCenter(PathCostType costType);
}

