package es.usc.citius.hipster.util;


import es.usc.citius.hipster.graph.GraphEdge;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class Iterators {

    public static <T,E> Iterator<E> map(final Iterator<T> it, final Function<T,E> mapf){
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public E next() {
                return mapf.apply(it.next());
            }
        };
    }

    public static <T> Iterator<T> filter(final Iterator<T> it, final Function<T, Boolean> condition) {
        return new Iterator<T>() {
            private T next = null;

            private T nextFiltered() {
                T nextElem = null;
                while (it.hasNext()) {
                    T elem = it.next();
                    if (condition.apply(elem)) {
                        nextElem = elem;
                        break;
                    }
                }
                return nextElem;
            }

            @Override
            public boolean hasNext() {
                if (next != null) return true;
                // Preload the next edge
                next = nextFiltered();
                return next != null;
            }

            @Override
            public T next() {
                if (next != null) {
                    T elem = next;
                    next = null;
                    return elem;
                }
                return nextFiltered();
            }
        };
    }


    public static <E> Iterator<E> empty() {
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public E next() {
                throw new NoSuchElementException("Iterator is empty");
            }
        };
    }
}
