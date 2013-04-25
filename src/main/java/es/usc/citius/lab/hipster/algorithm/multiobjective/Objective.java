package es.usc.citius.lab.hipster.algorithm.multiobjective;

public interface Objective<E extends Comparable<E>> {
	E create();
	E maxValue();
	E minValue();
	boolean isMinimizable();
}
