package es.usc.citius.lab.hipster.cost;

public interface CostElement<E extends CostElement<E>> extends Comparable<E> {
	E x(E e);
}
