package es.usc.citius.hipster.util;


import java.util.Iterator;
import java.util.NoSuchElementException;

public final class Iterators {

    public static abstract class AbstractIterator<E> implements Iterator<E> {
        protected E current;

        protected AbstractIterator(){}

        protected abstract E computeNext();

        @Override
        public boolean hasNext() {
            if (current == null){
                current = computeNext();
            }
            return current != null;
        }

        @Override
        public E next() {
            if (current != null) {
                E next = current;
                current = null;
                return next;
            }
            return computeNext();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }
    }

    public static <E> Iterator<E> empty() {
        return new AbstractIterator<E>() {
            @Override
            protected E computeNext() {
                return null;
            }
        };
    }
}
