package es.usc.citius.hipster.graph;

public class UnorderedPair<E> {
    private E e1, e2;

    public UnorderedPair(E e1, E e2) {
        if (e1 == null) throw new IllegalArgumentException("First element cannot be null");
        this.e1 = e1;
        if (e2 == null) throw new IllegalArgumentException("Second element cannot be null");
        this.e2 = e2;
    }

    public boolean contains(Object vertex){
        return e1.equals(vertex) || e2.equals(vertex);
    }

    public E getE1() {
        return e1;
    }

    public void setE1(E e1) {
        this.e1 = e1;
    }

    public E getE2() {
        return e2;
    }

    public void setE2(E e2) {
        this.e2 = e2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnorderedPair<?> that = (UnorderedPair<?>) o;
        return that.contains(e1) && that.contains(e2);
    }

    @Override
    public int hashCode() {
        return e1.hashCode() + e2.hashCode();
    }

    @Override
    public String toString() {
        return "(" + e1 + ", " + e2 + ")";
    }
}
