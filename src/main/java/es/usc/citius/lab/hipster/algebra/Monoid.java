package es.usc.citius.lab.hipster.algebra;

public interface Monoid<E extends Monoid<E>> extends Comparable<E> {

	/**
	 * A monoid element must implement an associative binary operation over
	 * the monoid set.
	 * 
	 * @param e Monoid
	 * @return result of applying the associative binary operation between this element and {@code e}.
	 */
	E operate(E e);
	

}
