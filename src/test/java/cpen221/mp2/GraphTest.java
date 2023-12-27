package cpen221.mp2;

import cpen221.mp2.graph.*;
import cpen221.mp2.sealevels.GridLocation;
import org.junit.jupiter.api.Test;
import java.util.*;

import static cpen221.mp2.sealevels.SeaLevels.dangerLevel;
import static cpen221.mp2.sealevels.SeaLevels.isSubmerged;
import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {

    @Test
    public void testCreateGraph() {
        Vertex v1 = new Vertex(1, "A");
        Vertex v2 = new Vertex(2, "B");
        Vertex v3 = new Vertex(3, "C");
        Vertex v4 = new Vertex(4, "D");

        Edge<Vertex> e1 = new Edge<>(v1, v2, 5);
        Edge<Vertex> e2 = new Edge<>(v2, v3, 7);
        Edge<Vertex> e3 = new Edge<>(v1, v4, 9);

        IGraph<Vertex, Edge<Vertex>> g = new ALGraph<>();
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);
        g.addEdge(e1);
        g.addEdge(e2);
        g.addEdge(e3);

        assertEquals(e2, g.getEdge(v2, v3));
        assertEquals(21,
                g.pathCost(
                        g.minimumCostPath(v3, v4, PathCostType.SUM_EDGES),
                        PathCostType.SUM_EDGES
                )
        );
    }

    @Test
    public void testALGraphBasics() { //That is, add, remove, has, edgeLength.
        Vertex v1 = new Vertex(1, "11");
        Vertex v2 = new Vertex(2, "22");
        Vertex v3 = new Vertex(3, "33");
        Vertex v4 = new Vertex(4, "44");
        Vertex v5 = new Vertex(5, "55");
        Vertex v6 = new Vertex(6, "66");
        Vertex v7 = new Vertex(7, "77");
        Vertex v8 = new Vertex(8, "88");
        Vertex spareV = new Vertex(9, "spare");

        Edge<Vertex> edgeA = new Edge<>(v1, v2, 5);
        Edge<Vertex> edgeB = new Edge<>(v4, v5, 99);
        Edge<Vertex> edgeC = new Edge<>(v7, v4, 128);
        Edge<Vertex> edgeD = new Edge<>(v5, v7, 27);
        Edge<Vertex> edgeE = new Edge<>(v7, v8, 0);
        Edge<Vertex> edgeF = new Edge<>(v8, v6, 69);
        Edge<Vertex> spareE1 = new Edge<>(v1, v8, 1000);
        Edge<Vertex> spareE2 = new Edge<>(spareV, v1, 1000);

        IGraph<Vertex, Edge<Vertex>> ALG = new ALGraph<>();
        ALG.addVertex(v1);
        ALG.addVertex(v2);
        ALG.addVertex(v3);
        ALG.addVertex(v4);
        ALG.addVertex(v5);
        ALG.addVertex(v6);
        ALG.addVertex(v7);
        ALG.addVertex(v8);

        ALG.addEdge(edgeA);
        ALG.addEdge(edgeB);
        ALG.addEdge(edgeC);
        ALG.addEdge(edgeD);
        ALG.addEdge(edgeE);
        ALG.addEdge(edgeF);
        //Testing ALG functionalities
        //Vertices
        assertTrue(ALG.addVertex(spareV));
        assertTrue(ALG.removeVertex(spareV));
        assertFalse(ALG.addVertex(null)); //can't add a null
        assertFalse(ALG.hasVertex(spareV)); //we just deleted this vertex
        assertTrue(ALG.hasVertex(v1));

        //Edges
        assertTrue(ALG.addEdge(spareE1));
        assertFalse(ALG.addEdge(spareE1)); //spareE1 already in graph
        assertTrue(ALG.removeEdge(spareE1));
        assertFalse(ALG.removeEdge(spareE1)); //spareE1 no longer exists
        assertFalse(ALG.removeEdge(null)); //can't remove a null
        assertFalse(ALG.addEdge(spareE2)); //spareE2 has invalid vertices
        assertFalse(ALG.addEdge(null)); //can't add a null
        assertTrue(ALG.removeEdge(v4, v5)); //this is edgeB
        assertFalse(ALG.removeEdge(spareV, v2));
        assertTrue(ALG.addEdge(edgeB)); //let's add edgeB back in

        assertFalse(ALG.hasEdge(null)); //can't have null
        assertTrue(ALG.hasEdge(edgeF));
        assertFalse(ALG.hasEdge(spareE2));

        assertEquals(128, ALG.edgeLength(v7, v4));
        assertTrue(ALG.edgeLength(v2, v6) < 0);

    }

    @Test
    public void testAMGraphBasics() { //That is, add, remove, has, edgeLength.
        Vertex v1 = new Vertex(1,"11");
        Vertex v2 = new Vertex(2,"22");
        Vertex v3 = new Vertex(3,"33");
        Vertex v4 = new Vertex(4,"44");
        Vertex v5 = new Vertex(5,"55");
        Vertex v6 = new Vertex(6,"66");
        Vertex v7 = new Vertex(7,"77");
        Vertex v8 = new Vertex(8,"88");
        Vertex spareV = new Vertex(9, "spare");

        Edge<Vertex> edgeA = new Edge<>(v1, v2, 5);
        Edge<Vertex> edgeB = new Edge<>(v4, v5, 99);
        Edge<Vertex> edgeC = new Edge<>(v7, v4, 128);
        Edge<Vertex> edgeD = new Edge<>(v5, v7, 27);
        Edge<Vertex> edgeE = new Edge<>(v7, v8, 0);
        Edge<Vertex> edgeF = new Edge<>(v8, v6, 69);
        Edge<Vertex> spareE1 = new Edge<>(v1, v8, 1000);
        Edge<Vertex> spareE2 = new Edge<>(spareV, v1, 1000);

        IGraph<Vertex, Edge<Vertex>> AMG = new AMGraph<>(9);
        AMG.addVertex(v1); AMG.addVertex(v2); AMG.addVertex(v3); AMG.addVertex(v4);
        AMG.addVertex(v5); AMG.addVertex(v6); AMG.addVertex(v7); AMG.addVertex(v8);

        AMG.addEdge(edgeA); AMG.addEdge(edgeB); AMG.addEdge(edgeC);
        AMG.addEdge(edgeD); AMG.addEdge(edgeE); AMG.addEdge(edgeF);

        //Testing ALG functionalities
        //Vertices
        assertTrue(AMG.addVertex(spareV));
        assertTrue(AMG.removeVertex(spareV));
        assertFalse(AMG.addVertex(null)); //can't add a null
        assertFalse(AMG.hasVertex(spareV)); //we just deleted this vertex
        assertTrue(AMG.hasVertex(v1));

        //Edges
        assertTrue(AMG.addEdge(spareE1));
        assertFalse(AMG.addEdge(spareE1)); //spareE1 already in graph
        assertTrue(AMG.removeEdge(spareE1));
        assertFalse(AMG.removeEdge(spareE1)); //spareE1 no longer exists
        assertFalse(AMG.removeEdge(null)); //can't remove a null
        assertFalse(AMG.addEdge(spareE2)); //spareE2 has invalid vertices
        assertFalse(AMG.addEdge(null)); //can't add a null
        assertTrue(AMG.removeEdge(v4, v5)); //this is edgeB
        assertFalse(AMG.removeEdge(spareV,v2));
        assertTrue(AMG.addEdge(edgeB)); //let's add edgeB back in

        assertFalse(AMG.hasEdge(null)); //can't have null
        assertTrue(AMG.hasEdge(edgeF));
        assertFalse(AMG.hasEdge(spareE2));

        assertEquals(128,AMG.edgeLength(v7, v4));
        assertTrue(AMG.edgeLength(v2, v6) < 0);
    }

    @Test
    public void testALGraphIntermediates() {
        Vertex v1 = new Vertex(1,"11");
        Vertex v2 = new Vertex(2,"22");
        Vertex v3 = new Vertex(3,"33");
        Vertex v4 = new Vertex(4,"44");
        Vertex v5 = new Vertex(5,"55");
        Vertex v6 = new Vertex(6,"66");
        Vertex v7 = new Vertex(7,"77");
        Vertex v8 = new Vertex(8,"88");
        Vertex spareV = new Vertex(9, "spare");

        Edge<Vertex> edgeA = new Edge<>(v1, v2, 5);
        Edge<Vertex> edgeB = new Edge<>(v4, v5, 99);
        Edge<Vertex> edgeC = new Edge<>(v7, v4, 128);
        Edge<Vertex> edgeD = new Edge<>(v5, v7, 27);
        Edge<Vertex> edgeE = new Edge<>(v7, v8, 0);
        Edge<Vertex> edgeF = new Edge<>(v8, v6, 69);
        Edge<Vertex> spareE1 = new Edge<>(v1, v8, 1000);
        Edge<Vertex> spareE2 = new Edge<>(spareV, v1, 1000);

        IGraph<Vertex, Edge<Vertex>> ALG = new ALGraph<>();
        ALG.addVertex(v1); ALG.addVertex(v2); ALG.addVertex(v3); ALG.addVertex(v4);
        ALG.addVertex(v5); ALG.addVertex(v6); ALG.addVertex(v7); ALG.addVertex(v8);
        ALG.addEdge(edgeA); ALG.addEdge(edgeB); ALG.addEdge(edgeC);
        ALG.addEdge(edgeD); ALG.addEdge(edgeE); ALG.addEdge(edgeF);

        IGraph<Vertex, Edge<Vertex>> emptyALG = new ALGraph<>();

        //Testing non-empty ALG functionalities
        assertEquals(5 + 99 + 128 + 27 + 69, ALG.edgeLengthSum());
        assertEquals(Set.of(v1,v2,v3,v4,v5,v6,v7,v8), ALG.allVertices());
        assertEquals(Set.of(edgeA,edgeB,edgeC,edgeD,edgeE,edgeF), ALG.allEdges());
        assertEquals(Set.of(edgeC,edgeD,edgeE), ALG.allEdges(v7));
        assertTrue(ALG.allEdges(v3).isEmpty()); //vertex 3 doesn't have any edges

        Map<Vertex, Edge<Vertex>> neighbourMap = ALG.getNeighbourhood(v8); //should contain edges E and F, vertices 6 and 7.
        assertEquals(edgeF, neighbourMap.getOrDefault(v6,null));
        assertEquals(edgeE, neighbourMap.getOrDefault(v7, null));

        //Testing empty ALG functionalities
        assertEquals(0, emptyALG.edgeLengthSum());
        assertTrue(emptyALG.allVertices().isEmpty());
        assertTrue(emptyALG.allEdges().isEmpty());
        assertTrue(emptyALG.allEdges(spareV).isEmpty());
        assertTrue(emptyALG.getNeighbourhood(spareV).isEmpty());
    }

    @Test
    public void testAMGraphIntermediates() {
        Vertex v1 = new Vertex(1,"11");
        Vertex v2 = new Vertex(2,"22");
        Vertex v3 = new Vertex(3,"33");
        Vertex v4 = new Vertex(4,"44");
        Vertex v5 = new Vertex(5,"55");
        Vertex v6 = new Vertex(6,"66");
        Vertex v7 = new Vertex(7,"77");
        Vertex v8 = new Vertex(8,"88");
        Vertex spareV = new Vertex(9, "spare");

        Edge<Vertex> edgeA = new Edge<>(v1, v2, 5);
        Edge<Vertex> edgeB = new Edge<>(v4, v5, 99);
        Edge<Vertex> edgeC = new Edge<>(v7, v4, 128);
        Edge<Vertex> edgeD = new Edge<>(v5, v7, 27);
        Edge<Vertex> edgeE = new Edge<>(v7, v8, 0);
        Edge<Vertex> edgeF = new Edge<>(v8, v6, 69);
        Edge<Vertex> spareE1 = new Edge<>(v1, v8, 1000);
        Edge<Vertex> spareE2 = new Edge<>(spareV, v1, 1000);

        IGraph<Vertex, Edge<Vertex>> AMG = new AMGraph<>(9);
        AMG.addVertex(v1); AMG.addVertex(v2); AMG.addVertex(v3); AMG.addVertex(v4);
        AMG.addVertex(v5); AMG.addVertex(v6); AMG.addVertex(v7); AMG.addVertex(v8);
        AMG.addEdge(edgeA); AMG.addEdge(edgeB); AMG.addEdge(edgeC);
        AMG.addEdge(edgeD); AMG.addEdge(edgeE); AMG.addEdge(edgeF);

        IGraph<Vertex, Edge<Vertex>> emptyAMG = new AMGraph<>(9);

        //Testing non-empty ALG functionalities
        assertEquals(5 + 99 + 128 + 27 + 69, AMG.edgeLengthSum());
        assertEquals(Set.of(v1,v2,v3,v4,v5,v6,v7,v8), AMG.allVertices());
        assertEquals(Set.of(edgeA,edgeB,edgeC,edgeD,edgeE,edgeF), AMG.allEdges());
        assertEquals(Set.of(edgeC,edgeD,edgeE), AMG.allEdges(v7));
        assertTrue(AMG.allEdges(v3).isEmpty()); //vertex 3 doesn't have any edges

        Map<Vertex, Edge<Vertex>> neighbourMap = AMG.getNeighbourhood(v8); //should contain edges E and F, vertices 6 and 7.
        assertEquals(edgeF, neighbourMap.getOrDefault(v6,null));
        assertEquals(edgeE, neighbourMap.getOrDefault(v7, null));

        //Testing empty ALG functionalities
        assertEquals(0, emptyAMG.edgeLengthSum());
        assertTrue(emptyAMG.allVertices().isEmpty());
        assertTrue(emptyAMG.allEdges().isEmpty());
        assertTrue(emptyAMG.allEdges(spareV).isEmpty());
        assertTrue(emptyAMG.getNeighbourhood(spareV).isEmpty());
    }

    @Test
    public void testALGraphMinimumCost() {
        Vertex v1 = new Vertex(1, "1");
        Vertex v2 = new Vertex(2, "2");
        Vertex v3 = new Vertex(3, "3");
        Vertex v4 = new Vertex(4, "4");
        Vertex v5 = new Vertex(5, "5");
        Vertex v6 = new Vertex(6, "6");
        Vertex v7 = new Vertex(7, "7");
        Vertex v8 = new Vertex(8, "8");
        Vertex v9 = new Vertex(9, "9");

        Edge<Vertex> edgeA = new Edge<>(v1, v2, 12);
        Edge<Vertex> edgeB = new Edge<>(v4, v5, 999);
        Edge<Vertex> edgeC = new Edge<>(v7, v4, 128);
        Edge<Vertex> edgeD = new Edge<>(v5, v7, 2);
        Edge<Vertex> edgeE = new Edge<>(v7, v8, 4);
        Edge<Vertex> edgeF = new Edge<>(v8, v6, 69);
        Edge<Vertex> edgeG = new Edge<>(v1, v4, 10);
        Edge<Vertex> edgeH = new Edge<>(v2, v4, 3);
        Edge<Vertex> edgeI = new Edge<>(v6, v2, 5);
        Edge<Vertex> edgeJ = new Edge<>(v7, v9, 1);
        Edge<Vertex> edgeK = new Edge<>(v3, v5, 1000);


        IGraph<Vertex, Edge<Vertex>> ALG = new ALGraph<>();
        ALG.addVertex(v1);
        ALG.addVertex(v2);
        ALG.addVertex(v3);
        ALG.addVertex(v4);
        ALG.addVertex(v5);
        ALG.addVertex(v6);
        ALG.addVertex(v7);
        ALG.addVertex(v8);
        ALG.addVertex(v9);

        ALG.addEdge(edgeA);
        ALG.addEdge(edgeB);
        ALG.addEdge(edgeC);
        ALG.addEdge(edgeD);
        ALG.addEdge(edgeE);
        ALG.addEdge(edgeF);
        ALG.addEdge(edgeG);
        ALG.addEdge(edgeH);
        ALG.addEdge(edgeI);
        ALG.addEdge(edgeJ);
        ALG.addEdge(edgeK);

        assertEquals(12+5+69,
                ALG.pathCost(ALG.minimumCostPath(v1, v8, PathCostType.SUM_EDGES), PathCostType.SUM_EDGES));
        assertEquals(2,
                ALG.pathCost(ALG.minimumCostPath(v1, v8, PathCostType.MIN_EDGE), PathCostType.MIN_EDGE));
        assertEquals(69,
                ALG.pathCost(ALG.minimumCostPath(v1, v8, PathCostType.MAX_EDGE), PathCostType.MAX_EDGE));
        assertEquals(12,
                ALG.pathCost(ALG.minimumCostPath(v2, v1, PathCostType.SUM_EDGES), PathCostType.SUM_EDGES));
//         this one is causing problems:
        assertEquals(80,
                ALG.pathCost(ALG.minimumCostPath(v5, v2, PathCostType.SUM_EDGES), PathCostType.SUM_EDGES));
        assertEquals(92,
                ALG.pathCost(ALG.minimumCostPath(v5, v1, PathCostType.SUM_EDGES), PathCostType.SUM_EDGES));
        assertEquals(1+4+69+5+12,
                ALG.pathCost(ALG.minimumCostPath(v9, v1, PathCostType.SUM_EDGES), PathCostType.SUM_EDGES));
    }

    @Test
    public void testAMGraphMinimumCost() {
        Vertex v1 = new Vertex(1, "11");
        Vertex v2 = new Vertex(2, "22");
        Vertex v3 = new Vertex(3, "33");
        Vertex v4 = new Vertex(4, "44");
        Vertex v5 = new Vertex(5, "55");
        Vertex v6 = new Vertex(6, "66");
        Vertex v7 = new Vertex(7, "77");
        Vertex v8 = new Vertex(8, "88");
        Vertex v9 = new Vertex(9, "99");

        Edge<Vertex> edgeA = new Edge<>(v1, v2, 12);
        Edge<Vertex> edgeB = new Edge<>(v4, v5, 999);
        Edge<Vertex> edgeC = new Edge<>(v7, v4, 128);
        Edge<Vertex> edgeD = new Edge<>(v5, v7, 2);
        Edge<Vertex> edgeE = new Edge<>(v7, v8, 4);
        Edge<Vertex> edgeF = new Edge<>(v8, v6, 69);
        Edge<Vertex> edgeG = new Edge<>(v1, v4, 10);
        Edge<Vertex> edgeH = new Edge<>(v2, v4, 3);
        Edge<Vertex> edgeI = new Edge<>(v6, v2, 5);
        Edge<Vertex> edgeJ = new Edge<>(v7, v9, 1);
        Edge<Vertex> edgeK = new Edge<>(v3, v5, 1000);


        IGraph<Vertex, Edge<Vertex>> AMG = new ALGraph<>();
        AMG.addVertex(v1);
        AMG.addVertex(v2);
        AMG.addVertex(v3);
        AMG.addVertex(v4);
        AMG.addVertex(v5);
        AMG.addVertex(v6);
        AMG.addVertex(v7);
        AMG.addVertex(v8);
        AMG.addVertex(v9);

        AMG.addEdge(edgeA);
        AMG.addEdge(edgeB);
        AMG.addEdge(edgeC);
        AMG.addEdge(edgeD);
        AMG.addEdge(edgeE);
        AMG.addEdge(edgeF);
        AMG.addEdge(edgeG);
        AMG.addEdge(edgeH);
        AMG.addEdge(edgeI);
        AMG.addEdge(edgeJ);
        AMG.addEdge(edgeK);

        assertEquals(12+5+69,
                AMG.pathCost(AMG.minimumCostPath(v1, v8, PathCostType.SUM_EDGES), PathCostType.SUM_EDGES));
        assertEquals(2,
                AMG.pathCost(AMG.minimumCostPath(v1, v8, PathCostType.MIN_EDGE), PathCostType.MIN_EDGE));
        assertEquals(69,
                AMG.pathCost(AMG.minimumCostPath(v1, v8, PathCostType.MAX_EDGE), PathCostType.MAX_EDGE));
        assertEquals(12,
                AMG.pathCost(AMG.minimumCostPath(v2, v1, PathCostType.SUM_EDGES), PathCostType.SUM_EDGES));
        // this one is causing problems:
        assertEquals(80,
                AMG.pathCost(AMG.minimumCostPath(v5, v2, PathCostType.SUM_EDGES), PathCostType.SUM_EDGES));
        assertEquals(1+4+69+5+12,
                AMG.pathCost(AMG.minimumCostPath(v9, v1, PathCostType.SUM_EDGES), PathCostType.SUM_EDGES));
    }

    @Test
    public void testNeighbourhood() {
        Vertex v1 = new Vertex(1, "1");
        Vertex v2 = new Vertex(2, "2");
        Vertex v3 = new Vertex(3, "3");
        Vertex v4 = new Vertex(4, "4");
        Vertex v5 = new Vertex(5, "5");
        Vertex v6 = new Vertex(6, "6");
        Vertex v7 = new Vertex(7, "7");
        Vertex v8 = new Vertex(8, "8");
        Vertex v9 = new Vertex(9, "9");

        Edge<Vertex> edgeA = new Edge<>(v1, v2, 1);
        Edge<Vertex> edgeB = new Edge<>(v1, v3, 1);
        Edge<Vertex> edgeC = new Edge<>(v1, v4, 2);
        Edge<Vertex> edgeD = new Edge<>(v1, v5, 3);
        Edge<Vertex> edgeE = new Edge<>(v1, v6, 4);
        Edge<Vertex> edgeF = new Edge<>(v1, v7, 4);
        Edge<Vertex> edgeG = new Edge<>(v2, v7, 3);
        Edge<Vertex> edgeH = new Edge<>(v4, v8, 2);
        Edge<Vertex> edgeI = new Edge<>(v4, v9, 1);
        Edge<Vertex> edgeJ = new Edge<>(v3, v6, 1);

        IGraph<Vertex, Edge<Vertex>> ALG = new ALGraph<>();
        IGraph<Vertex, Edge<Vertex>> AMG = new AMGraph<>(9);

        ALG.addVertex(v1);
        ALG.addVertex(v2);
        ALG.addVertex(v3);
        ALG.addVertex(v4);
        ALG.addVertex(v5);
        ALG.addVertex(v6);
        ALG.addVertex(v7);
        ALG.addVertex(v8);
        ALG.addVertex(v9);

        AMG.addVertex(v1);
        AMG.addVertex(v2);
        AMG.addVertex(v3);
        AMG.addVertex(v4);
        AMG.addVertex(v5);
        AMG.addVertex(v6);
        AMG.addVertex(v7);
        AMG.addVertex(v8);
        AMG.addVertex(v9);

        ALG.addEdge(edgeA);
        ALG.addEdge(edgeB);
        ALG.addEdge(edgeC);
        ALG.addEdge(edgeD);
        ALG.addEdge(edgeE);
        ALG.addEdge(edgeF);
        ALG.addEdge(edgeG);
        ALG.addEdge(edgeH);
        ALG.addEdge(edgeI);
        ALG.addEdge(edgeJ);

        AMG.addEdge(edgeA);
        AMG.addEdge(edgeB);
        AMG.addEdge(edgeC);
        AMG.addEdge(edgeD);
        AMG.addEdge(edgeE);
        AMG.addEdge(edgeF);
        AMG.addEdge(edgeG);
        AMG.addEdge(edgeH);
        AMG.addEdge(edgeI);
        AMG.addEdge(edgeJ);

        Map<Vertex, Edge> expectedMap = new HashMap<>();
        expectedMap.put(v2, edgeA);
        expectedMap.put(v3, edgeB);
        expectedMap.put(v4, edgeC);
        expectedMap.put(v5, edgeD);
        expectedMap.put(v6, edgeJ);
        expectedMap.put(v9, edgeI);

        assertEquals(expectedMap, ALG.getNeighbourhood(v1, 3));
        assertEquals(expectedMap, AMG.getNeighbourhood(v1, 3));
    }

    @Test
    public void testSubmergedBasic() {
        double[][] terrain = {{1, 1, 2}};
        GridLocation[] waterSources = {new GridLocation(0,0)};
        boolean[][] expSubmerged0 = {{false, false, false}};
        boolean[][] expSubmerged1 = {{true, true, false}};
        assertArrayEquals(expSubmerged0, isSubmerged(terrain, waterSources, 0));
        assertArrayEquals(expSubmerged1, isSubmerged(terrain, waterSources, 1));
    }

    @Test
    public void testSubmerged() {
        double[][] terrain = {
                {0, 1, 2, 1},
                {-69, 1, 999, 3},
                {2, 1, -10, 1},
                {-100, 2, 3, 0}
        };
        GridLocation[] waterSources = {new GridLocation(0, 0)};
        boolean[][] expSubmerged0 = {
                {true, false, false, false},
                {true, false, false, false},
                {false, false, false, false},
                {false, false, false, false}
        };
        boolean[][] expSubmerged1 = {
                {true, true, false, false},
                {true, true, false, false},
                {false, true, true, true},
                {false, false, false, true}
        };
        boolean[][] expSubmerged2 = {
                {true, true, true, true},
                {true, true, false, false},
                {true, true, true, true},
                {true, true, false, true}
        };
        boolean[][] expSubmerged3 = {
                {true, true, true, true},
                {true, true, false, true},
                {true, true, true, true},
                {true, true, true, true}
        };
        assertArrayEquals(expSubmerged0, isSubmerged(terrain, waterSources, 0));
        assertArrayEquals(expSubmerged1, isSubmerged(terrain, waterSources, 1));
        assertArrayEquals(expSubmerged2, isSubmerged(terrain, waterSources, 2));
        assertArrayEquals(expSubmerged3, isSubmerged(terrain, waterSources, 3));
    }

    @Test
    public void testSubmerged2() {
        double[][] terrain = {
                {0, 1, 2, 1},
                {0, 1, 3, 3},
                {1, 1, 1, 1},
                {2, 2, 3, 0}
        };
        GridLocation[] waterSources = {new GridLocation(0, 0),
                                       new GridLocation(0, 3)};
        boolean[][] expSubmerged0 = {
                {true, false, false, false},
                {true, false, false, false},
                {false, false, false, false},
                {false, false, false, false}
        };
        boolean[][] expSubmerged1 = {
                {true, true, false, true},
                {true, true, false, false},
                {true, true, true, true},
                {false, false, false, true}
        };
        boolean[][] expSubmerged2 = {
                {true, true, true, true},
                {true, true, false, false},
                {true, true, true, true},
                {true, true, false, true}
        };
        boolean[][] expSubmerged3 = {
                {true, true, true, true},
                {true, true, true, true},
                {true, true, true, true},
                {true, true, true, true}
        };
        assertArrayEquals(expSubmerged0, isSubmerged(terrain, waterSources, 0));
        assertArrayEquals(expSubmerged1, isSubmerged(terrain, waterSources, 1));
        assertArrayEquals(expSubmerged2, isSubmerged(terrain, waterSources, 2));
        assertArrayEquals(expSubmerged3, isSubmerged(terrain, waterSources, 3));
    }

    @Test
    public void testDanger(){
        double[][] terrain = {
                {0, 1, 2, 1},
                {0, 1, 3, 3},
                {1, 1, 1, 1},
                {2, 2, 3, 0}
        };
        GridLocation[] waterSources = {new GridLocation(0, 0)};
        double[][] expected = {
                {0, 1, 2, 2},
                {0, 1, 3, 3},
                {1, 1, 1, 1},
                {2, 2, 3, 1}
        };
        assertArrayEquals(expected, dangerLevel(terrain, waterSources));
    }

    @Test
    public void testDanger2(){
        double[][] terrain = {
                {0, 1, 2, 1},
                {0, 1, 3, 3},
                {1, 1, 1, 1},
                {2, 2, 3, 0}
        };
        GridLocation[] waterSources = {new GridLocation(0, 0),
                                       new GridLocation(0, 3)};
        double[][] expected = {
                {0, 1, 2, 1},
                {0, 1, 3, 3},
                {1, 1, 1, 1},
                {2, 2, 3, 1}
        };
        assertArrayEquals(expected, dangerLevel(terrain, waterSources));
    }

    @Test
    public void testDiameterAndCenterALGraph1() {
        Vertex v1 = new Vertex(1, "1");
        Vertex v2 = new Vertex(2, "2");
        Vertex v3 = new Vertex(3, "3");
        Vertex v4 = new Vertex(4, "4");
        Vertex v5 = new Vertex(5, "5");
        Vertex v6 = new Vertex(6, "6");
        Vertex v7 = new Vertex(7, "7");
        Vertex v8 = new Vertex(8, "8");
        Vertex v9 = new Vertex(9, "9");

        Edge<Vertex> edgeA = new Edge<>(v1, v2, 12);
        Edge<Vertex> edgeB = new Edge<>(v4, v5, 999);
        Edge<Vertex> edgeC = new Edge<>(v7, v4, 128);
        Edge<Vertex> edgeD = new Edge<>(v5, v7, 2);
        Edge<Vertex> edgeE = new Edge<>(v7, v8, 4);
        Edge<Vertex> edgeF = new Edge<>(v8, v6, 69);
        Edge<Vertex> edgeG = new Edge<>(v1, v4, 10);
        Edge<Vertex> edgeH = new Edge<>(v2, v4, 3);
        Edge<Vertex> edgeI = new Edge<>(v6, v2, 5);
        Edge<Vertex> edgeJ = new Edge<>(v7, v9, 1);
        Edge<Vertex> edgeK = new Edge<>(v3, v5, 1000);

        IGraph<Vertex, Edge<Vertex>> ALG = new ALGraph<>();
        ALG.addVertex(v1); ALG.addVertex(v2); ALG.addVertex(v3); ALG.addVertex(v4); ALG.addVertex(v5);
        ALG.addVertex(v6); ALG.addVertex(v7); ALG.addVertex(v8);  ALG.addVertex(v9);

        ALG.addEdge(edgeA); ALG.addEdge(edgeB); ALG.addEdge(edgeC); ALG.addEdge(edgeD); ALG.addEdge(edgeE); ALG.addEdge(edgeF);
        ALG.addEdge(edgeG); ALG.addEdge(edgeH); ALG.addEdge(edgeI); ALG.addEdge(edgeJ); ALG.addEdge(edgeK);

        assertEquals(1092, ALG.getDiameter(PathCostType.SUM_EDGES));
        assertEquals(1000, ALG.getDiameter(PathCostType.MAX_EDGE));
        assertEquals(1000, ALG.getDiameter(PathCostType.MIN_EDGE));

        assertEquals(v5, ALG.getCenter(PathCostType.SUM_EDGES));
        assertEquals(v8, ALG.getCenter(PathCostType.MAX_EDGE));
        assertEquals(v9, ALG.getCenter(PathCostType.MIN_EDGE));

    }

    /*@Test
    public void testDiameterAndCenterALGraph2() {
        Vertex v1 = new Vertex(1, "11");
        Vertex v2 = new Vertex(2, "22");
        Vertex v3 = new Vertex(3, "33");
        Vertex v4 = new Vertex(4, "44");
        Vertex v5 = new Vertex(5, "55");
        Vertex v6 = new Vertex(6, "66");
        Vertex v7 = new Vertex(7, "77");
        Vertex v8 = new Vertex(8, "88");

        Edge<Vertex> edgeA = new Edge<>(v1, v2, 5);
        Edge<Vertex> edgeB = new Edge<>(v4, v5, 99);
        Edge<Vertex> edgeC = new Edge<>(v7, v4, 128);
        Edge<Vertex> edgeD = new Edge<>(v5, v7, 27);
        Edge<Vertex> edgeE = new Edge<>(v7, v8, 0);
        Edge<Vertex> edgeF = new Edge<>(v8, v6, 69);

        IGraph<Vertex, Edge<Vertex>> ALG = new ALGraph<>();
        ALG.addVertex(v1);
        ALG.addVertex(v2);
        ALG.addVertex(v3);
        ALG.addVertex(v4);
        ALG.addVertex(v5);
        ALG.addVertex(v6);
        ALG.addVertex(v7);
        ALG.addVertex(v8);

        ALG.addEdge(edgeA);
        ALG.addEdge(edgeB);
        ALG.addEdge(edgeC);
        ALG.addEdge(edgeD);
        ALG.addEdge(edgeE);
        ALG.addEdge(edgeF);

        assertEquals(128+69, ALG.getDiameter(PathCostType.SUM_EDGES));
        assertEquals(128, ALG.getDiameter(PathCostType.MAX_EDGE));
        assertEquals(128, ALG.getDiameter(PathCostType.MIN_EDGE));
    }*/
    @Test
    public void testDiameterAndCenterAMGraph1() {
        Vertex v1 = new Vertex(1, "1");
        Vertex v2 = new Vertex(2, "2");
        Vertex v3 = new Vertex(3, "3");
        Vertex v4 = new Vertex(4, "4");
        Vertex v5 = new Vertex(5, "5");
        Vertex v6 = new Vertex(6, "6");
        Vertex v7 = new Vertex(7, "7");
        Vertex v8 = new Vertex(8, "8");
        Vertex v9 = new Vertex(9, "9");

        Edge<Vertex> edgeA = new Edge<>(v1, v2, 12);
        Edge<Vertex> edgeB = new Edge<>(v4, v5, 999);
        Edge<Vertex> edgeC = new Edge<>(v7, v4, 128);
        Edge<Vertex> edgeD = new Edge<>(v5, v7, 2);
        Edge<Vertex> edgeE = new Edge<>(v7, v8, 4);
        Edge<Vertex> edgeF = new Edge<>(v8, v6, 69);
        Edge<Vertex> edgeG = new Edge<>(v1, v4, 10);
        Edge<Vertex> edgeH = new Edge<>(v2, v4, 3);
        Edge<Vertex> edgeI = new Edge<>(v6, v2, 5);
        Edge<Vertex> edgeJ = new Edge<>(v7, v9, 1);
        Edge<Vertex> edgeK = new Edge<>(v3, v5, 1000);

        IGraph<Vertex, Edge<Vertex>> AMG = new ALGraph<>();
        AMG.addVertex(v1); AMG.addVertex(v2); AMG.addVertex(v3); AMG.addVertex(v4); AMG.addVertex(v5);
        AMG.addVertex(v6); AMG.addVertex(v7); AMG.addVertex(v8);  AMG.addVertex(v9);

        AMG.addEdge(edgeA); AMG.addEdge(edgeB); AMG.addEdge(edgeC); AMG.addEdge(edgeD); AMG.addEdge(edgeE); AMG.addEdge(edgeF);
        AMG.addEdge(edgeG); AMG.addEdge(edgeH); AMG.addEdge(edgeI); AMG.addEdge(edgeJ); AMG.addEdge(edgeK);

        assertEquals(1092, AMG.getDiameter(PathCostType.SUM_EDGES));
        assertEquals(1000, AMG.getDiameter(PathCostType.MAX_EDGE));
        assertEquals(1000, AMG.getDiameter(PathCostType.MIN_EDGE));

        assertEquals(v5, AMG.getCenter(PathCostType.SUM_EDGES));
        assertEquals(v8, AMG.getCenter(PathCostType.MAX_EDGE));
        assertEquals(v9, AMG.getCenter(PathCostType.MIN_EDGE));
    }

    /*@Test
    public void testDiameterAndCenterAMGraph2() {
        Vertex v1 = new Vertex(1, "11");
        Vertex v2 = new Vertex(2, "22");
        Vertex v3 = new Vertex(3, "33");
        Vertex v4 = new Vertex(4, "44");
        Vertex v5 = new Vertex(5, "55");
        Vertex v6 = new Vertex(6, "66");
        Vertex v7 = new Vertex(7, "77");
        Vertex v8 = new Vertex(8, "88");

        Edge<Vertex> edgeA = new Edge<>(v1, v2, 5);
        Edge<Vertex> edgeB = new Edge<>(v4, v5, 99);
        Edge<Vertex> edgeC = new Edge<>(v7, v4, 128);
        Edge<Vertex> edgeD = new Edge<>(v5, v7, 27);
        Edge<Vertex> edgeE = new Edge<>(v7, v8, 0);
        Edge<Vertex> edgeF = new Edge<>(v8, v6, 69);

        IGraph<Vertex, Edge<Vertex>> AMG = new ALGraph<>();
        AMG.addVertex(v1);
        AMG.addVertex(v2);
        AMG.addVertex(v3);
        AMG.addVertex(v4);
        AMG.addVertex(v5);
        AMG.addVertex(v6);
        AMG.addVertex(v7);
        AMG.addVertex(v8);

        AMG.addEdge(edgeA);
        AMG.addEdge(edgeB);
        AMG.addEdge(edgeC);
        AMG.addEdge(edgeD);
        AMG.addEdge(edgeE);
        AMG.addEdge(edgeF);

        assertEquals(128+69, AMG.getDiameter(PathCostType.SUM_EDGES));
        assertEquals(128, AMG.getDiameter(PathCostType.MAX_EDGE));
        assertEquals(128, AMG.getDiameter(PathCostType.MIN_EDGE));
    }*/
}
