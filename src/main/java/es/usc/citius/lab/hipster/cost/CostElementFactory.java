package es.usc.citius.lab.hipster.cost;

public interface CostElementFactory<E extends CostElement<E>> {
	E identity();
	E max();
}
