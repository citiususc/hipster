package es.usc.citius.hipster.util;


import java.util.Iterator;


/**
 * This class contains a few functional methods to lazily process iterables and iterators.
 * Required due to the removal of Guava dependencies (issue #125 https://github.com/citiususc/hipster/issues/125)
 */
public final class F {

    public static <T,E> Iterable<E> map(final Iterable<T> it, final Function<T,E> mapf){
        return new Iterable<E>() {
            @Override
            public Iterator<E> iterator() {
                return map(it.iterator(), mapf);
            }
        };
    }

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

    public static <T> Iterable<T> filter(final Iterable<T> it, final Function<T, Boolean> condition) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return filter(it.iterator(), condition);
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

}
