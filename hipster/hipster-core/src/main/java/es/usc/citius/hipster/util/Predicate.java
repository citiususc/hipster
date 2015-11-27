package es.usc.citius.hipster.util;

/**
 * Definition of predictcate to be used with search iterators implementing the
 * class {@link es.usc.citius.hipster.algorithm.Algorithm}. A predicate can
 * be used to define the stop condition of the algorithm
 * when using the method {@link es.usc.citius.hipster.algorithm.Algorithm#se}
 *
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public interface Predicate<T extends Object> {

    public boolean apply(T input);

}
