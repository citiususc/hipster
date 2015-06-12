package es.usc.citius.hipster.graph;


public interface HipsterMutableGraph<V,E> extends HipsterGraph<V,E> {
    boolean add(V vertex);
    boolean remove(V vertex);
    GraphEdge<V,E> connect(V vertex1, V vertex2, E edgeValue);
}
