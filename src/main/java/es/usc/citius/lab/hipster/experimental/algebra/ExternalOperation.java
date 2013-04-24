package es.usc.citius.lab.hipster.experimental.algebra;

public interface ExternalOperation<A,B> {
	A apply(A a, B b);
}
