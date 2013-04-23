package es.usc.citius.lab.hipster.function;

public interface Operator<E extends Comparable<E>> {
	E apply(E a, E b);
}
