package es.usc.citius.hipster.graph;

public class DirectedEdge<V,E> implements GraphEdge<V,E> {

    private Pair<V> vertices;
    private E value;

    public DirectedEdge(V vertex1, V vertex2, E value) {
        this.vertices = new Pair<V>(vertex1, vertex2);
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
        return Type.DIRECTED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DirectedEdge<?, ?> that = (DirectedEdge<?, ?>) o;

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
