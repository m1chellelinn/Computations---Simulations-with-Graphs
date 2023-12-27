package cpen221.mp2;

import java.util.stream.*;

import cpen221.mp2.graph.*;
import cpen221.mp2.sealevels.GridLocation;
import cpen221.mp2.sealevels.SeaLevels;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class Task3Tests {
    private static List<String> alphabet = List.of(
            "A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
            "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    );

    /* Graph 0 */
    private static List<Vertex> lv0 = List.of(
            new Vertex(1, "A"),
            new Vertex(2, "B"),
            new Vertex(3, "C"),
            new Vertex(4, "D")
    );
    private static List<Edge<Vertex>> le0 = List.of(
            new Edge<>(lv0.get(0), lv0.get(1), 5),
            new Edge<>(lv0.get(1), lv0.get(2), 7),
            new Edge<>(lv0.get(0), lv0.get(3), 9)
    );

    /* Graph 1 (No connection) */
    private static List<Edge<Vertex>> le1 = List.of(
            new Edge<>(lv0.get(0), lv0.get(1), 5),
            new Edge<>(lv0.get(2), lv0.get(3), 7)
    );

    /* Full Alphabet vertices */
    private static Map<String, Vertex> lv1 = IntStream.range(0, 26)
            .mapToObj(a -> new AbstractMap.SimpleEntry<>(
                    alphabet.get(a), new Vertex(a, alphabet.get(a))
            ))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    /*                 Graph 2
     *
     *                    D
     *  +V----------------+--+
     *  |                 |  E
     *  |                 |
     * U+-----+S         B+-----+
     *  |     |    Q      |     C        +G
     *  +T   R+----+      |              |
     *        |    |      |              |
     *        |    +------+---------+F   |
     *    Y   |    P      A         |    |
     *    +---+X-------------------W+----+H
     *    |   |                     |    |
     *    +   +-------+N----K+------+J   |
     *    Z   O       |      |           |
     *                |      |           +I
     *                |      |
     *                +------+L
     *                M
     */
    private static List<Edge<Vertex>> le2 = List.of(
            new Edge<>(lv1.get("A"), lv1.get("P"), 6),
            new Edge<>(lv1.get("A"), lv1.get("B"), 3),
            new Edge<>(lv1.get("A"), lv1.get("F"), 9),
            new Edge<>(lv1.get("B"), lv1.get("D"), 4),
            new Edge<>(lv1.get("B"), lv1.get("C"), 5),
            new Edge<>(lv1.get("D"), lv1.get("E"), 7),
            new Edge<>(lv1.get("D"), lv1.get("V"), 15),
            new Edge<>(lv1.get("F"), lv1.get("W"), 2),
            new Edge<>(lv1.get("G"), lv1.get("H"), 43),
            new Edge<>(lv1.get("H"), lv1.get("I"), 8),
            new Edge<>(lv1.get("J"), lv1.get("W"), 1),
            new Edge<>(lv1.get("J"), lv1.get("K"), 10),
            new Edge<>(lv1.get("K"), lv1.get("L"), 11),
            new Edge<>(lv1.get("L"), lv1.get("M"), 13),
            new Edge<>(lv1.get("M"), lv1.get("N"), 14),
            new Edge<>(lv1.get("N"), lv1.get("O"), 16),
            new Edge<>(lv1.get("N"), lv1.get("K"), 19),
            new Edge<>(lv1.get("O"), lv1.get("X"), 18),
            new Edge<>(lv1.get("P"), lv1.get("Q"), 22),
            new Edge<>(lv1.get("Q"), lv1.get("R"), 23),
            new Edge<>(lv1.get("R"), lv1.get("S"), 24),
            new Edge<>(lv1.get("R"), lv1.get("X"), 25),
            new Edge<>(lv1.get("S"), lv1.get("U"), 26),
            new Edge<>(lv1.get("T"), lv1.get("U"), 27),
            new Edge<>(lv1.get("U"), lv1.get("V"), 28),
            new Edge<>(lv1.get("W"), lv1.get("H"), 29),
            new Edge<>(lv1.get("X"), lv1.get("Y"), 30),
            new Edge<>(lv1.get("X"), lv1.get("W"), 42),
            new Edge<>(lv1.get("Y"), lv1.get("Z"), 40)
    );

    private static IGraph<Vertex, Edge<Vertex>> g0 = formGraph(lv0, le0);
    private static IGraph<Vertex, Edge<Vertex>> g1 = formGraph(lv0, le1);
    private static IGraph<Vertex, Edge<Vertex>> g2 = new ALGraph<>();
    private static IGraph<Vertex, Edge<Vertex>> g3 = formGraph(new ArrayList<>(lv1.values()), le2);

    private static ALGraph<Vertex, Edge<Vertex>> formGraph(List<Vertex> lv, List<Edge<Vertex>> le) {
        ALGraph<Vertex, Edge<Vertex>> g = new ALGraph<>();
        lv.forEach(g::addVertex);
        le.forEach(g::addEdge);
        return g;
    }

    /**
     * Argument provider
     *
     * @return Graph to test, Vertex, Vertex, expected length
     */
    private static Stream<Arguments> pathLengthProvider() {
        return Stream.of(
                // Arguments.of(new Graph<>(), List.of(), 0),
                Arguments.of(g0, List.of(lv0.get(0), lv0.get(1), lv0.get(2)), 12),
                Arguments.of(g3, List.of(
                        lv1.get("I"), lv1.get("H"), lv1.get("W"), lv1.get("J"), lv1.get("K")
                ), 48)
        );
    }

    /**
     * Argument provider
     *
     * @return Graph to test, v0, v1, edge
     */
    private static Stream<Arguments> edgeProvider() {
        return Stream.of(
                Arguments.of(g0, lv0.get(0), lv0.get(1), le0.get(0)),
                Arguments.of(g3, lv1.get("X"), lv1.get("W"), new Edge<Vertex>(lv1.get("X"), lv1.get("W")))
        );
    }

    /**
     * Argument provider
     *
     * @return Graph to test, distance, expected vertices
     */
    private static Stream<Arguments> searchProvider() {
        return Stream.of(
                Arguments.of(g0, lv0.get(0), 9,
                        Map.of(
                                lv0.get(1), new Edge<Vertex>(lv0.get(0), lv0.get(1)),
                                lv0.get(3), new Edge<Vertex>(lv0.get(0), lv0.get(3))
                        )
                ),
                Arguments.of(g0, lv0.get(0), 15,
                        Map.of(
                                lv0.get(1), new Edge<Vertex>(lv0.get(0), lv0.get(1)),
                                lv0.get(2), new Edge<Vertex>(lv0.get(1), lv0.get(2)),
                                lv0.get(3), new Edge<Vertex>(lv0.get(0), lv0.get(3))
                        )
                ),
                Arguments.of(g3, lv1.get("I"), 42,
                        Map.of(
                                lv1.get("J"), new Edge<Vertex>(lv1.get("W"), lv1.get("J")),
                                lv1.get("F"), new Edge<Vertex>(lv1.get("W"), lv1.get("F")),
                                lv1.get("W"), new Edge<Vertex>(lv1.get("H"), lv1.get("W")),
                                lv1.get("H"), new Edge<Vertex>(lv1.get("I"), lv1.get("H"))
                        )
                )
        );
    }

    /**
     * Argument provider
     *
     * @return Graph to test, expected sum
     */
    private static Stream<Arguments> edgeLengthSumProvider() {
        return Stream.of(
                Arguments.of(g0, 21),
                Arguments.of(g1, 12),     /* No path */
                Arguments.of(g2, 0),     /* No path */
                Arguments.of(g3, 520)
        );
    }

    /**
     * Argument provider
     *
     * @return Graph to test, expected
     */
    private static Stream<Arguments> shortestPathProvider() {
        return Stream.of(
                Arguments.of(g0, lv0.get(2), lv0.get(3), 21),
                Arguments.of(g1, lv0.get(0), lv0.get(3), 0),   /* No path */
                Arguments.of(g3, lv1.get("I"), lv1.get("X"), 79),
                Arguments.of(g3, lv1.get("U"), lv1.get("H"), 90),
                Arguments.of(g3, lv1.get("D"), lv1.get("M"), 53)
        );
    }

    @ParameterizedTest
    @MethodSource("pathLengthProvider")
    void pathLength(IGraph<Vertex, Edge<Vertex>> g, List<Vertex> path, int weight) {
        try {
            assertEquals(weight, g.pathCost(path, PathCostType.SUM_EDGES));
        }
        catch (Exception ex) {
            if (weight != 0) {
                fail();
            }
        }

    }

    @ParameterizedTest
    @MethodSource("edgeProvider")
    void edge(IGraph<Vertex, Edge<Vertex>> g, Vertex v0, Vertex v1, Edge<Vertex> e) {
        assertEquals(e, g.getEdge(v0, v1));
    }

    @ParameterizedTest
    @MethodSource("searchProvider")
    void search(IGraph<Vertex, Edge<Vertex>> g, Vertex v, int distance, Map<Vertex, Edge<Vertex>> expected) {
        var out = g.getNeighbourhood(v, distance);
        assertEquals(expected, out);
    }

    @ParameterizedTest
    @MethodSource("shortestPathProvider")
    void shortestPath(IGraph<Vertex, Edge<Vertex>> g, Vertex v0, Vertex v1, double shortest) {
        try {
            double answer = g.pathCost(g.minimumCostPath(v0, v1, PathCostType.SUM_EDGES), PathCostType.SUM_EDGES);
            if (g.minimumCostPath(v0, v1, PathCostType.SUM_EDGES).size() == 0) {
                if (answer == 0 || answer == -1 || answer >= Integer.MAX_VALUE) {
                    shortest = answer;
                }
            }
            assertEquals(shortest, g.pathCost(g.minimumCostPath(v0, v1, PathCostType.SUM_EDGES), PathCostType.SUM_EDGES));
        }
        catch (Exception ex) {
            if (shortest > 0 && shortest < Integer.MAX_VALUE) {
                fail();
            }
        }
    }

    @Test
    public void testPathCost() {
        Vertex v1 = new Vertex(1, "A");
        Vertex v2 = new Vertex(2, "B");
        Vertex v3 = new Vertex(3, "C");
        Vertex v4 = new Vertex(4, "D");

        List<Vertex> path = new ArrayList<>(Arrays.asList(v3, v2, v1, v4));
        List<Vertex> disconnectedPath = new ArrayList<>(Arrays.asList(v4, v1, v3, v2));
        List<Vertex> sourceIsSink = new ArrayList<>(Arrays.asList(v4, v1, v4));

        Edge<Vertex> e1 = new Edge<>(v1, v2, 5);
        Edge<Vertex> e2 = new Edge<>(v2, v3, 7);
        Edge<Vertex> e3 = new Edge<>(v1, v4, 9);

        IGraph<Vertex, Edge<Vertex>> graphAL = new ALGraph<>();
        graphAL.addVertex(v1);
        graphAL.addVertex(v2);
        graphAL.addVertex(v3);
        graphAL.addVertex(v4);
        graphAL.addEdge(e1);
        graphAL.addEdge(e2);
        graphAL.addEdge(e3);

        assertEquals(e2, graphAL.getEdge(v2, v3));
        assertEquals(21, graphAL.pathCost(path, PathCostType.SUM_EDGES));
        assertEquals(5, graphAL.pathCost(path, PathCostType.MIN_EDGE));
        assertEquals(9, graphAL.pathCost(path, PathCostType.MAX_EDGE));

        IGraph<Vertex, Edge<Vertex>> graphAM = new AMGraph<>(20);
        graphAM.addVertex(v1);
        graphAM.addVertex(v2);
        graphAM.addVertex(v3);
        graphAM.addVertex(v4);
        graphAM.addEdge(e1);
        graphAM.addEdge(e2);
        graphAM.addEdge(e3);

        assertEquals(e2, graphAM.getEdge(v2, v3));
        assertEquals(21, graphAM.pathCost(path, PathCostType.SUM_EDGES));
        assertEquals(5, graphAM.pathCost(path, PathCostType.MIN_EDGE));
        assertEquals(9, graphAM.pathCost(path, PathCostType.MAX_EDGE));

        try {
            graphAL.pathCost(disconnectedPath, PathCostType.SUM_EDGES);
            fail();
        }
        catch (Exception exception1) {
        }
    }

    @Test
    public void testMinPathCost() {
        Vertex v1 = new Vertex(1, "A");
        Vertex v2 = new Vertex(2, "B");
        Vertex v3 = new Vertex(3, "C");
        Vertex v4 = new Vertex(4, "D");
        Vertex v5 = new Vertex(5, "E");

        Edge<Vertex> e1 = new Edge<>(v1, v2, 5);
        Edge<Vertex> e2 = new Edge<>(v2, v3, 7);
        Edge<Vertex> e3 = new Edge<>(v1, v4, 9);
        Edge<Vertex> e4 = new Edge<>(v3, v4, 20);

        IGraph<Vertex, Edge<Vertex>> graphAL = new ALGraph<>();
        graphAL.addVertex(v1);
        graphAL.addVertex(v2);
        graphAL.addVertex(v3);
        graphAL.addVertex(v4);
        graphAL.addVertex(v5);
        graphAL.addEdge(e1);
        graphAL.addEdge(e2);
        graphAL.addEdge(e3);
        graphAL.addEdge(e4);

        assertEquals(e2, graphAL.getEdge(v2, v3));
        assertEquals(20, graphAL.pathCost(graphAL.minimumCostPath(v3, v4, PathCostType.SUM_EDGES),
                PathCostType.SUM_EDGES));
        assertEquals(5, graphAL.pathCost(graphAL.minimumCostPath(v3, v4, PathCostType.MIN_EDGE),
                PathCostType.MIN_EDGE));
        assertEquals(9, graphAL.pathCost(graphAL.minimumCostPath(v3, v4, PathCostType.MAX_EDGE),
                PathCostType.MAX_EDGE));

        IGraph<Vertex, Edge<Vertex>> graphAM = new AMGraph<>(20);
        graphAM.addVertex(v1);
        graphAM.addVertex(v2);
        graphAM.addVertex(v3);
        graphAM.addVertex(v4);
        graphAM.addVertex(v5);
        graphAM.addEdge(e1);
        graphAM.addEdge(e2);
        graphAM.addEdge(e3);
        graphAM.addEdge(e4);

        assertEquals(e2, graphAM.getEdge(v2, v3));
        assertEquals(20, graphAM.pathCost(graphAM.minimumCostPath(v3, v4, PathCostType.SUM_EDGES), PathCostType.SUM_EDGES));
        assertEquals(5, graphAM.pathCost(graphAM.minimumCostPath(v3, v4, PathCostType.MIN_EDGE), PathCostType.MIN_EDGE));
        assertEquals(9, graphAM.pathCost(graphAM.minimumCostPath(v3, v4, PathCostType.MAX_EDGE), PathCostType.MAX_EDGE));
    }

    @Test
    public void testIsSubmerged() {
        double[][] terrain = {
                {1, 2, 3, 4, 5},
                {2, 3, 4, 5, 6},
                {3, 4, 5, 6, 7},
                {4, 5, 6, 7, 8},
                {5, 6, 7, 8, 9}
        };
        double waterLevel = 5;
        GridLocation[] waterSources = {new GridLocation(0, 0)};

        boolean[][] expected = {
                {true, true, true, true, true},
                {true, true, true, true, false},
                {true, true, true, false, false},
                {true, true, false, false, false},
                {true, false, false, false, false}
        };

        boolean[][] result = SeaLevels.isSubmerged(terrain, waterSources, waterLevel);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testIsSubmerged2() {
        double[][] terrain = {
                {1.2, 2.5, 3, 4, 5},
                {2.1, -3, 4, 5, 6},
                {3, 4.4, -5, -6, 7},
                {4, 5, -6.2, 7, 8},
                {5, 6, 7, 8, -9.1}
        };
        double waterLevel = 5;
        GridLocation[] waterSources = {new GridLocation(0, 0)};

        boolean[][] expected = {
                {true, true, true, true, true},
                {true, true, true, true, false},
                {true, true, true, true, false},
                {true, true, true, false, false},
                {true, false, false, false, false}
        };

        boolean[][] result = SeaLevels.isSubmerged(terrain, waterSources, waterLevel);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDangerLevels() {
        double[][] terrain = {
                {1, 3, 3, 4, 5},
                {3, 3, 4, 5, 6},
                {3, 4, 5, 6, 7},
                {4, 5, 6, 7, 8},
                {5, 6, 7, 8, 9}
        };
        GridLocation[] waterSources = {new GridLocation(0, 0)};

        double[][] expected = {
                {1, 3, 3, 4, 5},
                {3, 3, 4, 5, 6},
                {3, 4, 5, 6, 7},
                {4, 5, 6, 7, 8},
                {5, 6, 7, 8, 9}
        };

        double[][] dangerLevels = SeaLevels.dangerLevel(terrain, waterSources);

        assertArrayEquals(expected, dangerLevels);
    }

    @Test
    public void testDangerLevels2() {
        double[][] terrain = {
                {1, 2, 1, 4, 5},
                {2, 1, 4, 5, 6},
                {1, 0, 1, 1, 7},
                {4, 1, 2, 0, 8},
                {5, 2, 2, -1, 9}
        };
        GridLocation[] waterSources = {new GridLocation(0, 0), new GridLocation(2, 1)};

        double[][] expected = {
                {1, 2, 2, 4, 5},
                {2, 1, 4, 5, 6},
                {1, 0, 1, 1, 7},
                {4, 1, 2, 1, 8},
                {5, 2, 2, 1, 9}
        };

        double[][] dangerLevels = SeaLevels.dangerLevel(terrain, waterSources);

        assertArrayEquals(expected, dangerLevels);
    }

    @Test
    public void testDangerLevels3() {
        double[][] terrain = {
                {1.1, 2.69, 1, 4, 5},
                {2, 1, 4, 5, 6},
                {1, 0, 1, 1, 7},
                {4, 1, 2, 0, 8},
                {5, 2, 2, -1, 9}
        };
        GridLocation[] waterSources = {new GridLocation(0, 0), new GridLocation(2, 1)};

        double[][] expected = {
                {1.1, 2.69, 2.69, 4, 5},
                {2, 1, 4, 5, 6},
                {1, 0, 1, 1, 7},
                {4, 1, 2, 1, 8},
                {5, 2, 2, 1, 9}
        };

        double[][] dangerLevels = SeaLevels.dangerLevel(terrain, waterSources);
        assertArrayEquals(expected, dangerLevels);
    }
}
