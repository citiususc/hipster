package es.usc.citius.hipster.graph;


public class UndirectedEdge<V,E> implements GraphEdge<V,E> {

    private UnorderedPair<V> vertices;
    private E value;

    public UndirectedEdge(V vertex1, V vertex2, E value) {
        this.vertices = new UnorderedPair<V>(vertex1, vertex2);
        this.value = value;
    }

    @Override
    public V getVertex1() {
        return vertices.getE1();
    }

    @Override
    public V getVertex2() {
        return vertices.getE2();
    }

    @Override
    public E getEdgeValue() {
        return value;
    }

    @Override
    public Type getType() {
        return Type.UNDIRECTED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UndirectedEdge<?, ?> that = (UndirectedEdge<?, ?>) o;

        if (!vertices.equals(that.vertices)) return false;
        return !(value != null ? !value.equals(that.value) : that.value != null);

    }

    @Override
    public int hashCode() {
        int result = vertices.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
