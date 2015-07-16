package es.usc.citius.hipster.graph;


public class Pair<E> {
    private E e1, e2;

    public Pair(E e1, E e2) {
        if (e1 == null) throw new IllegalArgumentException("First element cannot be null");
        this.e1 = e1;
        if (e2 == null) throw new IllegalArgumentException("Second element cannot be null");
        this.e2 = e2;
    }

    public E _1() {
        return e1;
    }

    public E _2() {
        return e2;
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

        Pair<?> pair = (Pair<?>) o;

        if (!e1.equals(pair.e1)) return false;
        return e2.equals(pair.e2);

    }

    @Override
    public int hashCode() {
        int result = e1.hashCode();
        result = 31 * result + e2.hashCode();
        return result;
    }
}
