package es.usc.citius.hipster.graph;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;


public class HashBasedHipsterGraphTest {
    protected HashBasedHipsterGraph graph;
    protected int size = 10;

    public static HashBasedHipsterGraph createStarGraph(int vertices){
        HashBasedHipsterGraph g = new HashBasedHipsterGraph();
        for(int i = 0; i < vertices; i++){
            g.add("v"+i);
            for(int j=i-1; j>=0; j--){
                g.connect("v"+i, "v"+j, Math.random());
            }
        }
        return g;
    }

    @Before
    public void setUp(){
        graph = createStarGraph(size);
    }

    private void testGraphEdges(){
        Iterable<GraphEdge> edges = graph.edges();
        int countEdgesV1 = 0;
        int countAllEdges = 0;
        for(GraphEdge e : edges){
            if (e.getVertex1().equals("v1") || e.getVertex2().equals("v1")) countEdgesV1++;
            countAllEdges++;
        }
        assertEquals((size-1)*2, countEdgesV1);
        assertEquals(size*(size-1), countAllEdges);
    }

    @Test
    public void testEdges() throws Exception {
        testGraphEdges();
    }

    @Test
    public void testEdgesWithDisconnectedVertices() throws Exception {
        graph.add("X");
        graph.add("Y");
        testGraphEdges();
    }

    @Test
    public void testAdd() throws Exception {
        graph.add("X");
        Set vertices = Sets.newHashSet(graph.vertices());
        assertTrue(vertices.contains("X"));
        assertTrue(vertices.size()==size+1);
    }

    @Test
    public void testRemove() throws Exception {
        graph.remove("v1");
        assertFalse(Sets.newHashSet(graph.vertices()).contains("v1"));
    }

    @Test
    public void testRemoveAndCheckEdges() throws Exception {
        graph.remove("v1");
        assertFalse(Sets.newHashSet(graph.vertices()).contains("v1"));
        Iterable<GraphEdge> edges = graph.edges();
        int countEdges = 0;
        for(GraphEdge e : edges){
            assertFalse(e.getVertex1().equals("v1") || e.getVertex2().equals("v1"));
            countEdges++;
        }
        assertEquals((size-1)*(size-2), countEdges);
    }

    @Test
    public void testConnect() throws Exception {
        graph.connect("X","Y",1.0d);
        assertTrue(Sets.newHashSet(graph.vertices()).contains("X"));
        assertTrue(Sets.newHashSet(graph.vertices()).contains("Y"));
    }


    @Test
    public void testVertices() throws Exception {
        Set vertices = Sets.newHashSet(graph.vertices());
        assertEquals(size, vertices.size());
    }

    @Test
    public void testEdgesOf() throws Exception {
        Set edges = Sets.newHashSet(graph.edgesOf("v1"));
        assertEquals(size-1, edges.size());
    }


}