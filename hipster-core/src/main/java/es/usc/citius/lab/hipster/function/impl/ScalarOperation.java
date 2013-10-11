package es.usc.citius.lab.hipster.function.impl;


import es.usc.citius.lab.hipster.function.ScalarFunction;

/**
 * A scalar operation is an implementation of {@link ScalarFunction} that
 * also defines:
 * <ul>
 * 		<li>identity element (A*i = A)</li>
 * </ul>
 * 
 * @author Adrián González <adrian.gonzalez@usc.es>
 *
 * @param <E> operator class
 */
public class ScalarOperation<E extends Comparable<E>> implements ScalarFunction<E> {

	private double identityElem;
	private ScalarFunction<E> op;

	/**
	 * Unique constructor for {@link es.usc.citius.lab.hipster.function.impl.ScalarOperation}, that takes the {@link ScalarFunction}
	 * applied and the identity element.
	 *
	 * @param operation
	 * @param identityElem
	 */
	public ScalarOperation(ScalarFunction<E> operation, double identityElem) {
		this.identityElem = identityElem;
		this.op = operation;
	}

	@Override
	public E scale(E a, double b) {
		return this.op.scale(a, b);
	}

	public double getIdentityElem() {
		return identityElem;
	}

	/**
	 * Builds the scaling operation for Doubles, that is the multiplying
	 * operation for the factor.
	 *
	 * @return {@link es.usc.citius.lab.hipster.function.impl.ScalarOperation} for Double
	 */
	public static ScalarOperation<Double> doubleMultiplicationOp() {
		return new ScalarOperation<Double>(new ScalarFunction<Double>() {

			@Override
			public Double scale(Double a, double b) {
				return a * b;
			}

		}, 1d);
	}
	
}
