package es.usc.citius.lab.hipster.algebra;

public interface MonoidFactory<E extends Monoid<E>> {

	E getIdentityElem();

}
