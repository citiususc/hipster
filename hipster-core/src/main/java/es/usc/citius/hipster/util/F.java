package es.usc.citius.hipster.util;


import java.util.Iterator;


/**
 * This class contains a very limited set of functional methods to process iterables and iterators.
 * Required due to the removal of Guava dependencies (issue #125 https://github.com/citiususc/hipster/issues/125)
 * NOTE: This class may be removed in future versions to take advantage of Java 8 functional Streams
 *
 * @author Pablo Rodríguez Mier
 */
public final class F {

    public static <T,E> Iterable<E> map(final Iterable<T> it, final Function<? super T,? extends E> mapf){
        return new Iterable<E>() {
            @Override
            public Iterator<E> iterator() {
                return map(it.iterator(), mapf);
            }
        };
    }

    public static <T,E> Iterator<E> map(final Iterator<T> it, final Function<? super T,? extends E> mapf){
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public E next() {
                return mapf.apply(it.next());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        };
    }

    public static <T> Iterable<T> filter(final Iterable<T> it, final Function<? super T, Boolean> condition) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return filter(it.iterator(), condition);
            }
        };
    }

    public static <T> Iterator<T> filter(final Iterator<T> it, final Function<? super T, Boolean> condition) {
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

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        };
    }

    public static <E,T> Iterable<T> flatMap(final Iterable<E> it, final Function<? super E, ? extends Iterable<? extends T>> mapf){
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return flatMap(it.iterator(), new Function<E, Iterator<? extends T>>() {
                    @Override
                    public Iterator<? extends T> apply(E input) {
                        return mapf.apply(input).iterator();
                    }
                });
            }
        };
    }

    public static <E,T> Iterator<T> flatMap(final Iterator<E> it, final Function<? super E, ? extends Iterator<? extends T>> mapf){
        return new Iterator<T>() {
            private Iterator<Iterator<? extends T>> mapIt = map(it, mapf);
            private Iterator<? extends T> current = mapIt.hasNext() ? mapIt.next() : Iterators.<T>empty();
            private T t;

            private T loadNext(){
                if (current.hasNext()) return current.next();
                if (mapIt.hasNext()){
                    current = mapIt.next();
                    return loadNext();
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                if (t == null) t = loadNext();
                return t != null;
            }

            @Override
            public T next() {
                if (t != null) {
                    T next = t;
                    t = null; // consumed
                    return next;
                } else {
                    return loadNext();
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        };
    }

}
