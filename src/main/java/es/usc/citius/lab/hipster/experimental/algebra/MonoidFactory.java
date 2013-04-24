package es.usc.citius.lab.hipster.experimental.algebra;

public interface MonoidFactory<E extends MonoidElement<E>> {
	E identity();
}
