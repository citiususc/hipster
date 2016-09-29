package es.usc.citius.lab.hipster.collections;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Implementation of a {@link java.util.Queue} backed by a {@link java.util.LinkedHashSet}
 *
 * @param <S> the type of elements held in this collection
 */
public class HashQueue<S> extends AbstractQueue<S> {

    private final Set<S> elements = new LinkedHashSet<S>();
    private S first = null;

    @Override
    public boolean offer(S e) {
        elements.add(e);
        if (first == null) {
            first = e;
        }
        return true;
    }

    @Override
    public S poll() {
        // Remove the first element
        elements.remove(first);
        S out = first;
        // Reasign first
        first = (elements.isEmpty()) ? null : elements.iterator().next();
        return out;
    }

    @Override
    public S peek() {
        return first;
    }

    @Override
    public Iterator<S> iterator() {
        return elements.iterator();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean contains(Object o) {
        return this.elements.contains(o);
    }

}
