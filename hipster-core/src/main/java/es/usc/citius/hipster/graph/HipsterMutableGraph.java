package es.usc.citius.hipster.graph;


import java.util.Set;

public interface HipsterMutableGraph<V,E> extends HipsterGraph<V,E> {
    boolean add(V vertex);
    Set<V> add(V... vertices);
    boolean remove(V vertex);
    Set<V> remove(V... vertices);
    GraphEdge<V,E> connect(V vertex1, V vertex2, E edgeValue);
}
