package es.usc.citius.hipster.graph;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;


public class HashBasedHipsterDirectedGraphTest {

    protected HipsterDirectedGraph graph;
    protected int size = 10;

    @Before
    public void setUp(){
        graph = createStarGraph(size);
    }

    protected HashBasedHipsterDirectedGraph createStarGraph(int vertices){
        HashBasedHipsterDirectedGraph g = new HashBasedHipsterDirectedGraph();
        for(int i = 0; i < vertices; i++){
            g.add("v"+i);
            for(int j=0; j<i; j++){
                g.connect("v"+j, "v"+i, Math.random());
            }
        }
        return g;
    }

    @Test
    public void testOutgoingEdgesOf() throws Exception {
        for(int i=0; i<size; i++) {
            Set edges = Sets.newHashSet(graph.outgoingEdgesOf("v"+i));
            assertEquals(size-(i+1), edges.size());
        }
    }

    @Test
    public void testIncomingEdgesOf() throws Exception {
        for(int i=0; i<size; i++) {
            Set edges = Sets.newHashSet(graph.incomingEdgesOf("v"+i));
            assertEquals(i, edges.size());
        }
    }

    @Test
    public void testEdges() throws Exception {
        Set edges = Sets.newHashSet(graph.edges());
        assertEquals(size*(size-1)/2, edges.size());
    }
}