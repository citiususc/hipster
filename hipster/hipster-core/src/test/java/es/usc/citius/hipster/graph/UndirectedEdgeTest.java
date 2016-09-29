package es.usc.citius.hipster.graph;

import org.junit.Test;

import static org.junit.Assert.*;

public class UndirectedEdgeTest {

    @Test
    public void testEqualsWithUndirectedEdgeSwapped() throws Exception {
        UndirectedEdge e1 = new UndirectedEdge("a", "b", 1);
        UndirectedEdge e2 = new UndirectedEdge("b", "a", 1);
        assertEquals(e1, e2);
    }

    @Test
    public void testEqualsWithUndirectedEdge() throws Exception {
        UndirectedEdge e1 = new UndirectedEdge("a", "b", 1);
        UndirectedEdge e2 = new UndirectedEdge("a", "b", 1);
        assertEquals(e1, e2);
    }

    @Test
    public void testNotEqualsWithUndirectedEdge() throws Exception {
        UndirectedEdge e1 = new UndirectedEdge("a", "b", 1);
        UndirectedEdge e2 = new UndirectedEdge("a", "c", 1);
        assertNotEquals(e1, e2);
    }
}