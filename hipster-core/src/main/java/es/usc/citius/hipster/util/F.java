package es.usc.citius.hipster.util;


import java.util.Iterator;


/**
 * This class contains a very limited set of functions to process iterables and iterators in a lazy way.
 * Guava / Java 8 is not an option due to current size / compatibility restrictions.
 * Required since the removal of Guava dependencies (issue #125 https://github.com/citiususc/hipster/issues/125)
 * NOTE: This class may be removed in future versions to take advantage of Java 8 functional Streams
 *
 * Pure functional programmers, please forgive us for this crime
 *
 * @author Pablo Rodríguez Mier
 */
public final class F {

    private F() {}

    public static <T,E> Iterable<E> map(final Iterable<T> it, final Function<? super T,? extends E> mapf){
        return new Iterable<E>() {
            @Override
            public Iterator<E> iterator() {
                return map(it.iterator(), mapf);
            }
        };
    }

    public static <T,E> Iterator<E> map(final Iterator<T> it, final Function<? super T,? extends E> mapf){
        return new Iterators.AbstractIterator<E>() {
            @Override
            protected E computeNext() {
                if (it.hasNext()){
                    return mapf.apply(it.next());
                }
                return null;
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
        return new Iterators.AbstractIterator<T>() {
            @Override
            protected T computeNext() {
                while(it.hasNext()){
                    T next = it.next();
                    if (condition.apply(next)){
                        return next;
                    }
                }
                return null;
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
        return new Iterators.AbstractIterator<T>() {
            Iterator<Iterator<? extends T>> mapIt = map(it, mapf);
            Iterator<? extends T> current = mapIt.hasNext() ? mapIt.next() : Iterators.<T>empty();

            @Override
            protected T computeNext() {
                if (current.hasNext()) return current.next();
                if (mapIt.hasNext()){
                    current = mapIt.next();
                    return computeNext();
                }
                return null;
            }
        };
    }

}
