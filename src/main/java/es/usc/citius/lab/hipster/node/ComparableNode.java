package es.usc.citius.lab.hipster.node;

/**
 * Interface that defines a comparable node.
 *
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @param <S> class defining the state
 * @since 26/03/2013
 * @version 1.0
 */
public interface ComparableNode<S> extends Node<S>, Comparable<ComparableNode<S>> {
}
