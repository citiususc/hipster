package es.usc.citius.hipster.util;


import java.util.Iterator;
import java.util.NoSuchElementException;

public final class Iterators {
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

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        };
    }
}
